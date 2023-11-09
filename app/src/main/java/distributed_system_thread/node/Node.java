package distributed_system_thread.node;

import distributed_system_thread.alg.MessageSender;
import distributed_system_thread.port.PortHandlerService;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Node extends Thread {
    private final ServerSocket server;
    private final PortHandlerService portHandler;

    public Node(ServerSocket server, PortHandlerService portHandler) {
        this.server = server;
        this.portHandler = portHandler;
    }

    @Override
    public void run() {
//        final String SERVER_ADDRESS = "localhost";
//        final int SERVER_PORT = 12345;
//
//        try (
//                var socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
//                var out = new PrintWriter(socket.getOutputStream(), true);
//                var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        ) {
//            System.out.println("Conectado ao servidor. Thread: " + super.getId());
//            var sender = new MessageSender(out, super.getId());
//            sender.start();
//
//            while (true) {
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    System.out.println("Sou a thread " + super.getId() + " e recebi a mensagem: " + inputLine);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // start sender here
        while (true) {
            try {
                var client = server.accept();

                var receiveHandler = new ReceiveHandler(client, super.getId());
                receiveHandler.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
