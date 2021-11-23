/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package de.its.fw;

/**
 *
 * @author flori
 */
public class appcontroller {

    /**
     * @param args the command line arguments
     */
    private ChatGUI gui = null;
    
    private void initService(){
        gui = new ChatGUI();
        gui.setVisible(true);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                appcontroller appController1 = new appcontroller();
                appController1.initService();
            }
        });
    }
    
}
