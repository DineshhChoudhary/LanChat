/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchat.server;

import java.net.InetAddress;

/**
 *
 * @author Dinesh
 */
public class ServerClient {
    public String username;
    public InetAddress address;
    private final int ID;
    public int attempt=0;
    public int port;
    
    public ServerClient(String username,InetAddress address,int ID,int port)
    {
        this.username=username;
        this.ID=ID;
        this.address=address;
        this.port=port;
    }
    public int getID(){
        return ID;
    }
}
