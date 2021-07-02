package com.deltaqin.io.netty.splitpacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyMessageDecoder extends ByteToMessageDecoder {

    int length = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println();
        System.out.println("MyMessageDecoder decode 被调用");
        //需要将得到二进制字节码-> MyMessageProtocol 数据包(对象)
        System.out.println(in);

        //if(in.readableBytes() >= 4) {  // int 4字节
        if(in.readableBytes() >= 4) {  // int 4字节
            if (length == 0){
                length = in.readInt();
            }
            if (in.readableBytes() < length) {
                //Reader不会移动，所以当前消息是完整的，等待下次的来了完整之后就可以了
                System.out.println("当前可读数据不够，继续等待。。");
                // 这里return之后length是成员变量，所以不会是0，下次来了Reader指针是在数据的位置的，直接就读取
                return;
            }
            byte[] content = new byte[length];
            if (in.readableBytes() >= length){
                // 注意Reader指针此时会移动
                in.readBytes(content);

                //封装成MyMessageProtocol对象，传递到下一个handler业务处理
                MyMessageProtocol messageProtocol = new MyMessageProtocol();
                messageProtocol.setLen(length);
                messageProtocol.setContent(content);
                out.add(messageProtocol);
            }
            length = 0;
        }
    }
}
