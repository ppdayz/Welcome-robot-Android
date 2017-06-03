package com.csjbot.rosclient.entity;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class AudioPacket extends MessagePacket {
    private byte[] mContent;

    public AudioPacket(byte[] content) {
        mContent = content;
    }

    @Override
    public byte[] getContent() {
        return mContent;
    }

    @Override
    public void setContent(byte[] content) {
        this.mContent = content;
    }
}
