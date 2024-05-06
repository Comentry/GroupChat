package server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;


public class MyRunable implements Runnable {
    Socket socket;
    HashMap<String, String> hs;

    public MyRunable(Socket socket, HashMap<String, String> hs) {
        this.socket = socket;
        this.hs = hs;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String choose = br.readLine();
                switch (choose) {
                    case "login" -> login(br);
                    case "register" -> System.out.println("用户选择注册操作");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void login(BufferedReader br) throws IOException {
        System.out.println("用户选择登录操作");
        String userInfo = br.readLine();
        String[] userInfoArr = userInfo.split("&");
        String userNameInput = userInfoArr[0].split("=")[1];
        String passwordInput = userInfoArr[1].split("=")[1];
        System.out.println("用户输入用户名为" + userNameInput);
        System.out.println("用户输入密码为" + passwordInput);
        if (hs.containsKey(userNameInput)) {
            String password = hs.get(userNameInput);
            if (passwordInput.equals(password)) {
                // 回写1 ，提示登录成功
                writeMessage2Client("1");
                Server.socketList.add(socket);
                talk2All(br, userNameInput);
            } else {
                writeMessage2Client("2");
            }
        } else {
            writeMessage2Client("3");
        }
    }


    private void talk2All(BufferedReader br, String username) throws IOException {
        while (true) {
            String message = br.readLine();
            System.out.println(username + "发送过来消息：" + message);

            //群发
            for (Socket s : Server.socketList) {
                //s依次表示每一个客户端的连接对象
                writeMessage2Client(s, username + "发送过来消息：" + message);
            }
        }
    }

    private void writeMessage2Client(String message) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(message);
        bw.newLine();
        bw.flush();
    }

    public void writeMessage2Client(Socket s, String message) throws IOException {
        //获取输出流
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        bw.write(message);
        bw.newLine();
        bw.flush();
    }


}
