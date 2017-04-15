/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lanchat.Client;
import lanchat.Login;
/**
 *
 * @author Dinesh
 */
public class Server extends Login implements Runnable{
    private int port;
    private Thread run,manage,send,receive,server;
    DatagramSocket socket;
    //Client cl;
    boolean running=false;
    public final int MAX_ATTEMPT=5;
    private List<ServerClient> clients=new ArrayList<ServerClient>();
    private List<Integer> clientResponse=new ArrayList<Integer>();
    public Server(int port){
        this.port=port;
        try {
            socket=new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        run =new Thread(this,"server");
        run.start();
    }

    @Override
    public void run() {
        running=true;
        System.out.println("server started on port"+port);
        manageClients();
        receive();
        

    }
    private void manageClients()
    {
        manage=new Thread("manage"){
            public void run()
            {
                while(running)
                {
                    onlineUsers();
                    sendToAll("/i/server/e/");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for(int i=0;i<clients.size();i++)
                    {
                        ServerClient c=clients.get(i);
                        if(!clientResponse.contains(c.getID()))
                        {
                            if(c.attempt>MAX_ATTEMPT)
                                disconnect(c.getID(),false);
                            else
                                c.attempt++;
                        }
                        else
                        {
                            clientResponse.remove(new Integer(c.getID()));
                        }
                    }
                }
            }
        };
        manage.start();
    }
    
    private void receive()
    {
        receive=new Thread("receive"){
            public void run()
            {
                while(running)
                {

                    byte[] data = new byte[1024];
                    DatagramPacket packet=new DatagramPacket(data,data.length);
                    try {
                        socket.receive(packet);
                        
                        //socket.send(packet);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String message=new String(packet.getData());
                    process(packet);
                    
                    //cl.console(message);
                }
            }
        };
        receive.start();
    }
    public void onlineUsers()
    {
        if(clients.size()==0)return;
        String users="/u/";
        for(int i=0;i<clients.size()-1;i++)
            users+=clients.get(i).username.toString()+"/m/";
        //System.out.println(clients.get(clients.size()-1).username);
        users+=clients.get(clients.size()-1).username.toString()+"/e/";
        System.out.println(users);
        sendToAll(users);
    }
    private void sendToAll(String message){
        for(int i=0;i<clients.size();i++)
        {
            ServerClient client=clients.get(i);
            send(message.getBytes(),client.address,client.port);
        }
    }
    private void send(final byte[] data,final InetAddress address,int port){
        send=new Thread("send"){
            public void run(){
                DatagramPacket packet=new DatagramPacket(data,data.length,address,port);//write address on the letter
                try {
                    socket.send(packet);//socket is a post office which send the letter we have written address on packet
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        send.start();
    }
    private void process(DatagramPacket packet)
    {
        String string=new String(packet.getData());
        if(string.startsWith("/c/"))
        {
            UniqueIdentifier UUID= new UniqueIdentifier();
            int id=UUID.getIdentifier();
             clients.add(new ServerClient(string.split("/c/|/e/")[1],packet.getAddress(),id,packet.getPort()));
            System.out.println(clients.get(0).address.toString() + clients.get(0).port);
            String ack_msg="/c/"+id+"/e/";
            System.out.println(ack_msg);
            send(ack_msg.getBytes(),packet.getAddress(),packet.getPort());
        }
        else if(string.startsWith("/m/"))
        {
            //String message=string.substring(3,string.length())+"/e/";
            sendToAll(string);
        }
        else if(string.startsWith("/d/"))
        {
            boolean status=true;
            int id=Integer.parseInt(string.split("/d/|/e/")[1]);
            disconnect(id,status);
        }
        else if(string.startsWith("/i/"))
        {
            clientResponse.add(Integer.parseInt(string.split("/i/|/e/")[1]));
        }
        
    }
    public void disconnect(int id,boolean status)
    {
        for(int i=0;i<clients.size();i++)
            {
            if(id==clients.get(i).getID())
                {clients.remove(i);status=false;System.out.println(id);}
            }
            if(!status)
            {
                String mes="/d/"+id+"/e/";
                sendToAll(mes);
            }
    }
}
