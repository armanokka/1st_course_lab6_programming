package network;

import commands.Save;
import handlers.CollectionHandler;
import handlers.CommandHandler;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

public class Server {
    private InetAddress address;
    private int port;

    private static final Logger serverLogger = Logger.getLogger("logger");

    private DatagramSocket socket;

    private CollectionHandler collectionHandler = new CollectionHandler();

    BufferedInputStream bf = new BufferedInputStream(System.in);
    BufferedReader reader = new BufferedReader(new InputStreamReader(bf));

    public Server(String address, int port) throws Exception {
         this.socket = new DatagramSocket(port);
         socket.setSoTimeout(0); // non-blocking mode
         this.port = port;
         this.address = InetAddress.getByName(address);
    }

    public void Run() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    System.out.println("\nSaving collection...");
                    new Save().execute(collectionHandler, null, reader);
                    System.out.println("Shutdown successfully");
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });

        Thread consoleReader = new Thread(()->{
            serverLogger.info("Server launched");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
            }
            while (true) {
                System.out.print("shell>>");
                try {
                    var line = reader.readLine();
                    System.out.println(CommandHandler.process(line, this.collectionHandler, this.reader));
                } catch (Exception e) {
                    serverLogger.warning(e.toString());
                }
            }
        });
        consoleReader.start();

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                this.socket.receive(packet);
                serverLogger.info("received new request");
                var is = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(packet.getData())));
                try {
                    var cmd = (Request) is.readObject();
                    if (cmd.getRoute() != null) {
                        var r = cmd.getRoute();
                        var collection = collectionHandler.getCollection();
                        collection.put(r.getKey(), r);
                        collectionHandler.updateCollection(collection);

                        serverLogger.info("response sent");
                        var response = "ok".getBytes();
                        socket.send(new DatagramPacket(response, response.length, address, 2000));
                        continue;
                    }
                    var response = CommandHandler.process(String.join(" ", cmd.getArgs()), this.collectionHandler, reader).getBytes();
                    serverLogger.info("response sent");
                    socket.send(new DatagramPacket(response, response.length, address, 2000));
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (Exception e) {
                serverLogger.warning(e.toString());
            }
        }
    }
}
