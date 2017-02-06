import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
//import java.awt.BorderLayout;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Drawing
{
    Color black = new Color(0,0,0);
    Color red     = new Color(255,0,0);
    Color green    = new Color(0,255,0);
    Color blue    = new Color(0,0,255); 
    Color white    = new Color(255,255,255);
    Color yellow = new Color(0,255,255);
    
    final int zero  = black.getRGB();
    final int one   = new Color(0x66,0x00,0x28).getRGB();
    final int two   = new Color(0x99,0x00,0x22).getRGB();
    final int three = new Color(0xff,0x00,0x00).getRGB();
    
    JFrame f;
    BufferedImage img;
    JLabel bild;
    GridBagLayout gbl;
    GridBagConstraints gbc;
    
    JPanel buttons;
    JButton back;
    JButton photo;
    
    int xlength;
    int ylength;
    int sand;
    int size;
    int iterations;
    boolean forcedcalc;
    
    int width;
    int height;
    
    public Drawing(int x, int y, int sand, int size, int iterations, boolean forcedcalc){
        this.size = size;
        this.sand = sand;
        this.xlength = x;
        this.ylength = y;
        this.iterations = iterations;
        this.forcedcalc = forcedcalc;
    }
    
    public void makeGraphics(){
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        
        width = xlength*size;
        height = ylength*size;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        f = new JFrame("Finite Plane");
        f.setLocation(20,20);
        f.setSize(width,height);
        f.setVisible(false);
        f.setResizable(true);
        f.setLayout(gbl);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setIconImage(new ImageIcon(getClass().getResource("pics/picDone.png")).getImage());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1; //  = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        if(height > 650){gbc.insets = new Insets((int)(height * 0.375),5,5,5);}
        else{gbc.insets = new Insets(5,5,5,5);}
        
        gbc.gridy = 0;
        back = new JButton("Back");
        gbl.setConstraints(back, gbc);
        f.getContentPane().add(back);
        
        gbc.insets = new Insets(5,5,5,5);
        
        gbc.gridy = 1;
        photo = new JButton("Save Image");
        gbl.setConstraints(photo, gbc);
        f.getContentPane().add(photo);
        
        gbc.gridy = 2;
        JLabel label = new JLabel("Iterations: "+String.valueOf(iterations));
        gbl.setConstraints(label, gbc);
        f.getContentPane().add(label);
        
        gbc.gridy = 3;
        label = new JLabel("x: "+String.valueOf(xlength));
        gbl.setConstraints(label, gbc);
        f.getContentPane().add(label);
        
        gbc.gridy = 4;
        label = new JLabel("y: "+String.valueOf(ylength));
        gbl.setConstraints(label, gbc);
        f.getContentPane().add(label);
        
        gbc.gridy = 5;
        label = new JLabel("Sand: "+String.valueOf(sand));
        gbl.setConstraints(label, gbc);
        f.getContentPane().add(label);
        
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 6;
        label = new JLabel("Size: "+String.valueOf(size));
        gbl.setConstraints(label, gbc);
        f.getContentPane().add(label);
        
        makeBufferedImage();
        
        f.pack();
        f.setVisible(true);
        
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                f.setVisible(false);
                img = null;
                f = null;
                Input in = new Input();
                in.setValues(xlength, ylength, sand, size, forcedcalc);
            }
        });
        
        photo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                saveImage();
            }
        });
    }
    
    public void update(int pos, int i){
        int c = zero;
        if(i == 0){c = zero;}
        if(i == 1){c = one;}
        if(i == 2){c = two;}
        if(i == 3){c = three;}
        
        int x = pos % xlength;
        int y = (int)(pos / xlength);
        for(int a = 0; a<size; a++){
            for(int b = 0; b < size; b++){
                img.setRGB(x*size+a, y*size+b, c);
            }
        }
    }
    
    public void update2(int pos, int i){
        int c = zero;
        if(i == 0){c = zero;}
        if(i == 1){c = one;}
        if(i == 2){c = two;}
        if(i == 3){c = three;}
        
        int x = pos % xlength;
        int y = (int)(pos / xlength);
        img.setRGB(x,y,c);
    }
    
    public void repaint(){
        f.pack();
        f.repaint();
        f.requestFocus();
    }
    
    public void makeBufferedImage(){
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.gridheight = 7;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        
        bild = new JLabel(new ImageIcon(img));
        gbl.setConstraints(bild, gbc);
        f.getContentPane().add(bild);
    }
    
    public void saveImage(){
        try{
            String filename = "pics/";
            filename += String.valueOf(xlength) + "-";
            filename += String.valueOf(ylength) + "-";
            filename += String.valueOf(sand) + "-";
            filename += String.valueOf(size) + "-";
            filename += String.valueOf(iterations) + ".png";
            
            ImageIO.write(img, "PNG", new File(filename));
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    public void loadInstant(){
        try{
            String filename = "pics/";
            filename += String.valueOf(xlength) + "-";
            filename += String.valueOf(ylength) + "-";
            filename += String.valueOf(sand) + "-";
            filename += String.valueOf(size) + "-";
            filename += String.valueOf(iterations) + ".png";
            
            img = ImageIO.read(new File(filename));
        }catch(IOException e){
            System.out.println(e);
        }
        makeBufferedImage();
        repaint();
    }
    
    public int[][] readArray(String filename){
        int[][] re = {{0,0},{0,0},{0,0}};
        try{
            img = ImageIO.read(new File(filename));
            int oldSize = Integer.valueOf(filename.split("-")[3]);
            
            re = new int[(int)(img.getWidth()/oldSize)][(int)(img.getHeight()/oldSize)];
            for(int col = 0; col<re.length; col++){
                for(int row = 0; row<re[col].length; row++){
                    switch(img.getRGB(col,row)){
                        case(-16777216):{
                            re[col][row] = 0;
                        }
                        case(-10092504):{
                            re[col][row] = 1;
                        }
                        case(-6750174):{
                            re[col][row] = 2;
                        }
                        case(-65536):{
                            re[col][row] = 3;
                        }
                    }
                }
            }
        }catch(IOException e){
            System.out.println(e);
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        return re;
    }
    
}
