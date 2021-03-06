/*
 * The MIT License
 *
 * Copyright 2021 Markus.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.edv.chatclient;

import de.edv.chatserver.ChatService;
import static de.edv.chatserver.Helper.resize;
import de.edv.chatserver.Protocol.Login;
import de.edv.chatserver.Protocol.Logout;
import de.edv.chatserver.Protocol.Message;
import de.edv.chatserver.Protocol.PayloadOffset;
import de.edv.chatserver.Protocol.User;
import de.edv.chatserver.Protocol.Users;
import de.edv.chatserver.ServerThread;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class ChatClientServerThread extends Thread {

    private Socket socket;
    private Expose e;

    public ChatClientServerThread(Socket socket, Expose e) {
        this.socket = socket;
        this.e = e;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            ChatService chat = new ChatService();
            do {
                // Process Request
                byte[] data = input.readAllBytes();
                if (data.length > 0) {
                    switch (data[0]) {
                        case PayloadOffset.LOGIN:
                            System.out.println("Recieved Login Confirmation from Server");
                            Login login = (Login) new Login().deserialization(resize(data));
                            login.setIP(socket.getInetAddress().toString());
                            e.getStatusLabel().setText("Online");
                            break;

                        case PayloadOffset.LOGOUT:
                            System.out.println("Recieved Logout Confirmation from Server");
                            Logout logout = (Logout) new Logout().deserialization(resize(data));
                            logout.setIP(socket.getInetAddress().toString());
                            e.getStatusLabel().setText("Offline");
                            break;

                        case PayloadOffset.MESSAGE:
                            System.out.println("Recieved Message from Server");
                            Message message = (Message) new Message().deserialization(resize(data));
                            e.getChatArea().append(new SimpleDateFormat("HH:mm:ss").format(new Date())
                                    + " " + message.getSender().getUsername() + " " + message.getMessage());
                            break;

                        case PayloadOffset.USER:
                            User user = (User) new User().deserialization(resize(data));
                            break;

                        case PayloadOffset.USERS:
                            System.out.println("Recieved Online Users from Server");
                            Users users = (Users) new Users().deserialization(resize(data));
                            for(User usr: users.getUsers()){
                                e.getUserArea().append(usr.getUsername()+" ("+usr.getStatus()+")");
                            }
                            break;

                    }
                }

            } while (socket.isConnected());

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
