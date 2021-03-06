/**
 PlayerClient.java
 Created by sehong on 5/1/15.
 Description: client for the crazy8 program
*/
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayerClient {
    public static ArrayList<Integer> playOptions(Card topDiscardCard, ArrayList<Card> hand, int chosenSuit){
        String result="";
        ArrayList<Integer> options = new ArrayList<Integer>();
        int num = 0;
        
        for(int i = 0 ; i < hand.size();i++){
            Card card = hand.get(i);
            if(chosenSuit == -1) { //top discarded card is not eight
                if (card.rank == topDiscardCard.rank || card.suit == topDiscardCard.suit || card.rank == 8) {
                    result += num + ": " + card.getName() + "    ";
                    options.add(i);
                    num++;
                }
            }else{ //top discarded card is eight
                if(card.suit == chosenSuit || card.rank == 8){
                    result += num + ": " + card.getName() + "    ";
                    options.add(i);
                    num++;
                }
            }
        }
        
        return options;
        
        
    }
    public static void printOptions(ArrayList<Card> hand, ArrayList<Integer> options){
        String result = "";
        for(int i= 0 ; i < options.size();i++){
            result += i + ": "+ hand.get(options.get(i)).getName() +"    ";
        }
        System.out.println("Your options are: \n"+result);
    }
    public static int chooseSuit( Scanner stdIn){
        System.out.println("Please choose a suit as top suit:\n1. Spades  2. Hearts  3. Diamonds  4. Clubs");
        int choice = stdIn.nextInt();
        
        while(choice < 1 && choice >4){
            System.out.println("Invalid input. Please choose again.");
            choice = stdIn.nextInt();
        }
        return choice;
    }
    
    public static String displayHand(ArrayList<Card> hand){
        String result = "Your hand is currently:\n";
        for(int i = 0 ; i < hand.size();i++){
            result += hand.get(i).getName() + "    ";
        }
        return result;
    }
    public static void draw(ObjectInputStream in, ObjectOutputStream out, ArrayList<Card> hand){
        try{
            out.writeObject("DRAW");
            hand.add((Card)in.readObject());
        }catch(ClassNotFoundException e){
            
        }catch(IOException ex){
            
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
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String serverHostname;
        int port;
        Scanner stdIn = new Scanner(System.in);
        if(args.length == 2){ //socket configuration with arguments
            serverHostname = args[0];
            port = Integer.parseInt(args[1]);
        }else{//default
            //localhost is 127.0.0.1
            System.out.println("Please enter the server hostname:");
            serverHostname = stdIn.next();
            System.out.println("Please enter the server port:");
            port = stdIn.nextInt();
        }
        
    
        System.out.println ("Attemping to connect to host " +
                            serverHostname + " on port " + port + ".");
        
        Socket echoSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        try {
            // echoSocket = new Socket("taranis", 7);
            echoSocket = new Socket(serverHostname, port);
            out = new ObjectOutputStream(echoSocket.getOutputStream());
            in = new ObjectInputStream(echoSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + serverHostname);
            System.exit(1);
        }
        System.out.println("You have successfully connected to the server!");
        
        //player configuration
        int id = (Integer)in.readObject();
        System.out.println("Your player id is " + id);
        int drawOption = (Integer)in.readObject();
        System.out.println("Draw option is set to " + drawOption + ".  num < 0 means unlimited draw");

        
        ArrayList<Card> hand = new ArrayList<Card>();
        
        System.out.println("Distributing cards");
        for(int i = 0 ; i < 5;i++) {
            try {
                Object receiveObject = in.readObject();
                if (receiveObject instanceof Card) {
                    Card temp = (Card) receiveObject;
                    hand.add(temp);
                }
            }catch (ClassNotFoundException e){
                
            }
            
            
        }
        boolean done = false;
        Card topDiscardCard = (Card)in.readObject();
        System.out.println("Current top card from the discard pile is: " + topDiscardCard.getName());
        System.out.println(displayHand(hand));
        System.out.println("Waiting for your turn to play\n");
        while(!done){
            String serverMsg = (String)in.readObject();
            int drawCount = 0;
            int chosenSuit = -1;
            switch(serverMsg.charAt(0)){
                case 'T': //server message for this player's turn
                    topDiscardCard = (Card)in.readObject();
                    System.out.println("Current top card from the discard pile is: " + topDiscardCard.getName());
                    break;
                case 'E': //server message for eight card as top discard card
                    chosenSuit = (Integer)in.readObject();
                    topDiscardCard = (Card)in.readObject();
                    System.out.println("Current top card from the discard pile is: " + topDiscardCard.getName());
                    System.out.println("Chosen suit: " +getSuit(chosenSuit));
                    break;
                case 'D': //server message for game over
                    done = true;
                    break;
            }
            
            if(!done){
                System.out.println("Your turn to pick");
                
                ArrayList<Integer> options = new ArrayList<Integer>();
                
                while(options.size()==0 && (drawCount < drawOption || drawOption <= 0)) {
                    
                    
                    options = playOptions(topDiscardCard, hand, chosenSuit);
                    
                    if(options.size()==0 && (drawCount < drawOption || drawOption <=0)){
                        System.out.println(displayHand(hand));
                        System.out.println("Don't have any card to play");
                        System.out.println("Drawing");
                        draw(in,out,hand);
                        drawCount++;
                    }
                }
                options = playOptions(topDiscardCard,hand,chosenSuit);
                if(drawCount >= drawOption && options.size()==0 && drawOption > 0 ){
                    System.out.println("Maximum draw count reached. Skipping this turn\n");
                    out.writeObject("MAXDRAW");
                }else {
                    System.out.println(displayHand(hand));
                    printOptions(hand,options);
                    System.out.println("Please select an option by entering its corresponding number");
                    boolean flag = true;
                    int choice = -1;
                    while (flag) {
                        choice = stdIn.nextInt();
                        if (choice >= 0 && choice <= options.size() - 1) {
                            flag = false;
                        } else {
                            System.out.println("Please enter a valid choice");
                        }
                    }
                    Card playCard = hand.get(options.get(choice));
                    System.out.println("Played: " + playCard.getName());
                    hand.remove((int)options.get(choice)); //Cast int so arraylist actually removes the index instead of object
                    if (hand.size() == 0) {
                        out.writeObject("FINISHED");
                    } else {
                        //check if played card is an eight
                        if(playCard.rank == 8){
                            out.writeObject("EIGHT");
                            chosenSuit = (Integer)chooseSuit(stdIn);
                            out.writeObject(chosenSuit);
                        }else {
                            out.writeObject("PLAYED");
                        }
                        out.writeObject(playCard);
                    }
                    
                    System.out.println("Waiting for your next turn\n");
                }
            }
        }
        System.out.println("GAME OVER");
        int winner = -2;
        winner = (Integer)in.readObject();
        if(winner == -1){//AI is the winner
            System.out.println("AI wins!");
        }else {
            System.out.println("The winner is Player " + winner + "!");
        }
        
        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}
