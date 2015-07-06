package com.byer.neuralnetcheckers;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Duane Byer
 */
public abstract class EvolutionSystem<Individual> {
    
    public EvolutionSystem(
            List<Individual> population, 
            int matchesPerGeneration,
            double cullRate) {
        this.matchType = MatchType.RANDOM;
        this.matchesPerGeneration = matchesPerGeneration;
        this.cullRate = cullRate;
        this.ratingSystem = new EloRatingSystem(population);
    }
    
    public EvolutionSystem(
            List<Individual> population, 
            double cullRate) {
        this.matchType = MatchType.ALL;
        this.matchesPerGeneration = 0;
        this.cullRate = cullRate;
        this.ratingSystem = new EloRatingSystem(population);
    }
    
    public double runGeneration() {
        
        this.ratingSystem.resetAllRatings();
        List<Individual> nets = this.getIndividuals();
        
        if (this.matchType == MatchType.RANDOM) {
            for (int i = 0; i < this.matchesPerGeneration; ++i) {
                Individual netA = nets.get(this.random.nextInt(nets.size()));
                Individual netB = this.ratingSystem.getBestMatch(netA);
                
                EloRatingSystem.Result result = this.playGame(netA, netB);
                
                this.ratingSystem.updateRating(netA, netB, result);
                
            }
        } else {            
            for (int a = 0; a < nets.size() - 1; a++) {
                for (int b = a+1; b < nets.size(); b++) {
                    Individual netA = nets.get(a);
                    Individual netB = nets.get(b);
                    EloRatingSystem.Result result = this.playGame(netA, netB);
                    this.ratingSystem.updateRating(netA, netB, result);
                }
            }
        }
        
        // Generate the next generation by removing underperforming individuals
        // and allowing fit individuals to reproduce.
        nets = this.getIndividuals();
        int numCulled = (int) (cullRate * nets.size());
        int numChildren = numCulled / (nets.size() - numCulled);
        for (int i = 0; i < numCulled; ++i) {
            this.ratingSystem.removePlayer(nets.get(i));
        }
        if (numChildren == 0) {
            for (int i = 0; i < numCulled; ++i) {
                Individual nextNet = this.makeChild(nets.get(nets.size() - i - 1));
                this.ratingSystem.addPlayer(nextNet);
            }
        }
        else {
            for (int i = numCulled; i < nets.size(); ++i) {
                Individual nextNet = this.makeChild(nets.get(i));
                this.ratingSystem.addPlayer(nextNet, this.ratingSystem.getRating(nets.get(i)));
            }
        }
        return this.ratingSystem.getHighestElo();
    }
    
    public List<Individual> getIndividuals() {
        return this.ratingSystem.getWorstToBest();
    }
    
    public abstract EloRatingSystem.Result playGame(Individual netA, Individual netB);
    public abstract Individual makeChild(Individual parent);
    
    public enum MatchType {
        RANDOM,
        ALL
    };
    
    private final MatchType matchType;
    private final Random random = new Random();
    private final int matchesPerGeneration;
    private final double cullRate;
    private EloRatingSystem<Individual> ratingSystem;
    
}
