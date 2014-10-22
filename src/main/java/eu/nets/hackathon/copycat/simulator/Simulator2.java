package eu.nets.hackathon.copycat.simulator;

import eu.nets.hackathon.copycat.KeyValueStore;
import net.kuujo.copycat.Copycat;
import net.kuujo.copycat.StateMachine;
import net.kuujo.copycat.cluster.TcpCluster;
import net.kuujo.copycat.cluster.TcpClusterConfig;
import net.kuujo.copycat.cluster.TcpMember;
import net.kuujo.copycat.log.InMemoryLog;
import net.kuujo.copycat.log.Log;
import net.kuujo.copycat.protocol.VertxTcpProtocol;
import net.kuujo.copycat.spi.protocol.AsyncProtocol;

public class Simulator2 {

    public static void main(String[] args) {

        Log log = new InMemoryLog();

        // Configure the cluster.
        TcpClusterConfig config = new TcpClusterConfig();
        config.setLocalMember(new TcpMember("localhost", 1236));
        config.setRemoteMembers(new TcpMember("localhost", 1234), new TcpMember("localhost", 1235));
//        config.setRemoteMembers(new TcpMember("10.40.24.116", 1234), new TcpMember("localhost", 4567));


        // Create the cluster.
        TcpCluster cluster = new TcpCluster(config);

        // Create the protocol.
        AsyncProtocol protocol = new VertxTcpProtocol();

        // Create a state machine instance.
        StateMachine stateMachine = new KeyValueStore();

        // Create a Copycat instance.
//        AsyncCopycat copycat = new AsyncCopycat(stateMachine, log, cluster, protocol);
//        copycat.start().thenRun(() -> {
//            System.out.println("************************");
//            System.out.println("Server started ");
//            System.out.println("************************");
//        });
//
//        copycat.submit("set", "foo", "Hello world!").thenRun(() -> {
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Hello !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        });

        Copycat copycat = new Copycat(stateMachine, log, cluster, protocol);
        copycat.start();
        copycat.submit("put", "foo", "HEllo");

        final Object submit = copycat.submit("get", "foo");
        System.out.println("submit = " + submit);

//        copycat.submit("get", "foo").whenComplete((o,throwable) -> {
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! GET !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        });
        
    }
}
