package com.byer.neuralnetcheckers;

import com.asemahle.neuralnet.NeuralNet;
import com.byer.checkers.Board;
import com.byer.checkers.PieceType;
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
        Board bestMove = null;
        double bestMoveValue = Double.NEGATIVE_INFINITY;
        for (Board move : possibleMoves) {
            double[] input = translateBoard(move, isWhite);
            double value = this.neuralNet.input(input)[0];
            if (value > bestMoveValue) {
                bestMove = move;
                bestMoveValue = value;
            }
        }
        return bestMove;
    }
    
    private static double[] translateBoard(Board board, boolean isWhite) {
        // 5 neurons for each board position, and 2 extra for # of pieces left
        double[] result = new double[NUM_INPUTS];
        int boardPosition = 0;
        int friendlyPieces = 0;
        int enemyPieces = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                int row = isWhite ? i : 7 - i;
                int col = isWhite ? j : 7 - j;
                if (row % 2 == col % 2) {
                    PieceType piece = board.getPieceAt(row, col);
                    if (piece == null) {
                        result[boardPosition * 5] = 1.0;
                    }
                    else if (piece == PieceType.WhiteChecker) {
                        if (isWhite) {
                            result[boardPosition * 5 + 1] = 1.0;
                            friendlyPieces += 1;
                        }
                        else {
                            result[boardPosition * 5 + 3] = 1.0;
                            enemyPieces += 1;
                        }
                    }
                    else if (piece == PieceType.WhiteKing) {
                        if (isWhite) {
                            result[boardPosition * 5 + 2] = 1.0;
                            friendlyPieces += 1;
                        }
                        else {
                            result[boardPosition * 5 + 4] = 1.0;
                            enemyPieces += 1;
                        }
                    }
                    else if (piece == PieceType.BlackChecker) {
                        if (isWhite) {
                            result[boardPosition * 5 + 3] = 1.0;
                            enemyPieces += 1;
                        }
                        else {
                            result[boardPosition * 5 + 1] = 1.0;
                            friendlyPieces += 1;
                        }
                    }
                    else if (piece == PieceType.BlackKing) {
                        if (isWhite) {
                            result[boardPosition * 5 + 4] = 1.0;
                            enemyPieces += 1;
                        }
                        else {
                            result[boardPosition * 5 + 2] = 1.0;
                            friendlyPieces += 1;
                        }
                    }
                }
                boardPosition += 1;
            }
        }
        result[5 * 32] = friendlyPieces;
        result[5 * 32 + 1] = enemyPieces;
        result[5 * 32 + 2] = isWhite ? 1 : -1;
        return result;
    }
    
    public final static int NUM_INPUTS = 5 * 32 + 3;
    
    private final NeuralNet neuralNet;
    
}
