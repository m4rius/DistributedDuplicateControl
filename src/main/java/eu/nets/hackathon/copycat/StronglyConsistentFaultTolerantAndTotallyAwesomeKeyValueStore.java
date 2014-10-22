package eu.nets.hackathon.copycat;

import net.kuujo.copycat.AsyncCopycat;
import net.kuujo.copycat.Command;
import net.kuujo.copycat.Query;
import net.kuujo.copycat.StateMachine;
import net.kuujo.copycat.cluster.TcpCluster;
import net.kuujo.copycat.cluster.TcpClusterConfig;
import net.kuujo.copycat.cluster.TcpMember;
import net.kuujo.copycat.log.ChronicleLog;
import net.kuujo.copycat.log.Log;
import net.kuujo.copycat.protocol.VertxTcpProtocol;
import net.kuujo.copycat.service.VertxHttpService;
import net.kuujo.copycat.spi.protocol.AsyncProtocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class StronglyConsistentFaultTolerantAndTotallyAwesomeKeyValueStore {

    public static void main(String[] args) {
        // Create the local file log.
        Log log = new ChronicleLog("data.log");

        // Configure the cluster.
        TcpClusterConfig config = new TcpClusterConfig();
        config.setLocalMember(new TcpMember("localhost", 1234));
        config.setRemoteMembers(new TcpMember("10.40.16.146", 1234), new TcpMember("localhost", 4567));

        // Create the cluster.
        TcpCluster cluster = new TcpCluster(config);

        // Create the protocol.
        AsyncProtocol protocol = new VertxTcpProtocol();

        // Create a state machine instance.
        StateMachine stateMachine = new KeyValueStore();

        // Create a Copycat instance.
        AsyncCopycat copycat = new AsyncCopycat(stateMachine, log, cluster, protocol);
        copycat.start().thenRun(() -> {
            System.out.println("************************");
            System.out.println("Server started ");
            System.out.println("************************");
        });

        // Create an HTTP service and start it.
        VertxHttpService service = new VertxHttpService(copycat, "localhost", 8080);
        service.start();
    }



}
