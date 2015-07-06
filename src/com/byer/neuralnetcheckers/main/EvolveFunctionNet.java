package com.byer.neuralnetcheckers.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.asemahle.neuralnet.ActivationFunction;
import com.asemahle.neuralnet.NeuralNet;
import com.byer.neuralnetcheckers.Function;
import com.byer.neuralnetcheckers.FunctionEvolutionSystem;

public class EvolveFunctionNet {
    
    public static final ActivationFunction AF = (double x) -> Math.sin(x);
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Graphs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel lbl = new JLabel();
        frame.getContentPane().add(lbl, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        
        NeuralNet[] nets = new NeuralNet[20];
        for (int i = 0; i < nets.length; ++i) {
            nets[i] = new NeuralNet(1, 3, 5, 10, true, EvolveCheckersNet.ACTIVATION_FUNCTION);
            nets[i].initConnectionWeights(0.0, 0.5, 0.0, 0.3, 1, 0.2);
        }
        
        Function[] functions = {
                (double x) -> Math.sin(x),
                (double x) -> Math.cos(x),
                (double x) -> Math.abs(x)
        };
        double[] domain = {-Math.PI, Math.PI};
        double[] range = {-1, Math.PI};
        FunctionEvolutionSystem system = new FunctionEvolutionSystem(Arrays.asList(nets), 0.3, 0.01, 0.01, 0.1, 100, domain, functions);
        
        int generation = 1;
        NeuralNet winner = null;
        while (generation < 5000) {
            double avgElo = system.runGeneration();
            System.out.println("Average Elo: " + avgElo);
            System.out.println("Generation " + generation + ": ");
            winner = system.getIndividuals().get(0);
            //NeuralNet.saveToFile("generation" + generation + ".nn", new NeuralNet[] { theNets[theNets.length - 1] });
            lbl.setIcon(draw(winner, functions, domain, range));
            generation += 1;
        }
    }
    
    public static ImageIcon draw(NeuralNet winner, Function[] functions, double[] domain, double[] range){
        int size = 500;
        BufferedImage image = new BufferedImage(size * functions.length, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2.setColor(Color.BLACK);
        double spreadx = Math.abs(domain[0] - domain[1]);
        double spready = Math.abs(range[0] - range[1]);
        
        double drawx = 0;
        for (int f = 0; f < functions.length; f++){
            for (int i = 0; i < size; i++){
                double x = domain[0] + (i * spreadx / size);
                double y = -1 * functions[f].call(x);
                double drawy = ( (y / spready)+0.5) * size; 
                g2.setColor(Color.BLUE); //ideal
                g2.drawLine(
                        (int) Math.round(drawx), 
                        (int) Math.round(drawy), 
                        (int) Math.round(drawx), 
                        (int) Math.round(drawy));
                
                g2.setColor(Color.BLACK); //neural net
                y = -1 * winner.input(new double[] {x})[f];
                drawy = ( (y / spready)+0.5) * size;
                g2.drawLine(
                        (int) Math.round(drawx), 
                        (int) Math.round(drawy), 
                        (int) Math.round(drawx), 
                        (int) Math.round(drawy));
                drawx ++;
            }
        }
        return new ImageIcon(image);
    }
}
