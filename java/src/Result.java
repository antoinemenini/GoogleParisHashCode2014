import java.io.*;
import java.util.LinkedList;

/**
 * Created by guillaume on 4/5/14.
 */
public class Result {
    final int C;
    final LinkedList<Integer>[] trips;
    final int[] step;

    public Result(int C, int S){
        this.C = C;
        trips = new LinkedList[C];
        step = new int[C];
        for(int i=0; i<C; i++){
            trips[i] = new LinkedList<Integer>();
            trips[i].addLast(S);
            step[i] = 1;
        }
    }

    public void addStep(int c, int i){
        trips[c].add(i);
        ++step[c];
    }

    public void print(String filename){
        try {
            this.print(new FileWriter(filename));
        } catch (IOException e) {
            System.out.println("Unable to write in "+filename);
        }
    }



    public void print(FileWriter out){
        try {
            out.write(""+C+"\n");
            for(int c=0; c<C; c++){
                out.write(""+step[c]+"\n");
                for(int e : trips[c]){
                    out.write(""+e+"\n");
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        Result r = new Result(2,0);
        r.addStep(1, 1);
        r.addStep(1, 2);
        r.print("output.txt");
    }
}
