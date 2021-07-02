package com.deltaqin.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author deltaqin
 * @date 2021/4/15 10:17 上午
 */
public class NIODemo {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8001));
        serverSocketChannel.configureBlocking(false);

        // 将Channel注册到selector上，并注册Accept事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("start!!!");

        while (true) {
            // 如果使用的是select(timeout)或selectNow()需要判断返回值是否大于0
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    // 注意强转
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) selectionKey.channel();

                    // socketChannel获取
                    SocketChannel socketChannel = serverSocketChannel1.accept();
                    System.out.println("accept a socket" + socketChannel.getRemoteAddress());
                    socketChannel.configureBlocking(false);

                    // 将SocketChannel注册到Selector上，并注册读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if (selectionKey.isReadable()) {
                    // 注意强转
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = socketChannel.read(buffer);
                    // 拷贝的阻塞
                    if (len > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        // 回车的换行会传递，这里是Mac。替换为空
                        String con = new String(bytes, "utf-8").replace("\n", "");
                        System.out.println("receive ：" + con);
                    }
                }
                // 读取之后
                iterator.remove();
            }
        }
    }
}
