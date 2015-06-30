package com.asemahle.neuralnetgames;
import com.asemahle.neuralnet.NeuralNet;

/**
 * 
 * @author Aidan Mahler
 *
 */

public interface GenericGame 
{
    public Result play(NeuralNet p1, NeuralNet p2);
    
    public enum Result{
        PLAYER_1_WIN,
        PLAYER_2_WIN,
        TIE
    }
}
