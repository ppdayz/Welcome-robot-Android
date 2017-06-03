package com.csjbot.rosclient.core.util;


import com.csjbot.rosclient.core.ClientManager;
import com.csjbot.rosclient.core.inter.Callbacker;
import com.csjbot.rosclient.core.inter.RequestListener;
import com.csjbot.rosclient.entity.MessagePacket;
import com.csjbot.rosclient.listener.ClientEvent;
import com.csjbot.rosclient.listener.EventListener;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com<br/>
 * <p>
 * 封装了 EventListener  ClientManager 和Callbacker ，使之可以一体使用
 */
public class ClientListenerWrapper implements RequestListener {
    private EventListener mListener;
    private ClientManager mClientManager;
    private Callbacker mCallbacker;

    public ClientListenerWrapper(ClientManager clientManager, Callbacker threadHelper, EventListener listener) {
        this.mListener = listener;
        this.mClientManager = clientManager;
        this.mCallbacker = threadHelper;
    }

    @Override
    public void onReqeust(MessagePacket packet) {
        mCallbacker.notifyRequest(mListener, packet);
    }

    @Override
    public void onEvent(ClientEvent event) {
        mCallbacker.notifyEvent(mListener, event);
    }

}