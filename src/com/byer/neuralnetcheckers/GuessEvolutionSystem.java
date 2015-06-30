package com.byer.neuralnetcheckers;

import com.asemahle.neuralnet.NeuralNet;
import java.util.List;

/**
 *
 * @author duane_000
 */
public class GuessEvolutionSystem extends EvolutionSystem<NeuralNet> {

    public GuessEvolutionSystem(
            List<NeuralNet> population,
            int matchesPerGeneration,
            double cullRate,
            double weightMutationRate,
            double biasMutationRate) {
        super(population, matchesPerGeneration, cullRate);
        this.weightMutationRate = weightMutationRate;
        this.biasMutationRate = biasMutationRate;
    }

    @Override
    public EloRatingSystem.Result playGame(NeuralNet netA, NeuralNet netB) {
        double[] input = new double[] { 1.0 };
        double resultA = Math.abs(netA.input(input)[0] - 0.33);
        double resultB = Math.abs(netB.input(input)[0] - 0.33);
        if (resultA < resultB) {
            return EloRatingSystem.Result.PLAYER_1_WIN;
        }
        else {
            return EloRatingSystem.Result.PLAYER_2_WIN;
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
