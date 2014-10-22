package eu.nets.hackathon.copycat.app;

import eu.nets.hackathon.copycat.CopycatNode;

public class Server3 {
    public static void main(String[] args) {
        CopycatNode server = new CopycatNode("server3", 4003, 8083, "server1", "server2");
        server.start();


    }
}
