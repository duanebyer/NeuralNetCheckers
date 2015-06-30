package com.byer.neuralnetcheckers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Aidan Mahler
 *
 */
public class EloRatingSystem<Player>
{   
    public EloRatingSystem(Collection<Player> players)
    {
        for (Player p : players)
        {
            this.players.add(p);
            this.ratings.add(this.defaultRating);
        }
    }
    
    public EloRatingSystem(Collection<Player> players, double defaultRating)
    {
        this.defaultRating = defaultRating;
        for (Player p : players)
        {
            this.players.add(p);
            this.ratings.add(this.defaultRating);
        }
    }
    
    public void updateRating(Player player1, Player player2, Result result)
    {
        double r1 = this.getRating(player1);
        double r2 = this.getRating(player2);
        double tr1 = EloRatingSystem.transformedRating(r1);
        double tr2 = EloRatingSystem.transformedRating(r2);
        double e1 = EloRatingSystem.estimatedScore(tr1, tr2);
        double e2 = EloRatingSystem.estimatedScore(tr2, tr1);
        double newRating1;
        double newRating2;
        
        if (result == Result.PLAYER_1_WIN)
        {
            newRating1 = r1 + this.k * (1 - e1);
            newRating2 = r2 + this.k * (0 - e2);
        }
        else if (result == Result.PLAYER_2_WIN)
        {
            newRating1 = r1 + this.k * (0 - e1);
            newRating2 = r2 + this.k * (1 - e2);
        }
        else
        {
            newRating1 = r1 + this.k * (0.5 - e1);
            newRating2 = r2 + this.k * (0.5 - e2);
        }
        this.updatePlayer(player1, newRating1);
        this.updatePlayer(player2, newRating2);
    }
    
    public List<Player> getWorstToBest()
    {
        return Collections.unmodifiableList(this.players);
    }
    
    public double getRating(Player player)
    {
        int ratingIndex = this.players.indexOf(player);
        return this.ratings.get(ratingIndex);
    }
    
    public Player getBestMatch(Player player)
    {
        if (this.players.size() <= 1)
        {
            throw new RuntimeException("The EloRatingSystem must contain 2 or more players to return a match!");
        }
        Player bestMatch;
        int index = this.players.indexOf(player);
        if (index == 0)
        {
            bestMatch = this.players.get(1);
        }
        else if (index == this.players.size() - 1)
        {
            bestMatch = this.players.get(this.players.size() - 2);
        }
        else
        {
            double thisRating = this.ratings.get(index);
            double aboveRating = this.ratings.get(index + 1);
            double belowRating = this.ratings.get(index - 1);
            double diffAbove = Math.abs(thisRating - aboveRating);
            double diffBelow = Math.abs(thisRating - belowRating);
            if (diffAbove < diffBelow)
            {
                bestMatch = this.players.get(index + 1);
            }
            else
            {
                bestMatch = this.players.get(index - 1);
            }
        }
        return bestMatch;
    }
    
    public void updatePlayer(Player player, double newScore)
    {
        this.removePlayer(player);
        this.addPlayer(player, newScore);
    }
    
    public double getAverageRating()
    {
        double sum = 0;
        for (double rating : this.ratings)
        {
            sum += rating;
        }
        return sum / this.ratings.size();
    }
    
    public void addPlayer(Player player)
    {
        this.addPlayer(player, defaultRating);
    }
    
    public void addPlayer(Player player, double rating)
    {
        int insertionIndex = Collections.binarySearch(this.ratings, rating);
        if (insertionIndex < 0)
        {
            insertionIndex = -1 * insertionIndex - 1;
        }
        this.ratings.add(insertionIndex, rating);
        this.players.add(insertionIndex, player);
    }
    
    public void removePlayer(Player player)
    {
        int removalIndex = players.indexOf(player);
        players.remove(removalIndex);
        ratings.remove(removalIndex);
    }
    
    private static double transformedRating(double rating)
    {
        double transformedRating = Math.pow(10, rating/400);
        return transformedRating;
    }
    
    private static double estimatedScore(double r1, double r2)
    {
        double estimatedRating = r1 / (r1 + r2);
        return estimatedRating;
    }
    
    public enum Result
    {
        PLAYER_1_WIN,
        PLAYER_2_WIN,
        TIE
    }
    
    private final double k = 32;
    private List<Player> players = new ArrayList<Player>();
    private List<Double> ratings = new ArrayList<Double>();
    private double defaultRating = 0;
}
