package ru.rickSanchez.jtwo.network;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketThread extends Thread{

    private Socket socket;
    private SocketThreadListener listener;
    private DataOutputStream out;
    private DataInputStream in;

    public SocketThread(SocketThreadListener listener, String name, Socket socket){
        super();
        this.socket = socket;
        this.listener = listener;
        start();
    }

    @Override
    public void run() {

        try {
            in = new DataInputStream(socket.getInputStream()) ;
            out = new DataOutputStream(socket.getOutputStream());
            listener.onSocketStart(this, socket);
            listener.onSocketReady(this, socket);
            while(!isInterrupted()){
                String msg = in.readUTF();
                listener.onReceiveString(this, socket, msg);
            }
        } catch(IOException e) {
            listener.onSocketException(this, e);
        } finally {
            close();
            listener.onSocketStop(this);
        }
    }

    public synchronized boolean sendMessage(String msg){
        try {
            out.writeUTF(msg);
            out.flush();
            return true;
        }catch(IOException e){
            listener.onSocketException(this, e);
            close();
            return false;
        }
    }

    public synchronized void close() {
        try {
            in.close();
            out.close();
        } catch(IOException e) {
            listener.onSocketException(this, e);
        }
        interrupt();
        try {
            socket.close();
        }catch(IOException e){
            listener.onSocketException(this, e);
        }
    }
}
