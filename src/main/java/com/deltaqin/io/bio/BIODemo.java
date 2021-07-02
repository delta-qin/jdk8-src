package com.deltaqin.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author deltaqin
 * @date 2021/4/15 9:58 上午
 */
public class BIODemo {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8000);

        System.out.println("start!!!");

        System.in.read();
        System.out.println("begin accept");

        while (true) {
            Socket socket = serverSocket.accept();

            System.out.println("accepted  a socket：" + socket);

            new Thread(()->{
                try {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

                    String msg;
                    while ((msg = bufferedReader.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
}
