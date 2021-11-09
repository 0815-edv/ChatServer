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

import com.google.protobuf.Any;
import de.protobuf.edv.ChatProtocol.ChatMessage;
import de.protobuf.edv.ChatProtocol.OnlineUsers;
import de.protobuf.edv.ChatProtocol.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class Client {

    private String ip;
    private int port = 2048;
    private Socket socket;
    private User user;

    public Client(String ip, int port, User user) {
        this.ip = ip;
        this.port = port;
        this.user = user;
    }

    public OnlineUsers getOnlineUsers() throws IOException {
        OutputStream output = socket.getOutputStream();
        OnlineUsers.Builder ou = OnlineUsers.newBuilder();

        ou.addUsers(User.newBuilder().setColor("").setUsername(user.getUsername()).setStatus(User.Status.ONLINE));
        
        // Send Request
        ou.build().writeTo(output);
        output.close();

        // Get Response
        InputStream input = socket.getInputStream();
        Any data = Any.parseFrom(input.readAllBytes());
        if (data.is(OnlineUsers.class)) {
            return data.unpack(OnlineUsers.class);
        }
        return null;
    }

    public void startMessageThread(javax.swing.JTextArea chat) {
        new ChatThread(this.socket, chat).start();
    }

    public void sendMessage(String message) throws IOException {
        OutputStream output = socket.getOutputStream();
        ChatMessage msg = ChatMessage.newBuilder().setArrivalTime(System.currentTimeMillis()).setFromUser(user).setMsg(message).setType(ChatMessage.Type.SEND).build();
        // Send Request
        msg.writeTo(output);
    }

    public void connect() {
        try {
            socket = new Socket(ip, port);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
