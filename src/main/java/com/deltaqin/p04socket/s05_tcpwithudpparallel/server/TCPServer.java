package com.deltaqin.p04socket.s05_tcpwithudpparallel.server;

import com.deltaqin.p04socket.s05_tcpwithudpparallel.server.handle.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {
    private final int port;
    private ClientListener mListener;
    private List<ClientHandler> clientHandlerList = new ArrayList<>();

    public TCPServer(int port) {
        this.port = port;
    }

    public boolean start() {
        try {
            ClientListener listener = new ClientListener(port);
            mListener = listener;
            // 新建一个server线程，循环接收新的tcp连接，客户端维护在clientHandlerList
            listener.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void stop() {
        if (mListener != null) {
            mListener.exit();
        }

        for (ClientHandler clientHandler: clientHandlerList) {
            clientHandler.exit();
        }
        clientHandlerList.clear();
    }
    public void broadcast(String string) {
        for (ClientHandler clientHandler : clientHandlerList) {
            clientHandler.send(string);
        }
    }


    // TCP 建立连接，读取数据
    private class ClientListener extends Thread {
        private ServerSocket server;
        private boolean done = false;

        private ClientListener(int port) throws IOException {
            server = new ServerSocket(port);
            System.out.println("服务器信息：" + server.getInetAddress() + " P:" + server.getLocalPort());
        }

        @Override
        public void run() {
            super.run();

            System.out.println("服务器准备就绪～");
            // 等待客户端连接
            do {
                // 得到客户端
                Socket client;
                try {
                    client = server.accept();
                } catch (IOException e) {
                    continue;
                }
                // 客户端构建异步线程
                ClientHandler clientHandler = null;
                try {
                    clientHandler = new ClientHandler(client,
                            handler -> clientHandlerList.remove(handler));
                    // 开启对应客户端的读取线程，读写分离
                    clientHandler.readToPrint();
                    clientHandlerList.add(clientHandler);

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    System.out.println("客户端连接异常：" + ioException.getMessage());
                }
                // 启动线程
                //clientHandler.start();
            } while (!done);

            System.out.println("服务器已关闭！");
        }

        void exit() {
            done = true;
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 客户端串行消息处理
     */
    //private static class ClientHandler extends Thread {
    //    private Socket socket;
    //    private boolean flag = true;
    //
    //    ClientHandler(Socket socket) {
    //        this.socket = socket;
    //    }
    //
    //    @Override
    //    public void run() {
    //        super.run();
    //        System.out.println("新客户端连接：" + socket.getInetAddress() +
    //                " P:" + socket.getPort());
    //
    //        try {
    //            // 得到打印流，用于数据输出；服务器回送数据使用
    //            PrintStream socketOutput = new PrintStream(socket.getOutputStream());
    //            // 得到输入流，用于接收数据
    //            BufferedReader socketInput = new BufferedReader(new InputStreamReader(
    //                    socket.getInputStream()));
    //
    //            do {
    //                // 客户端拿到一条数据
    //                String str = socketInput.readLine();
    //                if ("bye".equalsIgnoreCase(str)) {
    //                    flag = false;
    //                    // 回送
    //                    socketOutput.println("bye");
    //                } else {
    //                    // 打印到屏幕。并回送数据长度
    //                    System.out.println(str);
    //                    socketOutput.println("回送：" + str.length());
    //                }
    //
    //            } while (flag);
    //
    //            socketInput.close();
    //            socketOutput.close();
    //
    //        } catch (Exception e) {
    //            System.out.println("连接异常断开");
    //        } finally {
    //            // 连接关闭
    //            try {
    //                socket.close();
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //
    //        System.out.println("客户端已退出：" + socket.getInetAddress() +
    //                " P:" + socket.getPort());
    //
    //    }
    //}
}
