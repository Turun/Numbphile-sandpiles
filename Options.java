import javax.swing.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class Options
{
    JFrame f;
    JPanel jp;
    JTextField[][] jtfs;
    JLabel[] jls;
    int[] clrs;
    JButton jb;
    JLabel jl;
    JLabel warning;
    JButton back;
    
    
    GridBagLayout gbl;
    GridBagConstraints gbc;
    
    Input in;
    
    public Options(int[] colors, Input in){
        jtfs = new JTextField[3][4];
        jls = new JLabel[4];
        this.clrs = colors;
        this.in = in;
        makeFrame();
    }
    
    private void back(){
        for(int i = 0; i<4; i++){
            updateColor(i);
        }
        in.setColors(clrs);
        f.dispose();
    }
    
    private void updateColor(int color){
        int red = 0;
        int green = 0;
        int blue = 0;
        try{red = Integer.valueOf(jtfs[0][color].getText());}catch(Exception e){}
        try{green = Integer.valueOf(jtfs[1][color].getText());}catch(Exception e){}
        try{blue = Integer.valueOf(jtfs[2][color].getText());}catch(Exception e){}
        
        jls[color].setBackground(new Color(red,green,blue));
        clrs[color] = new Color(red, green, blue).getRGB();
        in.setColors(clrs);
    }
    
    private void makePanels(){
        jp = new JPanel(new GridLayout(1,6,5,5));
        
        jb = new JButton("Back");
        jb.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){back();}});
        jp.add(jb);
        
        jl = new JLabel("Red:");
        jp.add(jl);
        jl = new JLabel("Green:");
        jp.add(jl);
        jl = new JLabel("Blue:");
        jp.add(jl);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbl.setConstraints(jp, gbc);
        f.add(jp);
        
        
        for(int i = 0; i<4; i++){
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            jp = new JPanel(gbl);
            jl = new JLabel("Color for "+String.valueOf(i)+":");
            gbl.setConstraints(jl, gbc);
            jp.add(jl);
            
            gbc.gridx = 1;
            jls[i] = new JLabel();
            jls[i].setBackground(new Color(clrs[i]));
            gbl.setConstraints(jls[i], gbc);
            jp.add(jls[i]);
            
            gbc.gridx = 5;
            jb = new JButton("Ok");
            gbl.setConstraints(jb, gbc);
            jp.add(jb);
            
            for(int j = 0; j<3; j++){
                /**
                clrs[i] >> 16 // red
                clrs[i] >> 8  // green
                clrs[i] >> 0  // blue
                
                (clrs[i] >> (2-j)*8) & 255 --> correct color 
                */
                jtfs[j][i] = new JTextField(String.valueOf((clrs[i] >> (2-j)*8) & 255),3);
                //jtfs[j][i].setActionCommand(String.valueOf(j)+String.valueOf(i));
                addActionListener(j,i);
                gbc.gridx = j+2;
                gbl.setConstraints(jtfs[j][i], gbc);
                jp.add(jtfs[j][i]);
            } 
            
            gbc.gridx = 0;
            gbc.gridy = i+1;
            gbl.setConstraints(jp, gbc);
            f.add(jp);
        }
    }
    
    private void addActionListener(int j, int i){
        jtfs[j][i].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){updateColor(i);}});
        jb.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){updateColor(i);}});
    }
    
    private void makeFrame(){
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        f = new JFrame("Options");
        f.setBounds(50,50,100,100);
        f.setVisible(false);
        f.setResizable(false);
        f.setLayout(gbl);
        
        makePanels();/**
        jp = new JPanel(gbl);
        jl = new JLabel("Color for 0:");
        gbl.setConstraints(jl, gbc);
        jp.add(jl);
        /**
        jtf00 = new JTextField("");
        jtf00.setActionCommand("00");
        gridy = 1;
        gbl.setConstraints(jtf00, gbc);
        jp.add(jtf00);
        
        jtf01 = new JTextField("");
        jtf01.setActionCommand("01");
        gridy = 2;
        gbl.setConstraints(jtf01, gbc);
        jp.add(jtf01);
        
        jtf02 = new JTextField("");
        jtf02.setActionCommand("02");
        gridy = 3;
        gbl.setConstraints(jtf02, gbc);
        jp.add(jtf02);
        *//**
        jb = new JButton("Ok");
        jb.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){updateColor(0);}});*/
        
        f.pack();
        f.setVisible(true);
    }
}
