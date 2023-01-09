package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.ClientHandler;

public class Server {
    
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    /**
     * Méthode qui permet de créer un server
     */
    public void startServer(){
        try{
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("un nouveau client est connecté");
                ClientHandler handler = new ClientHandler(socket);

                Thread thread = new Thread(handler);
                thread.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Méthode qui permet de fermer le server
     */
    public void closeServer(){
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
