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
    
    public static final int NUM_HIDDEN_LAYERS = 2;
    public static final int NEURONS_PER_LAYER = 100;
    public static final double CULL_RATE = 0.2;
    public static final double WEIGHT_MUTATION_RATE = 0.0005;
    public static final double BIAS_MUTATION_RATE = 0.0005;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file name of the neural net population:");
        String fileName = scanner.nextLine();
        scanner.close();
        
        NeuralNet[] nets;
        if (fileName.equals("new")) {
            nets = new NeuralNet[100];
            for (int i = 0; i < nets.length; ++i) {
                nets[i] = new NeuralNet(NeuralNetPlayer.NUM_INPUTS, 1, NUM_HIDDEN_LAYERS, NEURONS_PER_LAYER, true, EvolveCheckersNet.ACTIVATION_FUNCTION);
                nets[i].initConnectionWeights(0.0, 1, 0.0, 0.5);
            }
        }
        else {
            nets = NeuralNet.loadFromFile(fileName, ACTIVATION_FUNCTION);
        }
        CheckersEvolutionSystem system = new CheckersEvolutionSystem(Arrays.asList(nets), CULL_RATE, WEIGHT_MUTATION_RATE, BIAS_MUTATION_RATE);
        
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
