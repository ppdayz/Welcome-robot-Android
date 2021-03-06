package com.csjbot.rosclient.core.util;


import com.csjbot.rosclient.constant.ClientConstant;
import com.csjbot.rosclient.core.inter.Callbacker;
import com.csjbot.rosclient.entity.MessagePacket;
import com.csjbot.rosclient.listener.ClientEvent;
import com.csjbot.rosclient.listener.EventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com
 * <p>
 * CallBacker的具体实现，定义了一个线程来处理event
 */
public class ExecutorCallbacker implements Callbacker {
    private ExecutorService mCallbackWorker;

    public ExecutorCallbacker(final String workerName) {
        mCallbackWorker = Executors.newSingleThreadExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, workerName);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @Override
    public void notifyRequest(final EventListener listener, final MessagePacket packet) {
        postCallback(new Runnable() {
            @Override
            public void run() {
                listener.onEvent(new ClientEvent(ClientConstant.EVENT_PACKET, packet));
            }
        });
    }

    @Override
    public void notifyEvent(final EventListener listener, final ClientEvent event) {
        postCallback(new Runnable() {

            @Override
            public void run() {
                listener.onEvent(event);
            }
        });
    }

    private void postCallback(Runnable r) {
        mCallbackWorker.execute(r);
    }

    @Override
    public void destroy() {
        mCallbackWorker.shutdownNow();
    }
}
