package distributed_system_thread.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class DistributedServer extends Thread {
    private static List<Socket> clientSockets = new ArrayList<>();
    private static ServerSocket serverSocket;

    public void run() {
        final int PORT = 12345;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor esperando por conexões na porta " + PORT);

            while (true) {
                var clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);

                var clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;

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
    }

    private static void broadcastMessage(String message) {
        for (Socket socket : clientSockets) {
            try {
                var socketOut = new PrintWriter(socket.getOutputStream(), true);
                socketOut.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
