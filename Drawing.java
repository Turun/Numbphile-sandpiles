import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

//import java.io.DataOutputStream;
import java.util.*;
import java.nio.*;
import java.io.*;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Drawing extends MouseAdapter implements KeyListener
{
    Color black = new Color(0,0,0);
    Color red     = new Color(255,0,0);
    Color green    = new Color(0,255,0);
    Color blue    = new Color(0,0,255); 
    Color white    = new Color(255,255,255);
    Color yellow = new Color(0,255,255);
    
    private static int zero  = new Color(0x00,0x00,0x00).getRGB();
    private static int one   = new Color(0x66,0x00,0x28).getRGB();
    private static int two   = new Color(0x99,0x00,0x22).getRGB();
    private static int three = new Color(0xff,0x00,0x00).getRGB();
    
    private static final int keyU = KeyEvent.VK_UP;
    private static final int keyR = KeyEvent.VK_RIGHT;
    private static final int keyD = KeyEvent.VK_DOWN;
    private static final int keyL = KeyEvent.VK_LEFT;
    
    JFrame f;
    JLabel bild;
    JButton back;
    JButton photo;
    JPanel buttons;
    JScrollPane jsp;
    BufferedImage img;
    GridBagLayout gbl;
    GridBagConstraints gbc;
    
    Point oldPoint;
    
    int xlength;
    int ylength;
    int sand;
    int size;
    int[] clrs;
    int iterations;
    boolean forcedcalc;
    String origin;
    
    int width;
    int height;
    
    public Drawing(int x, int y, int sand, int size, int iterations, boolean forcedcalc, String origin, int[] clrs){
        this.size = size;
        this.sand = sand;
        this.xlength = x;
        this.ylength = y;
        this.origin = origin;
        this.iterations = iterations;
        this.forcedcalc = forcedcalc;
        this.zero   = clrs[0];
        this.one    = clrs[1];
        this.two    = clrs[2];
        this.three  = clrs[3];
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
        jsp.paintImmediately(jsp.getBounds());
    }
    
    public void makeBufferedImage(){
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.gridheight = 7;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        
        if(img == null | f == null){makeGraphics();}
        bild = new JLabel(new ImageIcon(img));
        bild.setAutoscrolls(true);
        bild.addMouseListener(this);
        bild.addMouseMotionListener(this);
        
        jsp = new JScrollPane(bild, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setPreferredSize(new Dimension(600,600));
        
        gbl.setConstraints(jsp, gbc);
        f.getContentPane().add(jsp);
    }
    
    public void saveImage(){
        double length = xlength*ylength*0.25;
        if(length % 1 != 0){length = (int)(length)+1;}
        byte[] byteOutArr = new byte[(int)(length)]; 
        System.out.println(length);
        System.out.println(Integer.toBinaryString((byte)(zero>>16 & 0xff)) +"  "+Integer.toBinaryString((byte)(zero>>8 & 0xff)) +"  "+Integer.toBinaryString((byte)(zero)));
        System.out.println(Integer.toBinaryString((byte)(one>>16 & 0xff))  +"  "+Integer.toBinaryString((byte)(one>>8 & 0xff))  +"  "+Integer.toBinaryString((byte)(one)));
        System.out.println(Integer.toBinaryString((byte)(two>>16 & 0xff))  +"  "+Integer.toBinaryString((byte)(two>>8 & 0xff))  +"  "+Integer.toBinaryString((byte)(two)));
        System.out.println(Integer.toBinaryString((byte)(three>>16 & 0xff))+"  "+Integer.toBinaryString((byte)(three>>8 & 0xff))+"  "+Integer.toBinaryString((byte)(three)));
        System.out.println();
        
        int iterator = 0;
        for(int col = 0; col<xlength; col++){
            for(int row = 0; row<ylength; row++){
                int color = img.getRGB(col*size,row*size);
                byte value = 0;
                if(color == zero){value = 0;}
                if(color == one){value = 1;}
                if(color == two){value = 2;}
                if(color == three){value = 3;}
                byteOutArr[(int)(iterator*0.25)] |= value << ((3 - iterator % 4)*2);
                System.out.print(value+"\t");
                iterator++;
            }
            System.out.println();
        }
        System.out.println("\n\n");
        char[] charOutArr = new char[byteOutArr.length];
        for(int a = 0; a<byteOutArr.length; a++){
            charOutArr[a] = (char)(byteOutArr[a]);
            System.out.println(String.valueOf(byteOutArr[a]));
        }
        System.out.println("\n\n");
        System.out.println(new String(charOutArr));
        
        String filename = "pics/";
        filename += String.valueOf(xlength) + "-";
        filename += String.valueOf(ylength) + "-";
        filename += String.valueOf(sand) + "-";
        filename += String.valueOf(size) + "-";
        filename += String.valueOf(iterations) + ".png";
        try(PrintWriter out = new PrintWriter(filename)){
            out.write(new String(charOutArr));
            /**ImageIO.write(img, "PNG", new File(filename));*/
            //FileOutputStream out = new FileOutputStream(filename);
            //out.write(outArr);
            //new FileOutputStream(filename).write(outArr);
            
        }catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    public void loadInstant(){
        /**
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            for(int i = 0; i<y; i++){
                String[] input = br.readLine().split("-");
                for(int a = 0; a<x; a++){
                    re[i*x+a] = Integer.valueOf(input[a]);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }*/
        
        String filename = "pics/";
        filename += String.valueOf(xlength) + "-";
        filename += String.valueOf(ylength) + "-";
        filename += String.valueOf(sand) + "-";
        filename += String.valueOf(size) + "-";
        filename += String.valueOf(iterations) + ".png";
        File fileToRead = new File(filename);
        try(Scanner sc = new Scanner(fileToRead)){
            StringBuilder sb = new StringBuilder((int)(fileToRead.length()));
            while(sc.hasNextLine()){
                sb.append(sc.nextLine());
            }
            try{sb.append(sc.nextLine());}catch(NoSuchElementException e){System.out.println("this is not needed");}
            
            char[] charInArr = sb.toString().toCharArray();
            byte[] byteInArr = new byte[charInArr.length];
            for(int i = 0; i<charInArr.length; i++){
                byteInArr[i] = (byte)(charInArr[i]);
            }
            System.out.println(new String(charInArr));
            
            System.out.println(String.valueOf(+xlength)+"---"+String.valueOf(ylength)+"---"+String.valueOf(size)+"---"+String.valueOf(img.getWidth())+"---"+String.valueOf(img.getHeight()));
            for(int i = 0; i<byteInArr.length*4; i++){
                System.out.println(String.valueOf(byteInArr[(int)i/4])+"---"+String.valueOf(Integer.toBinaryString(byteInArr[(int)i/4])));
                byte value = (byte)((byteInArr[(int)(i/4)] >> (3-((byte)(i%4)))) & 3);
                
                try{update(i, value);}
                catch(ArrayIndexOutOfBoundsException e){}/**for when the last byte is half empty because x*y%4 != 0*/
            }
            //
            //byte[] in = new byte[(int)(fileToRead.length())];
            
            //img = ImageIO.read(new File(filename));
        }catch(Exception e){//catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
            redirectToNewFrame();
        }
        makeBufferedImage();
        repaint();
    }
    
    //@Deprecated
    public int[][] readArray(String filename){
        System.out.println("this is a deprecated method!");
        
        int[][] re = {{0,0},{0,0},{0,0}};
        try{
            //img = ImageIO.read(new File(filename));
            int xlength = Integer.valueOf(filename.split("-")[0].split("/")[1]);
            int ylength = Integer.valueOf(filename.split("-")[1]);
            int oldSize = Integer.valueOf(filename.split("-")[3]);
            //readColors(oldSize);
            
            File fileToRead = new File(filename);
            byte[] inArr = new byte[(int)(fileToRead.length())];
            
            new DataInputStream(new ByteArrayInputStream(inArr));
            
            
            re = new int[xlength][ylength];
            for(int col = 0; col<xlength; col++){
                for(int row = 0; row<ylength; row++){
                    re[col][row] = inArr[col*xlength+row];
                    /**int color = img.getRGB(col*oldSize,row*oldSize);
                    if(color == zero){re[col][row] = 0;}
                    if(color == one){re[col][row] = 1;}
                    if(color == two){re[col][row] = 2;}
                    if(color == three){re[col][row] = 3;}*/
                }
            }
            //}catch(IOException e){
                //  System.out.println(e);
        }catch(Exception ex){
            System.out.println(ex);
            ex.printStackTrace();
        }
        
        return re;
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
        f.addKeyListener(this);
        f.addMouseListener(this);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1; //  = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5,5,5,5);
        
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
        
        repaint();
        f.setVisible(true);
        
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                redirectToNewFrame();
            }
        });
        
        photo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                saveImage();
            }
        });
    }
    
    private void redirectToNewFrame(){
        f.setVisible(false);
        img = null;
        f = null;
        int[] clrs = {zero, one, two ,three};
        if(origin.equals("input")){
            Input in = new Input();
            in.setValues(xlength, ylength, sand, size, forcedcalc);
            in.setColors(clrs);
        }else if(origin.equals("pics")){
            List li = new List(false, xlength, ylength, sand, size, forcedcalc, clrs);
        }else if(origin.equals("incomplete")){
            List li = new List(true, xlength, ylength, sand, size, forcedcalc, clrs);
        }
    }
    
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        int i = e.getKeyCode();
        if(i == keyU){movePicture(0);}
        if(i == keyR){movePicture(1);}
        if(i == keyD){movePicture(2);}
        if(i == keyL){movePicture(3);}
    }
    
    private void movePicture(int direction){
        int scx = jsp.getX();
        int scy = jsp.getY();
        int delta = 15;
        if(direction == 0){
            jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getValue() - delta);
        }else if(direction == 1){
            jsp.getHorizontalScrollBar().setValue(jsp.getHorizontalScrollBar().getValue() + delta);
        }else if(direction == 2){
            jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getValue() + delta);
        }else if(direction == 3){
            jsp.getHorizontalScrollBar().setValue(jsp.getHorizontalScrollBar().getValue() - delta);
        }else{
            System.out.println(String.valueOf(direction) + " I failed");
        }
    }
    
    public void mouseDragged(MouseEvent e){
        if(oldPoint != null){
            JViewport viewPort =(JViewport)(SwingUtilities.getAncestorOfClass(JViewport.class, bild));
            if(viewPort != null){
                int deltaX = (int)((oldPoint.x - e.getX()));
                int deltaY = (int)((oldPoint.y - e.getY()));
                
                Rectangle view = viewPort.getViewRect();
                view.x += deltaX;
                view.y += deltaY;
                
                bild.scrollRectToVisible(view);
                bild.revalidate();
            }
        }
        oldPoint = e.getPoint();
    }
    
    public void mousePressed(MouseEvent e){
        oldPoint = new Point(e.getPoint());
    }
}
