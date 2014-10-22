package eu.nets.hackathon.copycat;

import net.kuujo.copycat.Command;
import net.kuujo.copycat.Query;
import net.kuujo.copycat.StateMachine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UniqueKeyValueStore implements StateMachine {

    private static final Logger log = Logger.getLogger(UniqueKeyValueStore.class.getName());

    private List<String> data = new ArrayList<>();

    @Override
    public byte[] takeSnapshot() {
        try {
            System.out.println("takeSnapshot");
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(data);
            return byteOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void installSnapshot(byte[] data)  {
        try {
            System.out.println("installSnapshot");
            ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(byteIn);
            this.data = (List<String>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Query
    public Object getAll() {
        StringBuilder b = new StringBuilder();
        for (String s : data) {
            b.append(s).append(" ");
        }
        
        return b.toString();
    }

//    @Query
//    public Object size() {
//        return data.size();
//    }

    @Command
    public String set(String value) {
        log.info(String.format("Set %s", value));
        System.out.println("Set Called *****************************'");
        if (!data.contains(value)) {
            data.add(value);
            return "OK";
        } else {
            return "DUPLICATE!!!!!!";
        }
    }

    @Command
    public void delete(String key) {
        data.remove(key);
    }

}
