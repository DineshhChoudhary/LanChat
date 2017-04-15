/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchat.server;

/**
 *
 * @author Dinesh
 */
public class ServerMain {
    private int port;
    private Server server;
    public ServerMain(int port)
    {
        this.port=port;
        server = new Server(port);
        
    }
    public static void main(String[] args){
        if(args.length!=1)
        {
            System.out.println("Usage: java -jar [Filename.jar] [port]");
            return;
        }
        new ServerMain(Integer.parseInt(args[0]));
    }
}
