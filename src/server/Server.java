package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    static ArrayList<Socket> socketList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(10086);
        HashMap<String, String> hs = new HashMap<>();

        //读取本地用户信息
        try {
            FileReader fr = new FileReader("D:\\javaproj\\GroupChat\\src\\userinfo.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split("=");
                hs.put(userInfo[0], userInfo[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //客户端链接，开启一个线程
        while(true) {
            Socket socket =ss.accept();
            System.out.println("有客户端来链接");
            new Thread(new MyRunable(socket,hs)).start();
        }

    }
}
