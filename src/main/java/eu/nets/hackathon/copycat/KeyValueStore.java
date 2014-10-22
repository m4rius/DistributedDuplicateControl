package eu.nets.hackathon.copycat;

import net.kuujo.copycat.Command;
import net.kuujo.copycat.Query;
import net.kuujo.copycat.StateMachine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class KeyValueStore implements StateMachine {

    private static final Logger log = Logger.getLogger(KeyValueStore.class.getName());

    private Map<String, Object> data = new HashMap<>();

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
            this.data = (Map<String, Object>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Query
    public Object get(String key) {
        return data.get(key);
    }

    @Command
    public void set(String key, Object value) {
        log.info(String.format("Set %s-%s", key, value));
        System.out.println("Set Called *****************************'");
        data.put(key, value);
    }

    @Command
    public void delete(String key) {
        data.remove(key);
    }

}
