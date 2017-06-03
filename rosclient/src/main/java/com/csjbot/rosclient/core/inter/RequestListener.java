package com.csjbot.rosclient.core.inter;

import com.csjbot.rosclient.entity.MessagePacket;
import com.csjbot.rosclient.listener.ClientEvent;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com<br/>
 */
public interface RequestListener {
    void onReqeust(MessagePacket packet);

    void onEvent(ClientEvent event);

}