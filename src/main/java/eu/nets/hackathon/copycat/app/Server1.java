package eu.nets.hackathon.copycat.app;

import eu.nets.hackathon.copycat.CopycatNode;

public class Server1 {
    public static void main(String[] args) {
        CopycatNode server = new CopycatNode("server1", 4001, 8081, "server2", "server3");
        server.start();

    }
}
