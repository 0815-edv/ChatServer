/*
 * The MIT License
 *
 * Copyright 2021 BackInBash.
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
package de.edv.chatserver;

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
 * @author BackInBash
 */
public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();

            do {
                byte[] buf = input.readAllBytes();
                Any data = Any.parseFrom(buf);
                if (buf.length > 0) {
                    System.out.println("Processed Input Stream");
                    if (data.is(OnlineUsers.class)) {

                        System.out.println("Get User Info");
                        ConnectionHandler.addConnection(new AdvancedSockets(socket, data.unpack(User.class)));
                        ConnectionHandler.sendOnlineUserInfo(socket);
                    }
                    if (data.is(ChatMessage.class)) {
                        // Send Message
                        ChatMessage msg = data.unpack(ChatMessage.class);
                        if (msg.getToUser().getUsername() != "") {
                            ConnectionHandler.sendUnicast(msg.getToUser(), msg.getMsg());
                        } else {
                            ConnectionHandler.sendBrodcast(msg.getMsg());
                        }

                    }
                    if (data.is(User.class)) {
                        System.out.println("");
                    }
                }
                Thread.sleep(300);

            } while (socket.isConnected());

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
