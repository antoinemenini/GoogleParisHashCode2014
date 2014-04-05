import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by guillaume on 4/5/14.
 */
public class Graph {
    final int N, M, T, C, S;
    final Point[] intersection;
    final HashMap<Integer, LinkedList<Edge> > streetsMap = new HashMap<Integer, LinkedList<Edge>>();

    public Graph(String filename){
        Scanner in = null;
        try {
            in = new Scanner(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.err.println("Not able to open file : "+filename);
        }
        System.out.println("Loading a graph from "+filename);
        N = in.nextInt();
        M = in.nextInt();
        T = in.nextInt();
        C = in.nextInt();
        S = in.nextInt();
        intersection = new Point[N];
        for(int i=0; i<N; i++){
            intersection[i] = new Point(in.nextDouble(), in.nextDouble());
        }
        int b, e, c, l, orientation;
        Edge edge, edge2;
        LinkedList<Edge> streets;
        for(int i=0; i<M; i++){
            b = in.nextInt();
            e = in.nextInt();
            orientation = in.nextInt();
            c = in.nextInt();
            l = in.nextInt();
            edge = new Edge(b, e, c, l);
            if(streetsMap.containsKey(b)){
                    streetsMap.get(b).add(edge);
                } else {
                    streets = new LinkedList<Edge>();
                    streets.add(edge);
                    streetsMap.put(b, streets);
                }
            if(orientation == 2) {
                edge2 = new Edge(e, b, c, l);
                if(streetsMap.containsKey(e)){
                    streetsMap.get(e).add(edge2);
                } else {
                    streets = new LinkedList<Edge>();
                    streets.add(edge2);
                    streetsMap.put(e, streets);
                }
            }
        }

        if(in.hasNext()){
            System.out.println("Line left out : ");
            System.out.println(in.nextLine());
        }

        System.out.println("Loaded a graph of "+N+" intersections and "+M+" streets ");

    }


}
