import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;


public class ParcoursNaif {
	public static Graph paris = new Graph("paris_54000.txt");
	
	
	public static void parcours1(){
		boolean[] parcouru = new boolean[2*paris.M];
		Result res = new Result(paris.C, paris.S);
		Arrays.fill(parcouru, false);
		for(int vehicule=0; vehicule<paris.C; vehicule++){
			int currentIntersection = paris.S;
			int timeLeft = paris.T;
			while(true){
				/* on parcourt la liste des rues pour cette intersection et on prend celle
				avec le plus grand score qu n'a pas été visitée */
				LinkedList<Edge> routes = paris.streetsMap.get(currentIntersection);
				float bestRatioNonParcouru = 0;
				Edge bestRouteNonParcourue = null;
				Edge routeChoisie = null;
				Edge bestRouteByScore = null;
				LinkedList<Edge> possibleRoutes = new LinkedList<Edge>();
				float bestScore2 = 0;
				for (Edge route : routes){
					float ratio = route.length / route.cost;
					float score2 = 0;
					// calcul du score de la route : c'est le nombre de routes non parcourues après la route
					LinkedList<Edge> routesSuivantes = paris.streetsMap.get(route.end);
					for (Edge routeSuivante : routesSuivantes){
						float score2aux = 0;
						if(!parcouru[routeSuivante.index] && !(routeSuivante.end==currentIntersection)){
							score2aux=(routeSuivante.length / routeSuivante.cost);
							if(score2aux > score2) score2 = score2aux;
						}
					}
					if(score2 > bestScore2 && route.cost <= timeLeft){
						bestRouteByScore = route;
						bestScore2 = score2;
					}
					if(ratio > bestRatioNonParcouru && !parcouru[route.index] && route.cost <= timeLeft){
						bestRouteNonParcourue = route;
						bestRatioNonParcouru = ratio;
					}
					if(route.cost <= timeLeft){
						possibleRoutes.add(route);
					}
				}
				if(bestRouteNonParcourue == null && possibleRoutes.isEmpty()){
					//System.out.println("breakdown "+ timeLeft + " camion "+vehicule);
					break;
				}
				else if(bestRouteNonParcourue == null && bestRouteByScore == null){
					Random generator = new Random();
					routeChoisie = possibleRoutes.get(generator.nextInt(possibleRoutes.size()));
				}
				else if(bestRouteNonParcourue == null){
					System.out.println("bestroutebyscore");
					routeChoisie = bestRouteByScore;
				}
				else{
					routeChoisie = bestRouteNonParcourue;
				}
				timeLeft -= routeChoisie.cost;
				parcouru[routeChoisie.index] = true;
				currentIntersection = routeChoisie.end;
				res.addStep(vehicule, currentIntersection);
				//System.out.println("camion "+vehicule+" intersection "+currentIntersection);
			}
			
			
		}
		res.print("outMenini.txt");
	}

	public static void parcours2(){
		boolean[] parcouru = new boolean[2*paris.M];
		Result res = new Result(paris.C, paris.S);
		Arrays.fill(parcouru, false);
		boolean terminated = false;
		boolean[] cam_term = new boolean[paris.C];
		Arrays.fill(cam_term, false);
		while(!terminated){
			int currentIntersection = paris.S;
			int timeLeft = paris.T;
			for(int vehicule=0; vehicule<paris.C; vehicule++){
				/* on parcourt la liste des rues pour cette intersection et on prend celle
				avec le plus grand score qu n'a pas été visitée */
				LinkedList<Edge> routes = paris.streetsMap.get(currentIntersection);
				float bestRatioNonParcouru = 0;
				Edge bestRouteNonParcourue = null;
				Edge routeChoisie = null;
				Edge bestRouteByScore = null;
				LinkedList<Edge> possibleRoutes = new LinkedList<Edge>();
				float bestScore2 = 0;
				for (Edge route : routes){
					float ratio = route.length / route.cost;
					float score2 = 0;
					// calcul du score de la route : c'est le nombre de routes non parcourues après la route
					LinkedList<Edge> routesSuivantes = paris.streetsMap.get(route.end);
					for (Edge routeSuivante : routesSuivantes){
						float score2aux = 0;
						if(!parcouru[routeSuivante.index] && !(routeSuivante.end==currentIntersection)){
							score2aux=(routeSuivante.length / routeSuivante.cost);
							if(score2aux > score2) score2 = score2aux;
						}
					}
					if(score2 > bestScore2 && route.cost <= timeLeft){
						bestRouteByScore = route;
						bestScore2 = score2;
					}
					if(ratio > bestRatioNonParcouru && !parcouru[route.index] && route.cost <= timeLeft){
						bestRouteNonParcourue = route;
						bestRatioNonParcouru = ratio;
					}
					if(route.cost <= timeLeft){
						possibleRoutes.add(route);
					}
				}
				if(bestRouteNonParcourue == null && possibleRoutes.isEmpty()){
					//System.out.println("breakdown "+ timeLeft + " camion "+vehicule);
					cam_term[vehicule] = true;
					break;
				}
				else if(bestRouteNonParcourue == null && bestRouteByScore == null){
					Random generator = new Random();
					routeChoisie = possibleRoutes.get(generator.nextInt(possibleRoutes.size()));
				}
				else if(bestRouteNonParcourue == null){
					System.out.println("bestroutebyscore");
					routeChoisie = bestRouteByScore;
				}
				else{
					routeChoisie = bestRouteNonParcourue;
				}
				timeLeft -= routeChoisie.cost;
				parcouru[routeChoisie.index] = true;
				currentIntersection = routeChoisie.end;
				res.addStep(vehicule, currentIntersection);
				//System.out.println("camion "+vehicule+" intersection "+currentIntersection);
			}
			
			// on teste si tous les camions sont bloqués
			for(int h=0; h<paris.C; h++){
				terminated = terminated || cam_term[h];
			}
		}
		res.print("outMenini.txt");
	}
	
	public static void parcours(){
		boolean[] parcouru = new boolean[2*paris.M];
		int[] pointInit = {7683,  6069,  7510,  3302,  7545,  7442,  5420,  5420};
		Result res = new Result(paris.C, paris.S);
		Arrays.fill(parcouru, false);
		boolean terminated = false;
		boolean[] cam_term = new boolean[paris.C];
		Arrays.fill(cam_term, false);
		boolean[] initDone = new boolean[paris.C];
		Arrays.fill(initDone, false);
		int[] currentIntersection = new int[paris.C];
		Arrays.fill(currentIntersection, paris.S);
		while(!terminated){
			
			int score = 0;
			int[] timeLeft = new int[paris.C];
			Arrays.fill(timeLeft, paris.T);
			for(int vehicule=0; vehicule<paris.C; vehicule++){
				if(!initDone[vehicule]){
					// on ammène le véhicule au point pointInit[vehicule]
					timeLeft[vehicule] = paris.findPath(currentIntersection[vehicule], pointInit[vehicule], timeLeft[vehicule], parcouru, res, vehicule);
					initDone[vehicule] = true;
				}
				/* on parcourt la liste des rues pour cette intersection et on prend celle
				avec le plus grand score qu n'a pas été visitée */
				LinkedList<Edge> routes = paris.streetsMap.get(currentIntersection[vehicule]);
				float bestRatioNonParcouru = 0;
				Edge bestRouteNonParcourue = null;
				Edge routeChoisie = null;
				Edge bestRouteByScore = null;
				LinkedList<Edge> possibleRoutes = new LinkedList<Edge>();
				float bestScore2 = 0;
				for (Edge route : routes){
					float ratio = route.length / route.cost;
					float score2 = 0;
					// calcul du score de la route : c'est le nombre de routes non parcourues après la route
					LinkedList<Edge> routesSuivantes = paris.streetsMap.get(route.end);
					for (Edge routeSuivante : routesSuivantes){
						float score2aux = 0;
						if(!parcouru[routeSuivante.index] && !(routeSuivante.end==currentIntersection[vehicule])){
							score2aux=(routeSuivante.length / routeSuivante.cost);
							if(score2aux > score2) score2 = score2aux;
						}
					}
					if(score2 > bestScore2 && route.cost <= timeLeft[vehicule]){
						bestRouteByScore = route;
						bestScore2 = score2;
					}
					if(ratio > bestRatioNonParcouru && !parcouru[route.index] && route.cost <= timeLeft[vehicule]){
						bestRouteNonParcourue = route;
						bestRatioNonParcouru = ratio;
					}
					if(route.cost <= timeLeft[vehicule]){
						possibleRoutes.add(route);
					}
				}
				if(bestRouteNonParcourue == null && possibleRoutes.isEmpty()){
					System.out.println("breakdown "+ timeLeft[vehicule] + " camion "+vehicule);
					cam_term[vehicule] = true;
					break;
				}
				else if(bestRouteNonParcourue == null && bestRouteByScore == null){
					Random generator = new Random();
					routeChoisie = possibleRoutes.get(generator.nextInt(possibleRoutes.size()));
				}
				else if(bestRouteNonParcourue == null){
					routeChoisie = bestRouteByScore;
				}
				else{
					routeChoisie = bestRouteNonParcourue;
				}
				score += routeChoisie.length;
				timeLeft[vehicule] -= routeChoisie.cost;
				parcouru[routeChoisie.index] = true;
				currentIntersection[vehicule] = routeChoisie.end;
				res.addStep(vehicule, currentIntersection[vehicule]);
				//System.out.println("camion "+vehicule+" intersection "+currentIntersection);
			}
			
			// on teste si tous les camions sont bloqués
			terminated = true;
			for(int h=0; h<paris.C; h++){
				terminated = terminated && !cam_term[h];
			}
		}
		res.print("outMenini.txt");
	}
}
