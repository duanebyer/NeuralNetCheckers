package com.byer.neuralnetcheckers;

import com.asemahle.neuralnet.NeuralNet;
import com.byer.checkers.Game;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Duane Byer
 */
public class EvolutionSystem {
    
    public EvolutionSystem(List<NeuralNet> population, int matchesPerGeneration, double cullRate, double weightMutationRate, double biasMutationRate) {
        
        this.matchesPerGeneration = matchesPerGeneration;
        this.weightMutationRate = weightMutationRate;
        this.biasMutationRate = biasMutationRate;
        this.cullRate = cullRate;
        this.ratingSystem = new EloRatingSystem(population);
    }
    
    public double runGeneration() {
        
        List<NeuralNet> nets = this.getIndividuals();
        
        for (int i = 0; i < this.matchesPerGeneration; ++i) {
            NeuralNet netA = nets.get(this.random.nextInt(nets.size()));
            NeuralNet netB = this.ratingSystem.getBestMatch(netA);
            
            // Run the game between the two players.
            NeuralNetPlayer playerA = new NeuralNetPlayer(netA);
            NeuralNetPlayer playerB = new NeuralNetPlayer(netB);
            
            Game game = new Game(playerA, playerB, Main.TURN_LIMIT);
            while (true) {
                Game.Status status = game.takeNextTurn();
                if (status == Game.Status.WhiteWin) {
                    this.ratingSystem.updateRating(netA, netB, EloRatingSystem.Result.PLAYER_1_WIN);
                    break;
                }
                else if (status == Game.Status.BlackWin) {
                    this.ratingSystem.updateRating(netA, netB, EloRatingSystem.Result.PLAYER_2_WIN);
                    break;
                }
                else if (status == Game.Status.Tie) {
                    this.ratingSystem.updateRating(netA, netB, EloRatingSystem.Result.TIE);
                    break;
                }
            }
            
        }
        
        // Generate the next generation by removing underperforming individuals
        // and allowing fit individuals to reproduce.
        int numCulled = (int) cullRate * nets.size();
        int numChildren = numCulled / (nets.size() - numCulled);
        for (int i = 0; i < numCulled; ++i) {
            this.ratingSystem.removePlayer(nets.get(nets.size() - i - 1));
        }
        if (numChildren == 0) {
            for (int i = 0; i < numCulled; ++i) {
                NeuralNet nextNet = nets.get(i).copy();
                nextNet.mutate(this.weightMutationRate, this.biasMutationRate);
                this.ratingSystem.addPlayer(nextNet);
            }
        }
        else {
            for (int i = 0; i < nets.size() - numCulled; ++i) {
                NeuralNet nextNet = nets.get(i).copy();
                nextNet.mutate(this.weightMutationRate, this.biasMutationRate);
                this.ratingSystem.addPlayer(nextNet, this.ratingSystem.getRating(nets.get(i)));
            }
        }
        return this.ratingSystem.getAverageRating();
    }
    
    public List<NeuralNet> getIndividuals() {
        return this.ratingSystem.getBestToWorst();
    }
    
    private final Random random = new Random();
    private final int matchesPerGeneration;
    private final double weightMutationRate;
    private final double biasMutationRate;
    private final double cullRate;
    private EloRatingSystem<NeuralNet> ratingSystem;
    
}
