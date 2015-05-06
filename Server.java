/**
 Server.java
 Created by Shien Hong on 5/1/15.
 Description: Server for the crazy8
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
    public static String getSuit(int i){
        switch (i){
            case 1:
                return "Spades";
            case 2:
                return "Hearts";
            case 3:
                return "Diamonds";
            case 4:
                return "Clubs";
        }
        return "input not valid";
    }
    public static void main(String[] args) throws IOException,ClassNotFoundException
    {
        ServerSocket serverSocket = null;
        int port;
        Scanner stdIn = new Scanner(System.in);
        if(args.length == 1){
            port = Integer.parseInt(args[0]);
        }else{
            System.out.println("Please enter the server port:");
            port = stdIn.nextInt();
        }
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: "+port + ".");
            System.exit(1);
        }
        
        //asking for game options
        System.out.println("How many players? 1 for playing against AI");
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
        //initialize deck
        Deck deck = new Deck(playerCount/4+1);//add a deck for every 4 additional players
        deck.shuffle();
        
        //initialize AI if needed
        AI cpu = new AI(drawOption,deck);
        
        //shuffle and distribute cards
        System.out.println("Distributing cards");
        for(int i = 0 ; i < 5;i++){
            for(int j=0; j < playerCount; j ++){
                out[j].writeObject(deck.draw());
            }
            if(playerCount==1) { //draw card for ai
                cpu.add(deck.draw());
            }
        }
        
        
        
        deck.discard(deck.draw());
        Card topDiscardCard = deck.topDiscardCard();
        for(int i = 0 ; i < playerCount;i++){
            out[i].writeObject(deck.topDiscardCard()); // sending top discard card info to the players
        }
        System.out.println("The top card of the discard pile is: " + topDiscardCard.getName());
        
        boolean finish = false;
        int winner = -2;
        int round = 1;
        boolean eight = false;
        int chosenSuit = -1;
        
        
        
        //start Rounds
        while(!finish) {
            System.out.println("Round " +round);
            for (int i = 0; i < playerCount; i++) {
                //send player message
                System.out.println("Player " + i + "'s turn to play");
                if(eight){
                    out[i].writeObject("EIGHT");
                    out[i].writeObject(chosenSuit);
                }else {
                    out[i].writeObject("TURN");
                }
                
                out[i].writeObject(deck.topDiscardCard()); //update top discard card for players
                boolean turnOver = false;  //whether a player's turn is over
                boolean maxDraw = false; //whether a player has reached maximum drawing limit
                eight = false;
                
                //process player message
                while (!turnOver) {
                    Object playerReply = in[i].readObject();
                    switch ((char)((String) playerReply).charAt(0)){
                        case 'P': //card played
                            turnOver = true;
                            break;
                        case 'E': //eight played
                            turnOver = true;
                            eight = true;
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
                    if(eight){//player has played an eight card
                        chosenSuit = (Integer) in[i].readObject();
                    }else{
                        chosenSuit = 0;
                    }
                    Card playCard = (Card) in[i].readObject();
                    System.out.println("Player " + i + " has played: " + playCard.getName());
                    deck.discard(playCard);
                    //how to handle 8
                }else{
                    System.out.println("Player " + i +" skips this turn");
                }
                if(finish){
                    break;
                }
                if(playerCount == 1){
                    //AI actions
                    Card topDiscardCardAI = deck.topDiscardCard();
                    System.out.println("Current top card from the discard pile is: "+ topDiscardCardAI.getName());
                    if(topDiscardCardAI.rank == 8){ //eight card operation
                        System.out.println("Chosen suit is: " + getSuit(chosenSuit));
                        chosenSuit = cpu.playCard(chosenSuit, 8);
                    }else {
                        chosenSuit = cpu.playCard(topDiscardCardAI.suit,topDiscardCardAI.rank);
                    }
                    
                    if(chosenSuit >= 1 ){//AI played an eight card
                        eight = true;
                    }else{
                        eight = false;
                    }
                    //check if cpu has won the game or not
                    if(cpu.checkWin()){
                        finish = true;
                        winner = -1;
                    }
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
        System.out.println("GAME OVER");
        if(winner == -1){// AI is the winner
            System.out.println("AI wins!");
        }else{
            System.out.println("Player " + winner +" is the winner!");
        }
        
        
        
        closeConnections(clientSockets,in,out);
        serverSocket.close();
    }
}