package com.deltaqin.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author deltaqin
 * @date 2021/4/15 10:58 上午
 */
public class AIODemo {

    public static void main(String[] args) throws IOException {
        AsynchronousServerSocketChannel asynchronousServerSocketChannel =
                AsynchronousServerSocketChannel.open();
        asynchronousServerSocketChannel.bind(new InetSocketAddress(8003));

        System.out.println("start!!!");

        asynchronousServerSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel asynchronousSocketChannel, Object o) {
                try{
                    System.out.println("accept a socket" + asynchronousSocketChannel.getRemoteAddress());
                    // 使用serverSocket阻塞获取
                    asynchronousServerSocketChannel.accept(null, this);

                    // 内核将数据拷贝到用户空间
                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        Future<Integer> future = asynchronousSocketChannel.read(byteBuffer);
                        if (future.get() > 0) {
                            byteBuffer.flip();
                            byte[] bytes = new byte[byteBuffer.remaining()];
                            byteBuffer.get(bytes);

                            String con = new String(bytes, "utf-8");
                            if (con.equals("\n")) {
                                continue;
                            }
                            System.out.println("receive ：" + con);
                        }
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable throwable, Object o) {
                System.out.println("failed");
            }
        });

        System.in.read();
    }

}
