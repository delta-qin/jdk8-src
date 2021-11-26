package com.deltaqin.p11rpc;

import com.deltaqin.p11rpc.entity.User;
import com.deltaqin.p11rpc.service.UserService;

/**
 * @author deltaqin
 * @date 2021/10/14 下午12:48
 */
public class RpcClient {
    public static void main(String[] args) {
        UserService proxyUserService = new UserService();

        User user = new User();
        user.setAge((short) 26);
        user.setSex((short) 1);

        int res = proxyUserService.addUser(user);
        if (res == 0) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }
}
