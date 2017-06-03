package com.csjbot.rosclient.core;

import com.csjbot.rosclient.constant.ClientConstant;
import com.csjbot.rosclient.core.inter.ActionListener;
import com.csjbot.rosclient.core.inter.RequestListener;
import com.csjbot.rosclient.core.util.Error;
import com.csjbot.rosclient.entity.MessagePacket;
import com.csjbot.rosclient.listener.ClientEvent;
import com.csjbot.rosclient.utils.CSJLogger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com<br/>
 * <p>
 * RosConntor的事件封装层，处理RosConnector的各种数据，承上启下
 */
public class ClientManager {
    private static ClientManager ourInstance = new ClientManager();

    public static ClientManager getInstance() {
        return ourInstance;
    }

    private static final long SEND_TIME_OUT = 25;
    private static final int MAX_HB_LIFE = 6;
    /**
     * 发送池，所有的MessagePacket进过包装之后都会丢在这里，按照先进先出的顺序来操作
     * 有两个线程来操作，所以这里的操作都是要加同步来实现
     */
    private final ArrayList<PacketEntity> mSendPool = new ArrayList<>();
    private boolean mIsRunning = false;
    private ExecutorService mMainExecutor;
    private RequestListener mRequestListener;
    private RosConnector mConnector = null;
    private ScheduledExecutorService mHeartBeatThread, mReConnectTread;
    private Thread mSendThread;
    private String mHostName;
    private int mPort;
    private ActionListener mListener = null;
    private int RECONNECT_INTERVAL = 5;
    private Integer receiveHeartBeatCounter = MAX_HB_LIFE;
    private final Object locker = new Object();


    private ClientManager() {
    }

    public void onReceive(byte[] data) {
    }

    /**
     * 初始化Manager
     *
     * @param hostName
     * @param port
     * @param listener
     */
    public void init(final String hostName, final int port, final ActionListener listener) {
        mIsRunning = true;
        mConnector = new RosConnector();
        mHostName = hostName;
        mPort = port;
        mListener = listener;

        mSendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                processSendPool();
            }
        }, "RosClientSendThread");
        mSendThread.setDaemon(true);

        mHeartBeatThread = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            /**
             * Constructs a new {@code Thread}.  Implementations may also initialize
             * priority, name, daemon status, {@code ThreadGroup}, etc.
             *
             * @param r a runnable to be executed by new thread instance
             * @return constructed thread, or {@code null} if the request to
             * create a thread is rejected
             */
            @Override
            public Thread newThread(Runnable r) {
                Thread ret = new Thread(r, "HeartBeatThreadPool");
                return ret;
            }
        });

        mMainExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread ret = new Thread(r, "mMainExecutor");
                ret.setDaemon(true);
                return ret;
            }
        });

        mMainExecutor.submit(connectRunnable);

        mReConnectTread = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread ret = new Thread(r, "mReConnectTread");
                return ret;
            }
        });
    }

    private Runnable connectRunnable = new Runnable() {
        @Override
        public void run() {
            CSJLogger.info("MainExecutor 启动");
            mConnector.setClientManager(ClientManager.this);
            int init_status = mConnector.connect(mHostName, mPort);
//            while ((init_status = mConnector.connect(mHostName, mPort)) != Error.SocketError.CONNECT_SUCCESS) {
//                CSJLogger.info("connect to " + mHostName + "   " + mPort);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mListener.onFailed(init_status);
//            }

            if (init_status == Error.SocketError.CONNECT_SUCCESS) {
                mListener.onSuccess();
                mConnector.receiveData();
                mSendThread.start();
            } else {
                mListener.onFailed(init_status);
            }
        }
    };

    private Runnable reConnectRunnable = new Runnable() {
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            if (!mConnector.isRunning()) {
                CSJLogger.info("重新连接");

                mConnector.setClientManager(ClientManager.this);
                int init_status = mConnector.connect(mHostName, mPort);
                mConnector.receiveData();
                if (init_status == Error.SocketError.CONNECT_SUCCESS) {
                    receiveHeartBeatCounter = MAX_HB_LIFE;
                    mRequestListener.onEvent(new ClientEvent(ClientConstant.EVENT_RECONNECTED));
                } else if (init_status == Error.SocketError.CONNECT_TIME_OUT) {
                    mRequestListener.onEvent(new ClientEvent(ClientConstant.EVENT_CONNECT_TIME_OUT));
                } else {
                    mListener.onFailed(init_status);
                }
            }
        }
    };

    public void sendRequest(final MessagePacket packet, final ActionListener listener) {
        mMainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                PacketEntity entity = new PacketEntity(packet, listener);
                addToSendPool(entity);
            }
        });
    }

    public void setRequestListener(RequestListener listener) {
        mRequestListener = listener;
    }


    private void addToSendPool(PacketEntity packetWrapper) {
        synchronized (mSendPool) {
            mSendPool.add(packetWrapper);
            mSendPool.notify();
        }
    }

    /**
     * 发送池
     */
    private void processSendPool() {
        CSJLogger.info("processSendPool");

        while (mIsRunning) {
            synchronized (mSendPool) {
                Iterator<PacketEntity> iterator = mSendPool.iterator();
//                CSJLogger.warn("onSuccess0  = " + mSendPool.size());

                while (iterator.hasNext()) {
                    PacketEntity entity = iterator.next();
                    int stat = mConnector.send(entity.packet);
                    iterator.remove();

//                    if (stat == Error.SocketError.SEND_SUCCESS) {
//                        entity.onSuccess();
//                        CSJLogger.info("onSuccess");
//
////                        continue;
//                    } else {
//                        if (entity.callback != null) {
//                            entity.onFailed(stat);
//                        }
//                    }

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    mSendPool.wait();
                } catch (InterruptedException e) {
                    CSJLogger.error(e.getMessage());
                }
            }
        }
    }

    public void destroy() {
        mMainExecutor.shutdown();
        mIsRunning = false;
    }

    static class PacketEntity {
        MessagePacket packet = null;
        ActionListener callback = null;

        public PacketEntity(MessagePacket packet, ActionListener callback) {
            this.packet = packet;
            this.callback = callback;
        }

        public void onSuccess() {
            if (callback != null) {
                callback.onSuccess();
            }
        }

        public void onFailed(int error) {
            if (callback != null) {
                callback.onFailed(error);
            }
        }
    }
}

