import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Input
{
    JTextField tfx;     //x length
    JTextField tfy;     //y length
    JTextField tfn;     //start pile sandpile number
    JTextField tfs;     //size of labels
    
    JFrame f;
    JPanel inpt;
    JPanel buttons;
    JButton bx;
    JButton by;
    JButton bn;
    JButton bs;
    JButton bdefault;
    JButton bcreate;
    JButton bList;
    JButton bPics;
    JCheckBox cb;
    
    GridBagLayout gbl;
    GridBagConstraints gbc;
    
    Mechanics mech;
    List list;
    
    int size;
    int x;
    int y;
    int sand;
    boolean forcedcalc;
    
    public Input(){
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        
        f = new JFrame("");
        f.setBounds(200,200,304,225);
        f.setVisible(false);
        f.setLayout(gbl);
        f.setResizable(true);
        try{f.setIconImage(new ImageIcon(getClass().getResource("pics/picDone.png")).getImage());}
        catch(NullPointerException ex){}
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        inpt = new JPanel();
        inpt.setLayout(gbl);
        
        gbc.gridwidth = 4;
        tfx = new JTextField("length of field in x direction", 20);
        gbl.setConstraints(tfx,gbc);
        inpt.add(tfx);
        
        gbc.gridy = 1;
        tfy = new JTextField("length of field in y direction", 20);
        gbl.setConstraints(tfy, gbc);
        inpt.add(tfy);
        
        gbc.gridy = 2;
        tfn = new JTextField("amount of sand in the start pile", 20);
        gbl.setConstraints(tfn, gbc);
        inpt.add(tfn);
        
        gbc.gridy = 3;
        tfs = new JTextField("size of one block in pixels", 20);
        gbl.setConstraints(tfs, gbc);
        inpt.add(tfs);
        
        gbc.gridy = 4;
        cb = new JCheckBox("force calculation");
        gbl.setConstraints(cb, gbc);
        inpt.add(cb);
        
        
        gbc.gridx = 4;
        gbc.gridwidth = 1;
        
        gbc.gridy = 0;
        bx = new JButton("ok");
        gbl.setConstraints(bx, gbc);
        inpt.add(bx);
        
        gbc.gridy = 1;
        by = new JButton("ok");
        gbl.setConstraints(by, gbc);
        inpt.add(by);
        
        gbc.gridy = 2;
        bn = new JButton("ok");
        gbl.setConstraints(bn, gbc);
        inpt.add(bn);
        
        gbc.gridy = 3;
        bs = new JButton("ok");
        gbl.setConstraints(bs, gbc);
        inpt.add(bs);
        
        
        buttons = new JPanel();
        buttons.setLayout(gbl);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        
        bList = new JButton("Complete an old one");
        gbl.setConstraints(bList, gbc);
        buttons.add(bList);
        
        gbc.gridy = 1;
        bdefault = new JButton("Default");
        gbl.setConstraints(bdefault, gbc);
        buttons.add(bdefault);
        
        gbc.gridy = 0;
        gbc.gridx = 1;
        bPics = new JButton("View completed");
        gbl.setConstraints(bPics, gbc);
        buttons.add(bPics);
        
        gbc.gridy = 1;
        bcreate = new JButton("Create");
        gbl.setConstraints(bcreate, gbc);
        buttons.add(bcreate);
        
        
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbl.setConstraints(inpt, gbc);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbl.setConstraints(buttons, gbc);
        
        f.add(inpt);
        f.add(buttons);
        f.pack();
        f.setVisible(true);
        f.repaint();
        
        
        bx.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                getx();
                gety();
                suggestSand();
                suggestSize();}});
        tfx.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                getx();
                gety();
                suggestSand();
                suggestSize();}});
        
        by.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                getx();
                gety();
                suggestSand();
                suggestSize();}});
        tfy.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                getx();
                gety();
                suggestSand();
                suggestSize();}});
        
        bn.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                getsand();}});
        tfn.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                getsand();}});
        
        bs.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                getsize();}});
        tfs.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                getsize();}});
        
        cb.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                forcedcalc = cb.isSelected();}});
        
        
        bdefault.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                setValues(2160,2160,74000,2,true);}});
        
        bcreate.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                create();}});
        
        bList.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                makeList(true);}});
        
        bPics.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
                makeList(false);}});
        
    }
    
    private void getx(){
        try{
            x = Integer.valueOf(tfx.getText());
        }catch(Exception ex){
            x = -1;
            //System.out.println(ex);
        }
    }
    
    private void gety(){
        try{
            y = Integer.valueOf(tfy.getText());
        }catch(Exception ex){
            y = -1;
            //System.out.println(ex);
        }
    }
    
    private void getsand(){
        try{
            sand = Integer.valueOf(tfn.getText());
        }catch(Exception ex){
            sand = -1;
            //System.out.println(ex);
        }
    }
    
    private void getsize(){
        try{
            size = Integer.valueOf(tfs.getText());
        }catch(Exception ex){
            size = -1;
            //System.out.println(ex);
        }
    }
    
    public void setValues(int x, int y, int sand, int size, boolean forcedcalc){
        if(x > 0){
            this.x = x;
            tfx.setText(String.valueOf(x));
        }else{
            tfx.setText("length of field in x direction");
        }
        if(y > 0){
            this.y = y;
            tfy.setText(String.valueOf(y));
        }else{
            tfy.setText("length of field in y direction");
        }
        if(sand > 0){
            this.sand = sand;
            tfn.setText(String.valueOf(sand));
        }else{
            tfn.setText("amount of sand in the start pile");
        }
        if(size > 0){
            this.size = size;
            tfs.setText(String.valueOf(size));
        }else{
            tfs.setText("size of one block in pixels");
        }
        
        this.forcedcalc = forcedcalc;
        cb.setSelected(forcedcalc);
    }
    
    private void create(){
        getx();
        gety();
        getsand();
        getsize();
        forcedcalc = cb.isSelected();
        if(x>0 & y>0 & sand>0 & size>0){
            f.setVisible(false);
            f.dispose();
            mech = new Mechanics(x, y, sand, size, forcedcalc, "input");
        }
    }
    
    private void makeList(boolean incomplete){
        getx();
        gety();
        getsand();
        getsize();
        forcedcalc = cb.isSelected();
        
        f.setVisible(false);
        f.dispose();
        list = new List(incomplete, x, y, sand, size, forcedcalc);
    }
    
    private void suggestSand(){
        int help = 0;
        if(x>y){help = y;}
        else{help = x;}
        
        if(help * help >= 1000000){
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
        if(x > 0 & y > 0){
            int h = (int)(600/y)+1;
            int w = (int)(600/x)+1;
            if(h < w){
                size = h;
            }else{
                size = w;
            }
            tfs.setText(String.valueOf(size));
        }
    }
    
    public static void main(String[] args){
        Input in = new Input();
    }
}
