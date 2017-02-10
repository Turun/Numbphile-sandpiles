import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class DirSearch{
    File picDir;
    File[] picList;
    File txtDir;
    File[] txtList;
    ArrayList<int[]> pic = new ArrayList<int[]>();
    
    
    public DirSearch(){
        picDir = new File("pics");
        picList = picDir.listFiles();
        for(int i = 0; i<picList.length; i++){
            fillList(picList[i]);
        }
        txtDir = new File("Incomplete");
    }
    
    private void fillList(File f){
        String name = f.getName();
        name = name.split("\\.")[0];
        String[] data = name.split("-");
        int[] help = new int[5];
        for(int i = 0; i<data.length; i++){
            try{
                help[i] = Integer.valueOf(data[i]);
            }catch(Exception e){}
        }
        pic.add(help);
    }
    
    public int checkPic(int x, int y, int sand, int size){ //returns iterations
        int[] comp = {x, y, sand, size};
        int re = -1;
        int correct = 0;
        
        for(int a = 0; a<pic.size(); a++){
            for(int i = 0; i<4; i++){
                if(comp[i] == pic.get(a)[i]){
                    correct++;
                }else{
                    i = 5;
                }
            }
            
            if(correct == 4){
                re = pic.get(a)[4];
            }
            correct = 0;
        }
        return re;
    }
    
    public int[] getUncompletedArray(String filename){
        int x = Integer.valueOf(filename.split("-")[0].split("/")[1]);
        int y = Integer.valueOf(filename.split("-")[1]);
        int[] re = new int[x*y];
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            for(int i = 0; i<y; i++){
                String[] input = br.readLine().split("-");
                for(int a = 0; a<x; a++){
                    re[i*x+a] = Integer.valueOf(input[a]);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return re;
    }
    
    public String getUncompletedName(int x, int y, int sand, int size){
        String filename = String.valueOf(x);
        filename += "-" + String.valueOf(y);
        filename += "-" + String.valueOf(sand);
        filename += "-" + String.valueOf(size);
        
        txtList = txtDir.listFiles();
        for(File f : txtList){
            if(f.getName().startsWith(filename)){
                filename = f.getName();
                f = null;
                System.gc();
                return "Incomplete/"+filename;
            }
        }
        return "foundNothing";
    }
    
    public String getNextSmallerPic(int x, int y, int sand, int size){
        int[] record = {0,0,0,0,0};
        boolean evenx = (x%2 == 0);
        boolean eveny = (y%2 == 0);
        float ratio = x/y;
        
        for(int i = 0; i < pic.size(); i++){
            int[] smallerPic = getSmallerPic(x, y, sand, size, i);
            if(smallerPic != null && smallerPic[2] * 2 <= sand){        //if pic has less than half as much sand then we want to put in on the new grid
                if(evenx && smallerPic[0]%2 == 0){                  //and if the pic has the same evenness as our new grid
                    if(eveny && smallerPic[1]%2 == 0){
                        if(smallerPic[2] > record[2]){              //and more sand than previous records we take it!
                            record = smallerPic;
                        }
                    }else if(!eveny && smallerPic[1]%2 == 1){
                        if(smallerPic[2] > record[2]){
                            record = smallerPic;
                        }
                    }
                }else if(!evenx && smallerPic[0]%2 == 1){
                    if(eveny && smallerPic[1]%2 == 0){
                        if(smallerPic[2] > record[2]){
                            record = smallerPic;
                        }
                    }else if(!eveny && smallerPic[1]%2 == 1){
                        if(smallerPic[2] > record[2]){
                            record = smallerPic;
                        }
                    }
                }
            }
        }
        
        String re = "pics/";
        re += String.valueOf(record[0]) + "-";
        re += String.valueOf(record[1]) + "-";
        re += String.valueOf(record[2]) + "-";
        re += String.valueOf(record[3]) + "-";
        re += String.valueOf(record[4]) + ".png";
        return re;
    }
    
    public int[] getSmallerPic(int x, int y, int sand, int size, int nr){
        int correct = 0;
        int[] comp = {x, y, sand, size};
        
        for(int i = 0; i < 3; i++){
            if(pic.get(nr)[i] <= comp[i]){
                correct++;
            }else{
                i = 5;
            }
        }
        
        if(correct == 3){
            return pic.get(nr);
        }else{
            return null;
        }
    }
}



