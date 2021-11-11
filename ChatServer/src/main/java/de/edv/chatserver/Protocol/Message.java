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
package de.edv.chatserver.Protocol;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus
 */
public class Message implements BaseProto {

    private byte MESSAGE = (byte) 0xA0;
    private User sender;
    private User reciever;
    private MessageType type;
    private String message;

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public byte[] convert() {
        // ( ͡° ͜ʖ ͡°)
        byte[] type = new byte[1];
        type[0] = MESSAGE;

        byte[] data = (new Gson().toJson(this)).getBytes(StandardCharsets.UTF_8);

        byte[] destination = new byte[type.length + data.length];

        // copy ciphertext into start of destination (from pos 0, copy ciphertext.length bytes)
        System.arraycopy(type, 0, destination, 0, type.length);

        // copy mac into end of destination (from pos ciphertext.length, copy mac.length bytes)
        System.arraycopy(data, 0, destination, type.length, data.length);

        return destination;
    }
}
