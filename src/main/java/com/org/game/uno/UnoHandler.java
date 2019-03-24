package com.org.game.uno;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UnoHandler {
	public  void printHand(Deal play1, boolean drawCard, boolean uno, boolean unoCalled) {
        int display = 0;
        for (int x = 0; x < play1.getSize(); x++) {
            display = x + 1;
            System.out.println(display + ". " + play1.getCard(x));
        }
        display++;
        if (!drawCard) {
            System.out.println(display + ". Draw Card");
        } else if (drawCard) {
            System.out.println(display + ". End Turn");
        }
        display++;
        if (uno && !unoCalled) {
            System.out.println(display + ". Uno");
        } else if (uno && unoCalled) {
            System.out.println("Uno Called");
        }
    }
/**
 * check the status of if the player can call uno
 * */
    public  boolean checkUno(Deal play) {
        boolean uno = false;
        if (play.getSize() == 2) {
            uno = true;
        } else {
            uno = false;
        }
        return uno;
    }
  //calls to wildcolor to get the appropriate card to add to discard
    public  Card wildColor(int cardNumber,Scanner s) {
        System.out.print("What color do you want the deck to be?: ");
        String input = s.nextLine();
        char color = 'a';
        input = input.toLowerCase();
        switch (input.charAt(0)) {
            case 'b':
                color = 'b';
                break;
            case 'r':
                color = 'r';
                break;
            case 'g':
                color = 'g';
                break;
            case 'y':
                color = 'y';
                break;
            default:
                System.out.println("Type: (b)lue, (g)reen, (r)ed or (y)ellow");
                wildColor(cardNumber,s);
                break;
        }
        return new Card(cardNumber, color);
    }

    public  void draw2(Deal play,CardHandler deck) {
        for (int i = 0; i <= 1; i++) {
            play.addCard(deck);
        }
    }

    public  void draw4(Deal play,CardHandler deck) {
        for (int i = 0; i <= 3; i++) {
            play.addCard(deck);
        }
    }
  //print the discard pile's top card
    public  void printDiscard(Deal discardPile) {
        System.out.print("Discard Pile: ");
        System.out.println(discardPile.getLast());
    }

    public  void checkDraw(CardHandler deck, Deal discardPile) {
        if (deck.getSize() <= 4) {
            System.out.println();
            System.out.println("Shuffling Deck");
            System.out.println();
            for (int i = 0; i < discardPile.getSize(); i++) {
                if (Card.getCardNumber(discardPile.getLast()) == 13 && Card.getCardColor(discardPile.getLast()) != 'a') {
                    discardPile.removeCard(discardPile.getSize() - 1);
                    discardPile.addCard(new Card(13, 'a'));
                } else if (Card.getCardNumber(discardPile.getLast()) == 14 && Card.getCardColor(discardPile.getLast()) != 'a') {
                    discardPile.removeCard(discardPile.getSize() - 1);
                    discardPile.addCard(new Card(14, 'a'));
                } else {
                    deck.addCard(discardPile.getLast());
                    discardPile.removeCard(discardPile.getSize() - 1);
                }
            }
            deck.shuffleDeck();
        } else {

        }

    }

    public  int getComputerChoice(Card dCard, Deal Computer, boolean unoCalled) {
        int choice = 0;
        boolean hWild = false;
        boolean hSkip = false;
        boolean hReverse = false;
        boolean hDTwo = false;
        boolean hDFour = false;
        boolean hPlayable = false;
        hWild = hasWild(dCard, Computer);
        hSkip = hasSkip(dCard, Computer);
        hReverse = hasReverse(dCard, Computer);
        hDTwo = hasDrawTwo(dCard, Computer);
        hDFour = hasDrawFour(dCard, Computer);
        hPlayable = hasPlayable(dCard, Computer);
        if (Computer.getSize() == 2 && !unoCalled) {
            choice = Computer.getSize() + 2;
        } else if (hDTwo) {
            choice = findDTwo(dCard, Computer);
            choice++;
        } else if (hSkip) {
            choice = findSkip(Computer, dCard);
            choice++;
        } else if (hReverse) {
            choice = findReverse(dCard, Computer);
            choice++;
        } else if (hPlayable) {
            choice = findPlayable(dCard, Computer);
            choice++;
        } else if (hWild && !hDTwo && !hSkip && !hReverse && !hPlayable) {
            choice = findWild(Computer);
            choice++;
        } else if (hDFour && !hWild && !hDTwo && !hSkip && !hReverse && !hPlayable) {
            choice = findDrawFour(Computer);
            choice++;
        } else {
            choice = Computer.getSize() + 1;
        }
        return choice;
    }

    public  int findWild(Deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 13) {
                elem = i;
            }
        }
        return elem;
    }

    public  int findDTwo(Card dCard, Deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 11 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                elem = i;
            }
        }
        return elem;
    }

    public  int findSkip(Deal Computer, Card dCard) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 10 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                elem = i;
            }
        }
        return elem;
    }

    public  int findReverse(Card dCard, Deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 12 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                elem = i;
            }
        }
        return elem;
    }

    public  int findDrawFour(Deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 14) {
                elem = i;
            }
        }
        return elem;
    }

    public  int findPlayable(Card dCard, Deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) < 10 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                elem = i;
            }
        }
        return elem;
    }

    public  boolean hasWild(Card dCard, Deal Computer) {
        boolean hWild = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 13) {
                hWild = true;
            }
        }
        return hWild;
    }

    public  boolean hasSkip(Card dCard, Deal Computer) {
        boolean hSkip = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 10 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                hSkip = true;
            }
        }
        return hSkip;
    }

    public  boolean hasReverse(Card dCard, Deal Computer) {
        boolean hReverse = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 12 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                hReverse = true;
            }
        }
        return hReverse;
    }

    public  boolean hasDrawTwo(Card dCard, Deal Computer) {
        boolean hDTwo = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 11 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                hDTwo = true;
            }
        }
        return hDTwo;
    }

    public  boolean hasDrawFour(Card dCard, Deal Computer) {
        boolean hDrawFour = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 14) {
                hDrawFour = true;
            }
        }
        return hDrawFour;
    }

    public  boolean hasPlayable(Card dCard, Deal Computer) {
        boolean hPlayable = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) < 10 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                hPlayable = true;
            }
        }
        return hPlayable;
    }

    public  Card wildComputerColor(int cardNumber, Deal computer) {
        int blue = 0;
        int red = 0;
        int yellow = 0;
        int green = 0;
        char cColor = 'a';
        for (int i = 0; i < computer.getSize(); i++) {
            if (Card.getCardColor(computer.getCard(i)) == 'b') {
                blue++;
            } else if (Card.getCardColor(computer.getCard(i)) == 'r') {
                red++;
            } else if (Card.getCardColor(computer.getCard(i)) == 'y') {
                yellow++;
            } else if (Card.getCardColor(computer.getCard(i)) == 'g') {
                green++;
            }
        }
        if (blue > green && blue > yellow && blue > red) {
            cColor = 'b';
        } else if (green > blue && green > yellow && green > red) {
            cColor = 'g';
        } else if (yellow > green && yellow > blue && yellow > red) {
            cColor = 'y';
        } else if (red > green && red > yellow && red > blue) {
            cColor = 'r';
        } else {
            cColor = 'b';
        }
        return new Card(cardNumber, cColor);
    }

    public  void computerPlay(CardHandler deck,Deal discardPile,Deal comp, boolean skip, boolean draw2, boolean draw4, boolean uno, boolean reverse, boolean gameEnded, int compNumber) throws InterruptedException {
        skip = false;
        int special = 0;
        draw2 = false;
        draw4 = false;
        int choice = 0;
        int cardPlayed = 0; //this is similar to a boolean. 1's and 0's ya know. 
        boolean drawCard = false;
        boolean unoCalled = false;
        do {//do this while card played = 0
            Thread.sleep(2000);
            uno = checkUno(comp);
            Card dCard = discardPile.getLast();
            choice = getComputerChoice(dCard, comp, unoCalled);
            int elem = choice - 1;
            if (choice == (comp.getSize() + 1)) {
                if (!drawCard) {
                    System.out.println("Computer " + compNumber + " has drawn a card");
                    comp.addCard(deck);
                    drawCard = true;
                } else if (drawCard) {
                    System.out.println("Computer " + compNumber + " has ended their turn without playing a card");
                    cardPlayed = 1;
                }
            } else if (choice == (comp.getSize() + 2)) {
                if (!unoCalled) {
                    System.out.println("Computer " + compNumber + " Calls Uno");
                    unoCalled = true;
                }
            } else if (Card.getCardNumber(comp.getCard(elem)) == 13) {
                discardPile.addCard(wildComputerColor(13, comp));
                System.out.println("Computer " + compNumber + " played " + discardPile.getLast());
                comp.removeCard(elem);//remove the card from the player deck. it's no longer needed there
                cardPlayed = 1;
            } else if (Card.getCardNumber(comp.getCard(elem)) == 14) {
                discardPile.addCard(wildComputerColor(14, comp));
                System.out.println("Computer " + compNumber + " played " + discardPile.getLast());
                draw4 = true;
                comp.removeCard(choice - 1);
                cardPlayed = 1;
            } else if (Card.getCardColor(comp.getCard(elem)) == Card.getCardColor(discardPile.getLast()) || Card.getCardNumber(comp.getCard(choice - 1)) == Card.getCardNumber(discardPile.getLast())) {
                discardPile.addCard(comp.getCard(elem));
                switch (Card.getCardNumber(comp.getCard(elem))) {
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
                        } else {

                        }
                        break;
                    default:
                        break;
                }
                System.out.println("Computer " + compNumber + " played " + discardPile.getLast());
                comp.removeCard(choice - 1);
                cardPlayed = 1;
            }
        } while (cardPlayed == 0);
        if (comp.getSize() == 1 && !unoCalled) {
            System.out.println("Computer " + compNumber + " did not call uno. +2");
            comp.addCard(deck);
            comp.addCard(deck);
        }
        if (comp.getSize() == 0) {
            System.out.println();
            System.out.println("Computer " + compNumber + " won");
            gameEnded = true;
            System.exit(0);
        }
        if (reverse && skip) {
            special = 1;
        } else if (!reverse && skip) {
            special = 2;
        } else if (reverse && !skip) {
            if (draw2) {
                special = 4;
            } else if (draw4) {
                special = 5;
            } else {
                special = 3;
            }
        } else if (!reverse && !skip) {
            if (draw2) {
                special = 7;
            } else if (draw4) {
                special = 8;
            } else {
                special = 6;
            }
        }
    }
  //call to the method to try-catch the players input for input mismatch
    public  int getCardNumber(Scanner si) {
        int choice = 0;
        do {
            try {
                System.out.print("Which card do you want to play: ");
                choice = si.nextInt();
                si.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
                si.nextLine();
            }
        } while (choice == 0);
        return choice;
}
}
