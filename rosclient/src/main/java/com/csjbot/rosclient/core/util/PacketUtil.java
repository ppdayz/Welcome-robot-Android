package com.csjbot.rosclient.core.util;

import com.csjbot.rosclient.entity.CommonPacket;
import com.csjbot.rosclient.entity.MessagePacket;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com
 */
public class PacketUtil {
    public static MessagePacket parser(byte[] data) {
        return (CommonPacket) new CommonPacket().decodeBytes(data);
    }
}
