import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.Color;

import javax.swing.*;
import java.awt.*;
import java.util.*;
public class Mechanics
{
    Drawing dr;
    
    JFrame calc;
    JFrame draw;
    JLabel l;
    JLabel label;
    JPanel jp;
    
    int pile[];
    int xlength;
    int ylength;
    int sand;
    int size;
    int iterations;
    
    
    
    public Mechanics(int x, int y, int sand, int size){
        this.xlength = x;
        this.ylength = y;
        this.sand = sand;
        this.size = size;
        
        pile = new int[xlength*ylength];
        
        setSand();
        makeWaitWindow();
        calculate();
    }
    
    private void setSand(){
        int middle = (int)(xlength * ylength * 0.5);
        if(xlength % 2 == 0){
            if(ylength % 2 == 0){
                pile[middle + (int)(xlength * 0.5)] = (int)(sand * 0.25);
                pile[middle + ((int)(xlength * 0.5) - 1)] = (int)(sand * 0.25);
                pile[middle - (int)(xlength * 0.5)] = (int)(sand * 0.25);
                pile[middle - ((int)(xlength * 0.5) + 1)] = (int)(sand * 0.25);
            }else{
                pile[middle] = (int)(sand * 0.5);
                pile[middle - 1] = (int)(sand * 0.5);
            }
        }else{
            if(ylength % 2 == 0){
                int pos1 = ((int)(ylength * 0.5) * xlength) + (int)((xlength-1) * 0.5);
                int pos2 = (((int)(ylength * 0.5) - 1) * xlength) + (int)((xlength-1) * 0.5); 
                pile[pos1] = (int)(sand * 0.5);
                pile[pos2] = (int)(sand * 0.5);
            }else{
                pile[(int)(xlength * ylength * 0.5)] = sand;
            }
        }
    }
    
    private void makeWaitWindow(){
        calc = new JFrame("Calculating...");
        calc.setSize(300,300);
        calc.setLocation(400,300);
        calc.setVisible(false);
        calc.setResizable(false);
        GridBagLayout gblFrame = new GridBagLayout();
        GridBagConstraints gbcFrame = new GridBagConstraints();
        calc.setLayout(gblFrame);
        calc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calc.setIconImage(new ImageIcon(getClass().getResource("picCalc.png")).getImage());
        
        
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        jp = new JPanel();
        jp.setLayout(gbl);
        jp.setVisible(true);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                        
        gbc.gridy = 0;
        label = new JLabel("Iterations:");
        gbl.setConstraints(label, gbc);
        jp.add(label);
                        
        gbc.gridy = 1;
        label = new JLabel("x:");
        gbl.setConstraints(label, gbc);
        jp.add(label);
        
        gbc.gridy = 2;
        label = new JLabel("y:");
        gbl.setConstraints(label, gbc);
        jp.add(label);
        
        gbc.gridy = 3;
        label = new JLabel("Sand:");
        gbl.setConstraints(label, gbc);
        jp.add(label);
        
        gbc.gridy = 4;
        label = new JLabel("Size:");
        gbl.setConstraints(label, gbc);
        jp.add(label);
                
        
        gbc.gridx = 1;
                
        gbc.gridy = 0;
        l = new JLabel(String.valueOf(iterations));
        gbl.setConstraints(l, gbc);
        jp.add(l);
        
        gbc.gridy = 1;
        label = new JLabel(String.valueOf(xlength));
        gbl.setConstraints(label, gbc);
        jp.add(label);
        
        gbc.gridy = 2;
        label = new JLabel(String.valueOf(ylength));
        gbl.setConstraints(label, gbc);
        jp.add(label);
        
        gbc.gridy = 3;
        label = new JLabel(String.valueOf(sand));
        gbl.setConstraints(label, gbc);
        jp.add(label);
        
        gbc.gridy = 4;
        label = new JLabel(String.valueOf(size));
        gbl.setConstraints(label, gbc);
        jp.add(label);
        
        
        gbcFrame.gridx = 0;
        gbcFrame.gridy = 0;
        gbcFrame.gridwidth = 1;
        gbcFrame.gridheight = 1;
        gbcFrame.fill = GridBagConstraints.BOTH;
        gbcFrame.insets = new Insets(45,60,45,60);
        gblFrame.setConstraints(jp, gbcFrame);
                
        calc.add(jp);
        calc.pack();
        calc.setLocation(1338-calc.getWidth(),27); //oben rechts ins eck
        calc.setVisible(true);
        calc.repaint();
        jp.update(jp.getGraphics());
    }
    
    private void makeDrawWindow(){
        draw = new JFrame("Drawing...");
        draw.setBounds(400,300,300,300);
        try{draw.setIconImage(new ImageIcon(getClass().getResource("picDraw.png")).getImage());} //picDraw
        catch(NullPointerException ex){}
        draw.setResizable(false);
        draw.setVisible(true);
    }
    
    private void calculate(){
        boolean dummy;
        iterations =0;
        while(distribute()){
            dummy = distribute();
            dummy = distribute();
            dummy = distribute();
            dummy = distribute();
            dummy = distribute();
            dummy = distribute();
            updateIterations();
        }
        calc.dispose();
        
        makeDrawWindow();
        dr = new Drawing(xlength, ylength, sand, size, iterations);
        update();
        if(sand > 1000000){
            dr.saveImage();
        }
        draw.dispose();
    }
    
    private boolean distribute(){
        iterations++;
        boolean re = false;
        for(int x = 0; x<pile.length; x++){
            if(pile[x] >= 4){
                split(x);
                re=true;
            }
        }
        return re;
    }
    
    private void split(int x){
        int val = pile[x];
        pile[x] = val % 4;
        int i = (int)(val * 0.25);
        try{
            if(x % xlength != 0){
                pile[x-1] += i;
            }
            if(x % xlength != xlength-1){
                pile[x+1] += i;
            }
            if(x >= xlength){
                pile[x-xlength] += i;
            }
            if(x < xlength*(ylength-1)){
                pile[x+xlength] += i;
            }
        }catch(ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: x = "+ String.valueOf(x) +"; i = "+ String.valueOf(i));
        }catch(IllegalArgumentException e){
            System.out.println("COMP ERROR: x = "+String.valueOf(x)+" i = "+String.valueOf(i));
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    
    private void update(){
        if(size > 1){
            for(int x = 0; x<pile.length; x++){
                dr.update(x, pile[x]);
            }
        }else{
            for(int x = 0; x<pile.length; x++){
                dr.update2(x, pile[x]);
            }
        }
        dr.repaint();
    }
    
    private void updateIterations(){
        l.setText(String.valueOf(iterations));
        jp.paintImmediately(l.getBounds());
    }
}





















