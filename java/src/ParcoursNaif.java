import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;


public class ParcoursNaif {
	public static Graph paris = new Graph("paris_54000.txt");

    static Point[] favoriteDirections = new Point[]{
            new Point(0,1),
            new Point(1,1),
            new Point(1,0),
            new Point(-1,1),
            new Point(-1,0),
            new Point(-1,-1),
            new Point(0,-1),
            new Point(1,-1)
    };
	
	public static void parcours(){
		boolean[] parcouru = new boolean[2*paris.M];
        //{7683,  6069,  7510,  3302,  7545,  7442,  5420,  5420}
		int[] pointInit = {7,  6069,  7510,  3302,  7545,  7442,  5420,  5420, 7683, 530};
		Result res = new Result(paris.C, paris.S);
		Arrays.fill(parcouru, false);
        int[] currentIntersection = new int[paris.C];
        Arrays.fill(currentIntersection, paris.S) ;
        int[] timeLeft = new int[paris.C];
        Arrays.fill(timeLeft, paris.T);
//        for(int c=0; c<paris.C; c++){
//            // on ammene le vehicule au point pointInit[vehicule]
//            timeLeft[c] = paris.findPath(currentIntersection[c], pointInit[c], timeLeft[c], parcouru, res, c);
//            currentIntersection[c] = pointInit[c];
//            System.out.println("Vehicule "+c+" at position "+currentIntersection[c]+" with time left : "+timeLeft[c]);
//        }
        int totalScore=0;
        boolean[] blocked = new boolean[paris.C];
        boolean terminated=false;
        int countRoutes;
        while(!terminated){
            for(int vehicule=0; vehicule<paris.C; vehicule++){
//                if(vehicule==0){
//                    if(blocked[vehicule])
//                        System.out.println("camion 0 blocked");
//                    else
//                        System.out.println("camion "+vehicule+" intersection "+currentIntersection[vehicule]+" time : "+timeLeft[vehicule]);
//                }

                    if(blocked[vehicule])
                        continue;


				/* on parcourt la liste des rues pour cette intersection et on prend celle
				avec le plus grand score qu n'a pas �t� visit�e */
				LinkedList<Edge> routes = paris.getRoutesFrom(currentIntersection[vehicule]);
				float bestRatioNonParcouru = 0;
				Edge bestRouteNonParcourue = null;
				Edge routeChoisie = null;
				Edge bestRouteByScore = null;
                countRoutes =0;
				LinkedList<Edge> possibleRoutes = new LinkedList<Edge>();
				float bestScore2 = 0;
				for (Edge route : routes){
					float ratio = route.length / route.cost;
					float score2 = 0;
					// calcul du score de la route : c'est le nombre de routes non parcourues apr�s la route
					LinkedList<Edge> routesSuivantes = paris.getRoutesFrom(route.end);
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
                        ++countRoutes;

					}
				}
				if(bestRouteNonParcourue == null && possibleRoutes.isEmpty()){
					//System.out.println("breakdown "+ timeLeft + " camion "+vehicule);
					blocked[vehicule]=true;
				} else if(bestRouteNonParcourue == null && bestRouteByScore == null){
//                    Point force = paris.forceFromTrucks(currentIntersection, vehicule);
//                    Point force = favoriteDirections[vehicule];
//                    double bestScal=0, scal;
//                    Edge forceEdge = null;
//                    for(Edge e : possibleRoutes){
//                        scal = force.dotProduct(Point.vect(paris.intersection[e.begin], paris.intersection[e.end]));
//                        if(scal > bestScal){
//                            bestScal = scal;
//                            forceEdge = e;
//                        }
//                    }
//                    if(forceEdge==null){
                        Random generator = new Random();
                        routeChoisie = possibleRoutes.get(generator.nextInt(possibleRoutes.size()));
//                    } else {
//                        routeChoisie = forceEdge;
//                        System.out.println("Edge "+routeChoisie.begin+", "+routeChoisie.end+" choose from force");
//                    }
				} else if(bestRouteNonParcourue == null){
//					System.out.println("bestroutebyscore : "+timeLeft[vehicule]);
					routeChoisie = bestRouteByScore;
				} else{
					routeChoisie = bestRouteNonParcourue;
				}
                if(routeChoisie == null){
                    System.out.println("breakdown "+ timeLeft[vehicule] + " camion "+vehicule);
                    blocked[vehicule]=true;
                } else {
                    if(routeChoisie.cost == 0 || currentIntersection[vehicule] == routeChoisie.end){
                        System.err.println("cost = "+0);
                    }
                    timeLeft[vehicule] -= routeChoisie.cost;
                    if(!parcouru[2*(routeChoisie.index/2)] && !parcouru[2*(routeChoisie.index/2)+1]){
                        totalScore += routeChoisie.length;
                    }
                    parcouru[2*(routeChoisie.index/2)] = true;
                    parcouru[2*(routeChoisie.index/2)+1] = true;
                    currentIntersection[vehicule] = routeChoisie.end;
                    res.addStep(vehicule, currentIntersection[vehicule]);
                }

                if(countRoutes<1){
                    System.out.println("camion "+vehicule+" intersection "+currentIntersection[vehicule]+" time : "+timeLeft[vehicule]+" | routes = "+countRoutes);
                }
			}
            int vehicule = (int) (Math.random()*paris.C);
            if(!blocked[vehicule]){
//                System.out.println("camion "+vehicule+" intersection "+currentIntersection[vehicule]+" time : "+timeLeft[vehicule]);
            }

            terminated = true;
            for(int c=0; c<paris.C; c++){
                if(!blocked[c])
                    terminated=false;
            }

			
		}
        System.out.println("Distance traveled : "+totalScore);
		res.print("outMenini.txt");
	}

    public static void parcoursWithNewMethod(){

        //{7683,  6069,  7510,  3302,  7545,  7442,  5420,  5420}
        int[] pointInit = {7,  6069,  7510,  3302,  7545,  7442,  5420,  5420, 7683, 530};
        Result res = new Result(paris.C, paris.S);
        int[] currentIntersection = new int[paris.C];
        Arrays.fill(currentIntersection, paris.S) ;
        int[] timeLeft = new int[paris.C];
        Arrays.fill(timeLeft, paris.T);
        for(int c=0; c<paris.C; c++){
            // on ammene le vehicule au point pointInit[vehicule]
            timeLeft[c] = paris.findPath(currentIntersection[c], pointInit[c], timeLeft[c], res, c);
            currentIntersection[c] = pointInit[c];
            System.out.println("Vehicule "+c+" at position "+currentIntersection[c]+" with time left : "+timeLeft[c]);
        }
        int totalScore=0;
        boolean[] blocked = new boolean[paris.C];
        boolean terminated=false;
        int countRoutes;
        Random generator = new Random();
        while(!terminated){
            for(int vehicule=0; vehicule<paris.C; vehicule++){
                if(blocked[vehicule])
                    continue;


				/* on parcourt la liste des rues pour cette intersection et on prend celle
				avec le plus grand score qu n'a pas �t� visit�e */
                LinkedList<Edge> routes = paris.getRoutesFrom(currentIntersection[vehicule]);
                float bestRatioNonParcouru = 0;
                Edge bestRouteNonParcourue = null;
                Edge routeChoisie = null;
                Edge bestRouteByScore = null;
                countRoutes =0;
                LinkedList<Edge> possibleRoutes = new LinkedList<Edge>();
                float bestScore2 = 0;
                for (Edge route : routes){
                    float ratio = route.length / route.cost;
                    float score2 = 0;
                    // calcul du score de la route : c'est le nombre de routes non parcourues apr�s la route
                    LinkedList<Edge> routesSuivantes = paris.getRoutesFrom(route.end);
                    for (Edge routeSuivante : routesSuivantes){
                        float score2aux = 0;
                        if(!paris.isVisited(routeSuivante) && !(routeSuivante.end==currentIntersection[vehicule])){
                            score2aux=(routeSuivante.length / routeSuivante.cost);
                            if(score2aux > score2) score2 = score2aux;
                        }
                    }
                    if(score2 > bestScore2 && route.cost <= timeLeft[vehicule]){
                        bestRouteByScore = route;
                        bestScore2 = score2;
                    }
                    if(ratio > bestRatioNonParcouru && !paris.isVisited(route) && route.cost <= timeLeft[vehicule]){
                        bestRouteNonParcourue = route;
                        bestRatioNonParcouru = ratio;
                    }
                    if(route.cost <= timeLeft[vehicule]){
                        possibleRoutes.add(route);
                        ++countRoutes;
                    }
                }
                if(bestRouteNonParcourue == null && possibleRoutes.isEmpty()){
                    //System.out.println("breakdown "+ timeLeft + " camion "+vehicule);
                    blocked[vehicule]=true;
                } else if(bestRouteNonParcourue == null && bestRouteByScore == null){
                    routeChoisie = possibleRoutes.get(generator.nextInt(possibleRoutes.size()));
                } else if(bestRouteNonParcourue == null){
//					System.out.println("bestroutebyscore : "+timeLeft[vehicule]);
                    routeChoisie = bestRouteByScore;
                } else{
                    routeChoisie = bestRouteNonParcourue;
                }
                if(routeChoisie == null){
                    System.out.println("breakdown "+ timeLeft[vehicule] + " camion "+vehicule);
                    blocked[vehicule]=true;
                } else {
                    if(routeChoisie.cost == 0 || currentIntersection[vehicule] == routeChoisie.end){
                        System.err.println("cost = "+0);
                    }
                    timeLeft[vehicule] -= routeChoisie.cost;
                    paris.visitEdge(routeChoisie);
                    currentIntersection[vehicule] = routeChoisie.end;
                    res.addStep(vehicule, currentIntersection[vehicule]);
                }

                if(countRoutes<1){
                    System.out.println("camion "+vehicule+" intersection "+currentIntersection[vehicule]+" time : "+timeLeft[vehicule]+" | routes = "+countRoutes);
                }
            }
            int vehicule = (int) (Math.random()*paris.C);
            if(!blocked[vehicule]){
//                System.out.println("camion "+vehicule+" intersection "+currentIntersection[vehicule]+" time : "+timeLeft[vehicule]);
            }

            terminated = true;
            for(int c=0; c<paris.C; c++){
                if(!blocked[c])
                    terminated=false;
            }


        }
        System.out.println("Distance traveled : "+paris.score);
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
				avec le plus grand score qu n'a pas �t� visit�e */
				LinkedList<Edge> routes = paris.getRoutesFrom(currentIntersection);
				float bestRatioNonParcouru = 0;
				Edge bestRouteNonParcourue = null;
				Edge routeChoisie = null;
				Edge bestRouteByScore = null;
				LinkedList<Edge> possibleRoutes = new LinkedList<Edge>();
				float bestScore2 = 0;
				for (Edge route : routes){
					float ratio = route.length / route.cost;
					float score2 = 0;
					// calcul du score de la route : c'est le nombre de routes non parcourues apr�s la route
					LinkedList<Edge> routesSuivantes = paris.getRoutesFrom(route.end);
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
			
			// on teste si tous les camions sont bloqu�s
			for(int h=0; h<paris.C; h++){
				terminated = terminated || cam_term[h];
			}
		}
		res.print("outMenini.txt");
	}
	
	public static void parcours3(){
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
		int[] timeLeft = new int[paris.C];
		Arrays.fill(timeLeft, paris.T);
		/*for(int vehicule=0; vehicule<paris.C; vehicule++){
			// on ammene le vehicule au point pointInit[vehicule]
			timeLeft[vehicule] = paris.findPath(currentIntersection[vehicule], pointInit[vehicule], timeLeft[vehicule], parcouru, res, vehicule);
			initDone[vehicule] = true;
			currentIntersection[vehicule] = pointInit[vehicule];
		}*/
		
		while(!terminated){		
			for(int vehicule=0; vehicule<paris.C; vehicule++){				
				/* on parcourt la liste des rues pour cette intersection et on prend celle
				avec le plus grand score qu n'a pas ete visitee */
				LinkedList<Edge> routes = paris.getRoutesFrom(currentIntersection[vehicule]);
				float bestRatioNonParcouru = 0;
				Edge bestRouteNonParcourue = null;
				Edge routeChoisie = null;
				Edge bestRouteByScore = null;
				LinkedList<Edge> possibleRoutes = new LinkedList<Edge>();
				float bestScore2 = 0;
				for (Edge route : routes){
					float ratio = route.length / route.cost;
					float score2 = 0;
					// calcul du score de la route : c'est le nombre de routes non parcourues apres la route
					LinkedList<Edge> routesSuivantes = paris.getRoutesFrom(route.end);
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
					System.out.println("1");
				}
				else{
					routeChoisie = bestRouteNonParcourue;
				}
				timeLeft[vehicule] -= routeChoisie.cost;
				parcouru[routeChoisie.index] = true;
				currentIntersection[vehicule] = routeChoisie.end;
				res.addStep(vehicule, currentIntersection[vehicule]);
				//System.out.println("camion "+vehicule+" intersection "+currentIntersection);
			}
			
			// on teste si tous les camions sont bloqu�s
			terminated = true;
			for(int h=0; h<paris.C; h++){
				terminated = terminated && !cam_term[h];
			}
		}
		res.print("outMenini.txt");
	}
}
