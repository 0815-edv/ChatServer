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

import de.edv.chatserver.Protocol.BaseProto;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class ChatClientService {

    private String ip;
    private final int port = 2048;
    
    public ChatClientService(String ip){
        this.ip = ip;
    }

    
    public boolean send(BaseProto generic) {
        try {
            Socket socket = new Socket(ip, port);
            OutputStream output = socket.getOutputStream();
            output.write(generic.serialization());
            output.flush();
            output.close();
            socket.close();
            return true;
            
        } catch (IOException ex) {
            Logger.getLogger(ChatClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
