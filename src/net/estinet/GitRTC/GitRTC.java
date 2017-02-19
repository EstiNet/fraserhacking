package net.estinet.GitRTC;

import io.socket.client.Ack;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GitRTC {
    public static String IP = "138.51.159.246";
    public static int port = 80;
    public static String username = "user";
    public static String password = "password";
    public static boolean connected = false;
    public static String pushCache = "";
    public static String recieveCache = "";

    public static String hostDirectory = "";

    public static String projectName = "test";

    public static List<Cursor> cursorList = new ArrayList<>();

    public static List<Path> files = new ArrayList<>();
    public static List<Long> lastModified = new ArrayList<>();

    public static void main(String[] args){
        System.out.println("Starting GitRTC...");
        while(true) {
            System.out.println("Please specify the host directory.");
            java.util.Scanner scan = new java.util.Scanner(System.in);
            hostDirectory = scan.nextLine();
            if (!new File(hostDirectory).isDirectory()) {
                System.out.println("Not a directory, try again.");
                continue;
            }
            break;
        }
        System.out.println("Scanning filesystem...");
        try(Stream<Path> paths = Files.walk(Paths.get(hostDirectory))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    files.add(filePath.toAbsolutePath());
                    lastModified.add(filePath.toFile().lastModified());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connecting to GitRTC...");
        SocketIO.doSocket();
        System.out.println("Should be online! Now constantly scanning files...");
        new Thread(new Runnable(){
            public void run(){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Finding file scan...");
                try(Stream<Path> paths = Files.walk(Paths.get(hostDirectory))) {
                    paths.forEach(filePath -> {
                        if (Files.isRegularFile(filePath)) {
                            if(!files.contains(filePath.toAbsolutePath())) {
                                System.out.println("absolutepathfound");
                                files.add(filePath.toAbsolutePath());
                                lastModified.add(filePath.toFile().lastModified());
                                SocketIO.socket.emit("changeDir", filePath, new Ack(){
                                    @Override
                                    public void call(Object... objects) {
                                        File file = filePath.toFile();
                                        FileInputStream fis = null;
                                        try {
                                            fis = new FileInputStream(file);

                                        byte[] data = new byte[(int) file.length()];
                                        fis.read(data);
                                        fis.close();

                                        String str = new String(data, "UTF-8");
                                        SocketIO.socket.emit("changefile", str);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            else {
                                System.out.println("absolutepathfound!!");
                                for(int i = 0; i < files.size(); i++){
                                    System.out.println("file: " + files.get(i).toFile().getAbsolutePath() + " " + filePath.toAbsolutePath() + " modified: " + lastModified.get(i) + " " + filePath.toFile().lastModified());
                                    //files.get(i).toFile().getAbsolutePath().equals(filePath.toAbsolutePath()) &&
                                    if((lastModified.get(i) < filePath.toFile().lastModified())){
                                        lastModified.set(i, filePath.toFile().lastModified());
                                        System.out.println("emitter");
                                        SocketIO.socket.emit("changeDir", filePath, new Ack(){
                                            @Override
                                            public void call(Object... objects) {
                                                File file = filePath.toFile();
                                                FileInputStream fis = null;
                                                try {
                                                    fis = new FileInputStream(file);

                                                    byte[] data = new byte[(int) file.length()];
                                                    fis.read(data);
                                                    fis.close();

                                                    String str = new String(data, "UTF-8");
                                                    SocketIO.socket.emit("changefile", str);
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        files.set(i, filePath);
                                        break;
                                    }
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                run();
            }
        }).start();
    }
}
