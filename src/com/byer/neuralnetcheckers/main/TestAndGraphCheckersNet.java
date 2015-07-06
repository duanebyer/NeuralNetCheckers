package com.byer.neuralnetcheckers.main;

import com.asemahle.neuralnet.NeuralNet;
import com.byer.checkers.Game;
import com.byer.checkers.HumanPlayer;
import com.byer.checkers.StupidCheckersPlayer;
import com.byer.neuralnetcheckers.Function;
import com.byer.neuralnetcheckers.NeuralNetPlayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Duane Byer
 */
public class TestAndGraphCheckersNet {
    
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of the first generation to be tested:");
        int startGen = scanner.nextInt();
        System.out.println("Enter the number of the final generation to be tested:");
        int endGen = scanner.nextInt();
        System.out.println("Enter the number of generations to increment between tests:");
        int increment = scanner.nextInt();
        System.out.println("Enter the number of games each generation winner should play against the stupidBot:");
        int numGames = scanner.nextInt();
        
        JFrame frame = new JFrame("Graphs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel lbl = new JLabel();
        frame.getContentPane().add(lbl, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        
        ArrayList<Double> winsVSLosses = new ArrayList<Double>();
        NeuralNetPlayer netPlayer = null;
        for (int i = startGen; i <= endGen; i += increment){
            String fileName = "generation" + i + ".nn";
            NeuralNet[] nets = null;
            while (true) {
                try {
                    nets = NeuralNet.loadFromFile(fileName, EvolveCheckersNet.ACTIVATION_FUNCTION);
                    break;
                } catch(IOException e) {/*Wait for the file to be created*/}
            }
            int netNumber = 0;
            int stupidBotWins = 0;
            int neuralBotWins = 0;
            netPlayer = new NeuralNetPlayer(nets[netNumber]);
            for (int j = 0; j < numGames; j++) {
                Game game = new Game(new StupidCheckersPlayer(), netPlayer, EvolveCheckersNet.TURN_LIMIT);
                while (true) {
                    //game.getBoard().print(new PrintWriter(System.out));
                    Game.Status result = game.takeNextTurn();
                    if (result == Game.Status.BlackWin) {
                        //System.out.println("Neural net wins!");
                        neuralBotWins += 1;
                        break;
                    }
                    if (result == Game.Status.WhiteWin) {
                        //System.out.println("Human wins!");
                        stupidBotWins += 1;
                        break;
                    }
                    if (result == Game.Status.Tie) {
                        //System.out.println("Tie!");
                        break;
                    }
                }
            }
            System.out.println("Generation" + i + 
                    ".nn  |  Neural: " + neuralBotWins + 
                    "  |  Random: " + stupidBotWins + 
                    "  |  N/R = " + (double)neuralBotWins/(double)stupidBotWins);
            winsVSLosses.add((double)neuralBotWins/(double)stupidBotWins);       
            lbl.setIcon(draw(winsVSLosses, lbl.getWidth(), lbl.getHeight()));
        }
        System.out.println("DONE!");
    }
    
    public static ImageIcon draw(List<Double> points, int width, int height){
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        if (points.size() < 2){
            //nothing to draw if one point or fewer
            return new ImageIcon(image);
        }
        
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (Double d : points) {
            if (d < min){
                min = d;
            }
            if (d > max){
                max = d;
            }
        }
        
        Graphics2D g2 = image.createGraphics();
        
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLACK);
        g2.drawString("Max: " + max, 10, 10);
        g2.drawString("Min: " + min, 10, 22);
        
        AffineTransform tform = AffineTransform.getTranslateInstance( 0, image.getHeight());
        tform.scale( 1, -1);
        g2.setTransform(tform);
        g2.setColor(Color.BLACK);
        double spacing = (double) width / (double) (points.size() - 1);
        int drawX1 = 0;
        int drawX2 = drawX1 + (int)spacing;
        for (int i = 0; i < points.size() - 1; i++) {
            int drawY1 = (int) (((points.get(i) - min) / (max - min)) * height);
            int drawY2 = (int) (((points.get(i+1) - min) / (max - min)) * height);
            g2.drawLine(drawX1, drawY1, drawX2, drawY2);
            drawX1 = drawX2;
            drawX2 = (int) (spacing * (i + 2));
        }
        g2.dispose();
        
        return new ImageIcon(image);
    }
}
