package distributed_system_thread.node;

import distributed_system_thread.alg.MessageSender;
import distributed_system_thread.server.DistributedServer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Node extends Thread {
    private final List<Socket> connectedNodes = new ArrayList<>();
    private ServerSocket server;
    private final String GENERAL_SERVER_ADDRESS = "localhost";

    public Node(final int PORT) {
        try {
            server = new ServerSocket(PORT);
        } catch (IOException ignored) {

        }
    }

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
                var clientSocket = server.accept();
                connectedNodes.add(clientSocket);

                var clientHandler = new Node.ClientHandler(clientSocket);
                clientHandler.start();

//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    System.out.println("Sou a thread " + super.getId() + " e recebi a mensagem: " + inputLine);
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNodeConnection(Node node) {
        var nodePort = node.getPort();
        try(var socket = new Socket(GENERAL_SERVER_ADDRESS, nodePort)) {
            connectedNodes.add(socket);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private List<Socket> clients = new ArrayList<>();

        public void addClient(Socket client) {
            clients.add(client);
        }

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                    var out = new PrintWriter(clientSocket.getOutputStream(), true);
                    var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    broadcastMessage(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void broadcastMessage(String message) {
            for (Socket socket : clients) {
                try {
                    var socketOut = new PrintWriter(socket.getOutputStream(), true);
                    socketOut.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getPort() {
        return this.server.getLocalPort();
    }
}
