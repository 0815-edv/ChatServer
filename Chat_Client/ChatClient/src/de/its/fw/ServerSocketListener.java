/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.its.fw;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author flori
 */
public class ServerSocketListener implements Runnable{

    private DefaultListModel DLMincommingMessage;
    private ServerSocket inServersocket;
    
    public ServerSocketListener(DefaultListModel dlm){
        DLMincommingMessage = dlm;
        buildSocket();
    }
    
    private void buildSocket(){
        try {
            inServersocket = new ServerSocket(5699);
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            String seperator = ";";
            Socket inSocket = inServersocket.accept();
            InputStream input = inSocket.getInputStream();
            byte[] daten = input.readAllBytes();
            int anzahlBytes = input.read(daten);
            String message = new String(daten, 0, anzahlBytes);
            DLMincommingMessage.addElement(message);
            
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
