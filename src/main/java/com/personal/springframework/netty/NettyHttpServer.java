package com.personal.springframework.netty;

import com.personal.springframework.netty.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @description:netty httpserver
 * @author: anshaojun
 * @time: 2021-05-19 10:11
 */
@Slf4j
@Component
@PropertySource("classpath:common.properties")
public class NettyHttpServer {
    @Value("${netty.http.port}")
    private int port;
    @Value("${netty.http.host}")
    private String host;
    private EventLoopGroup bossGroup, workerGroup;

    @PostConstruct
    public void run() {
        log.info("Starting HttpServer ........................................");
        log.info(" HttpServer ....port...................................." + port);
        bossGroup = new NioEventLoopGroup(4);
        workerGroup = new NioEventLoopGroup(8);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        // time out
                        pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(60));
                        // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                        pipeline.addLast(new HttpResponseEncoder());
                        // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                        pipeline.addLast(new HttpRequestDecoder());
                        pipeline.addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
                        //HttpObjectAggregator把多个消息转换为一个单一的FullHttpRequest
                        // Please note we create a handler for every new channel
                        // because it has stateful properties.
                        pipeline.addLast("handler", new HttpServerHandler());
                    }
                });
        try {
            // Start the server.
            b.bind(host, port).sync();
            log.info(" HttpServer  is running on serverADDR:" + host + ",port :" + port);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info(" HttpServer  is shutdown on serverADDR:" + host + ",port :" + port);
    }
}
