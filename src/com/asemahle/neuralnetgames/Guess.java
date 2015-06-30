package com.asemahle.neuralnetgames;

import com.asemahle.neuralnet.NeuralNet;

/**
 * 
 * @author Aidan Mahler
 *
 */

public class Guess implements GenericGame {
    
    public Guess(double[] inputs, double[] outputs)
    {
        this.inputs = inputs;
        this.outputs = outputs;
    }
    
    public GenericGame.Result play(NeuralNet p1, NeuralNet p2)
    {
        double[] p1Results = p1.input(inputs);
        double[] p2Results = p2.input(inputs);
        double p1Score = 0;
        double p2Score = 0;
        for (int i = 0; i < outputs.length; i++)
        {
            p1Score += Math.abs(p1Results[i] - outputs[i]);
            p2Score += Math.abs(p2Results[i] - outputs[i]);
        }
        
        //Lower Score is better! Score is a measure of deviation from the 
        //expected output. Less deviation is better.
        
        if (p1Score < 0.01 || p2Score < 0.01)
        {
            assert Boolean.TRUE;
        }
        
        if (p1Score < p2Score)
        {
            return GenericGame.Result.PLAYER_1_WIN;
        }
        else if (p2Score < p1Score)
        {
            return GenericGame.Result.PLAYER_2_WIN;
        }
        else
        {
            return GenericGame.Result.TIE;
        }
        
    }
    
    private double[] inputs;
    private double[] outputs;
    

}
