package eu.nets.hackathon.copycat;

import net.kuujo.copycat.cluster.TcpMember;

import java.util.ArrayList;
import java.util.List;

public class Properties {

    public static TcpMember getLocalMember() {
        return new TcpMember("localhost", getInteger("local.port"));
    }

    public static List<TcpMember> getRemoteMembers() {
        List<TcpMember> members = new ArrayList<>();

        members.add(new TcpMember(getString("remote.member1.host"), getInteger("remote.member1.port")));
        members.add(new TcpMember(getString("remote.member2.host"), getInteger("remote.member2.port")));

        return members;

    }

    private static String getString(String s) {
        String property = System.getProperty(s);
        return property;
    }

    private static Integer getInteger(String s) {
        String property = System.getProperty(s);
        return Integer.parseInt(property);
    }
}
