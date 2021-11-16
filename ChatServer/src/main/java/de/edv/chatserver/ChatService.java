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

import de.edv.chatserver.Protocol.Login;
import de.edv.chatserver.Protocol.Logout;
import de.edv.chatserver.Protocol.Message;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BackInBash
 */
public class ChatService {

    private static List<Login> logins;
    private final int port = 2048;

    public ChatService(){
        logins = Collections.synchronizedList(new ArrayList<Login>());
    }
    
    public void login(Login login) {
        if (!logins.contains(login)) {
            logins.add(login);
        }
    }

    public void logout(Logout logout) {
        var delete = new Object();
        for (Login login : logins) {
            if (login.getUser().getUsername().equals(logout.getUser().getUsername())
                    && login.getIP().equals(logout.getIP())) {
                delete = login;
            }
        }
        logins.remove(delete);
    }

    public void sendMessage(Message msg) {
        List<Socket> connections = new ArrayList<Socket>();
        if (msg.getReciever() == null) {
            for (Login login : logins) {
                connections.add(openConnection(login.getIP(), port));
            }
        } else {
            for (Login login : logins) {
                if (login.getUser().getUsername().equals(msg.getReciever().getUsername())) {
                    connections.add(openConnection(login.getIP(), port));
                }
            }
        }

        for (Socket sock : connections) {
            try {
                OutputStream output = sock.getOutputStream();
                output.write(msg.serialization());
                output.flush();
                output.close();
                sock.close();
            } catch (IOException ex) {
                Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Socket openConnection(String ip, int port) {
        try {
            return new Socket(ip, port);
        } catch (IOException ex) {
            Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
