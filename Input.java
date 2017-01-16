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
    
    Mechanics mech;
    
    int size = 10;
    int x = 20;
    int y = 20;
    int sand = 200;
    
    public Input(){
        this.setBounds(200,200,314,220);
        this.setVisible(true);
        this.setLayout(null);
        //this.setResizable(false);
        
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
        
        
        bdefault = new JButton("Default");
        bdefault.setSize(100,50);
        bdefault.setLocation(25,110);
        bdefault.setEnabled(true);
        bdefault.setVisible(true);
        this.add(bdefault);
        
        bcreate = new JButton("Create");
        bcreate.setSize(100,50);
        bcreate.setLocation(165,110);
        bcreate.setEnabled(true);
        bcreate.setVisible(true);
        this.add(bcreate);
        
        
        
        bx.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                getx();
            }
        });
        
        by.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e){
                gety();
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
        
        bdefault.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e){
                setDefault();
            }
        });
        
        bcreate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e){
                create();
            }
        });
    }
    
    public void reactivate(int x, int y, int size, int sand){
        this.x = x;
        tfx.setText(String.valueOf(x));
        this.y = y;
        tfy.setText(String.valueOf(x));
        this.size = size;
        tfs.setText(String.valueOf(size));
        this.sand = sand;
        tfn.setText(String.valueOf(sand));
        setVisible(true);
    }
    
    private void getx(){
        try{
            x = Integer.valueOf(tfx.getText());
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    
    private void gety(){
        try{
            y = Integer.valueOf(tfy.getText());
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    
    private void getsand(){
        try{
            sand = Integer.valueOf(tfn.getText());
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    
    private void getsize(){
        try{
            size = Integer.valueOf(tfs.getText());
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    
    private void setDefault(){
        tfx.setText("20");
        tfy.setText("20");
        tfn.setText("200");
        tfs.setText("10");
    }
    
    private void create(){
        getx();
        gety();
        getsand();
        getsize();
        setVisible(false);
        mech = new Mechanics(size,x,y,sand,this);
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
