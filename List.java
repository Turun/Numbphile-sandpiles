import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

import java.io.File;
import java.io.IOException;
public class List{
    File dir;
    
    JFrame f;
    JLabel l;
    JButton back;
    JButton b;
    
    JPanel header;
    JPanel main;
    
    JScrollPane jsp;
    
    File[] fList;
    
    //GridBagLayout gbl;
    //GridBagConstraints gbc;
    
    boolean positiveSort;
    
    int x;
    int y;
    int sand;
    int size;
    int iterations;
    int[] clrs;
    String directory;
    boolean forcedcalc;
    
    int version;
    
    public List(int[] clrs, int x, int y, int sand, int size, int iterations, int version, boolean forcedcalc, String directory){
        if(directory.startsWith("incomplete")){
            dir = new File("./Incomplete");
        }else if(directory.startsWith("encodedPics")){
            dir = new File("./encodedPics");
        }else if(directory.startsWith("pics")){
            dir = new File("./pics");
        }
        this.x = x;
        this.y = y;
        this.sand = sand;
        this.size = size;
        this.iterations = iterations;
        this.version = version;
        this.clrs = clrs;
        this.forcedcalc = forcedcalc;
        this.positiveSort = true;
        this.directory = directory;
        
        if(directory.startsWith("pics")){
            main = new JPanel(new GridLayout(0,7,5,5));
            header = new JPanel(new GridLayout(1,7,5,5));
        }else{
            main = new JPanel(new GridLayout(0,6,5,5));
            header = new JPanel(new GridLayout(1,6,5,5));
        }
        makeHeader();
        
        fList = dir.listFiles();
        if(directory.startsWith("pics")){
            int iterator = 0;
            File[] help = new File[fList.length-3];
            for(int i = 0; i< help.length; i++){
                if(!fList[iterator].getName().startsWith("pic")){
                    help[i] = fList[iterator];
                }else{
                    iterator++;
                }
                iterator++;
            }
            fList = null;
            fList = help;
        }
        
        sortList(0); //by x
        makeWindow();
    }
    
    private void openFile(int[] arr){
        f.setVisible(false);
        
        if(directory.startsWith("incomplete")){
            Chooser cho = new Chooser(clrs, arr[0], arr[1], arr[2], arr[3], forcedcalc, "incomplete");
        }else if(directory.equals("encodedPics")){
            Drawing dr = new Drawing(clrs, arr[0], arr[1], arr[2], arr[3], arr[4], version, forcedcalc, "encodedPics");
            dr.makeGraphics();
            dr.loadInstant();
        }else if(directory.equals("pics")){
            Drawing dr = new Drawing(clrs, arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], forcedcalc, "pics");
            dr.makeGraphics();
            dr.loadImage(arr[5]);
        }else if(directory.startsWith("encodedPics-drawing")){
            Drawing dr = new Drawing(clrs, arr[0], arr[1], arr[2], arr[3], arr[4], version, forcedcalc, directory.split("-")[2]);
            dr.makeGraphics();
            dr.loadInstant();
        }else if(directory.startsWith("pics-drawing")){
            Drawing dr = new Drawing(clrs, arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], forcedcalc, directory.split("-")[2]);
            dr.makeGraphics();
            dr.loadImage(arr[5]);
        }else{
            System.out.println("well, looks like i fucked up...");
            System.out.println("Error message: "+directory);
        }
        
        f.dispose();
    }
    
    private void makePanel(int i){
        String filename = fList[i].getName().split("\\.")[0];
        String[] arr = filename.split("-");
        int[] data = new int[arr.length];
        for(int a = 0; a<arr.length; a++){
            data[a] = Integer.valueOf(arr[a]);
        }
        
        l = new JLabel(String.valueOf(data[0]), JLabel.RIGHT);
        main.add(l);
        
        l = new JLabel(String.valueOf(data[1]), JLabel.RIGHT);
        main.add(l);
        
        l = new JLabel(String.valueOf(data[2]), JLabel.RIGHT);
        main.add(l);
        
        l = new JLabel(String.valueOf(data[3]), JLabel.RIGHT);
        main.add(l);
        
        l = new JLabel(String.valueOf(data[4]), JLabel.RIGHT);
        main.add(l);
        
        if(directory.startsWith("pics")){
            l = new JLabel(String.valueOf(data[5]), JLabel.RIGHT);
            main.add(l);
        }
        
        
        JButton b = new JButton("Ok");
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                openFile(data); //without fileextension
            }
        });
        main.add(b);
    }
    
    private void makeHeader(){
        b = new JButton("X:");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){sortList(0);makeJSP();}});
        header.add(b);
        
        b = new JButton("Y:");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){sortList(1);makeJSP();}});
        header.add(b);
        
        b = new JButton("Sand:");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){sortList(2);makeJSP();}});
        header.add(b);
        
        b = new JButton("Size:");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){sortList(3);makeJSP();}});
        header.add(b);
        
        b = new JButton("Iterations:");
        b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){sortList(4);makeJSP();}});
        header.add(b);
        
        if(directory.startsWith("pics")){
            b = new JButton("Version");
            b.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){sortList(5);makeJSP();}});
            header.add(b);
        }
        
        back = new JButton("Back");
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    if(directory.split("-")[1].equals("drawing")){
                        Drawing dr = new Drawing(clrs, x, y, sand, size, iterations, version, forcedcalc, directory.split("-")[2]);
                        dr.makeGraphics();
                        if(directory.split("-")[2].equals("pics")){
                            dr.loadImage(version);
                        }else if(directory.split("-")[2].equals("encodedPics")){
                            dr.loadInstant();
                        }else{
                            System.out.println("looks like we made an error!");
                            System.out.println("Error message: "+directory);
                        }
                    }else{
                        Input in = new Input();
                        in.setValues(x,y,sand,size,forcedcalc);
                        in.setColors(clrs);
                    }
                }catch(ArrayIndexOutOfBoundsException ex){
                    Input in = new Input();
                    in.setValues(x,y,sand,size,forcedcalc);
                    in.setColors(clrs);
                }
                f.setVisible(false);
                f.dispose();
            }
        });
        header.add(back);
    }
    
    public void sortList(int type){
        //sort fList, call makePanel() in loop
        for(int i = 0; i<fList.length; i++){  //bubblesort!
            for(int a = fList.length-1; a>0; a--){
                int var1 = 0;
                int var2 = 0;
                var1 = Integer.valueOf(fList[a-1].getName().split("\\.")[0].split("-")[type]);
                var2 = Integer.valueOf(fList[a  ].getName().split("\\.")[0].split("-")[type]);
                if(var1 > var2){
                    File help = fList[a];
                    fList[a] = fList[a-1];
                    fList[a-1] = help;
                }
            }
        }
    }
    
    private void makeJSP(){
        if(directory.startsWith("pics")){
            main = new JPanel(new GridLayout(0,7,5,5));
        }else{
            main = new JPanel(new GridLayout(0,6,5,5));
        }
        
        if(positiveSort){
            positiveSort = false;
            for(int i = 0; i<fList.length; i++){
                makePanel(i);
            }
        }else{
            positiveSort = true;
            for(int i = fList.length; i>0; i--){
                makePanel(i-1);
            }
        }
        
        if(jsp == null){
            jsp = new JScrollPane(main, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            //jsp.setPreferredSize(new Dimension(500,500));
            f.add(jsp);
        }else{
            jsp.setViewportView(main);
        }
        jsp.revalidate();
    }
    
    private void makeWindow(){
        f = new JFrame("Choose a file");
        f.setLayout(new BorderLayout());
        f.setMaximumSize(new Dimension(600, 500));
        f.setBounds(50,50,100,100);
        f.setResizable(true);
        f.setVisible(false);
        try{f.setIconImage(new ImageIcon(getClass().getResource("pics/picDone.png")).getImage());}
        catch(NullPointerException ex){}
        
        
        
        f.add(header, BorderLayout.PAGE_START);
        makeJSP();
        repaint();
    }
    
    private void repaint(){
        try{jsp.repaint();}
        catch(NullPointerException ex){}
        f.pack();
        f.setVisible(true);
        f.repaint();
        f.requestFocus();
    }
    
    
}
