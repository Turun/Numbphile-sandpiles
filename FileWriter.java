import java.io.PrintWriter;
import java.io.File;
import java.lang.StringBuilder;

public class FileWriter
{
    Mechanics mech;
    DirSearch dir;
    
    private final int x;
    private final int y;
    private final int sand;
    private final int size;
    
    public FileWriter(Mechanics mech){
        this.mech = mech;
        this.x = mech.xlength;
        this.y = mech.ylength;
        this.size = mech.size;
        this.sand = mech.sand;
        dir = new DirSearch();
    }
    
    public void saveToFile(){
        String formerFilename = dir.getUncompletedName(x,y,sand,size);
        
        String filename = "Incomplete/";
        filename += String.valueOf(x) + "-";
        filename += String.valueOf(y) + "-";
        filename += String.valueOf(sand) + "-";
        filename += String.valueOf(size) + "-";
        filename += String.valueOf(mech.iterations);
        filename += ".txt";
        int[] p = mech.pile;
        
        try(PrintWriter pw = new PrintWriter(filename)){
            for(int a = 0; a<y; a++){
                StringBuilder sb = new StringBuilder(x);
                sb.append(String.valueOf(p[a*x]));
                for(int b = 1; b<x; b++){
                    sb.append("-");
                    sb.append(String.valueOf(p[a*x+b]));
                }
                pw.println(sb.toString());
                sb = null;
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        if(!formerFilename.equals("foundNothing")){
            File f = new File(formerFilename);
            f.delete();
            f = null;
            System.gc();
        }
    }
    
    public void deleteFile(){
        String filename = dir.getUncompletedName(x,y,sand,size);
        try{
            File f = new File(filename);
            f.delete();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
