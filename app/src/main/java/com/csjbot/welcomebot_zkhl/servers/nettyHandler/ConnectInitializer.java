package com.csjbot.welcomebot_zkhl.servers.nettyHandler;


import java.nio.charset.Charset;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by huantingting on 16/6/16.
 */
public class ConnectInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        socketChannel.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
        socketChannel.pipeline().addLast(new ConnectHandler());
    }
}
