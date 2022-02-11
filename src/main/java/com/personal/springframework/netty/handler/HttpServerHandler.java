package com.personal.springframework.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:HttpserverHandler
 * @author: anshaojun
 * @time: 2021-05-19 10:24
 */
@Slf4j
public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            log.info("from HostString [" + ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString()
                    + "] has been received " + msg.toString());
            String uri = request.uri();
            log.info("URI: " + uri);
            ByteBuf buf = request.content();
            log.info(" Request content: " + buf.toString(io.netty.util.CharsetUtil.UTF_8));
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            if (HttpUtil.isKeepAlive(request)) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }

            //...................................................................

            Map map = new HashMap<>();
            String res = null;
            try {
                res = JSONObject.fromObject(map).toString();
                response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                        Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                map.put("resultCode", "SUCCESS");
                try {
                    res = JSONObject.fromObject(map).toString();
                    response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                            Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    log.error(e1.getMessage());
                    e1.printStackTrace();
                }
            }
            response.headers().add("resultCode", "SUCCESS");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
