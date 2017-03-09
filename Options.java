import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class Options
{
    JFrame f;
    JTextField[][] jtfs;
    JLabel[] jls;
    JButton jb;
    
    int[] clrs;
    int x;
    int y;
    int sand; 
    int size;
    int iterations;
    int version;
    boolean forcedcalc;
    String source;
    
    public Options(int[] clrs, int x, int y, int sand, int size, int iterations, int version, boolean forcedcalc, String source){
        jtfs = new JTextField[3][4];
        jls = new JLabel[4];
        this.clrs = clrs;
        this.x = x;
        this.y = y;
        this.sand = sand;
        this.size = size;
        this.iterations = iterations;
        this.version = version;
        this.forcedcalc = forcedcalc;
        this.source = source;
        makeFrame();
    }
    
    private void back(){
        for(int i = 0; i<4; i++){
            updateColor(i);
        }
        f.setVisible(false);
        if(source.equals("input")){
            Input in = new Input();
            in.setValues(x, y, sand, size, forcedcalc);
            in.setColors(clrs);
        }else if(source.startsWith("drawing")){
            Drawing dr = new Drawing(clrs, x, y, sand, size, iterations, version, forcedcalc, source.split("-")[1]); //source of drawing: input/list/pics(list)...
            dr.makeGraphics();
            dr.loadInstant();
        }
        f.dispose();
    }
    
    private void setDefault(){
        clrs[0] = -16777216;
        clrs[1] = -10092504;
        clrs[2] = -6750174;
        clrs[3] = -65536;
        for(int i = 0; i<4; i++){
            for(int j = 0; j<3; j++){
                jtfs[j][i].setText(String.valueOf((clrs[i] >> (2-j)*8) & 255));
            } 
            updateColor(i);
        }
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
    }
    
    private void addActionListener(int j, int i){
        jtfs[j][i].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){updateColor(i);}});
        jb.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){updateColor(i);}});
    }
    
    private void makeFrame(){
        f = new JFrame("Options");
        f.setBounds(50,50,100,100);
        f.setVisible(false);
        f.setResizable(false);
        f.setLayout(new BorderLayout(10,10));
        
        JPanel jp = new JPanel(new GridLayout(5,6,5,5));
        
        jb = new JButton("Back");
        jb.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){back();}});
        jp.add(jb);
        
        jb = new JButton("Default");
        jb.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){setDefault();}});
        jp.add(jb);
        
        jb = new JButton("Confirm");
        jb.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){for(int i = 0; i<4; i++){updateColor(i);}}});
        jp.add(jb);
        
        jp.add(new JLabel("Red:"));
        jp.add(new JLabel("Green:"));
        jp.add(new JLabel("Blue:"));
        
        for(int i = 0; i<4; i++){
            jp.add(new JLabel("Color for "+String.valueOf(i)+":"));
            
            jls[i] = new JLabel();
            jls[i].setBackground(new Color(clrs[i]));
            jls[i].setOpaque(true);
            jp.add(jls[i]);
            
            jb = new JButton("Ok");
            jp.add(jb);
            
            for(int j = 0; j<3; j++){
                jtfs[j][i] = new JTextField(String.valueOf((clrs[i] >> (2-j)*8) & 255));
                addActionListener(j,i);
                jp.add(jtfs[j][i]);
            } 
        }
        
        f.add(new JLabel(), "North");
        f.add(new JLabel(), "South");
        f.add(new JLabel(), "East");
        f.add(new JLabel(), "West");
        f.add(jp, "Center");
        f.pack();
        f.setVisible(true);
    }
}
