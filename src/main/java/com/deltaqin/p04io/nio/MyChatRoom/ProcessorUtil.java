package com.deltaqin.p04io.nio.MyChatRoom;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author deltaqin
 * @date 2021/4/15 11:29 上午
 */
public class ProcessorUtil {
    private static final Map<SocketChannel, String> USER_MAP = new ConcurrentHashMap<>();

    public static void join(SocketChannel socketChannel) {
        String userId = "user" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        send(socketChannel, "用户ID是" + userId + "\n");

        for (SocketChannel socketChannel1 : USER_MAP.keySet()) {
            if (socketChannel1 != socketChannel) {
                send(socketChannel1 , userId + "加入群聊" + "\n");
            }
        }

        USER_MAP.put(socketChannel, userId);

    }

    public static void quit(SocketChannel socketChannel) {
        String userId = USER_MAP.get(socketChannel);
        send(socketChannel, "您退出群聊" + "\n");
        USER_MAP.remove(socketChannel);

        for (SocketChannel channel: USER_MAP.keySet()) {
            if (channel != socketChannel) {
                send(socketChannel, userId + "退出群聊" + "\n");
            }
        }

    }

    // 只处理传播
    public static void propagate(SocketChannel socketChannel, String con) {
        String userId = USER_MAP.get(socketChannel);
        for (SocketChannel socketChannel1 : USER_MAP.keySet()) {
            if (socketChannel1 != socketChannel) {
                send(socketChannel1, con);
            }
        }
    }

    /**
     * 只处理发送消息
     * @param socketChannel
     * @param con
     */
    public static void send(SocketChannel socketChannel, String con) {
        try{
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(con.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
