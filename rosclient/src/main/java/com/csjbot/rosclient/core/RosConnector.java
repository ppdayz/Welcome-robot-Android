package com.csjbot.rosclient.core;

import com.csjbot.rosclient.core.util.Error;
import com.csjbot.rosclient.entity.MessagePacket;
import com.csjbot.rosclient.utils.CSJLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com
 * <p>
 * 具体的连接实现层
 */
class RosConnector {
    private static final int TIME_OUT = 5000;

    private boolean isRunning;
    private Socket socket = null;
    private ClientManager clientManager = null;
    private InputStream in;
    private OutputStream out;

    void setClientManager(ClientManager mgr) {
        clientManager = mgr;
    }


    int send(MessagePacket packet) {
        if (socket == null || out == null) {
            return Error.SocketError.SEND_SOCKET_OR_OUT_NULL;
        }
        CSJLogger.error("MessagePacket");

        try {
            out.write(packet.encodeBytes());
            out.flush();
            return Error.SocketError.SEND_SUCCESS;
        } catch (IOException e) {
            return Error.SocketError.SEND_IO_ERROR;
        }
    }

    /**
     * 接收数据，新建一个线程
     */
    void receiveData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        in = socket.getInputStream();
                        byte[] data = inputStreamToByte(in);
                        if (data != null && data.length > 0) {
                            onReceive(data);
//                            CSJLogger.info(Arrays.toString(data));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * @param data
     */
    private void onReceive(byte[] data) {
        clientManager.onReceive(data);
    }

    /**
     * InputStream 转为 byte
     *
     * @param inStream Socket的InputStream
     * @return 读入的字节数组
     * @throws IOException
     */
    private byte[] inputStreamToByte(InputStream inStream) throws IOException {
        int count = 0;
        while (count == 0) {
            if (inStream != null) {
                count = inStream.available();
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        byte[] b = null;
        if (count > 2048) {
            b = new byte[2048];
        } else {
            if (count != -1) {
                b = new byte[count];
            }
        }

        if (null != b) {
            int ret = inStream.read(b);
            if (ret != count) {
                CSJLogger.info("读取 inStream 错误， 预计读取 " + count + " 实际读取 " + ret);
                return null;
            }
        }

        return b;
    }


    /**
     * 用源生 Socket 连接 Ros
     *
     * @param hostName 主机名，可以是 ip（192.168.2.2） 也可以是 域名（www.baidu.com）
     * @param port     连接的端口
     * @return 返回状态 int： Error.SocketError.UNKONWN_HOST 未知的主机
     * Error.SocketError.CONNECT_TIME_OUT   连接超时
     * Error.SocketError.CONNECT_SUCCESS    连接成功
     * @see Error.SocketError
     */
    int connect(String hostName, int port) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(hostName);
            CSJLogger.info("hostName " + hostName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return Error.SocketError.UNKONWN_HOST;
        }

        final InetSocketAddress socketAddress = new InetSocketAddress(address.getHostAddress(), port);

        try {
            socket = new Socket();
            socket.connect(socketAddress, TIME_OUT);
            socket.setKeepAlive(true);
            out = socket.getOutputStream();
            if (!isRunning) {
                isRunning = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Error.SocketError.CONNECT_TIME_OUT;
        }
        return Error.SocketError.CONNECT_SUCCESS;
    }

    void destroy() {
        isRunning = false;
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isRunning() {
        return isRunning;
    }
}
