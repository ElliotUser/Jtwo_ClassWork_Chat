package ru.rickSanchez.company.lesson_7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8189);
            Socket s = serverSocket.accept();
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream())) {

            System.out.println("Client Connected");
            while(true) {
                String msg = in.readUTF();
                System.out.println("Client sent: " + msg);
                out.writeUTF("echo: " + msg);
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
