package com.tiiaan.rpc.socket;

import com.tiiaan.rpc.MyRpcClient;
import com.tiiaan.rpc.entity.MyRpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class SocketRpcClient implements MyRpcClient {

    @Override
    public Object sendRequest(MyRpcRequest myRpcRequest, String host, Integer port) {
        try (Socket socket = new Socket(host, port);) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(myRpcRequest);
            objectOutputStream.flush();
            log.info("等待 RPC 调用请求返回...");
            return objectInputStream.readObject();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
