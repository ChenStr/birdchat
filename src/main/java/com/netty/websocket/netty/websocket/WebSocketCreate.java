package com.netty.websocket.netty.websocket;

import com.netty.websocket.netty.websocket.WServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * 初始化方法
 */
//@Component
public class WebSocketCreate {

    //主从线程池组
    //主线程池组：
    private EventLoopGroup mainGroup;
    //从线程池组：
    private EventLoopGroup subGroup;
    //服务器类
    private ServerBootstrap server;
    //启动服务，并且设置端口号，启动方式为同步
    ChannelFuture channelWebSocket;

//    public WebSocketRunServer() {
//        mainGroup = new NioEventLoopGroup();
//        subGroup = new NioEventLoopGroup();
//        server = new ServerBootstrap();
//        server.group(mainGroup,subGroup).channel(NioServerSocketChannel.class).childHandler(new WServerInitializer());
//    }

    public static void run() throws Exception {
        //主从线程池组
        //主线程池组：
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        //从线程池组：
        EventLoopGroup subGroup = new NioEventLoopGroup();


        try{
            //创建服务器类
            ServerBootstrap server = new ServerBootstrap();

            //设置主从线程池组
            server.group(mainGroup,subGroup)
                    //设置NIO双向通道
                    .channel(NioServerSocketChannel.class)
                    //添加子处理器，用于处理从线程池的任务
                    .childHandler(new WServerInitializer());

            //启动服务，并且设置端口号，启动方式为同步
            ChannelFuture channelWebSocket = server.bind("192.168.5.33",8003).sync();
            //监听关闭的channel，设置为同步方式
            channelWebSocket.channel().closeFuture().sync();
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            //优雅关闭主从线程池组
            mainGroup.shutdownGracefully().sync();
            subGroup.shutdownGracefully().sync();
        }



    }

}
