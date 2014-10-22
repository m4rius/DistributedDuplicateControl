package eu.nets.hackathon.copycat.app;

import eu.nets.hackathon.copycat.CopycatNode;
import net.kuujo.copycat.service.VertxHttpService;

public class Server1 {
    public static void main(String[] args) {
        CopycatNode server = new CopycatNode("server1", 4001, "server2", "server3");
        server.start();

        //Create an HTTP service and start it.
        VertxHttpService service = new VertxHttpService(server.copycat, "localhost", 8080);
        service.start().thenRun(() -> System.out.println("VertxHttpService Started = " + service));
    }
}
