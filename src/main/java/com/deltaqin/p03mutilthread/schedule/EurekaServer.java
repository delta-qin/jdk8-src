package com.deltaqin.p03mutilthread.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class EurekaServer {

    static ConcurrentLinkedQueue<HeartBeat> queue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        while(true){
            Socket accept = serverSocket.accept();

            new Thread(()->{
                try {
                    InputStream inputStream = accept.getInputStream();

                    byte[] bytes = new byte[1024];
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    int readlen = 0;
                    StringBuilder stb = new StringBuilder();
                    while((readlen = bufferedInputStream.read(bytes)) != -1){
                        stb.append(new String(bytes,0,readlen));
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    HeartBeat heartBeat = objectMapper.readValue(stb.toString(), HeartBeat.class);
                    queue.add(heartBeat);
                    log.info("heartbeat info:->" + heartBeat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }


    }
}
