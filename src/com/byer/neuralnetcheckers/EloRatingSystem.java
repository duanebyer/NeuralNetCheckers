package com.byer.neuralnetcheckers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Aidan Mahler
 *
 */
public class EloRatingSystem<Player>
{
    Map<Player, Double> map = new HashMap<Player, Double>();
    
    public EloRatingSystem(Collection<Player> players)
    {
        for (Player p : players)
        {
            map.put(p, this.defaultRating);
        }
    }
    
    public EloRatingSystem(Collection<Player> players, double defaultRating)
    {
        for (Player p : players)
        {
            map.put(p, defaultRating);
        }
        this.defaultRating = defaultRating;
    }
    
    private static double transformedRating(double rating)
    {
        double transformedRating = Math.pow(10, this.rating/400);
        return transformedRating;
    }
    
    public double getRating()
    {
        return rating;
    }
    
    public void updateRating(Player playerA, Player playerB, Result result)
    {
        double score1 = this.map.get(playerA);
        double score2 = this.map.get(playerB);
        
        if (result = Result.PLAYER_A_WIN)
        {
            
        }
    }
    
    public double getRating(Player player)
    {
        return this.map.get(player);
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
    
    private double expectedScore(EloRating opponent)
    {
        double tr1 = transformedRating(this.getRating());
        double tr2 = transformedRating(opponent.getRating());
        double expectedScore = tr1 = 
    }
    
    
    public enum Result
    {
        PLAYER_A_WIN,
        PLAYER_B_WIN,
        TIE
    }
    
    private double defaultRating = 0;
}
