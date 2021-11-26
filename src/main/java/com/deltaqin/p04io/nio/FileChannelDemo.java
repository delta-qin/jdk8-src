package com.deltaqin.p04io.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author deltaqin
 * @date 2021/4/15 12:55 下午
 */
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        FileChannel fileChannel = new RandomAccessFile("a.txt", "rw").getChannel();
        // Byte类型的buffer
        ByteBuffer buffer = ByteBuffer.allocate(10);

        while ((fileChannel.read(buffer)) != -1) {
            // buffer 默认是写模式，flip 是切换为读模式
            buffer.flip();
            // buffer是否有数据未读取
            while (buffer.hasRemaining()) {
                // 未读取数据的长度
                int remain = buffer.remaining();
                byte[] bytes = new byte[remain];
                //读出buffer的数据
                buffer.get(bytes);
                System.out.println(new String(bytes, StandardCharsets.UTF_8));
            }
            // 清空buffer，为下一次数据写入准备。将buffer切换为写模式
            buffer.clear();
        }
    }
}
