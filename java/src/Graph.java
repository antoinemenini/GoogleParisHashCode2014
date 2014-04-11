import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by guillaume on 4/5/14.
 */
public class Graph {
    final int N, M, T, C, S;
    final Point[] intersection;
    private final HashMap<Integer, LinkedList<Edge> > streetsMap = new HashMap<Integer, LinkedList<Edge>>();
    private final boolean[] visited;
    int score=0;
    final int[] degree;

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
        visited = new boolean[M];
        degree = new int[N];
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
            edge = new Edge(b, e, c, l, 2*i, orientation==2);
            if(streetsMap.containsKey(b)){
                    streetsMap.get(b).add(edge);
                } else {
                    streets = new LinkedList<Edge>();
                    streets.add(edge);
                    streetsMap.put(b, streets);
                }
            if(orientation == 2) {
                edge2 = new Edge(e, b, c, l, 2*i+1, orientation==2);
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

    public void visitEdge(Edge e){
        if(!visited[e.index/2]){
            score+=e.length;
            visited[e.index/2]=true;
            --degree[e.begin];
            if(e.doubleWay)
                --degree[e.end];
        }
    }

    public boolean isVisited(Edge e){
        return visited[e.index/2];
    }

    public int getScore(){
        return score;
    }

    public LinkedList<Edge> getRoutesFrom(int begin){
        return streetsMap.get(begin);
    }

    public LinkedList<Edge> getRoutesFrom(int begin, int maxCost){
        LinkedList<Edge> routes = new LinkedList<Edge>();
        for(Edge e : streetsMap.get(begin)){
            if(e.cost<=maxCost)
                routes.add(e);
        }
        return routes;
    }

    public LinkedList<Edge> getNonVisitedRoutesFrom(int begin){
        LinkedList<Edge> routes = new LinkedList<Edge>();
        for(Edge e : streetsMap.get(begin)){
            if(!isVisited(e))
                routes.add(e);
        }
        return routes;
    }

    public LinkedList<Edge> getNonVisitedRoutesFrom(int begin, int maxCost){
        LinkedList<Edge> routes = new LinkedList<Edge>();
        for(Edge e : streetsMap.get(begin)){
            if(!isVisited(e) && e.cost <= maxCost)
                routes.add(e);
        }
        return routes;
    }

    public void initializeDegrees(){
        for(int n=0; n<N; n++){
            degree[n] = getRoutesFrom(n).size();
        }
    }

    public void naiveTrip(){
        int[] time = new int[C];
        int[] position = new int[C];
        for(int c=0; c<C; c++){
            time[c] = T;
            position[c] = S;
        }
        Result res = new Result(C, S);
        boolean[] blocked = new boolean[C];
        boolean terminated=false;
        Edge route;
        LinkedList<Edge> routes;
        Random random = new Random();
        while(!terminated){
            for(int c=0; c<C; c++){
                if(blocked[c])
                    continue;
                routes = getNonVisitedRoutesFrom(position[c], time[c]);
                if(routes.size()==0){
                    routes = getRoutesFrom(position[c], time[c]);
                    if(routes.size()==0){
                        System.out.println("Car "+c+ " blocked with "+time[c]+" meters left");
                        blocked[c]=true;
                        continue;
                    } else {
                        route = routes.get(random.nextInt(routes.size()));
                        int d = degree[route.end];
                        for(Edge e : routes){
                            if(degree[e.end]>d || (degree[e.end]==d && Math.random()>0.5)){
                                route = e;
                            }
                        }
                    }
                } else {
                    route = routes.get(random.nextInt(routes.size()));
                    float ratio = route.length/route.cost;
                    for(Edge e : routes){
                        if(e.length/e.cost>ratio || (e.length/e.cost==ratio && Math.random()>0.5)){
                            route = e;
                        }
                    }
                }
                time[c] -= route.cost;
                position[c] = route.end;
                visitEdge(route);
                res.addStep(c, position[c]);
//                 System.out.println("Camion "+c+ " visite "+route.end);
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

    public Point forceFromTrucks(int[] trucksPos, int truck){
        Point force = new Point();
        Point a = intersection[truck];
        Point b, ba;
        for(int c=0; c<trucksPos.length; c++){
            if(c!= truck){
                b = intersection[trucksPos[c]];
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
        boolean[] pointsVisited = new boolean[N];

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
                force = forceFromVisited(pointsVisited, position[c]);
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

                            if(!pointsVisited[route.end]){
                                f*=20;
                            }
                            if(!isVisited(route)){
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


                    if(!pointsVisited[bestRoute.end]){
                        System.out.println("Camion "+c+ " visite encore "+position[c]+" | "+score);
                        if( (c==0) && bestRoute.end==10625){
                            System.out.println("---- ???? ------");
                        }
                    }

                    time[c] -= bestRoute.cost;
                    oldPosition[c]=position[c];
                    position[c] = bestRoute.end;
                    pointsVisited[bestRoute.end]=true;
                    visitEdge(bestRoute);
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

    int findPath(int a0, int a1, int timeLeft, Result res, int c){

        HashMap<Integer, Integer> timeFromO = new HashMap<Integer, Integer>(N);
        HashMap<Integer, Edge> comeFrom = new HashMap<Integer, Edge>(N);
        LinkedList<Integer> toTreat=new LinkedList<Integer>();
        int t=-1;
        toTreat.add(a0);
        timeFromO.put(a0, 0);
//        comeFrom.put(a0, a0);
        int a, d0, d1;
        while (!toTreat.isEmpty()){
            a = toTreat.pop();
            if(a==a1){
                break;
            }
            if(timeFromO.get(a)<timeLeft){
                for(Edge e : streetsMap.get(a)){
                    if(!timeFromO.containsKey(e.end)){
                        toTreat.add(e.end);
                        timeFromO.put(e.end, timeFromO.get(a)+e.cost);
                        comeFrom.put(e.end, e);
                    } else {
                        d0 = timeFromO.get(e.end);
                        d1 = timeFromO.get(a)+e.cost;
                        if(d1<d0){
                            timeFromO.put(e.end, d1);
                            comeFrom.put(e.end, e);
                        }
                    }
                }
            }

        }
        if(timeFromO.get(a1) > timeLeft){
            System.err.println("Path not found in time : "+timeLeft+", need "+timeFromO.get(a1));
        } else {
            LinkedList<Integer> path = new LinkedList<Integer>();
            path.addLast(a1);
            Edge e = comeFrom.get(a1);
            a = e.begin;
            visitEdge(e);
            int l=0;
            t = timeLeft-timeFromO.get(a1);
            while(a!=a0){
                path.addFirst(a);
                l++;
                if(comeFrom.containsKey(a)){
                    e = comeFrom.get(a);
                    a = e.begin;
                    visitEdge(e);
                } else {
                    System.err.println("Point not found : "+a);
                    break;
                }
            }
            System.out.println("Found a path between "+a0+" and "+a1+". Used "+timeFromO.get(a1)+" out of "+T);
            for(int step : path){
                res.addStep(c, step);
            }
        }
        return t;
    }

    public static void main(String[] args){
        Graph paris = new Graph("paris_54000.txt");
//        boolean[] visitedEdges = new boolean[2*paris.M];
//        Result res = new Result(paris.C, paris.S);
//        paris.findPath(paris.S, 1000, paris.T, visitedEdges, res, 0);
//        res.print("path.txt");
//        int[] trucks = new int[]{0, 1, 3};
//        int t = 0;
//        Point force = paris.forceFromTrucks(trucks, t);
//        System.out.println(paris.intersection[trucks[0]]);
//        System.out.println(paris.intersection[trucks[1]]);
//        System.out.println(paris.intersection[trucks[2]]);
//        System.out.println("Force : "+force);
        System.out.println("Using method naiveTrip from class Graph");
        paris.naiveTrip();
        System.out.println("score = "+paris.score);

    }


}
