import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Input extends JFrame implements ActionListener
{
    JTextField tfx;     //x length
    JTextField tfy;     //y length
    JTextField tfn;     //start pile sandpile number
    JTextField tfs;     //size of labels
    
    JButton bx;
    JButton by;
    JButton bn;
    JButton bs;
    JButton bdefault;
    JButton bcreate;
    JCheckBox cb;
    JLabel cbl;
    
    Mechanics mech;
    
    int size;
    int x;
    int y;
    int sand;
    boolean forcedcalc;
    
    public Input(){
        this.setBounds(200,200,304,225);
        this.setVisible(true);
        this.setLayout(null);
        this.setResizable(false);
        try{this.setIconImage(new ImageIcon(getClass().getResource("pics/picDone.png")).getImage());}
        catch(NullPointerException ex){}
        
        
        tfx = new JTextField("length of field in x direction");
        tfx.setSize(220,20);
        tfx.setLocation(5,5);
        tfx.setActionCommand("tfx");
        tfx.addActionListener(this);
        this.add(tfx);
        
        bx = new JButton("ok");
        bx.setSize(60,20);
        bx.setLocation(230,5);
        bx.setEnabled(true);
        bx.setVisible(true);
        this.add(bx);
        
        
        tfy = new JTextField("length of field in y direction");
        tfy.setSize(220,20);
        tfy.setLocation(5,30);
        tfy.setActionCommand("tfy");
        tfy.addActionListener(this);
        this.add(tfy);
        
        by = new JButton("ok");
        by.setSize(60,20);
        by.setLocation(230,30);
        by.setEnabled(true);
        by.setVisible(true);
        this.add(by);
        
        
        tfn = new JTextField("amount of sand in the start pile");
        tfn.setSize(220,20);
        tfn.setLocation(5,55);
        tfn.setActionCommand("tfn");
        tfn.addActionListener(this);
        this.add(tfn);
        
        bn = new JButton("ok");
        bn.setSize(60,20);
        bn.setLocation(230,55);
        bn.setEnabled(true);
        bn.setVisible(true);
        this.add(bn);
        
        
        tfs = new JTextField("size of labels");
        tfs.setSize(220,20);
        tfs.setLocation(5,80);
        tfs.setActionCommand("tfs");
        tfs.addActionListener(this);
        this.add(tfs);
        
        bs = new JButton("ok");
        bs.setSize(60,20);
        bs.setLocation(230,80);
        bs.setEnabled(true);
        bs.setVisible(true);
        this.add(bs);
        
        cb = new JCheckBox("force calculation");
        cb.setSize(20,20);
        cb.setLocation(5,105); //-5
        cb.setEnabled(true);
        cb.setVisible(true);
        this.add(cb);
        
        cbl = new JLabel("force calculation");
        cbl.setSize(100,20);
        cbl.setLocation(27,105);
        cbl.setVisible(true);
        this.add(cbl);
        
        bdefault = new JButton("Default");
        bdefault.setSize(100,50);
        bdefault.setLocation(25,135);
        bdefault.setEnabled(true);
        bdefault.setVisible(true);
        this.add(bdefault);
        
        bcreate = new JButton("Create");
        bcreate.setSize(100,50);
        bcreate.setLocation(165,135);
        bcreate.setEnabled(true);
        bcreate.setVisible(true);
        this.add(bcreate);
        
        
        this.repaint();
        
        
        
        bx.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                getx();
                gety();
                suggestSand();
                suggestSize();
            }
        });
        
        by.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e){
                getx();
                gety();
                suggestSand();
                suggestSize();
            }
        });
        
        bn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e){
                getsand();
            }
        });
        
        bs.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e){
                getsize();
            }
        });
        
        cb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                forcedcalc = cb.isSelected();
            }
        });
        
        bdefault.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e){
                setValues(2160,2160,74000,2,true);
            }
        });
        
        bcreate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e){
                create();
            }
        });
    }
    
    private void getx(){
        try{
            x = Integer.valueOf(tfx.getText());
            if(x<1){
                x = 1;
            }
        }catch(Exception ex){
            //System.out.println(ex);
        }
    }
    
    private void gety(){
        try{
            y = Integer.valueOf(tfy.getText());
            if(y<1){
                y = 1;
            }
        }catch(Exception ex){
            //System.out.println(ex);
        }
    }
    
    private void getsand(){
        try{
            sand = Integer.valueOf(tfn.getText());
        }catch(Exception ex){
            //System.out.println(ex);
        }
    }
    
    private void getsize(){
        try{
            size = Integer.valueOf(tfs.getText());
        }catch(Exception ex){
            //System.out.println(ex);
        }
    }
    
    public void setValues(int x, int y, int sand, int size, boolean forcedcalc){
        this.x = x;
        tfx.setText(String.valueOf(x));
        this.y = y;
        tfy.setText(String.valueOf(y));
        this.sand = sand;
        tfn.setText(String.valueOf(sand));
        this.size = size;
        tfs.setText(String.valueOf(size));
        this.forcedcalc = forcedcalc;
        cb.setSelected(forcedcalc);
    }
    
    private void create(){
        getx();
        gety();
        getsand();
        getsize();
        forcedcalc = cb.isSelected();
        setVisible(false);
        mech = new Mechanics(x, y, sand, size, forcedcalc);
    }
    
    private void suggestSand(){
        int help = 0;
        if(x>y){help = y;}
        else{help = x;}
        
        if(help * help >= 2000000){
            sand = (int)(help * help * 1.9);
        }else if(help * help >= 1000000){
            sand = (int)(help * help * 1.8);
        }else if(help * help >= 500000){
            sand = (int)(help * help * 1.7); 
        }else if(help * help >= 200000){
            sand = (int)(help * help * 1.6);
        }else{
            sand = (int)(help * help * 1.5);
        }
        
        tfn.setText(String.valueOf(sand));
    }
    
    private void suggestSize(){
        int h = (int)(600/y);
        int w = (int)(600/x);
        if(h < w){
            size = h;
        }else{
            size = w;
        }
        if(x*y >= 360000){
            size = 1; 
        }
        tfs.setText(String.valueOf(size));
    }
    
    public void actionPerformed(ActionEvent e){
        //do this
        String actioncmd = e.getActionCommand();
        switch(actioncmd){
            case("tfx"):{
                getx();
            }
            case("tfy"):{
                gety();
            }
            case("tfn"):{
                getsand();
            }
            case("tfs"):{
                getsize();
            }
            default:{
                System.out.println("this: "+actioncmd);
            }
        }
    }
    
    public static void main(String[] args){
        Input in = new Input();
    }
}
