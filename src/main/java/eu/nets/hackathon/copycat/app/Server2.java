package eu.nets.hackathon.copycat.app;

import eu.nets.hackathon.copycat.CopycatNode;

public class Server2 {
    public static void main(String[] args) {
        CopycatNode server = new CopycatNode("server2", 4002, 8082, "server1", "server3", "server4");
        server.start();
    }
}
