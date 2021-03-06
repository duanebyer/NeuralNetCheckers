package com.byer.neuralnetcheckers;

import com.asemahle.neuralnet.NeuralNet;
import com.byer.checkers.Game;
import com.byer.neuralnetcheckers.main.EvolveCheckersNet;

import java.util.List;

/**
 *
 * @author Duane Byer
 */
public class CheckersEvolutionSystem extends EvolutionSystem<NeuralNet> {

    public CheckersEvolutionSystem(
            List<NeuralNet> population,
            int matchesPerGeneration,
            double cullRate,
            double weightMutationRate,
            double biasMutationRate) {
        super(population, matchesPerGeneration, cullRate);
        this.weightMutationRate = weightMutationRate;
        this.biasMutationRate = biasMutationRate;
    }
    
    public CheckersEvolutionSystem(
            List<NeuralNet> population,
            double cullRate,
            double weightMutationRate,
            double biasMutationRate) {
        super(population, cullRate);
        this.weightMutationRate = weightMutationRate;
        this.biasMutationRate = biasMutationRate;
    }
    
    @Override
    public EloRatingSystem.Result playGame(NeuralNet netA, NeuralNet netB) {
        // Run the game between the two players.
        NeuralNetPlayer playerA = new NeuralNetPlayer(netA);
        NeuralNetPlayer playerB = new NeuralNetPlayer(netB);
        
        int netAScore = 0;
        Game game = new Game(playerA, playerB, EvolveCheckersNet.TURN_LIMIT);
        while (true) {
            Game.Status status = game.takeNextTurn();
            if (status == Game.Status.WhiteWin) {
                netAScore ++;
                break;
            }
            else if (status == Game.Status.BlackWin) {
                netAScore --;
                break;
            }
            else if (status == Game.Status.Tie) {
                break;
            }
        }
        
        game = new Game(playerB, playerA, EvolveCheckersNet.TURN_LIMIT);
        while (true) {
            Game.Status status = game.takeNextTurn();
            if (status == Game.Status.WhiteWin) {
                netAScore --;
                break;
            }
            else if (status == Game.Status.BlackWin) {
                netAScore ++;
                break;
            }
            else if (status == Game.Status.Tie) {
                break;
            }
        }
        
        if (netAScore > 0) {
            return EloRatingSystem.Result.PLAYER_1_WIN;
        } else if(netAScore < 0) {
            return EloRatingSystem.Result.PLAYER_2_WIN;
        } else {
            return EloRatingSystem.Result.TIE;
        }
    }

    @Override
    public NeuralNet makeChild(NeuralNet parent) {
        NeuralNet next = parent.copy();
        next.mutate(this.weightMutationRate, this.biasMutationRate);
        return next;
    }
    
    private final double weightMutationRate;
    private final double biasMutationRate;
    
}
