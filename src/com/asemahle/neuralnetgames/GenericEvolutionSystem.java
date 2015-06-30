package com.asemahle.neuralnetgames;

import com.asemahle.neuralnet.NeuralNet;
import com.byer.checkers.Game;
import com.byer.neuralnetcheckers.EloRatingSystem;
import com.byer.neuralnetcheckers.EloRatingSystem.Result;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Aidan Mahler & Duane Byer
 */
public class GenericEvolutionSystem 
{
    
    public GenericEvolutionSystem(
            List<NeuralNet> population, 
            int matchesPerGeneration, 
            double cullRate, 
            double weightMutationRate, 
            double biasMutationRate,
            GenericGame game) 
    {
        this.matchesPerGeneration = matchesPerGeneration;
        this.weightMutationRate = weightMutationRate;
        this.biasMutationRate = biasMutationRate;
        this.cullRate = cullRate;
        this.ratingSystem = new EloRatingSystem(population);
        this.game = game;
    }
    
    public double runGeneration() 
    {
        
        List<NeuralNet> nets = this.getIndividuals();
        
        for (int i = 0; i < this.matchesPerGeneration; ++i) 
        {
            NeuralNet net1 = nets.get(this.random.nextInt(nets.size()));
            NeuralNet net2 = this.ratingSystem.getBestMatch(net1);
            
            GenericGame.Result result = game.play(net1, net2);
            if (result == GenericGame.Result.PLAYER_1_WIN) 
            {
                this.ratingSystem.updateRating(net1, net2, EloRatingSystem.Result.PLAYER_1_WIN);
            }
            else if (result == GenericGame.Result.PLAYER_2_WIN) 
            {
                this.ratingSystem.updateRating(net1, net2, EloRatingSystem.Result.PLAYER_2_WIN);
            }
            else 
            {
                this.ratingSystem.updateRating(net1, net2, EloRatingSystem.Result.TIE);
                break;
            }
            
        }
        
        // Generate the next generation by removing underperforming individuals
        // and allowing fit individuals to reproduce.
        nets = this.getIndividuals(); //update the list of nets
        int numCulled = (int) (cullRate * nets.size());
        int numChildren = numCulled;
        for (int i = 0; i < numCulled; ++i) 
        {
            this.ratingSystem.removePlayer(nets.remove(0));
        }
        
        int numChildrenBorn = 0;
        for (int i = 0; i < nets.size(); ++i) 
        {
            NeuralNet parentNet;
            if (nets.size() > 1) 
            {
                parentNet = nets.get( (nets.size() - i - 1) % (nets.size() -1 ));
            }
            else {
                parentNet = nets.get(0);
            }
            
            if (numChildrenBorn < numChildren)
            {
                NeuralNet childNet = parentNet.copy();
                childNet.mutate(this.weightMutationRate, this.biasMutationRate);
                this.ratingSystem.addPlayer(childNet, this.ratingSystem.getRating(parentNet));
                numChildrenBorn++;
            }
            parentNet.mutate(this.weightMutationRate, this.biasMutationRate);
        }
        return this.ratingSystem.getAverageRating();
    }
    
    public List<NeuralNet> getIndividuals() 
    {
        return this.ratingSystem.getWorstToBest();
    }
    
    private final Random random = new Random();
    private final int matchesPerGeneration;
    private final double weightMutationRate;
    private final double biasMutationRate;
    private final double cullRate;
    private EloRatingSystem<NeuralNet> ratingSystem;
    private GenericGame game;
    
}
