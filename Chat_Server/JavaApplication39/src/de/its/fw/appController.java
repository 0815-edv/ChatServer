/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package de.its.fw;

/**
 *
 * @author flori
 */
public class appController {

    /**
     * @param args the command line arguments
     */
    private void initService() {
        ChatGUI gui = new ChatGUI();
        gui.setVisible(true);
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                appController appController1 = new appController();
                appController1.initService();
            }
        });
    }
    
}
