package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurTCP {
 
    public static void main(String[] args) throws IOException {
        //! Create socket server
        try(ServerSocket serveur = new ServerSocket(8888);){
             System.out.println("Socket server started");
             try(
                //! wait for connection
                Socket socket = serveur.accept();
                //! recuperation du flux d'entree
                Scanner in = new Scanner(socket.getInputStream());
                //! recuperation du flux de sortie
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                ){
                    System.out.println("Connection established" + socket);
                    String str = "";
                    //! wait for data
                    while(!(str.equalsIgnoreCase("FIN"))){
                        //! wait for text
                        str = in.nextLine();
                        System.out.println("serveur -> message recu : "+str);
                        //! retour a l'envoyeur
                        out.println(str);
                    }
                }
        }
    }
}
