import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;


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
				float bestRatioNonParcouru = 0;
				Edge bestRouteNonParcourue = null;
				Edge routeChoisie = null;
				LinkedList<Edge> possibleRoutes = new LinkedList<Edge>();
				for (Edge route : routes){
					float ratio = route.length / route.cost + 1;
					if(ratio > bestRatioNonParcouru && !parcouru[route.index] && route.cost <= timeLeft){
						bestRouteNonParcourue = route;
						bestRatioNonParcouru = ratio;
					}
					if(route.cost <= timeLeft){
						possibleRoutes.add(route);
					}
				}
				if(bestRouteNonParcourue == null && possibleRoutes.isEmpty()){
					System.out.println("breakdown "+ timeLeft + " camion "+vehicule);
					break;
				}
				else if(bestRouteNonParcourue == null){
					Random generator = new Random();
					routeChoisie = possibleRoutes.get(generator.nextInt(possibleRoutes.size()));
				}
				else{
					routeChoisie = bestRouteNonParcourue;
				}
				score += routeChoisie.length;
				timeLeft -= routeChoisie.cost;
				parcouru[routeChoisie.index] = true;
				currentIntersection = routeChoisie.end;
				res.addStep(vehicule, currentIntersection);
				System.out.println("camion "+vehicule+" intersection "+currentIntersection);
			}
			
			
		}
		res.print("outMenini.txt");
	}
}
