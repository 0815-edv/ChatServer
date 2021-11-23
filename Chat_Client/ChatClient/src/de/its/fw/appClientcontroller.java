/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package de.its.fw;

/**
 *
 * @author flori
 */
public class appClientcontroller {

    /**
     * @param args the command line arguments
     */
    private ChatClientGUI gui = null;
    
    private void initService(){
        gui = new ChatClientGUI();
        gui.setVisible(true);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                appClientcontroller appController1 = new appClientcontroller();
                appController1.initService();
            }
        });
    }
    
}
