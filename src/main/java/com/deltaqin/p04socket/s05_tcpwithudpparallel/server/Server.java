package com.deltaqin.p04socket.s05_tcpwithudpparallel.server;

import com.deltaqin.p04socket.s05_tcpwithudpparallel.constants.TCPConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Server {
    public static void main(String[] args) throws IOException {
        TCPServer tcpServer = new TCPServer(TCPConstants.PORT_SERVER);
        boolean isSucceed = tcpServer.start();
        if (!isSucceed) {
            System.out.println("Start TCP server failed!");
            return;
        }

        UDPProvider.start(TCPConstants.PORT_SERVER);


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String string;
        do {
            string = bufferedReader.readLine();
            tcpServer.broadcast(string);
        } while (!"00bye00".equalsIgnoreCase(string));

        //try {
        //    //noinspection ResultOfMethodCallIgnored
        //    System.in.read();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

        UDPProvider.stop();
        tcpServer.stop();
    }
}
