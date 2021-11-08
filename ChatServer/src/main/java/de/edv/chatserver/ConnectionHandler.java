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
package de.edv.chatserver;

import de.protobuf.edv.ChatProtocol.ChatMessage;
import de.protobuf.edv.ChatProtocol.User;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class ConnectionHandler {

    private static List<AdvancedSockets> openConnections = new ArrayList<AdvancedSockets>();

    /**
     * Add Connection to Pool
     *
     * @param socket
     */
    public static void addConnection(AdvancedSockets socket) {
        if (socket.getSocket().isConnected()) {
            openConnections.add(socket);
        }
    }

    /**
     * Remove Connection from Pool
     *
     * @param socket
     */
    public static void removeConnection(AdvancedSockets socket) {
        if (!socket.getSocket().isConnected()) {
            openConnections.remove(socket);
        }
    }

    /**
     * Send Brodcast Message
     *
     * @param msg
     * @throws IOException
     */
    public static void sendBrodcast(String msg) throws IOException {
        for (AdvancedSockets sock : openConnections) {
            if (sock.getSocket().isConnected()) {
                ChatMessage message = ChatMessage.newBuilder().setType(ChatMessage.Type.SEND).setMsg(msg).setArrivalTime(System.currentTimeMillis()).build();
                OutputStream output = sock.getSocket().getOutputStream();

                message.writeTo(output);
                output.close();
            } else {
                // Close Socket if not Connected
                try {
                    sock.getSocket().close();
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Send Unicast Message
     *
     * @param user
     * @param msg
     * @throws IOException
     */
    public static void sendUnicast(User user, String msg) throws IOException {
        for (AdvancedSockets sock : openConnections) {
            if (sock.getSocket().isConnected() && user.getUsername().equals(sock.getUser().getUsername())) {
                ChatMessage message = ChatMessage.newBuilder().setType(ChatMessage.Type.SEND).setMsg(msg).setArrivalTime(System.currentTimeMillis()).build();
                OutputStream output = sock.getSocket().getOutputStream();

                message.writeTo(output);
                output.close();
            } else {
                // Close Socket if not Connected
                try {
                    sock.getSocket().close();
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
