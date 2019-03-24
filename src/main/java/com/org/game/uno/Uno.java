package com.org.game.uno;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Uno {
	static Random rn = new Random();
    static Deal play1 = new Deal();//for the user
    static Deal comp1 = new Deal();//for computer 1
    static Deal comp2 = new Deal();//for computer 2
    static Deal comp3 = new Deal(); //for computer 3
    static CardHandler deck = new CardHandler();
    static UnoHandler unoHandler=new UnoHandler();
    static Scanner s = new Scanner(System.in);
    static Scanner si = new Scanner(System.in);
    static Deal discardPile = new Deal();
    static int numComp = 0;
 // will be used to denote a reverse card played. 
    static boolean reverse = false; 
  //will be used to denote a skip card played
    static boolean skip = false; 
  //will be set to true when the first player runs out of cards. 
    static boolean gameEnded = false; 
  //will be used to denote a draw 2 card played
    static boolean draw2 = false; 
  //will be used to denote a draw 4 card played
    static boolean draw4 = false; 

    public static void main(String[] args) throws InterruptedException {
        deck.shuffleDeck();
        deck.shuffleDeck();
        boolean compNumGot = false;
        do {
            try {
                System.out.print("How many computers do you want to play against?(1-3): ");
                numComp = si.nextInt();
                si.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
                si.nextLine();
            }
            if (numComp <= 3 && numComp >= 1) {
                compNumGot = true;
            } else {
                System.out.println("Please enter a number between 1 and 3");
                System.out.println();
                compNumGot = false;
            }
        } while (!compNumGot);
        if (numComp == 1) {
            for (int i = 0; i <= 6; i++) { //adds 7 cards to each players hand. 
                play1.addCard(deck);
                comp1.addCard(deck);
            }
        } else if (numComp == 2) {
            for (int i = 0; i <= 6; i++) {
                play1.addCard(deck);
                comp1.addCard(deck);
                comp2.addCard(deck);
            }
        } else if (numComp == 3) {
            for (int i = 0; i <= 6; i++) {
                play1.addCard(deck);
                comp1.addCard(deck);
                comp2.addCard(deck);
                comp3.addCard(deck);
            }
        }
        discardPile.addCard(deck);//adds the top card of the deck to the discard pile "face up"
        if (Card.getCardNumber(discardPile.getLast()) == 14) { //the following if-else block will change any wilds to a given color
            discardPile.addCard(unoHandler.wildColor(14,s));
        } else if (Card.getCardNumber(discardPile.getLast()) == 13) {
            discardPile.addCard(unoHandler.wildColor(13,s));
        }
        int currentPlayer = 1; 
        boolean uno = false; 
        do { 
        	//the following will be done when the current player is 1
            while (currentPlayer == 1) { 
                skip = false; 
                draw2 = false; 
                draw4 = false; 
                Thread.sleep(1500);
                boolean unoCalled = false;
                int cardPlayed = 0; 
                System.out.println(); 
                int choice = 0; 
                Boolean drawCard = false; 
                do {
                    uno = unoHandler.checkUno(play1); 
                    System.out.println(); 
                    unoHandler.printHand(play1, drawCard, uno, unoCalled); //show the user what is in their hand
                    System.out.println(); 
                    unoHandler.printDiscard(discardPile);
                    choice = unoHandler.getCardNumber(si);
                    while (choice > play1.getSize() + 2 || choice < 0) { 
                        System.out.println("Invalid Input");
                        choice = unoHandler.getCardNumber(si);
                    }
                    int elem = choice - 1; 
                    if (choice == (play1.getSize() + 1)) {
                        if (!drawCard) {
                            play1.addCard(deck);
                            drawCard = true; 
                        } else if (drawCard) {
                            cardPlayed = 1;
                        }
                    } else if (choice == (play1.getSize() + 2)) {
                        if (!unoCalled) { 
                            System.out.println(); //spacing
                            System.out.println("Player 1 Calls Uno");
                            unoCalled = true;
                        } else if (unoCalled || !uno) {
                            System.out.println();
                            System.out.println("Please Select a Valid Card");
                        }
                    } else if (Card.getCardColor(play1.getCard(elem)) == 'a' && Card.getCardNumber(play1.getCard(elem)) == 13) { 
                        discardPile.addCard(unoHandler.wildColor(13,s)); 
                        play1.removeCard(elem);
                        cardPlayed = 1;
                    } else if (Card.getCardColor(play1.getCard(elem)) == 'a' && Card.getCardNumber(play1.getCard(elem)) == 14) { //wild draw 4. 
                        discardPile.addCard(unoHandler.wildColor(14,s));
                        draw4 = true;
                        play1.removeCard(elem);
                        cardPlayed = 1;
                    } else if (Card.getCardColor(play1.getCard(elem)) == Card.getCardColor(discardPile.getLast()) || Card.getCardNumber(play1.getCard(elem)) == Card.getCardNumber(discardPile.getLast())) {
                        discardPile.addCard(play1.getCard(elem));
                        switch (Card.getCardNumber(play1.getCard((elem)))) {
                            case 10:
                                skip = true;
                                break;
                            case 11:
                                draw2 = true;
                                break;
                            case 12:
                                if (reverse) {
                                    reverse = false;
                                } else if (!reverse) {
                                    reverse = true;
                                }
                                break;
                            default:
                                break;
                        }
                        play1.removeCard(elem);
                        cardPlayed = 1;
                    } else {
                        System.out.println();
                        System.out.println("Please Select a valid card");
                    }
                } while (cardPlayed == 0);
                if (play1.getSize() == 1 && !unoCalled) {
                    System.out.println("Player 1 did not call uno. +2");
                    play1.addCard(deck);
                    play1.addCard(deck);
                }
                if (play1.getSize() > 1 && unoCalled) {
                    System.out.println("Player 1 falsely called uno. +2");
                    play1.addCard(deck);
                    play1.addCard(deck);
                }
                if (play1.getSize() == 0) {
                    System.out.println();
                    System.out.println("Player 1 won");
                    gameEnded = true;
                    System.exit(0);
                }
                if (numComp == 1) {
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 1;
                    } else if (reverse && !skip || !reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                        	unoHandler.draw2(comp1,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp1,deck);
                        }
                    }
                } else if (numComp == 2) {
                    if (reverse && skip) {
                        currentPlayer = 2;
                    } else if (!reverse && skip) {
                        currentPlayer = 3;
                    } else if (reverse && !skip) {
                        currentPlayer = 3;
                        if (draw2) {
                        	unoHandler.draw2(comp2,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp2,deck);
                        }
                    } else if (!reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                        	unoHandler.draw2(comp1,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp1,deck);
                        }
                    }
                } else if (numComp == 3) { //PLAYER 1
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 3;
                    } else if (!reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                        	unoHandler.draw2(comp1,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp1,deck);
                        }
                    } else if (reverse && !skip) {
                        currentPlayer = 4;
                        if (draw2) {
                        	unoHandler.draw2(comp3,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp3,deck);
                        }
                    } else {
                        System.out.println("ERROR");
                    }
                }
                unoHandler.checkDraw(deck, discardPile);
                System.out.println();
                break;
            }
            while (currentPlayer == 2) {
                int compNumber = 1;
                unoHandler.computerPlay(deck,discardPile,comp1, skip, draw2, draw4, uno, reverse, gameEnded, compNumber);
                if (numComp == 1) { //PLAYER 2
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 2;
                    } else if (reverse && !skip || !reverse && !skip) {
                        currentPlayer = 1;
                        if (draw2) {
                        	unoHandler.draw2(play1,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(play1,deck);
                        }
                    }
                } else if (numComp == 2) { //PLAYER 2 CURRENTLY
                    if (reverse && skip) {
                        currentPlayer = 3;
                    } else if (!reverse && skip) {
                        currentPlayer = 1;
                    } else if (reverse && !skip) {
                        currentPlayer = 1;
                        if (draw2) {
                        	unoHandler.draw2(play1,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(play1,deck);
                        }
                    } else if (!reverse && !skip) {
                        currentPlayer = 3;
                        if (draw2) {
                        	unoHandler.draw2(comp2,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp2,deck);
                        }
                    }
                } else if (numComp == 3) {
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 4;
                    } else if (!reverse && !skip) {
                        currentPlayer = 3;
                        if (draw2) {
                        	unoHandler.draw2(comp2,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp2,deck);
                        }
                    } else if (reverse && !skip) {
                        currentPlayer = 1;
                        if (draw2) {
                        	unoHandler.draw2(play1,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(play1,deck);
                        }
                    }
                }
                unoHandler.checkDraw(deck, discardPile);
                break;

            }
            while (currentPlayer == 3) {
                int compNumber = 2;
                unoHandler.computerPlay(deck,discardPile,comp2, skip, draw2, draw4, uno, reverse, gameEnded, compNumber);
                if (numComp == 2) { //COMPUTER 2 CURRENTLY
                    if (reverse && skip) {
                        currentPlayer = 3;
                    } else if (!reverse && skip) {
                        currentPlayer = 1;
                    } else if (reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                        	unoHandler.draw2(comp1,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp1,deck);
                        }
                    } else if (!reverse && !skip) {
                        currentPlayer = 1;
                        if (draw2) {
                        	unoHandler.draw2(comp3,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp3,deck);
                        }
                    }
                } else if (numComp == 3) { //player 3
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 1;
                    } else if (!reverse && !skip) {
                        currentPlayer = 4;
                        if (draw2) {
                        	unoHandler.draw2(comp3,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp3,deck);
                        }
                    } else if (reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                        	unoHandler.draw2(comp1,deck);
                        }
                        if (draw4) {
                        	unoHandler.draw4(comp1,deck);
                        }
                    }
                }
                unoHandler.checkDraw(deck, discardPile);
                break;

            }
            while (currentPlayer == 4) {
                int compNumber = 3;
                unoHandler.computerPlay(deck,discardPile,comp3, skip, draw2, draw4, uno, reverse, gameEnded, compNumber);
                if (reverse && skip || !reverse && skip) {
                    currentPlayer = 2;
                } else if (!reverse && !skip) {
                    currentPlayer = 1;
                    if (draw2) {
                    	unoHandler.draw2(comp1,deck);
                    }
                    if (draw4) {
                    	unoHandler.draw4(comp1,deck);
                    }
                } else if (reverse && !skip) {
                    currentPlayer = 3;
                    if (draw2) {
                    	unoHandler.draw2(comp3,deck);
                    }
                    if (draw4) {
                    	unoHandler.draw4(comp3,deck);
                    }
                }
                unoHandler.checkDraw(deck, discardPile);
                break;

            }
        } while (!gameEnded);
    }

    
}
