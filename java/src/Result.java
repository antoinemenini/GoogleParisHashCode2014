import java.io.OutputStream;
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
            trips[i].add(S);
            step[i] = 1;
        }
    }

    public void addStep(int c, int i){
        trips[c].add(i);
        ++step[c];
    }

    public void print(OutputStream out){

    }
}
