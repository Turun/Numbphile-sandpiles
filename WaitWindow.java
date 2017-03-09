import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import javax.imageio.ImageIO;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.EventQueue;

//import java.util.Timer;
//import java.util.TimerTask;

import java.io.File;
import java.io.IOException;

public class WaitWindow
{
    //Timer timer;
    //TimerTask action;
    //public void printThreadName(){
    //    System.out.println("Ww (saving / timer): "+Thread.currentThread().getName());
    //}
    
    
    private JFrame calc;
    private JFrame draw;
    
    private JLabel[][] lbs;
    
    private JPanel jp;
    
    private Mechanics mech;
    private Ticker ticker;
    
    int x;
    int y;
    int sand;
    int size;
    int iterations;
    
    boolean updating = true;
    
    public WaitWindow(Mechanics mech)
    {
        //System.out.println("Ww (initialising): "+Thread.currentThread().getName());
        
        this.mech = mech;
        this.x = mech.xlength;
        this.y = mech.ylength;
        this.sand = mech.sand;
        this.size = mech.size;
        this.ticker = new Ticker();
        
        //action = new TimerTask(){public void run(){printThreadName();}};
        //timer = new Timer();
        //timer.scheduleAtFixedRate(action, 1000L, 1000L);
    }
    
    public void start(){
        //System.out.println("Ww (starting): "+Thread.currentThread().getName());
        
        makeWaitWindow();
        ticker.start();
    }
    
    public  void update(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                lbs[1][1].setText(String.valueOf(iterations = mech.iterations));
                for(int i = 0; i<6; i++){
                    jp.paintImmediately(lbs[0][i].getBounds());
                    jp.paintImmediately(lbs[1][i].getBounds());
                }
            }
        });
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
        try{
            draw.setIconImage(new ImageIcon(getClass().getResource("pics/picDraw.png")).getImage());
            draw.add(new JLabel(new ImageIcon(ImageIO.read(new File("pics/picDraw.png")))));
        }catch(NullPointerException ex){}
        catch(IOException e){}
        draw.setResizable(false);
        draw.setVisible(true);
        draw.requestFocus();
    }
    
    private void beenden(){
        //System.out.println("Ww (closing): "+Thread.currentThread().getName());
        //System.out.println("closing...");
        mech.close = true;
        closeWaitWindow();
        ticker = null;
    }
    
    private void makeWaitWindow(){
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        
        calc = new JFrame("Calculating...");
        calc.setVisible(false);
        calc.setResizable(false);
        calc.setLayout(gbl);
        calc.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //(beenden());
        calc.setIconImage(new ImageIcon(getClass().getResource("pics/picCalc.png")).getImage());
        calc.addWindowListener(new WindowAdapter(){
            public void WindowClosing(WindowEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        //System.out.println("action registered");
                        beenden();
                    }
                });
            }
        });
        
        
        jp = new JPanel(gbl);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        lbs = new JLabel[2][6];
        lbs[0][0] = new JLabel("Expected Iterations:");
        lbs[0][1] = new JLabel("Iterations:");
        lbs[0][2] = new JLabel("X:");
        lbs[0][3] = new JLabel("Y:");
        lbs[0][4] = new JLabel("Sand:");
        lbs[0][5] = new JLabel("Size:");
        
        lbs[1][0] = new JLabel(String.valueOf((int)(mech.sand * 0.1)));
        lbs[1][1] = new JLabel("waiting...");
        lbs[1][2] = new JLabel(String.valueOf(mech.xlength));
        lbs[1][3] = new JLabel(String.valueOf(mech.ylength));
        lbs[1][4] = new JLabel(String.valueOf(mech.sand));
        lbs[1][5] = new JLabel(String.valueOf(mech.size));
        
        for(int i = 0; i<6; i++){
            gbc.gridy = i;
            gbc.gridx = 0;
            gbl.setConstraints(lbs[0][i], gbc);
            jp.add(lbs[0][i]);
            
            gbc.gridx = 1;
            gbl.setConstraints(lbs[1][i], gbc);
            jp.add(lbs[1][i]);
        }
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(45,60,45,60);
        gbl.setConstraints(jp, gbc);
                
        calc.add(jp);
        calc.pack();
        calc.setLocation(1338-calc.getWidth(),27); //oben rechts ins eck
        calc.setVisible(true);
        calc.repaint();
        jp.update(jp.getGraphics());
    }
    
    class Ticker extends Thread{
        public Ticker(){
            iterations = mech.iterations;
        }
        
        public void run(){
            //System.out.println("Ticker (running): "+Thread.currentThread().getName());
            int i = 0;
            while(updating){
                update();
                if(iterations - i > 5000){
                    //System.out.println("Ticker (saving): "+Thread.currentThread().getName());
                    i = iterations;
                    mech.save = true;
                    
                    //ww.printThreadName();
                }
                try{
                    this.sleep(70L);
                }catch(InterruptedException e){}
            }
            closeWaitWindow();
        }
    }
}
