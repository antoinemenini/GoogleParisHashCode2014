/**
 * Created by guillaume on 4/5/14.
 */
public class Edge {
    public final int begin, end;
    public final int cost, length;
    public final int index;
    public final boolean doubleWay;

    public Edge(int b, int e, int c, int l, int i, boolean d){
        begin = b;
        end = e;
        cost = c;
        length = l;
        index = i;
        doubleWay = d;
    }


}
