package com.asemahle.neuralnetgames;

import java.util.Arrays;
import java.util.Scanner;

import com.asemahle.neuralnet.ActivationFunction;
import com.asemahle.neuralnet.NeuralNet;
import com.byer.neuralnetcheckers.EvolutionSystem;
import com.byer.neuralnetcheckers.Main;

/**
 * 
 * @author Aidan Mahler
 *
 */
public class PlayGuess {

    public static final ActivationFunction ACTIVATION_FUNCTION = (double x) -> x / Math.sqrt(1 + x * x);

    public static void main(String[] args) {
        NeuralNet[] nets = new NeuralNet[100];
        for (int i = 0; i < nets.length; ++i) {
            nets[i] = new NeuralNet(1, 1, 1, 1, false, Main.ACTIVATION_FUNCTION);
            nets[i].initConnectionWeights(0.0, 0.5);
        }
        double[] inputs = {-1};
        double[] outputs = {1};
        
        GenericGame game = new Guess(inputs, outputs);
        GenericEvolutionSystem system = new GenericEvolutionSystem(Arrays.asList(nets), nets.length * 2, 0.1, 0.05, 0.05, game);
        
        int generation = 1;
        while (true) {
            System.out.println("Generation " + generation + ": "); 
            System.out.println("Average ELO: " + system.runGeneration());
            NeuralNet[] theNets = new NeuralNet[system.getIndividuals().size()];
            theNets = system.getIndividuals().toArray(theNets);
            generation += 1;
        }

    }

}
