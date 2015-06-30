package com.byer.neuralnetcheckers.main;

import com.asemahle.neuralnet.NeuralNet;
import com.byer.checkers.Game;
import com.byer.checkers.HumanPlayer;
import com.byer.checkers.StupidCheckersPlayer;
import com.byer.neuralnetcheckers.NeuralNetPlayer;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Duane Byer
 */
public class TestCheckersNet {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file name of the neural net you want to play:");
        String fileName = scanner.nextLine();
        NeuralNet[] nets = NeuralNet.loadFromFile(fileName, EvolveCheckersNet.ACTIVATION_FUNCTION);
        System.out.println("There are " + nets.length + " nets in this file. Enter the number of the one you want:");
        int netNumber = scanner.nextInt();
        int stupidBotWins = 0;
        int neuralBotWins = 0;
        while (true) {
            NeuralNetPlayer netPlayer = new NeuralNetPlayer(nets[netNumber]);
            Game game = new Game(new StupidCheckersPlayer(), netPlayer, EvolveCheckersNet.TURN_LIMIT);
            while (true) {
                //game.getBoard().print(new PrintWriter(System.out));
                Game.Status result = game.takeNextTurn();
                if (result == Game.Status.BlackWin) {
                    //System.out.println("Neural net wins!");
                    neuralBotWins += 1;
                    break;
                }
                if (result == Game.Status.WhiteWin) {
                    //System.out.println("Human wins!");
                    stupidBotWins += 1;
                    break;
                }
                if (result == Game.Status.Tie) {
                    //System.out.println("Tie!");
                    break;
                }
            }
            System.out.println(neuralBotWins + "   " + stupidBotWins);
        }
    }
    
}
