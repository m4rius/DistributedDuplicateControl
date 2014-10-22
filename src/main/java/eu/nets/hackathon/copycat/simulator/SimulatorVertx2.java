package eu.nets.hackathon.copycat.simulator;

import eu.nets.hackathon.copycat.KeyValueStore;
import net.kuujo.copycat.AsyncCopycat;
import net.kuujo.copycat.StateMachine;
import net.kuujo.copycat.cluster.EventBusCluster;
import net.kuujo.copycat.cluster.EventBusClusterConfig;
import net.kuujo.copycat.cluster.EventBusMember;
import net.kuujo.copycat.event.Events;
import net.kuujo.copycat.log.InMemoryLog;
import net.kuujo.copycat.log.Log;
import net.kuujo.copycat.protocol.VertxEventBusProtocol;
import net.kuujo.copycat.spi.protocol.AsyncProtocol;

public class SimulatorVertx2 {

    public static void main(String[] args) {
        // Create the local file log.
//        Log log = new ChronicleLog("data.log");
        Log log = new InMemoryLog();

        // Configure the cluster.
//        TcpClusterConfig config = new TcpClusterConfig();
//        config.setLocalMember(new TcpMember("localhost", 1234));
//        config.setRemoteMembers(new TcpMember("localhost", 1236), new TcpMember("localhost", 4567));
//        config.setRemoteMembers(new TcpMember("10.40.24.116", 1234), new TcpMember("localhost", 4567));

        EventBusClusterConfig config = new EventBusClusterConfig();
        config.setLocalMember(new EventBusMember("baz"));
        config.addRemoteMember(new EventBusMember("foo"));
        config.addRemoteMember(new EventBusMember("bar"));

        EventBusCluster cluster = new EventBusCluster(config);

        // Create the cluster.
//        TcpCluster cluster = new TcpCluster(config);

        // Create the protocol.
        AsyncProtocol protocol = new VertxEventBusProtocol("localhost", 9070);

        // Create a state machine instance.
        StateMachine stateMachine = new KeyValueStore();

        // Create a Copycat instance.
        AsyncCopycat copycat = new AsyncCopycat(stateMachine, log, cluster, protocol);
        copycat.start().thenRun(() -> {
            System.out.println("************************");
            System.out.println("Server started ");
            System.out.println("************************");
        });

        copycat.event(Events.STATE_CHANGE).registerHandler((event) -> {
            System.out.println(String.format("------------ %s members", event.state()));
        });

//        Copycat copycat = new Copycat(stateMachine, log, cluster, protocol);
//        copycat.start();
//        copycat.submit("put", "foo", "Hello world");
//
//        final String submit = copycat.submit("get", "foo");
//        System.out.println("submit = " + submit);

        //Create an HTTP service and start it.
//        VertxHttpService service = new VertxHttpService(copycat, "localhost", 8080);
//        service.start().thenRun(() -> System.out.println("VertxHttpService Started = " + service));
//
//        System.out.println("HTTP DOne");
    }



}
