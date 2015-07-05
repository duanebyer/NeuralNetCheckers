package com.byer.neuralnetcheckers.main;

import com.asemahle.neuralnet.NeuralNet;
import com.byer.checkers.Game;
import com.byer.checkers.HumanPlayer;
import com.byer.checkers.Player;
import com.byer.checkers.StupidCheckersPlayer;
import com.byer.neuralnetcheckers.NeuralNetPlayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Duane Byer
 */
public class PlayCheckersNet {
    
    public static void main(String[] args) throws IOException {
        
        Player playerA = getPlayer();
        Player playerB = getPlayer();

        Game game = new Game(playerA, playerB, EvolveCheckersNet.TURN_LIMIT);
        while (true) {
            game.getBoard().print(new PrintWriter(System.out));
            Game.Status result = game.takeNextTurn();
            if (result == Game.Status.BlackWin) {
                System.out.println("Player 1 wins!");
                break;
            }
            if (result == Game.Status.WhiteWin) {
                System.out.println("Player 2 wins!");
                break;
            }
            if (result == Game.Status.Tie) {
                System.out.println("Tie!");
            }
        }
    }
    
    public static Player getPlayer() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file name of the neural net you want to play:");
        String fileName = scanner.nextLine();
        Player playerA = null;
        if (fileName.equals("human")) {
            playerA = new HumanPlayer();
        }
        else if (fileName.equals("stupid")) {
            playerA = new StupidCheckersPlayer();
        }
        else {
            NeuralNet[] nets = NeuralNet.loadFromFile(fileName, EvolveCheckersNet.ACTIVATION_FUNCTION);
            System.out.println("There are " + nets.length + " nets in this file. Enter the number of the one you want:");
            int netNumber = scanner.nextInt();
            playerA = new NeuralNetPlayer(nets[netNumber]);
        }
        return playerA;
    }
    
}
