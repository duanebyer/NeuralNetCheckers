package com.byer.neuralnetcheckers;

import com.asemahle.neuralnet.NeuralNet;
import com.byer.checkers.Board;
import com.byer.checkers.Player;
import java.util.List;

/**
 *
 * @author Duane Byer
 */
public class NeuralNetPlayer extends Player {
    
    public NeuralNetPlayer(NeuralNet neuralNet) {
        this.neuralNet = neuralNet;
    }
    
    @Override
    public Board getMove(Board board, boolean isWhite) {
        List<Board> possibleMoves = board.getPossibleMoves(isWhite);
        Board bestMove;
        double bestMoveValue = Double.NEGATIVE_INFINITY;
        for (Board move : possibleMoves) {
        }
    }
    
    private double[] translateBoard(Board board, boolean isWhite) {
        // 5 neurons for each board position, and 2 extra for # of pieces left
        double[] result = new double[5 * 32 + 2];
        int boardPosition = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
            }
        }
    }
    
    private final NeuralNet neuralNet;
    
}
