package com.byer.neuralnetcheckers;

import java.util.List;
import java.util.Random;

import com.asemahle.neuralnet.NeuralNet;

public class FunctionEvolutionSystem extends EvolutionSystem<NeuralNet>{
    
    public FunctionEvolutionSystem(
            List<NeuralNet> population,
            double cullRate,
            double weightMutationRate,
            double biasMutationRate,
            int numGames,
            double[] domain,
            Function[] functions) {
        super(population, cullRate);
        this.weightMutationRate = weightMutationRate;
        this.biasMutationRate = biasMutationRate;
        
        this.functions = functions;
        this.numGames = numGames;
        
        this.spread = Math.abs(domain[0] - domain[1]);
        this.offset = (domain[0] + domain[1]) / 2.0;
    }

    @Override
    public EloRatingSystem.Result playGame(NeuralNet netA, NeuralNet netB) {
        double diffA = 0;
        double diffB = 0;
        for (int games = 0; games < this.numGames; games ++) {
            double[] input = {this.random.nextDouble() * this.spread + this.offset - this.spread / 2.0};
            double[] resultA = netA.input(input);
            double[] resultB = netB.input(input);
            for (int i = 0; i < netA.getNumOutputs(); i++){
                diffA += Math.pow((resultA[i] - functions[i].call(input[0])),2);
                diffB += Math.pow((resultB[i] - functions[i].call(input[0])),2);
            }
        }
        
        if (diffA < diffB) {
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
    
    public void updateMutationRates(double factor) {
        this.biasMutationRate *= factor;
        this.weightMutationRate *= factor;
    }
    
    private Random random = new Random();
    private Function[] functions;
    private double offset;
    private double spread;
    private int numGames;
    public double weightMutationRate;
    public double biasMutationRate;
}
