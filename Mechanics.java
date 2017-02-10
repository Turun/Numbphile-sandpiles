import java.util.ArrayList;
public class Mechanics extends Thread
{
    Drawing dr;
    WaitWindow ww;
    DirSearch dir;
    
    int pile[];
    int xlength;
    int ylength;
    int sand;
    int size;
    int iterations;
    boolean forcedcalc;
    String origin;
    
    boolean warte;
    
    public Mechanics(int x, int y, int sand, int size, boolean forcedcalc, String origin){
        this.xlength = x;
        this.ylength = y;
        this.sand = sand;
        this.size = size;
        this.iterations = 0;
        this.origin = origin;
        this.forcedcalc = forcedcalc;
        this.warte = false;
        
        pile = new int[xlength*ylength];
        
        dir = new DirSearch();
        int expectedIterations = dir.checkPic(xlength,ylength,sand,size);
        String filenameIncomplete = dir.getUncompletedName(xlength, ylength, sand, size);
        String filenameSmaller = dir.getNextSmallerPic(xlength, ylength, sand, size);
        
        /**
         * forcedcalc                           true/false
         * filename (incomplete)                foundNothing / filename
         * expectedIterations (finished pic)    0 / !0
         * filename (smaller)                   0-0-0-0-0.png / filename
         * 
         *                                      ______________________||____________________
         *                                     /                                            \
         *ei:                         ________0________                                      !0
         *                           /                 \                                     ||
         *fnI              ____foundN_____              name                              load, pic
         *                /               \              ||
         *fc        _false_               true       load, calc   
         *         /       \               ||  
         *fnS    000        name          calc
         *       ||          ||
         *      calc    load, read, calc
         */
        
        if(expectedIterations >= 0){
            //System.out.println("0");
            loadInstant(expectedIterations);
        }else{
            if(filenameIncomplete.equals("foundNothing")){
                if(forcedcalc){
                    //System.out.println("1");
                    calculate();
                }else{
                    if(filenameSmaller.equals("0-0-0-0-0.png")){
                        //System.out.println("2");
                        calculate();
                    }else{
                        //System.out.println("3");
                        makeFromSmaller(expectedIterations, filenameSmaller);
                    }
                }
            }else{
                //System.out.println("4");
                makeFromUncompleted(filenameIncomplete);
            }
        }
    }
    
    private void setSand(){
        int middle = (int)(xlength * ylength * 0.5);
        if(xlength % 2 == 0){
            if(ylength % 2 == 0){
                pile[middle + (int)(xlength * 0.5)] = (int)(sand * 0.25);
                pile[middle + ((int)(xlength * 0.5) - 1)] = (int)(sand * 0.25);
                pile[middle - (int)(xlength * 0.5)] = (int)(sand * 0.25);
                pile[middle - ((int)(xlength * 0.5) + 1)] = (int)(sand * 0.25);
            }else{
                pile[middle] = (int)(sand * 0.5);
                pile[middle - 1] = (int)(sand * 0.5);
            }
        }else{
            if(ylength % 2 == 0){
                int pos1 = ((int)(ylength * 0.5) * xlength) + (int)((xlength-1) * 0.5);
                int pos2 = (((int)(ylength * 0.5) - 1) * xlength) + (int)((xlength-1) * 0.5); 
                pile[pos1] = (int)(sand * 0.5);
                pile[pos2] = (int)(sand * 0.5);
            }else{
                pile[(int)(xlength * ylength * 0.5)] = sand;
            }
        }
    }
    
    private void calculate(){
        setSand();
        ww = new WaitWindow(this);
        ww.start();
        iterate();
    }
    
    private void loadInstant(int expectedIterations){
        dr = new Drawing(xlength, ylength, sand, size, expectedIterations, forcedcalc, origin);
        dr.makeGraphics();
        dr.loadInstant();
    }
    
    private void makeFromUncompleted(String filename){
        ww = new WaitWindow(this);
        ww.start();
        pile = dir.getUncompletedArray(filename);
        dir = null;
        iterations = Integer.valueOf(filename.split("-")[4].split("\\.")[0]);
        iterate();
    }
    
    private void makeFromSmaller(int expectedIterations, String filename){
        dr = new Drawing(xlength, ylength, sand, size, expectedIterations, forcedcalc, origin);
        int[][] helparr = dr.readArray(filename);
        //System.out.println(filename);
        dr = null;
        
        mergeGrids(helparr);
        sand -= Integer.valueOf(filename.split("-")[2]);
        setSand();
        sand += Integer.valueOf(filename.split("-")[2]);
        iterations = Integer.valueOf(filename.split("-")[4].split("\\.")[0])-1;
        
        ww = new WaitWindow(this);
        ww.start();
        iterate();
    }
    
    private void mergeGrids(int[][] toMerge){
        int[][] help = new int[xlength][ylength];
        
        for(int i = 0; i < pile.length; i++){
            int x = i % xlength;
            int y = (int)(i / xlength);
            help[x][y] = pile[i];
        }
        
        int xoff = (int)((help.length - toMerge.length) * 0.5);
        int yoff = (int)((help[0].length - toMerge[0].length) * 0.5);
        
        for(int x = 0; x<toMerge.length; x++){
            for(int y = 0; y<toMerge[x].length; y++){
                help[x+xoff][y+yoff] = toMerge[x][y];
            }
        }
        
        for(int a = 0; a < pile.length; a++){
            int x = a % xlength;
            int y = (int)(a / xlength);
            pile[a] = help[x][y];
        }
    }
    
    private void iterate(){
        boolean dummy;
        while(distribute()){
            dummy = distribute();
            dummy = distribute();
            dummy = distribute();
            dummy = distribute();
            dummy = distribute();
            dummy = distribute();
            if(warte){
                try{this.sleep(300);}
                catch(InterruptedException e){}
                warte = false;
            }
        }
        
        ww.updating = false;
        ww.closeWaitWindow();
        ww.makeDrawWindow();
        
        dr = new Drawing(xlength, ylength, sand, size, iterations, forcedcalc, origin);
        dr.makeGraphics();
        update();
        if(sand > 1000000){
            dr.saveImage();
        }
        
        ww.closeDrawWindow();
    }
    
    private boolean distribute(){
        boolean re = false;
        for(int x = 0; x<pile.length; x++){
            if(pile[x] >= 4){
                split(x);
                re=true;
            }
        }
        if(re){
            iterations++;
        }
        return re;
    }
    
    private void split(int x){
        int val = pile[x];
        pile[x] = val % 4;
        int i = (int)(val * 0.25);
        try{
            if(x % xlength != 0){
                pile[x-1] += i;
            }
            if(x % xlength != xlength-1){
                pile[x+1] += i;
            }
            if(x >= xlength){
                pile[x-xlength] += i;
            }
            if(x < xlength*(ylength-1)){
                pile[x+xlength] += i;
            }
        }catch(ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: x = "+ String.valueOf(x) +"; i = "+ String.valueOf(i));
        }catch(IllegalArgumentException e){
            System.out.println("COMP ERROR: x = "+String.valueOf(x)+" i = "+String.valueOf(i));
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    
    private void update(){
        if(size > 1){
            for(int x = 0; x<pile.length; x++){
                dr.update(x, pile[x]);
            }
        }else{
            for(int x = 0; x<pile.length; x++){
                dr.update2(x, pile[x]);
            }
        }
        dr.repaint();
    }
    
    public void warte(){
        warte = true;
    }
}





















