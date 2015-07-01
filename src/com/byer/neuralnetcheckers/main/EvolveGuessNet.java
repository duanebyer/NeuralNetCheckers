package com.byer.neuralnetcheckers.main;

import com.asemahle.neuralnet.ActivationFunction;
import com.asemahle.neuralnet.NeuralNet;
import com.byer.neuralnetcheckers.GuessEvolutionSystem;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Duane Byer
 */
public class EvolveGuessNet {
    
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
                nets[i] = new NeuralNet(1, 1, 1, 1, true, EvolveCheckersNet.ACTIVATION_FUNCTION);
                nets[i].initConnectionWeights(0.0, 0.5, 0.0, 0.5);
            }
        }
        else {
            nets = NeuralNet.loadFromFile(fileName, EvolveCheckersNet.ACTIVATION_FUNCTION);
        }
        GuessEvolutionSystem system = new GuessEvolutionSystem(Arrays.asList(nets), nets.length * 2, 0.4, 0.5, 0.5);
        
        int generation = 1;
        double historicBest = Double.MAX_VALUE;
        int generationsSinceLastNewBest = 0;
        while (generation < 500) {
            double avgElo = system.runGeneration();
            NeuralNet[] theNets = new NeuralNet[system.getIndividuals().size()];
            theNets = system.getIndividuals().toArray(theNets);
            double bestGuess = theNets[theNets.length - 1].input(new double[] { 1.0 })[0];
            if (Math.abs(bestGuess - 0.33) < Math.abs(historicBest - 0.33))
            {
                System.out.println("Average Elo: " + avgElo);
                System.out.println("Generation " + generation + ": ");
                System.out.println("Best guess: " + bestGuess);
                historicBest = bestGuess;
                generationsSinceLastNewBest = 0;
            }
            else
            {
                generationsSinceLastNewBest ++;
            }
            
            if (generationsSinceLastNewBest > 2)
            {
                system.updateMutationRates(0.5);
                //System.out.println("Mutation Rate: " + system.weightMutationRate);
                generationsSinceLastNewBest = 0;
            }
            //NeuralNet.saveToFile("generation" + generation + ".nn", new NeuralNet[] { theNets[theNets.length - 1] });
            generation += 1;
        }
        
    }
    
}
