import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

import java.io.PrintWriter;

public class WaitWindow extends Thread
{
    
    private JFrame calc;
    private JFrame draw;
    
    private JLabel l00;
    private JLabel l01;
    private JLabel l02;
    private JLabel l03;
    private JLabel l04;
    
    private JLabel l10;
    private JLabel l11;
    private JLabel l12;
    private JLabel l13;
    private JLabel l14;
    
    private JPanel jp;
    
    private Mechanics mech;
    int x;
    int y;
    int sand;
    int size;
    int iterations;
    
    boolean updating = true;
    
    public WaitWindow(Mechanics mech)
    {
        this.mech = mech;
        this.x = mech.xlength;
        this.y = mech.ylength;
        this.sand = mech.sand;
        this.size = mech.size;
    }
    
    public void run(){
        makeWaitWindow();
        int i = 0;
        while(updating){
            update();
            if(iterations - i > 10000){
                i = iterations;
                mech.save = true;
            }
            try{
                this.sleep(70);
            }catch(InterruptedException e){}
        }
        calc.dispose();
    }
    
    private void update(){
        l10.setText(String.valueOf(iterations = mech.iterations));
        jp.paintImmediately(l00.getBounds());
        jp.paintImmediately(l01.getBounds());
        jp.paintImmediately(l02.getBounds());
        jp.paintImmediately(l03.getBounds());
        jp.paintImmediately(l04.getBounds());
        
        jp.paintImmediately(l10.getBounds());
        jp.paintImmediately(l11.getBounds());
        jp.paintImmediately(l12.getBounds());
        jp.paintImmediately(l13.getBounds());
        jp.paintImmediately(l14.getBounds());
    }
    
    public void closeDrawWindow(){
        if(draw != null)draw.dispose();
    }
    
    public void closeWaitWindow(){
        if(calc != null)calc.dispose();
    }
    
    public void makeDrawWindow(){
        draw = new JFrame("Drawing...");
        draw.setBounds(400,300,300,300);
        try{draw.setIconImage(new ImageIcon(getClass().getResource("pics/picDraw.png")).getImage());} //picDraw
        catch(NullPointerException ex){}
        draw.setResizable(false);
        draw.setVisible(true);
        draw.requestFocus();
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
        calc.setIconImage(new ImageIcon(getClass().getResource("pics/picCalc.png")).getImage());
        
        
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
        l00 = new JLabel("Iterations:");
        gbl.setConstraints(l00, gbc);
        jp.add(l00);
                        
        gbc.gridy = 1;
        l01 = new JLabel("x:");
        gbl.setConstraints(l01, gbc);
        jp.add(l01);
        
        gbc.gridy = 2;
        l02 = new JLabel("y:");
        gbl.setConstraints(l02, gbc);
        jp.add(l02);
        
        gbc.gridy = 3;
        l03 = new JLabel("Sand:");
        gbl.setConstraints(l03, gbc);
        jp.add(l03);
        
        gbc.gridy = 4;
        l04 = new JLabel("Size:");
        gbl.setConstraints(l04, gbc);
        jp.add(l04);
                
        
        gbc.gridx = 1;
                
        gbc.gridy = 0;
        l10 = new JLabel("waiting...");
        gbl.setConstraints(l10, gbc);
        jp.add(l10);
        
        gbc.gridy = 1;
        l11 = new JLabel(String.valueOf(mech.xlength));
        gbl.setConstraints(l11, gbc);
        jp.add(l11);
        
        gbc.gridy = 2;
        l12 = new JLabel(String.valueOf(mech.ylength));
        gbl.setConstraints(l12, gbc);
        jp.add(l12);
        
        gbc.gridy = 3;
        l13 = new JLabel(String.valueOf(mech.sand));
        gbl.setConstraints(l13, gbc);
        jp.add(l13);
        
        gbc.gridy = 4;
        l14 = new JLabel(String.valueOf(mech.size));
        gbl.setConstraints(l14, gbc);
        jp.add(l14);
        
        
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
}
