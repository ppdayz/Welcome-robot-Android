package com.csjbot.cameraclient.constant;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br>
 * www.csjbot.com<br>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br>
 * Email: puyz@csjbot.com
 */
public class ClientConstant {
    /**
     * 获取到数据包
     */
    public static final int EVENT_PACKET = 0x00;

    /**
     * 和ros连接成功
     */
    public static final int EVENT_CONNECT_SUCCESS = 0x01;

    /**
     * 和ros重新连接超时
     */
    public static final int EVENT_CONNECT_TIME_OUT = 0x02;

    /**
     * 和ros连接超时
     */
    public static final int EVENT_CONNECT_FAILD = 0x03;

    /**
     * 和ros连接断开
     */
    public static final int EVENT_DISCONNET = 0x04;

    /**
     * 和ros重新建立连接
     */
    public static final int EVENT_RECONNECTED = 0x05;

    /**
     * 发送包失败
     */
    public static final int SEND_FAILED = 0x15;


    /**
     * 心跳包间隔时间，单位秒
     */
    public static final int HEART_BEAT_INTERVAL = 5;
    public static final int CONNECT_TIME_OUT = 5000;


}
