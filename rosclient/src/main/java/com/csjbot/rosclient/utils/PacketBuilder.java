package com.csjbot.rosclient.utils;


import com.csjbot.rosclient.entity.AudioPacket;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com
 */
public class PacketBuilder {

    public static AudioPacket createAudioPacket(byte[] audioData) {
        return new AudioPacket(audioData);
    }


}
