//import java.util.ArrayList;
public class Mechanics extends Thread
{
    Drawing dr;
    DirSearch dir;
    FileWriter fw;
    WaitWindow ww;
    
    int pile[];
    int xlength;
    int ylength;
    int sand;
    int size;
    int[] clrs;
    int iterations;
    boolean forcedcalc;
    String origin;
    
    int operationmode;
    String filename;
    int expectedIterations;
    
    boolean save;
    boolean close;
    
    public Mechanics(int[] clrs, int x, int y, int sand, int size, boolean forcedcalc, String origin){
        //System.out.println("Mechanics (initialising): "+Thread.currentThread().getName());
        
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
        
        pile = new int[xlength*ylength];
        
    }
    
    public void run(){
        //System.out.println();
        //System.out.println("Mechanics (running): "+Thread.currentThread().getName());
        //System.out.println(operationmode);
        //System.out.println(filename);
        //System.out.println(expectedIterations);
        //System.out.println();
        
        
        switch(operationmode){
            case(0):{
                //System.out.println("Mechanics - run - switch - 0");
                setSand();
                break;
            }
            case(1):{
                //System.out.println("Mechanics - run - switch - 1");
                makeFromUncompleted();
                break;
            }
            case(2):{
                //System.out.println("Mechanics - run - switch - 2");
                makeFromSmaller();
                break;
            }
            default:{
                System.out.println("unexpected state: " + String.valueOf(operationmode));
                break;
            }
        }
        iterate();
    }
    
    public void setMode(String... mode){
        operationmode = mode.length;
        if(operationmode == 0){ // calculate everyhting
        }else if(operationmode == 1){ // makeFromUncompleted (filename)
            filename = mode[0];
        } else if(operationmode == 2){ //makeFromSmaller (expexted iterations, filename)
            expectedIterations = Integer.valueOf(mode[0]);
            filename = mode[1];
        }else{
            System.out.println("ooooops");
        }
    }
    
    public void setWW(WaitWindow ww){
        this.ww = ww;
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
    
    private void makeFromUncompleted(){
        
        dir = new DirSearch();
        pile = dir.getUncompletedArray(filename);
        dir = null;
        iterations = Integer.valueOf(filename.split("-")[4].split("\\.")[0]);
    }
    
    private void makeFromSmaller(){
        
        dr = new Drawing(clrs, xlength, ylength, sand, size, expectedIterations, 0, forcedcalc, origin);
        int[][] helparr = dr.readArray(filename);
        //System.out.println(filename);
        dr = null;
        
        mergeGrids(helparr);
        sand -= Integer.valueOf(filename.split("-")[2]);
        setSand();
        sand += Integer.valueOf(filename.split("-")[2]);
        iterations = Integer.valueOf(filename.split("-")[4].split("\\.")[0])-1;
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
        fw = new FileWriter(this);
        while(distribute()){
            if(save){
                //System.out.println("Mechanics (saving): "+Thread.currentThread().getName());
                save = false;
                saveToFile();
            }
            if(close){System.out.println("we are here");break;}
        }
        
        if(close){
            saveToFile();
        }else{
            fw.deleteFile();
            
            
            ww.updating = false;
            ww.closeWaitWindow();
            ww.makeDrawWindow();
            
            
            dr = new Drawing(clrs, xlength, ylength, sand, size, iterations, 0, forcedcalc, origin);
            dr.makeGraphics();
            update();
            if(sand > 1000000){
                dr.saveImage();
            }
            
            ww.closeDrawWindow();
        }
        fw = null;
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
        if(x % xlength != 0)        {pile[x-1]       += i;}
        if(x % xlength != xlength-1){pile[x+1]       += i;}
        if(x >= xlength)            {pile[x-xlength] += i;}
        if(x < xlength*(ylength-1)) {pile[x+xlength] += i;}
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
    
    public void saveToFile(){
        try{this.sleep(10+(int)(xlength*ylength/10000));}
        catch(InterruptedException e){}
        fw.saveToFile();
        try{this.sleep(10+(int)(xlength*ylength/10000));}
        catch(InterruptedException e){}
    }
}





















