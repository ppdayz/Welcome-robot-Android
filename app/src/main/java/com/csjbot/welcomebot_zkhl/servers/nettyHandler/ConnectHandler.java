package com.csjbot.welcomebot_zkhl.servers.nettyHandler;

import com.orhanobut.logger.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by huantingting on 16/6/16.
 */
public class ConnectHandler extends SimpleChannelInboundHandler<String> {

    private static ClientListener listener = null;


    public static void setListener(ClientListener l) {
        listener = l;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (listener != null) {
            listener.clientConnected();
        }
        Logger.d("Client Active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (listener != null) {
            listener.clientDisConnected();
        }
        Logger.d("Client Close");
    }

    /**
     * <strong>Please keep in mind that this method will be renamed to
     * {@code messageReceived(ChannelHandlerContext, I)} in 5.0.</strong>
     * <p/>
     * Is called for each message of type {@link I}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error occurred
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
//        Logger.d("Server : " + msg);
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        if (listener != null) {
            listener.recMessage(body);
        }
        Logger.d("Server : " + body);
    }

    /**
     * Is called for each message of type {@link I}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error occurred
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        Logger.d("Server : " + msg);
    }
}
