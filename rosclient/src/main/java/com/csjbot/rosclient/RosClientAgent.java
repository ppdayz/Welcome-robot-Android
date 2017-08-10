package com.csjbot.rosclient;


import com.csjbot.rosclient.constant.ClientConstant;
import com.csjbot.rosclient.core.util.ClientListenerWrapper;
import com.csjbot.rosclient.core.ClientManager;
import com.csjbot.rosclient.core.util.ExecutorCallbacker;
import com.csjbot.rosclient.core.inter.ActionListener;
import com.csjbot.rosclient.core.inter.Callbacker;
import com.csjbot.rosclient.entity.MessagePacket;
import com.csjbot.rosclient.listener.ClientEvent;
import com.csjbot.rosclient.listener.EventListener;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com
 * <p>
 * 直接面向用户的类，直接对外的封装
 */
public class RosClientAgent {
    private static RosClientAgent ourInstance;

    private static final Object lock = new Object();
    private ClientManager mClientManager;
    private EventListener mEventListener;
    private Callbacker mCallbacker;
    private boolean isConnected = false;


    /**
     * 创建client agent 的实例
     *
     * @param hostName 连接的主机名 一般是 192.168.x.3
     * @param port     端口
     * @param listener 所有事件的监听器
     * @return 返回RosClientAgent 的实例
     * @see ClientConstant
     */
    public static RosClientAgent createRosClientAgent(String hostName, int port, EventListener listener) {
        synchronized (lock) {
            if (ourInstance == null) {
                ourInstance = new RosClientAgent(hostName, port, listener);
            }
        }

        return ourInstance;
    }

    /**
     * 创建client agent 的实例
     *
     * @param listener 所有事件的监听器
     * @return 返回RosClientAgent 的实例
     * @see ClientConstant
     */
    public static RosClientAgent createRosClientAgent(EventListener listener) {
        synchronized (lock) {
            if (ourInstance == null) {
                ourInstance = new RosClientAgent(listener);
            }
        }

        return ourInstance;
    }

    /**
     * 获取 client agent 实例
     *
     * @return 返回RosClientAgent 的实例
     */
    public static RosClientAgent getRosClientAgent() {
        if (ourInstance == null) {
            throw new IllegalStateException("Please invoke createAgent firstly");
        }
        return ourInstance;
    }


    public void connect(String hostName, int port) {
        mClientManager.init(hostName, port, new ActionListener() {
            @Override
            public void onSuccess() {
                isConnected = true;
                notifyEvent(new ClientEvent(ClientConstant.EVENT_CONNECT_SUCCESS));
            }

            @Override
            public void onFailed(int errorCode) {
                notifyEvent(new ClientEvent(ClientConstant.EVENT_CONNECT_FAILD, errorCode));
            }
        });
    }


    private RosClientAgent(EventListener listener) {
        mClientManager = ClientManager.getInstance();
        mEventListener = listener;
        mCallbacker = new ExecutorCallbacker("RosClientAgentCallback");
        mClientManager.setRequestListener(new ClientListenerWrapper(mClientManager, mCallbacker, mEventListener));

    }

    /**
     * RosClientAgent 构造函数， 初始化clientManager，设置 EventListener <br/>
     * 这里会起一个线程 ExecutorCallbacker,处理所有的事件
     *
     * @param hostName 连接的主机名 一般是 192.168.x.3
     * @param port     端口
     * @param listener 监听器
     * @see ClientManager
     * @see EventListener
     */
    private RosClientAgent(String hostName, int port, EventListener listener) {
        mClientManager = ClientManager.getInstance();
        mEventListener = listener;
        mCallbacker = new ExecutorCallbacker("RosClientAgentCallback");
        mClientManager.setRequestListener(new ClientListenerWrapper(mClientManager, mCallbacker, mEventListener));
        mClientManager.init(hostName, port, new ActionListener() {
            @Override
            public void onSuccess() {
                notifyEvent(new ClientEvent(ClientConstant.EVENT_CONNECT_SUCCESS));
            }

            @Override
            public void onFailed(int errorCode) {
                notifyEvent(new ClientEvent(ClientConstant.EVENT_CONNECT_FAILD, errorCode));
            }
        });
    }

    /**
     * 发送构造好的数据包
     *
     * @param packet 要发送的数据包
     */
    public void sendMessage(final MessagePacket packet) {
        mClientManager.sendRequest(packet, new ActionListener() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailed(int errorCode) {
                notifyEvent(new ClientEvent(ClientConstant.SEND_FAILED, errorCode));
            }
        });
    }

    private void notifyEvent(ClientEvent event) {
        mCallbacker.notifyEvent(mEventListener, event);
    }

    /**
     * 关闭所有的东西
     */
    public void destroy() {
        mClientManager.destroy();
        mCallbacker.destroy();

        mClientManager = null;
        mCallbacker = null;
    }

    public boolean isConnected() {

        return isConnected;
    }
}
