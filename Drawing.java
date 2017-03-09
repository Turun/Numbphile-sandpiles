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
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.io.RandomAccessFile;
import java.io.FileOutputStream;
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
    int version;
    boolean forcedcalc;
    String origin;
    
    int width;
    int height;
    
    public Drawing(int[] clrs, int x, int y, int sand, int size, int iterations, int version, boolean forcedcalc, String origin){
        this.size = size;
        this.sand = sand;
        this.xlength = x;
        this.ylength = y;
        this.origin = origin;
        this.iterations = iterations;
        this.version = version;
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
    
    private void saveAsImage(){
        saveImage();
        try{
            File dir = new File("pics");
            
            String filename = "";
            filename += String.valueOf(xlength) + "-";
            filename += String.valueOf(ylength) + "-";
            filename += String.valueOf(sand) + "-";
            filename += String.valueOf(size) + "-";
            filename += String.valueOf(iterations);
            File[] all = dir.listFiles();
            
            int recordVersion = 0;
            for(File f: all){
                if(f.getName().startsWith(filename)){
                    int version = Integer.valueOf(f.getName().split("\\.")[0].split("-")[5]);
                    if(version>recordVersion){
                        recordVersion = version;
                    }
                }
            }
            
            filename ="pics/"+filename+ "-"+String.valueOf(recordVersion+1)+".png";
            ImageIO.write(img, "PNG", new File(filename));
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    public void saveImage(){
        double length = xlength*ylength*0.25;
        if(length % 1 != 0){length = (int)(length)+1;}
        byte[] byteOutArr = new byte[(int)(length)];
        
        for(int i = 0; i<byteOutArr.length; i++){byteOutArr[i] = 0;}
        
        int iterator = 0;
        for(int row = 0; row<ylength; row++){
            for(int col = 0; col<xlength; col++){
                int color = img.getRGB(col*size,row*size);
                byte value = 0;
                if(color == one){value = 1;}
                else if(color == two){value = 2;}
                else if(color == three){value = 3;}
                byteOutArr[(int)(iterator*0.25)] |= (byte)(value << ((3 - iterator % 4)*2));
                iterator++;
            }
        }
        
        String filename = "encodedPics/";
        filename += String.valueOf(xlength) + "-";
        filename += String.valueOf(ylength) + "-";
        filename += String.valueOf(sand) + "-";
        filename += String.valueOf(size) + "-";
        filename += String.valueOf(iterations) + ".bin";
        
        try(FileOutputStream out = new FileOutputStream(filename)){
            out.write(byteOutArr);
        }catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    public void loadInstant(){
        String filename = "encodedPics/";
        filename += String.valueOf(xlength) + "-";
        filename += String.valueOf(ylength) + "-";
        filename += String.valueOf(sand) + "-";
        filename += String.valueOf(size) + "-";
        filename += String.valueOf(iterations) + ".bin";
        File fileToRead = new File(filename);
        
        try(RandomAccessFile raf = new RandomAccessFile(filename, "r")){  
            byte[] inArr = new byte[(int)(fileToRead.length())];
            raf.readFully(inArr);
            
            for(int i = 0; i<inArr.length*4; i++){
                byte value = (byte)((inArr[(int)(i/4)] >> ((3-(i%4))*2)) & 3);
                try{update(i, value);}
                catch(ArrayIndexOutOfBoundsException e){}/**for when the last byte is half empty because x*y%4 != 0*/
            }
        }catch(Exception e){//catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
            redirectToNewFrame();
        }
        makeBufferedImage();
        repaint();
    }
    
    public void loadImage(int version){
        this.version = version;
        
        String filename = "pics/";
        filename += String.valueOf(xlength) + "-";
        filename += String.valueOf(ylength) + "-";
        filename += String.valueOf(sand) + "-";
        filename += String.valueOf(size) + "-";
        filename += String.valueOf(iterations) + "-";
        filename += String.valueOf(version) + ".png";
        File fileToRead = new File(filename);
        try{
           img = ImageIO.read(fileToRead); 
        }catch(IOException e){
            System.out.println(e);
            System.out.println(filename);
        }
        makeBufferedImage();
        repaint();
    }
    
    private void extractColors(){
        String filename = "encodedPics/";
        filename += String.valueOf(xlength) + "-";
        filename += String.valueOf(ylength) + "-";
        filename += String.valueOf(sand) + "-";
        filename += String.valueOf(size) + "-";
        filename += String.valueOf(iterations) + ".bin";
        File fileToRead = new File(filename);
        
        if(img == null){makeBufferedImage();}
        if(clrs == null){clrs = new int[4];}
        byte[] inArr;
        byte colorRead = 0;
        try(RandomAccessFile raf = new RandomAccessFile(filename, "r")){  
            inArr = new byte[(int)(fileToRead.length())];
            raf.readFully(inArr);
            
            for(int i = 0; i<inArr.length*4; i++){
                byte value = (byte)((inArr[(int)(i/4)] >> ((3-(i%4))*2)) & 3);
                if(value == colorRead){
                    clrs[colorRead] = img.getRGB((i%xlength)*size, ((int)(i/xlength))*size);
                    colorRead++;
                }
                if(colorRead >3){break;}
            }
            zero = clrs[0];
            one = clrs[1];
            two = clrs[2];
            three = clrs[3];
        }catch(Exception e){//catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
            redirectToNewFrame();
        }
    }
    
    public int[][] readArray(String filename){
        
        int[][] re = {{0,0},{0,0},{0,0}};
        try(RandomAccessFile raf = new RandomAccessFile(filename, "r")){
            int x = Integer.valueOf(filename.split("-")[0].split("/")[1]);
            int y = Integer.valueOf(filename.split("-")[1]);
            int oldSize = Integer.valueOf(filename.split("-")[3]);
            
            File fileToRead = new File(filename);
            byte[] inArr = new byte[(int)(fileToRead.length())];
            raf.readFully(inArr);
            
            
            re = new int[x][y];
            for(int col = 0; col<x; col++){
                for(int row = 0; row<y; row++){
                    re[col][row] = inArr[col*x+row];
                }
            }
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
        f.setResizable(false);
        f.setLayout(gbl);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setIconImage(new ImageIcon(getClass().getResource("pics/picDone.png")).getImage());
        f.addKeyListener(this);
        f.addMouseListener(this);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1; //  = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5,5,5,5);
        
        gbc.gridy = 0;
        back = new JButton("Back");
        gbl.setConstraints(back, gbc);
        f.add(back);
        
        gbc.gridy = 1;
        JButton options = new JButton("Options");
        gbl.setConstraints(options, gbc);
        f.add(options);
        
        gbc.gridy = 2;
        photo = new JButton("Save");
        gbl.setConstraints(photo, gbc);
        f.add(photo);
        
        gbc.gridy = 3;
        JButton saveAsImage = new JButton("Save as image");
        gbl.setConstraints(saveAsImage, gbc);
        f.add(saveAsImage);
        
        gbc.gridy = 4;
        JButton extractColors = new JButton("Extract Colors");
        gbl.setConstraints(extractColors, gbc);
        f.add(extractColors);
        
        gbc.gridy = 5;
        JButton viewPictures = new JButton("View Pictures");
        gbl.setConstraints(viewPictures, gbc);
        f.add(viewPictures);
        
        gbc.gridy = 6;
        JButton viewCompleted = new JButton("View Completed");
        gbl.setConstraints(viewCompleted, gbc);
        f.add(viewCompleted);
        
        
        gbc.gridy = 15;
        JLabel label = new JLabel("Iterations: "+String.valueOf(iterations));
        gbl.setConstraints(label, gbc);
        f.add(label);
        
        gbc.gridy = 16;
        label = new JLabel("x: "+String.valueOf(xlength));
        gbl.setConstraints(label, gbc);
        f.add(label);
        
        gbc.gridy = 17;
        label = new JLabel("y: "+String.valueOf(ylength));
        gbl.setConstraints(label, gbc);
        f.add(label);
        
        gbc.gridy = 18;
        label = new JLabel("Sand: "+String.valueOf(sand));
        gbl.setConstraints(label, gbc);
        f.add(label);
        
        gbc.gridy = 19;
        label = new JLabel("Size: "+String.valueOf(size));
        gbl.setConstraints(label, gbc);
        f.add(label);
        
        if(version > 0){
            gbc.gridy = 20;
            label = new JLabel("Version: "+String.valueOf(version));
            gbl.setConstraints(label, gbc);
            f.add(label);
        }
        
        makeBufferedImage();
        
        repaint();
        f.setVisible(true);
        
        back.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)         {redirectToNewFrame(); }});
        options.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)      {openOptions();        }});
        photo.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)        {saveImage();          }});
        saveAsImage.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)  {saveAsImage();        }});
        extractColors.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){extractColors();      }});
        viewPictures.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {viewPictures();       }});
        viewCompleted.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){viewCompleted();      }});
    }
    
    public void makeBufferedImage(){
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 21;
        gbc.gridheight = 21;
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
    
    private void viewPictures(){
        f.setVisible(false);
        img = null;
        int[] clrs = {zero, one, two ,three};
        f.dispose();
        List li = new List(clrs, xlength, ylength, sand, size, iterations, version, forcedcalc, "pics-drawing-"+origin);
    }
    
    private void viewCompleted(){
        f.setVisible(false);
        img = null;
        int[] clrs = {zero, one, two ,three};
        f.dispose();
        List li = new List(clrs, xlength, ylength, sand, size, iterations, version, forcedcalc, "encodedPics-drawing-"+origin);
    }
    
    private void openOptions(){
        extractColors();
        
        f.setVisible(false);
        f.dispose();
        String source = "drawing-" + origin;
        int[] clrs = {zero, one, two ,three};
        Options opt = new Options(clrs, xlength, ylength, sand, size, iterations, version, forcedcalc, source);
    }
    
    private void redirectToNewFrame(){
        f.setVisible(false);
        img = null;
        int[] clrs = {zero, one, two ,three};
        f.dispose();
        if(origin.equals("input")){
            Input in = new Input();
            in.setValues(xlength, ylength, sand, size, forcedcalc);
            in.setColors(clrs);
        }else{
            List li = new List(clrs, xlength, ylength, sand, size, iterations, version, forcedcalc, origin);
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
