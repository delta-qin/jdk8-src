package com.deltaqin.socket.s05_tcpwithudpparallel.client;

import com.deltaqin.socket.s05_tcpwithudpparallel.client.bean.ServerInfo;
import com.deltaqin.socket.s05_tcpwithudpparallel.clink.utils.CloseUtils;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {
    public static void linkWith(ServerInfo info) throws IOException {
        Socket socket = new Socket();
        // 超时时间
        socket.setSoTimeout(3000);

        // 连接本地，端口2000；超时时间3000ms
        socket.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()), 3000);

        System.out.println("已发起服务器连接，并进入后续流程～");
        System.out.println("客户端信息：" + socket.getLocalAddress() + " P:" + socket.getLocalPort());
        System.out.println("服务器信息：" + socket.getInetAddress() + " P:" + socket.getPort());

        try {
            ReadHandler readHandler = new ReadHandler(socket.getInputStream());
            readHandler.start();

            // 发送接收数据
            write(socket);

            // 退出操作
            readHandler.exit();
        } catch (Exception e) {
            System.out.println("异常关闭");
        }

        //try {
        //    // 发送接收数据
        //    todo(socket);
        //} catch (Exception e) {
        //    System.out.println("异常关闭");
        //}

        // 释放资源
        socket.close();
        System.out.println("客户端已退出～");

    }

    private static void write(Socket client) throws IOException {
        // 构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        // 得到Socket输出流，并转换为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);

        do {
            // 键盘读取一行
            String str = input.readLine();
            // 发送到服务器
            socketPrintStream.println(str);

            if ("00bye00".equalsIgnoreCase(str)) {
                break;
            }
        } while (true);

        // 资源释放
        socketPrintStream.close();
    }

    static class ReadHandler extends Thread {
        private boolean done = false;
        private final InputStream inputStream;

        ReadHandler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            super.run();

            try {

                BufferedReader socketInput = new BufferedReader(new InputStreamReader(inputStream));
                do {
                    String str;
                    try {
                        str = socketInput.readLine();
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                    if (str == null) {
                        System.out.println("连接已关闭，无法读取数据！");
                        break;
                    }
                    // 打印到屏幕
                    System.out.println(str);
                } while (!done);
            } catch (Exception e) {
                if (!done) {
                    System.out.println("连接异常断开：" + e.getMessage());
                }
            } finally {
                // 连接关闭
                CloseUtils.close(inputStream);
            }
        }
        void exit() {
            done = true;
            CloseUtils.close(inputStream);
        }

    }

    //private static void todo(Socket client) throws IOException {
    //    // 构建键盘输入流
    //    InputStream in = System.in;
    //    BufferedReader input = new BufferedReader(new InputStreamReader(in));
    //
    //
    //    // 得到Socket输出流，并转换为打印流
    //    OutputStream outputStream = client.getOutputStream();
    //    PrintStream socketPrintStream = new PrintStream(outputStream);
    //
    //
    //    // 得到Socket输入流，并转换为BufferedReader
    //    InputStream inputStream = client.getInputStream();
    //    BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    //
    //    boolean flag = true;
    //    do {
    //        // 键盘读取一行
    //        String str = input.readLine();
    //        // 发送到服务器
    //        socketPrintStream.println(str);
    //
    //
    //        // 从服务器读取一行
    //        String echo = socketBufferedReader.readLine();
    //        if ("bye".equalsIgnoreCase(echo)) {
    //            flag = false;
    //        } else {
    //            System.out.println(echo);
    //        }
    //    } while (flag);
    //
    //    // 资源释放
    //    socketPrintStream.close();
    //    socketBufferedReader.close();
    //
    //}

}
