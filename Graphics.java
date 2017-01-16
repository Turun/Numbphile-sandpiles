import java.awt.*;
import java.awt.event.*;
public class Graphics
{
    Color black = new Color(0,0,0);
    Color red     = new Color(255,0,0);
    Color green    = new Color(0,255,0);
    Color blue    = new Color(0,0,255); 
    Color white    = new Color(255,255,255);
    Color yellow = new Color(0,255,255);
    
    Frame f;
    Frame wait;
    Button back;
    Label[][] field;
    int size;
    int sand;
    int xlength;
    int ylength;
    
    Input in;
    
    public Graphics(int size, int xlength, int ylength, Input in, int sand){
        f = new Frame("Finite Plane");
        f.setLocation(200,200);
        f.setSize((14+(xlength*size)+14),(37+(ylength*size)+14+25));
        f.setVisible(false);
        f.setResizable(false);
        
        wait = new Frame("working on it...");
        f.setLocation(200,200);
        f.setSize((14+(xlength*size)+14),(37+(ylength*size)+14+25));
        f.setVisible(true);
        f.setResizable(false);
        
        back = new Button("Back");
        back.setSize(40,20);
        back.setLocation(14,37);
        back.setVisible(true);
        back.setEnabled(true);
        f.add(back);
        
        this.in = in;
        this.size = size;
        this.sand = sand;
        this.xlength = xlength;
        this.ylength = ylength;
        
        field = new Label[xlength][ylength];
        makeField();
        wait.setVisible(false);
        f.setVisible(true);
        
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(1);     
            }
        });
        
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                f.setVisible(false);
                f = null;
                in.reactivate(xlength,ylength,size,sand);
            }
        });
    }
    
    private void makeField(){
        for(int x = 0; x < xlength; x++){
            for(int y = 0; y < ylength; y++){
                field[x][y] = new Label();
                field[x][y].setSize(size,size);
                field[x][y].setLocation(14+x*size, 37+y*size+25); //25 for button
                field[x][y].setBackground(black);
                field[x][y].setVisible(true);
                f.add(field[x][y]);
            }
        }
    }
    
    public void update(int x, int y, int i){
        try{
            if(i == 0){field[x][y].setBackground(white);}
            if(i == 1){field[x][y].setBackground(yellow);}
            if(i == 2){field[x][y].setBackground(blue);}
            if(i == 3){field[x][y].setBackground(red);}
            if(i > 3) {field[x][y].setBackground(black);}
        }catch(IllegalArgumentException e){
            System.out.println("COMP ERROR in update x = "+String.valueOf(x)+" y = "+String.valueOf(y)+" i = "+String.valueOf(i));
        }catch(Exception ex){
            System.out.println("ERROR in update x = "+String.valueOf(x)+" y = "+String.valueOf(y)+" i = "+String.valueOf(i));
        }
    }
}
