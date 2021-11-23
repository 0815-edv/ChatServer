/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.its.fw;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author flori
 */
public class ChatService implements Runnable {

    private ServerSocket server;
    private DefaultListModel ListServer;
    private DefaultListModel ListNachrichten;
    private ArrayList<InetAddress> clients = new ArrayList<InetAddress>();

    public ChatService(DefaultListModel lstModel, DefaultListModel lstip) {

        try {
            server = new ServerSocket(5682);
            lstModel.addElement("Service Initialisiert");

        } catch (IOException ex) {
            Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
            lstModel.addElement("Service Fehlerhaft");
        }

        this.ListNachrichten = lstModel;
        this.ListServer = lstip;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String inputString = "<login>";
                Socket client = server.accept();
                InputStream input = client.getInputStream();
                
                byte[] daten = new byte[2048];
                int anzahlBytes = input.read(daten);
                String message = new String(daten, 0, anzahlBytes);
                
                if (inputString.contains(message)) {
                    ListServer.addElement(client.getInetAddress());
                    clients.add(client.getInetAddress());
                } else {
                    ListNachrichten.addElement("From: " + client.getInetAddress() + ":" + message);
                    System.out.println(message);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    @Override
    public void finalize(){
        try {
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                super.finalize();
            } catch (Throwable ex) {
                Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
