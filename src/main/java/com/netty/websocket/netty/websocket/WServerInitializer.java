package com.netty.websocket.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


public class WServerInitializer extends ChannelInitializer<SocketChannel> {


    protected void initChannel(SocketChannel socketChannel){
        System.out.println("收到新连接");
        //获得对应的管道(pipeline)
        ChannelPipeline pipeline = socketChannel.pipeline();
        //WebSocket 基于http协议，所需要的http 编解码器
        pipeline.addLast(new HttpServerCodec());
        //在http上有一些数据流产生，有大有小，我们对其进行处理，既然如此，我们需要使用netty 对数据流读写  提供支持，这个类叫：ChunkedWriteHandler
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage 进行聚合处理，聚合成request 或 response Netty中都需要用到这个助手类
        //里面需要提供最大内容的长度
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        /**
         * WebSocketServerProtocolHandler会帮你处理一些繁重复杂的事情
         * 会帮你处理握手动作：handshaking ( close、ping、pong )  ping、pong 为 websocket 的心跳机制
         * 对于 websocket 来讲，都是以 frams 进行传输的，不同的数据类型对应的 frams 也不同
         * 定义WebSocket的Http服务并定义其路由地址
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/WS"));

        //自定义的 handler
        pipeline.addLast(new WebSocketChatHandler());
    }


}
