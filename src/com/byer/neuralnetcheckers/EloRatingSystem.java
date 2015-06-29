package com.byer.neuralnetcheckers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
            players.add(p);
            ratings.add(this.defaultRating);
        }
    }
    
    public EloRatingSystem(Collection<Player> players, double defaultRating)
    {
        this.defaultRating = defaultRating;
        for (Player p : players)
        {
            players.add(p);
            ratings.add(this.defaultRating);
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
    
    public List<Player> getBestToWorst()
    {
        return Collections.unmodifiableList(this.players);
    }
    
    public double getRating(Player player)
    {
        int ratingIndex = this.players.indexOf(player);
        return this.ratings.get(ratingIndex);
    }
    
    public void updatePlayer(Player player, Double newScore)
    {
        
    }
    
    public double getAverageRating()
    {
        return null
    }
    
    public void addPlayer(Player player)
    {
        this.map.put(player, defaultRating);
    }
    
    public void addPlayer(Player player, double rating)
    {
        this.map.put(player, rating);
    }
    
    public void removePlayer(Player player)
    {
        this.map.remove(player);
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
    private List<Player> players;
    private List<Double> ratings;
    private double defaultRating = 0;
}
