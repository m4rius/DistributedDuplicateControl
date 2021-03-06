package eu.nets.hackathon.copycay.simulator;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadTest {


    private static final int NUMBER_OF_THREADS = 100;
    private static final int NO_OF_INVOCATIONS = 100;

    @Test
    public void doStuffInThreads() throws InterruptedException {
        final Random random = new Random(2l);
        final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
            
        final CountDownLatch latch = new CountDownLatch(NUMBER_OF_THREADS);
        
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < NO_OF_INVOCATIONS; j++) {
                        try {
                            int port = 8081 + (j % 3);
                            sendPost("[\"" + j + "\"]", port);
                        } catch (Exception e) {
                            System.out.println("*********** e = " + e);
                        }

                        try {
                            Thread.sleep(random.nextInt(10));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    latch.countDown();
                }
                
            };
            executorService.submit(task);
        }

        latch.await(40, TimeUnit.SECONDS);
        
    }
    
    private String sendPost(String urlParameters, int port) throws Exception {

        String url = String.format("http://localhost:%s/set", port);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);
//        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + urlParameters);
//        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(String.format("Sending 'POST' request to URL : %s %s  Response: %s", url, urlParameters, response));
        return response.toString();

    }


}
