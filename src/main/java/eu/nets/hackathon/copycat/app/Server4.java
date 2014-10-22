package eu.nets.hackathon.copycat.app;

import eu.nets.hackathon.copycat.CopycatNode;

public class Server4 {
    public static void main(String[] args) {
        CopycatNode server = new CopycatNode("server4", 4004, 8084, "server1", "server2", "server3");
        server.start();
    }
}
