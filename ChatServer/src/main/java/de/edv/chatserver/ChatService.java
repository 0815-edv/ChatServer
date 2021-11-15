/*
 * The MIT License
 *
 * Copyright 2021 hax0r.
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
import de.edv.chatserver.Protocol.Message;
import de.edv.chatserver.Protocol.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hax0r
 */
public class ChatService {
    private static List<Login> logins = new ArrayList<Login>();
    
    public static void login(Login user){
        if(!logins.contains(user))
            logins.add(user);
    }
    
    public static void logout(Login user){
        if(logins.contains(user))
        logins.remove(user);
    }
    
    public static void sendMessage(User from, User to, Message msg){
        
    }
}
