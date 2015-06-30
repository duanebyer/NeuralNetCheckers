package com.byer.neuralnetcheckers;

import com.asemahle.neuralnet.ActivationFunction;
import com.asemahle.neuralnet.NeuralNet;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Duane Byer
 */
public class PlayGuess {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file name of the neural net population:");
        String fileName = scanner.nextLine();
        NeuralNet[] nets;
        if (fileName.equals("new")) {
            nets = new NeuralNet[100];
            for (int i = 0; i < nets.length; ++i) {
                nets[i] = new NeuralNet(1, 1, 10, 100, true, Main.ACTIVATION_FUNCTION);
                nets[i].initConnectionWeights(0.0, 0.5, 0.0, 0.5);
            }
        }
        else {
            nets = NeuralNet.loadFromFile(fileName, Main.ACTIVATION_FUNCTION);
        }
        GuessEvolutionSystem system = new GuessEvolutionSystem(Arrays.asList(nets), nets.length * 2, 0.4, 0.05, 0.05);
        
        int generation = 1;
        while (true) {
            System.out.println("Generation " + generation + ": ");
            System.out.println("Average ELO: " + system.runGeneration());
            NeuralNet[] theNets = new NeuralNet[system.getIndividuals().size()];
            theNets = system.getIndividuals().toArray(theNets);
            double bestGuess = theNets[theNets.length - 1].input(new double[] { 1.0 })[0];
            System.out.println("Best guess: " + bestGuess);
            //NeuralNet.saveToFile("generation" + generation + ".nn", new NeuralNet[] { theNets[theNets.length - 1] });
            generation += 1;
        }
        
    }
    
}
