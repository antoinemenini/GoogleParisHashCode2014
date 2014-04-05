import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
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
            edge = new Edge(b, e, c, l, 2*i);
            if(streetsMap.containsKey(b)){
                    streetsMap.get(b).add(edge);
                } else {
                    streets = new LinkedList<Edge>();
                    streets.add(edge);
                    streetsMap.put(b, streets);
                }
            if(orientation == 2) {
                edge2 = new Edge(e, b, c, l, 2*i+1);
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

    public void naiveTrip(){
        int[] time = new int[C];
        int[] position = new int[C];
        int score=0;
        for(int c=0; c<C; c++){
            time[c] = T;
            position[c] = S;
        }
        Result res = new Result(C, S);
        boolean[] visited = new boolean[2*M];
        boolean[] blocked = new boolean[C];
        boolean terminated=false;
        while(!terminated){
            for(int c=0; c<C; c++){
                LinkedList<Edge> routes = streetsMap.get(position[c]);
                float bestRatio = 0;
                float secondBestRatio = 0;
                Edge bestRoute = null;
                Edge secondBestRoute = null;
                for (Edge route : routes){
                    float ratio = route.length / route.cost +1 ;
                    if(ratio > bestRatio && !visited[route.index] && route.cost <= time[c]){
                        bestRoute = route;
                        bestRatio = ratio;
                    }
                    if(ratio > secondBestRatio && route.cost <= time[c]){
                        secondBestRoute = route;
                        secondBestRatio = ratio;
                    }
                }
                if(bestRoute == null){

                    bestRoute = secondBestRoute;
                    if(secondBestRoute!=null){
                        System.out.println("Camion "+c+ " visite encore "+secondBestRoute.end);
                    }
                }
                if(bestRoute == null){
                    if(!blocked[c]){
                        System.out.println("Camion "+c+ " bloqué");
                        blocked[c]=true;
                    }
                } else {
                     score += bestRoute.length;
                     time[c] -= bestRoute.cost;
                     position[c] = bestRoute.end;
                     visited[bestRoute.index]=true;
                     res.addStep(c, position[c]);
                     System.out.println("Camion "+c+ " visite "+bestRoute.end);
                }
            }
            terminated = true;
            for(int c=0; c<C; c++){
                if(!blocked[c])
                    terminated=false;
            }
        }
        res.print("outGW.txt");
    }

    public Point forceFromVisited(boolean[] visitedNode, int node){
        Point force = new Point();
        Point a = intersection[node];
        Point b, ba;
        for(Edge e : streetsMap.get(node)){
            if(visitedNode[e.end]){
                b = intersection[e.end];
                ba = Point.vect(b , a);
                ba.mul(1./a.squaredDist(b));
                force.add(ba);
            }
        }
        return force;
    }

    public Point forceFromTrucks(int[] trucks, int truck){
        Point force = new Point();
        Point a = intersection[truck];
        Point b, ba;
        for(int c=0; c<C; c++){
            if(c!= truck){
                b = intersection[trucks[c]];
                ba = Point.vect(b , a);
                ba.mul(1./a.squaredDist(b));
                force.add(ba);
            }

        }
        return force;
    }

    public Point forceFromBOnA(int a, int b){
        Point force = Point.vect(intersection[b] , intersection[a]);
        force.mul(1. / intersection[a].squaredDist(intersection[b]));
        return force;
    }

    public double forceOnEdge(Point force, Edge e){
        Point ab = Point.vect(intersection[e.begin], intersection[e.end]);
        ab = Point.vect(intersection[e.begin], intersection[e.end]);
        return ab.dotProduct(force);
    }

    public void EuristicTrip(){
        int[] time = new int[C];
        int[] position = new int[C];
        int[] oldPosition = new int[C];
        boolean[] blocked = new boolean[C];
        int score=0;
        for(int c=0; c<C; c++){
            time[c] = T;
            position[c] = S;
            oldPosition[c] = S;
        }
        Result res = new Result(C, S);
        boolean[] visited = new boolean[N];
        boolean[] edgeVisited = new boolean[2*M];

        boolean terminated=false;
        double biggestF, f;
        Edge bestRoute;
        Point force;
        float bestRatio = 0;
        float ratio;

        while(!terminated){
            for(int c=0; c<C; c++){
                biggestF = -Math.PI*500;
                bestRoute=null;
                force = forceFromVisited(visited, position[c]);
                force.add(forceFromTrucks(position, c));
                if(position[c] != S)
                    force.add(forceFromBOnA(position[c], S));
                if(streetsMap.get(position[c]).size() == 1){
                    Edge route =  streetsMap.get(position[c]).getFirst();
                    if(route.cost <= time[c] ){
                        bestRoute = route;
                    }

                } else {
                    for(Edge route : streetsMap.get(position[c])){
                        if(route.end != oldPosition[c]){
                            f = forceOnEdge(force, route);

                            if(!visited[route.end]){
                                f*=20;
                            }
                            if(!edgeVisited[route.index]){
                                ratio = route.length / route.cost +1 ;
                                f+= ratio;
                            }
                            if(biggestF== -Math.PI*500 || (f>biggestF && route.cost <= time[c]) ){
                                bestRoute = route;
                                biggestF = f;
                            }
                        }
                    }
                }
                if(bestRoute == null){
                    if(!blocked[c]){
                        System.out.println("Camion "+c+ " bloqué");
                        blocked[c]=true;
                    }
                } else {

                    if(!edgeVisited[bestRoute.index]){
                        score += bestRoute.length;
                    }
                    if(!visited[bestRoute.end]){
                        System.out.println("Camion "+c+ " visite encore "+position[c]+" | "+score);
                        if( (c==0) && bestRoute.end==10625){
                            System.out.println("---- ???? ------");
                        }
                    }

                    time[c] -= bestRoute.cost;
                    oldPosition[c]=position[c];
                    position[c] = bestRoute.end;
                    visited[bestRoute.end]=true;
                    edgeVisited[bestRoute.index]=true;
                    res.addStep(c, position[c]);
//                    System.out.println("Camion "+c+ " visite "+position[c]);
                }

            }

            terminated = true;
            for(int c=0; c<C; c++){
                if(!blocked[c])
                    terminated=false;
            }
//            System.out.println("Camion "+1+ " visite "+position[1]+", time = "+ time[1]);
        }
        System.out.println("Score = "+score);
        res.print("outGWEuristic.txt");

    }

    int findPath(int a0, int a1, int timeLeft, boolean[] visitedEdge, Result res, int c){

        HashMap<Integer, Integer> distance = new HashMap<Integer, Integer>(N);
        HashMap<Integer, Integer> comeFrom = new HashMap<Integer, Integer>(N);
        LinkedList<Integer> toTreat=new LinkedList<Integer>();

        toTreat.add(a0);
        distance.put(a0, 0);
        int a, d0, d1;
        while (!toTreat.isEmpty()){
            a = toTreat.pop();
            if(a==a1){
                break;
            }
            if(distance.get(a)<timeLeft){
                for(Edge e : streetsMap.get(a)){
                    if(!distance.containsKey(e.end)){
                        toTreat.add(e.end);
                        distance.put(e.end, distance.get(a)+e.cost);
                        comeFrom.put(e.end, a);
                    } else {
                        d0 = distance.get(e.end);
                        d1 = distance.get(a)+e.cost;
                        if(d1<d0){
                            distance.put(e.end, d1);
                            comeFrom.put(e.end, a);
                        }
                    }
                }
            }

        }
        if(distance.get(a1) > timeLeft){
            System.err.println("Path not found in time : "+timeLeft+", need "+distance.get(a1));
        }
        LinkedList<Integer> path = new LinkedList<Integer>();
        path.addLast(a1);
        a = comeFrom.get(a1);
        while(a!=a1){
            path.addFirst(a);
            a = comeFrom.get(a);
        }
        for(int step : path){
            res.addStep(c, step);
        }
        return timeLeft;
    }


}
