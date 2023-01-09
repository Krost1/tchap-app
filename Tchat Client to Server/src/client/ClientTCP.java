package client;


import java.io.*;
import java.net.*;
import java.util.*;


public class ClientTCP {
    public static void main(String[] args) throws IOException {
        //! recuperation de l'adresse internet
        //! null permet de tester les apllis sur une machine unique
        InetAddress addr = InetAddress.getByName(null);
        try(
            Socket socket = new Socket(addr, 8888);
            //! recuperation du flux d'entree
            Scanner in = new Scanner(socket.getInputStream());
            //! recuperation du flux de sortie
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            Scanner scanKeyboard = new Scanner(System.in);
        ){
            System.out.println("client = " + socket);
            String ligne = "";
            while(!(ligne.equalsIgnoreCase("FIN"))){
                //! lecture au clavier
                ligne = scanKeyboard.nextLine();
                //! envoie au server
                out.println(ligne);
                //! attente du retour
                String str = in.nextLine();
                System.out.println("client -> recu du server " + str);
            }
        }
    }
}
