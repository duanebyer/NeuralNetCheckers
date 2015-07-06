package com.byer.neuralnetcheckers.main;

import com.asemahle.neuralnet.ActivationFunction;
import com.asemahle.neuralnet.NeuralNet;
import com.byer.neuralnetcheckers.CheckersEvolutionSystem;
import com.byer.neuralnetcheckers.NeuralNetPlayer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Duane Byer
 */
public class EvolveCheckersNet {
    
    public static final ActivationFunction ACTIVATION_FUNCTION = (double x) -> x / Math.sqrt(1 + x * x);
    public static final int TURN_LIMIT = 20;
    
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
                nets[i] = new NeuralNet(NeuralNetPlayer.NUM_INPUTS, 1, 2, 100, true, EvolveCheckersNet.ACTIVATION_FUNCTION);
                nets[i].initConnectionWeights(0.0, 0.6, 0.0, 0.3);
            }
        }
        else {
            nets = NeuralNet.loadFromFile(fileName, ACTIVATION_FUNCTION);
        }
        CheckersEvolutionSystem system = new CheckersEvolutionSystem(Arrays.asList(nets), 0.4, 0.001, 0.001);
        
        int generation = 1;
        while (true) {
            System.out.println("Generation " + generation + ": ");
            System.out.println("Highest ELO: " + system.runGeneration());
            NeuralNet[] theNets = new NeuralNet[system.getIndividuals().size()];
            theNets = system.getIndividuals().toArray(theNets);
            if (/*generation % 10 == 0 || generation == 1*/true){
                NeuralNet.saveToFile("generation" + generation + ".nn", new NeuralNet[] { theNets[theNets.length - 1] });
            }
            generation += 1;
        }
        
    }
    
}
