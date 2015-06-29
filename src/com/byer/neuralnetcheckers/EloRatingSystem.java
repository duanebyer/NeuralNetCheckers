package com.byer.neuralnetcheckers;

/**
 * 
 * @author Aidan Mahler
 *
 */
public class EloRating {
	
	public EloRatingSystem()
	{
		rating = 0;
	}
	
	public static double transformedRating(double rating)
	{
		double transformedRating = Math.pow(10, this.rating/400);
		return transformedRating;
	}
	
	public double getRating()
	{
		return rating;
	}
	
	public double updateRating(EloRating opponent, Outcome outcome)
	{
		
	}
	
	private double expectedScore(EloRating opponent)
	{
		double tr1 = transformedRating(this.getRating());
		double tr2 = transformedRating(opponent.getRating());
		double expectedScore = trThis 
	}
	
	public enum Outcome {
        Win,
        Loss,
        Tie,
    }
	
	private double rating;
}
