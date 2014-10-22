package eu.nets.hackathon.copycat;

import net.kuujo.copycat.AsyncCopycat;
import net.kuujo.copycat.StateMachine;
import net.kuujo.copycat.cluster.EventBusCluster;
import net.kuujo.copycat.cluster.EventBusClusterConfig;
import net.kuujo.copycat.cluster.EventBusMember;
import net.kuujo.copycat.event.Events;
import net.kuujo.copycat.log.InMemoryLog;
import net.kuujo.copycat.log.Log;
import net.kuujo.copycat.protocol.VertxEventBusProtocol;
import net.kuujo.copycat.service.VertxHttpService;
import net.kuujo.copycat.spi.protocol.AsyncProtocol;

public class CopycatNode {

    private final Log log;
    private final EventBusCluster cluster;
    private final AsyncProtocol protocol;
    private final StateMachine stateMachine;
    public final AsyncCopycat copycat;
    private final VertxHttpService service;

    public CopycatNode(String localMember, Integer vertexPort, int httpPort, String... remoteMembers) {
        log = new InMemoryLog();

        EventBusClusterConfig config = new EventBusClusterConfig();
        config.setLocalMember(new EventBusMember(localMember));

        for (String remoteMember : remoteMembers) {
            config.addRemoteMember(new EventBusMember(remoteMember));
        }

        cluster = new EventBusCluster(config);
        protocol = new VertxEventBusProtocol("localhost", vertexPort);

        // Create a state machine instance.
        stateMachine = new UniqueKeyValueStore();

        // Create a Copycat instance.
        copycat = new AsyncCopycat(stateMachine, log, cluster, protocol);

        //Create an HTTP service and start it.
        service = new VertxHttpService(copycat, "localhost", httpPort);

    }

    public void start() {
        copycat.start().thenRun(() -> {
            System.out.println("************************");
            System.out.println("Server started ");
            System.out.println("************************");
        });

        copycat.event(Events.STATE_CHANGE).registerHandler((event) -> {
            System.out.println(String.format("------------ %s members", event.state()));
        });

        service.start().thenRun(() -> System.out.println("VertxHttpService Started = " + service));
    }

}
