package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String nameClient;
    /**
     * On créer le constructeur de la Classe ClientHandler qui va permettre 
     * de faire le liens entre les N clients créer et le serveur
     * @param socket
     */
    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.nameClient = reader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + nameClient + " viens de rejoindre le chat !");
        } catch (Exception e) {
            closeEverything(socket, reader, writer);
        }
    }


    @Override
    public void run() {
        String messageDuClient;
        while (socket.isConnected()) {
            try {
                messageDuClient = reader.readLine();
                broadcastMessage(messageDuClient);
            } catch (Exception e) {
                closeEverything(socket, reader, writer);
                break;
            }
        }
    }
    /**
     * Méthode permettant d'envoyer les messages à l'enssemble des utilisateur
     * @param message
     */
    public void broadcastMessage(String message){
        for(ClientHandler client: clientHandlers){
            try {
                if(!client.nameClient.equals(nameClient)){
                    client.writer.write(message);
                    client.writer.newLine();
                    client.writer.flush();
                }
            } catch (Exception e) {
                closeEverything(socket, reader, writer);
            }
        }
    }

    public void removeClient(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER : " + nameClient + " à quitter le chat!");
    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer){
        removeClient();
        try {
            if(reader != null){
                reader.close();
            }
            if (writer != null){ 
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}


