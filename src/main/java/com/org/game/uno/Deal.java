package com.org.game.uno;

import java.util.ArrayList;

public class Deal extends CardHandler{
    private ArrayList<Card> hand;
    public Deal(){
        hand = new ArrayList<Card>();
    }
    public void addCard(CardHandler deck){
        hand.add(0,deck.getLast());
        deck.removeLast();
    }
    public void addCard(Card addCard){
        hand.add(addCard);
    }
    public void removeCard(int elem){
        hand.remove(elem);
    }
    public Card getCard(int elem){
        return hand.get(elem);
    }
    @Override
    public int getSize(){
        return hand.size();
    }
    public void printArray(){
        System.out.println(hand.toString());
    }
    public Card getLast(){
        return hand.get(hand.size()-1);
}

}
