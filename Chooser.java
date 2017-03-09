//import java.util.ArrayList;
public class Chooser
{
    Drawing dr;
    WaitWindow ww;
    DirSearch dir;
    Mechanics mech;
    
    int xlength;
    int ylength;
    int sand;
    int size;
    int[] clrs;
    int iterations;
    boolean forcedcalc;
    String origin;
    
    boolean save;
    boolean close;
    
    public Chooser(int[] clrs, int x, int y, int sand, int size, boolean forcedcalc, String origin){
        //System.out.println("Cho (initialising): "+Thread.currentThread().getName());
        
        this.xlength = x;
        this.ylength = y;
        this.sand = sand;
        this.size = size;
        this.clrs = clrs;
        this.iterations = 0;
        this.origin = origin;
        this.forcedcalc = forcedcalc;
        this.save = false;
        this.close = false;
        
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
        
        //System.out.print("choose method: ");
        if(expectedIterations >= 0){
            //System.out.println("0 - load instant");
            loadInstant(expectedIterations);
        }else{
            if(filenameIncomplete.equals("foundNothing")){
                if(forcedcalc){
                    //System.out.println("1 - calculate");
                    calculate();
                }else{
                    if(filenameSmaller.equals("0-0-0-0-0.bin")){
                        //System.out.println("2 - calculate");
                        calculate();
                    }else{
                        //System.out.println("3 - make from smaller");
                        makeFromSmaller(expectedIterations, filenameSmaller);
                    }
                }
            }else{
                //System.out.println("4 - make from uncomplete");
                makeFromUncompleted(filenameIncomplete);
            }
        }
    }
    
    private void calculate(){
        mech = new Mechanics(clrs, xlength, ylength, sand, size, forcedcalc, origin);
        ww = new WaitWindow(mech);
        mech.setMode();
        mech.setWW(ww);
        mech.start();
        ww.start();
    }
    
    private void loadInstant(int expectedIterations){
        dr = new Drawing(clrs, xlength, ylength, sand, size, expectedIterations, 0, forcedcalc, origin);
        dr.makeGraphics();
        dr.loadInstant();
    }
    
    private void makeFromUncompleted(String filename){
        mech = new Mechanics(clrs, xlength, ylength, sand, size, forcedcalc, origin);
        ww = new WaitWindow(mech);
        mech.setMode(filename);
        mech.setWW(ww);
        mech.start();
        ww.start();
    }
    
    private void makeFromSmaller(int expectedIterations, String filename){
        mech = new Mechanics(clrs, xlength, ylength, sand, size, forcedcalc, origin);
        ww = new WaitWindow(mech);
        mech.setMode(String.valueOf(expectedIterations), filename);
        mech.setWW(ww);
        mech.start();
        ww.start();
    }
}





















