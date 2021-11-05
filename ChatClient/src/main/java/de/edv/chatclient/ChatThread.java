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
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class ChatThread extends Thread {

    private Socket socket;
    private javax.swing.JTextArea chat;

    public ChatThread(Socket socket, javax.swing.JTextArea chat) {
        this.socket = socket;
        this.chat = chat;
    }
    
    public void run(){
        do {
            try {
                InputStream input = socket.getInputStream();
                Any data = Any.parseFrom(input.readAllBytes());
                if (data.is(ChatMessage.class)){
                    ChatMessage msg = data.unpack(ChatMessage.class);
                    chat.append(msg.getFromUser().getUsername()+": "+msg.getMsg());
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatThread.class.getName()).log(Level.SEVERE, null, ex);
            } 
        } while (socket.isConnected());

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
