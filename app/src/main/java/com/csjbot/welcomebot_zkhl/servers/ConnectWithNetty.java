package com.csjbot.welcomebot_zkhl.servers;


import com.csjbot.welcomebot_zkhl.servers.nettyHandler.ConnectInitializer;
import com.orhanobut.logger.Logger;

import java.util.LinkedList;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by huantingting on 16/6/16.
 */

public class ConnectWithNetty {

    public interface ClientStateListener {
        void connectSuccess();

        void connectFaild();
    }

    private Channel channel = null;

    private static ConnectWithNetty ConnectWithNetty = new ConnectWithNetty();

    private ConnectWithNetty() {
        SendThread.start();
    }

    public static ConnectWithNetty getInstence() {
        return ConnectWithNetty;
    }

    private ClientStateListener listener = null;

    public void connect(final String ip, final ClientStateListener l) {
        if ("".equals(ip) && ip == null) {
            Logger.d("connect()  ip is null");


            return;
        }
        this.listener = l;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int port = 60002;
                EventLoopGroup workgroup = null;

                try {
                    workgroup = new NioEventLoopGroup();

                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(workgroup)
                            .channel(NioSocketChannel.class)
                            .handler(new ConnectInitializer());

                    //设置TCP协议的属性
                    bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

                    ChannelFuture channelFuture = bootstrap.connect(ip, port).awaitUninterruptibly();
                    channelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                if (listener != null) {
                                    listener.connectSuccess();
                                }
                                Logger.d("Connect success");
                                isStop = false;
                            } else {
                                Logger.d("Connect failed");
                                if (listener != null) {
                                    listener.connectFaild();
                                }
                            }
                        }
                    });
                    channel = channelFuture.channel();
                    channel.closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (workgroup != null) {
                        workgroup.shutdownGracefully();
                        Logger.d("close connect");
                    }
                }
            }
        }).start();
    }

    public boolean isConnected() {
        if (channel != null) {
            return channel.isActive();
        }
        return false;
    }


    private LinkedList<String> cmdList = new LinkedList<>();
    private boolean isStop = false;

    Thread SendThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (!isStop) {
                if (!isStop) {
                    if (cmdList.isEmpty()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                }
//                Logger.d("sendmsg() : " + msg);
                final String cmd = cmdList.get(0);

                if (channel.isActive()) {
                    ChannelFuture channelFuture = channel.writeAndFlush(cmd);
                    channelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                Logger.d("send msg : " + cmd + " isSuccess");
//                            Iterator<String> iterator = cmdList.iterator();
//                            iterator.next();
//                            iterator.remove();
                                cmdList.remove(0);
                            } else {
                                Logger.d("send failed");
                            }
                        }
                    });
                }
                if (!isStop) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    );

    public void sendMsg(final String msg) {
        if (channel != null && channel.isActive()) {
            cmdList.add(msg + "\n");
        }
//        if (channel == null) {
//            Logger.d("Connect is close");
//            return;
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Logger.d("sendmsg() : " + msg);
//                ChannelFuture channelFuture = channel.writeAndFlush(msg);
//                channelFuture.addListener(new ChannelFutureListener() {
//                    @Override
//                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                        if (channelFuture.isSuccess()) {
//                            Logger.d("send msg : " + msg + " isSuccess");
//                        } else {
//                            Logger.d("send failed");
//                        }
//                    }
//                });
//            }
//        }).start();
    }

    public void exitClient() {
        closeConnect();
        isStop = true;
        SendThread.interrupt();
    }

    public void closeConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (channel != null)
                    channel.close();
            }
        }).start();
    }
}
