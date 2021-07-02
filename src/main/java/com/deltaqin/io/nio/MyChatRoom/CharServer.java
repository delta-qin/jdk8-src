package com.deltaqin.io.nio.MyChatRoom;

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
 * @date 2021/4/15 11:19 上午
 */
public class CharServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8004));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel1.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("accept a socket ：" + socketChannel.getRemoteAddress());
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    ProcessorUtil.join(socketChannel);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = socketChannel.read(buffer);
                    if (len > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);

                        String con = new String(bytes, "UTF-8").replace("\n", "");
                        if (con.equalsIgnoreCase("quit")) {
                            ProcessorUtil.quit(socketChannel);
                            // 当前Channel关闭
                            selectionKey.cancel();
                            socketChannel.close();
                        } else {
                            ProcessorUtil.propagate(socketChannel, con);
                        }
                    }
                }
                iterator.remove();
            }
        }

    }
}
