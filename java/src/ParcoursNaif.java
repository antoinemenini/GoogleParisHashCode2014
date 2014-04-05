import java.util.Arrays;
import java.util.LinkedList;


public class ParcoursNaif {
	public static Graph paris = new Graph("paris_54000.txt");
	
	
	public static void parcours(){
		boolean[] parcouru = new boolean[2*paris.M];
		Result res = new Result(paris.C, paris.S);
		Arrays.fill(parcouru, false);
		for(int vehicule=0; vehicule<paris.C; vehicule++){
			int currentIntersection = paris.S;
			int score = 0;
			int timeLeft = paris.T;
			while(true){
				/* on parcourt la liste des rues pour cette intersection et on prend celle
				avec le plus grand score qu n'a pas ŽtŽ visitŽe */
				LinkedList<Edge> routes = paris.streetsMap.get(currentIntersection);
				float bestRatio = 0;
				Edge bestRoute = null;
				Edge secondBestRoute = null;
				for (Edge route : routes){
					float ratio = route.length / route.cost + 1;
					if(ratio > bestRatio && !parcouru[route.index] && route.cost <= timeLeft){
						bestRoute = route;
					}
					if(ratio > bestRatio && route.cost <= timeLeft){
						secondBestRoute = route;
					}
				}
				if(bestRoute == null){
					if(secondBestRoute == null) break;
					else bestRoute = secondBestRoute;
				}
				else{
					score += bestRoute.length;
					timeLeft -= bestRoute.cost;
					currentIntersection = bestRoute.end;
					res.addStep(vehicule, currentIntersection);
				}
			}
			
			
		}
		res.print("outMenini.txt");
	}
}
