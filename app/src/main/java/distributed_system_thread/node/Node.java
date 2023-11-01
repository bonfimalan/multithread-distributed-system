package distributed_system_thread.node;

import distributed_system_thread.alg.MessageSender;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Node extends Thread {
    @Override
    public void run() {
        final String SERVER_ADDRESS = "localhost";
        final int SERVER_PORT = 12345;

        try (
                var socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                var out = new PrintWriter(socket.getOutputStream(), true);
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            System.out.println("Conectado ao servidor. Thread: " + super.getId());
            var sender = new MessageSender(out, super.getId());
            sender.start();

            while (true) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Sou a thread " + super.getId() + " e recebi a mensagem: " + inputLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
