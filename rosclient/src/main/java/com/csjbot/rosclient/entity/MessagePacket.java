package com.csjbot.rosclient.entity;

import com.csjbot.rosclient.utils.NetDataTypeTransform;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com
 * <p>
 * <pre>
 *
 *
 *  header
 *  length
 *  content
 *
 *
 * </pre>
 */
public abstract class MessagePacket implements Packet {

    private PacketHeader header = null;

    public abstract byte[] getContent();

    public abstract void setContent(byte[] content);

    @Override
    public byte[] encodeBytes() {
        byte[] contentData = getContent();
        byte[] header = new PacketHeader().getHeaderByte();
        int messageLen = contentData.length;
        byte[] bytes = new byte[header.length + 4 + messageLen];

        int offset = 0;
        System.arraycopy(header, 0, bytes, 0, header.length);
        offset += header.length;

        System.arraycopy(NetDataTypeTransform.intToBytesQiPa(messageLen), 0, bytes, offset, 4);
        offset += 4;

        System.arraycopy(contentData, 0, bytes, offset, messageLen);

        return bytes;
    }

    @Override
    public Packet decodeBytes(byte[] rawData) {
        if (!checkPacket(rawData)) {
            return null;
        }

        header = new PacketHeader(rawData);


        return this;
    }

    public boolean checkPacket(byte[] data) {
        // TODO: 2017/5/3 check packet

        return true;
    }
}
