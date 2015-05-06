import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kegan on 5/2/15.
 */
public class Deck {
    ArrayList<Card> deck = new ArrayList<Card>();
    ArrayList<Card> discardPile = new ArrayList<Card>();

    public Deck(int deckCount){
        for(int c = 0; c < deckCount;c++) {
            for (int i = 1; i <= 13; i++) {
                deck.add(new Card(i, 1));
                deck.add(new Card(i, 2));
                deck.add(new Card(i, 3));
                deck.add(new Card(i, 4));
            }
        }
    }
    public void printDeck(){
        for (Card crd : deck){
            System.out.println(crd.getRank() + " of " + crd.getSuit());
        }
    }
    public void shuffle(){
        Collections.shuffle(deck);
    }
    //draw a card and return it from the deck. If deck size is 0, reset the deck
    public Card draw(){
        Card temp = deck.remove(0);
        if(deck.size() ==0){
            resetDeck();
        }
        return temp;
    }
    public Card topDiscardCard(){
        return discardPile.get(discardPile.size()-1);
    }
    public void discard(Card crd){
        discardPile.add(crd);
    }
    //add all discard pile cards back to the deck except for the top one
    public void resetDeck(){
        System.out.println("No more card in deck. Placing all discarded cards except the top card back into deck and shuffle. ");
        deck.addAll(discardPile);
        shuffle();
        Card temp = topDiscardCard();
        deck.remove(temp);
        discardPile.clear();
        discardPile.add(temp);
    }

}
