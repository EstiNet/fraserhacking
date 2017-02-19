package net.estinet.GitRTC;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

public class SocketIO {
    public static Socket socket = null;
    public static void doSocket(){
        try {
            socket = IO.socket("http://" + GitRTC.IP + ":" + GitRTC.port);
            String[] s = new String[2];
            s[0] = GitRTC.username;
            s[1] = GitRTC.password;
            socket.on(Socket.EVENT_CONNECT, args -> {
                socket.emit("hello", GitRTC.username, new Ack(){
                    @Override
                    public void call(Object... objects) {
                        GitRTC.connected = true;
                    }
                });
            }).on("alive", args -> {
                //alive message
            }).on("cursor", args -> {
                Cursor cursor = new Cursor(args[0].toString(), args[3].toString(), Integer.parseInt(args[1].toString()), Integer.parseInt(args[2].toString()));
                for(int i = 0; i < GitRTC.cursorList.size(); i++){
                    if(GitRTC.cursorList.get(i).name.equals(args[0].toString())){
                        GitRTC.cursorList.remove(i);
                    }
                }
                GitRTC.cursorList.add(cursor);
            }).on("changedir", args -> {
                System.out.println("changedir");
                GitRTC.recieveCache = args[0].toString();
            }).on("changefile", args -> {
                System.out.println("changefile");
                File f = new File(GitRTC.hostDirectory + "\\" + GitRTC.recieveCache);
                f.delete();
                try {
                    f.createNewFile();
                    FileOutputStream fos = null;
                    fos = new FileOutputStream(f);
                    fos.write(args[0].toString().getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).on(Socket.EVENT_DISCONNECT, args -> {
                GitRTC.connected = false;
            });
            socket.connect();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
