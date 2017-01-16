
public class Mechanics
{
    Graphics gr;
    int pile[][];
    int xlength;
    int ylength;
    
    
    
    public Mechanics(int size, int xlength, int ylength, int startPiles, Input in){
        this.xlength = xlength;
        this.ylength = ylength;
        pile = new int[xlength][ylength];
        pile[(int)(xlength/2)][(int)(ylength/2)] = startPiles;
        gr = new Graphics(size, xlength, ylength, in, startPiles);
        calculate();
    }
    
    private void calculate(){
        boolean dummy;
        while(distribute()){
            //dummy = distribute();
        }
        update();
    }
    
    private boolean distribute(){
        for(int x = 0; x < xlength; x++){
            for(int y = 0; y < ylength; y++){
                try{
                    if(pile[x][y] >= 4){
                        split(x,y);
                        return true;
                    }
                }catch(IllegalArgumentException e){
                    System.out.println("COMP ERROR in distribute x = "+String.valueOf(x)+" y = "+String.valueOf(y)+" value = "+String.valueOf(pile[x][y]));
                }catch(Exception ex){
                    System.out.println("ERROR in distribute x = "+String.valueOf(x)+" y = "+String.valueOf(y)+" value = "+String.valueOf(pile[x][y]));
                }
            }
        }
        return false;
    }
    
    private void split(int x, int y){
        int val = pile[x][y];
        pile[x][y] = val%4;
        int i = (int)(val/4);
        gr.update(x,y,pile[x][y]);
        add(x+1,y,i);
        add(x-1,y,i);
        add(x,y+1,i);
        add(x,y-1,i);
    }
    
    private void add(int x, int y, int i){
        try{
            pile[x][y] += i;
            gr.update(x,y,pile[x][y]);
        }catch(ArrayIndexOutOfBoundsException e){
            //System.out.println("nothing...");
        }catch(IllegalArgumentException e){
            System.out.println("COMP ERROR in add x = "+String.valueOf(x)+" y = "+String.valueOf(y)+" value = "+String.valueOf(pile[x][y])+" i = "+String.valueOf(i));
        }catch(Exception ex){
            System.out.println("ERROR in add x = "+String.valueOf(x)+" y = "+String.valueOf(y)+" value = "+String.valueOf(pile[x][y])+" i = "+String.valueOf(i));
        }
    }
    
    private void update(){
        for(int x = 0; x < xlength; x++){
            for(int y = 0; y < ylength; y++){
                gr.update(x,y,pile[x][y]);
            }
        }
    }
}
