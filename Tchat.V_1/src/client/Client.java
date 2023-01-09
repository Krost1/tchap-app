package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String pseudo;

    public Client (Socket socket, String name) {
        try {
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pseudo = name;
        } catch (Exception e) {
            closeEverything(socket,reader,writer);
        }
    }

    public void send(){
        try {
            writer.write(pseudo);
            writer.newLine();
            writer.flush();

            try (Scanner scanner = new Scanner(System.in)) {
                while(socket.isConnected()){
                    String message = scanner.nextLine();
                    writer.write(pseudo + " : " + message);
                    writer.newLine();
                    writer.flush();
                }
            }
        } catch (Exception e) {
            closeEverything(socket, reader,writer);
        }
    }

    public void lireMessage(){
        new Thread( new Runnable() {
            @Override
            public void run() {
                String msg;
                while(socket.isConnected()){
                    try {
                        msg = reader.readLine();
                        System.out.println(msg);
                    } catch (Exception e) {
                        closeEverything(socket, reader, writer);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer){
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

    public static void main(String[] args){
        
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Entrer votre pseudo : ");
            String name = scanner.nextLine();
            Socket socket = new Socket("localhost",8888);
            Client client = new Client(socket, name);
            client.lireMessage();
            client.send();
        }catch(Exception e){
           e.printStackTrace();;
        }
    }
}
