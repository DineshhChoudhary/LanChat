/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchat;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Dinesh
 */
public class Client extends javax.swing.JFrame  implements Runnable{

    /**
     * Creates new form Client
     */
    String username,address;
    int port;
    private InetAddress ip;
    private DatagramSocket socket;
    private Thread send,receive,run;
    boolean running=false;
    private int ID=-1;
    public Client(String username,String address,int port){
        this.username=username;
        this.address=address;
        this.port=port;
         setVisible(true);
         
        boolean connect=openConnection(address,port);
        if(!connect)
        {
            chatta.append("Connection Failed");
        }

         initComponents();
         String welcomemsg=new String();
         welcomemsg="/c/" +username+"/e/";
         send(welcomemsg.getBytes());
         run= new Thread(this,"run");
         run.start();
         
         addWindowListener(new WindowAdapter(){
            @Override
             public void windowClosing(WindowEvent e){
                String disconnect ="/d/"+getID()+"/e/";
                send(disconnect.getBytes());
                //close();
                running=false;
                
            }
        });
        

    }
    public void console(String msg)
    {
        if(msg.length()!=0)
        {
            //chatta.append(username+":"+msg+"\n");
            String newmsg=new String();
            newmsg="/m/"+username+":"+msg;
            send(newmsg.getBytes());
        }
        messageta.setText("");
    }
    public boolean openConnection(String address,int port)
    {
        try {
            socket= new DatagramSocket();
            ip=InetAddress.getByName(address);
        } catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
        
    }
    private void receive()
    {
        receive=new Thread("receive"){
            public void run()
            {
                while(running)
                {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data,data.length);

                    try {
                        socket.receive(packet);// receive data from socket and put data in packet its a while loop as it will run until it receive some data
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String message= new String(packet.getData());  
                    process(message);
                    
                }
            }
        };
        receive.start();
    }
    private void process(String message)
    {
        if(message.startsWith("/c/"))
        {
            int id=Integer.parseInt(message.split("/c/|/e/")[1]);
            setID(id);
            chatta.append("Succesfully connected to server."+id+"\n");
        }
        else if(message.startsWith("/m/"))
        {
            /*File f=new File("./answer.wav");
            try {
                //Toolkit.getDefaultToolkit().beep();//To play sound
                AudioInputStream ais=AudioSystem.getAudioInputStream(f);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            String msg=message.substring(3, message.length());
            chatta.append(msg+"\n");
        }
        else if(message.startsWith("/d/"))
        {
           chatta.append(message.split("/d/|/e/")[1]+"left");
        }
        else if(message.startsWith("/i/"))
        {
            String msg="/i/"+getID()+"/e/";
            send(msg.getBytes());
        }
        else if(message.startsWith("/u/"))
        {
            String u[]=message.split("/u/|/m/|/e/");
            update(Arrays.copyOfRange(u, 1, u.length-1));
        }
    }
    public void update(String[] users)
    {
       
       onlineusers.setListData(users);
        
    }
    public void setID(int ID)
    {
        this.ID=ID;
    }
    public int getID()
    {
        return this.ID;
    }
    private void send(final byte[] data)
    {
        send = new Thread("Send"){
            public void run(){
                DatagramPacket packet = new DatagramPacket(data,data.length,ip,port);
                try {
                    socket.send(packet);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        send.start();
    }
    

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatta = new javax.swing.JTextArea();
        messageta = new javax.swing.JTextField();
        sendtb = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        onlineusers = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chatta.setColumns(20);
        chatta.setRows(5);
        chatta.setFocusable(false);
        jScrollPane1.setViewportView(chatta);

        messageta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messagetaActionPerformed(evt);
            }
        });
        messageta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messagetaKeyPressed(evt);
            }
        });

        sendtb.setText("Send");
        sendtb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendtbActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(onlineusers);

        jLabel1.setText("                        Online Users");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageta, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendtb)
                        .addGap(22, 22, 22)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageta, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendtb, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendtbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendtbActionPerformed
        // TODO add your handling code here:
        String msg=messageta.getText();
        console(msg);
        
    }//GEN-LAST:event_sendtbActionPerformed

    private void messagetaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messagetaKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            String msg=messageta.getText();
               console(msg);
        }
        
    }//GEN-LAST:event_messagetaKeyPressed

    private void messagetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messagetaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_messagetaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField messageta;
    private javax.swing.JList onlineusers;
    private javax.swing.JButton sendtb;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        running=true;
        receive();
    }
    
    public void close(){
        synchronized(socket){
            socket.close();
        }
    }
}
