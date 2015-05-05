package project2; /**
 * Created by sehong on 5/1/15.
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server
{
    public static void closeConnections(Socket[] sockets, ObjectInputStream[] in , ObjectOutputStream[] out){
        if(sockets.length != in.length && sockets.length != out.length){
            System.out.println("different connection count");
        }else{
            try{
                for(int i = 0 ; i < sockets.length;i++){
                    sockets[i].close();
                    in[i].close();
                    out[i].close();
                }
            }
            catch(IOException e){

            }
        }


    }
    public static void main(String[] args) throws IOException,ClassNotFoundException
    {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(10007);
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: 10007.");
            System.exit(1);
        }

        //asking for game options
        Scanner stdIn = new Scanner(System.in);
        System.out.println("How many players?");
        int playerCount = Integer.parseInt(stdIn.next());
        System.out.println("Number of Draws before skipping to next person?  num <= 0 : for unlimited drawing");
        int drawOption = Integer.parseInt(stdIn.next());

        Socket[] clientSockets = new Socket[playerCount];
        System.out.println ("Waiting for connection.....");

        //connect to player clients
        try {
            for(int i = 0 ; i < playerCount;i++){
                clientSockets[i] = serverSocket.accept();
                System.out.println("Player "+ i +" connected.");
            }

        }
        catch (IOException e)
        {
            System.err.println("Accept failed.");
            System.exit(1);
        }


        //setting IO with player clients
        System.out.println ("All connection successful");
        System.out.println ("Setting up I/O");
        ObjectInputStream[] in = new ObjectInputStream[playerCount];
        ObjectOutputStream[] out = new ObjectOutputStream[playerCount];
        for(int i=0 ; i < playerCount;i++ ) {
            System.out.println("Assigning player "+ i);
            in[i] = new ObjectInputStream(clientSockets[i].getInputStream());
            out[i] = new ObjectOutputStream(clientSockets[i].getOutputStream());
            out[i].writeObject(i);
        }

        //applying options
        for(int i = 0 ; i < playerCount;i++){
            System.out.println("Applying options to player " + i);
            out[i].writeObject(drawOption);
        }

        //shuffle and distribute cards
        Deck deck = new Deck();
        deck.shuffle();
        System.out.println("Distributing cards");
        for(int i = 0 ; i < 5;i++){
            for(int j=0; j < playerCount; j ++){
                out[j].writeObject(deck.draw());
            }
        }



        deck.discard(deck.draw());
        Card topDiscardCard = deck.topDiscardCard();
        for(int i = 0 ; i < playerCount;i++){
           out[i].writeObject(deck.topDiscardCard());
        }
        System.out.println("The top card of the discard pile is: " + topDiscardCard.getName());

        boolean finish = false;
        int winner = -1;
        int round = 1;
        //start Rounds

        while(!finish) {
            System.out.println("Round " +round);
            for (int i = 0; i < playerCount; i++) {
                System.out.println("Player " + i + "'s turn to play");
                out[i].writeObject("TURN");
                out[i].writeObject(deck.topDiscardCard()); //update top discard card for players
                boolean turnOver = false;
                boolean maxDraw = false;
                while (!turnOver) {
                    Object playerReply = in[i].readObject();
                    switch ((char)((String) playerReply).charAt(0)){
                        case 'P': //card played
                            turnOver = true;
                            break;
                        case 'D': //draw a card
                            out[i].writeObject(deck.draw());
                            break;
                        case 'M': //reached max draw
                            turnOver = true;
                            maxDraw = true;
                            break;
                        case 'F': //hand finished
                            turnOver = true;
                            finish = true;
                            winner = i;
                            break;

                    }
                }
                if(!maxDraw && !finish) {
                    Card playCard = (Card) in[i].readObject();
                    System.out.println("Player " + i + " has played: " + playCard.getName());
                    deck.discard(playCard);
                }else{
                    System.out.println("Player " + i +" skips this turn");
                }
                if(finish){
                    break;
                }

            }

            round++;
            System.out.println();
        }

        //declaring winner
        for(int i = 0 ; i <playerCount;i++){
            out[i].writeObject("DONE");
            out[i].writeObject(winner);
        }
        System.out.println("Game Over\nPlayer " + winner +" is the winner!");


        closeConnections(clientSockets,in,out);
        serverSocket.close();
    }
}