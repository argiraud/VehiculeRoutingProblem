package metavoisinage;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RecuitSimule {

    public RecuitSimule() {

    }

    public Solution MethodeRecuit(Solution routesInit, Integer temperature, Integer chargeMax) {
        Solution meilleuresolution = new Solution(routesInit);
        Solution precSol = new Solution(routesInit);
        Route routeCour = meilleuresolution.getRoutes().get(0);
        double fitMin = routesInit.getDistanceTotal();
        //System.out.printf(String.valueOf(fitMin));
        double deltaFit = 0;
        double p;
        Random r = new Random();
        for (int k = 0; k < temperature; k++) {
            for (int l = 1; l < temperature; l++) {
                Solution randsolution = genererVoisins(precSol, chargeMax);
                int j = r.nextInt(randsolution.getRoutes().size());

                deltaFit = randsolution.getDistanceTotal() - precSol.getDistanceTotal();
                if (deltaFit <= 0) {
                    precSol = randsolution;
                    if (precSol.getDistanceTotal() < fitMin) {
                        meilleuresolution = randsolution;
                        fitMin = meilleuresolution.getDistanceTotal();
                    }
                } else {
                    p = ThreadLocalRandom.current().nextDouble(0, 1);
                    double t =  Math.exp((-deltaFit) / temperature);
                    if (p < t) {
                        meilleuresolution = randsolution;
                        precSol = randsolution;
                    } else {
                        meilleuresolution = precSol;
                    }
                }
            }
            temperature = (int) (0.9 * temperature);
        }

        return meilleuresolution;
    }

    private Solution genererVoisins(Solution routes, Integer chargeMax) {
        Random r = new Random();
        Solution voisin = null;
        for (int i = 0; i < 100; i++) {
            Solution s = new Solution(routes);
            voisin = OperateurVoisinage.enleverUnPoint(s, chargeMax);
            /**int j = r.nextInt(3);
            switch (j) {
                case 3:
                    //System.out.println("crossArreteBetweenRoutes");
                    voisin = OperateurVoisinage.crossArreteBetweenRoutes(s, chargeMax);
                    break;
                case 1:
                    //System.out.println("echangePointsBetweenRoutes");
                    voisin = OperateurVoisinage.echangePointsBetweenRoutes(s, chargeMax);
                    break;
                case 2:
                //    System.out.println("crossArreteInsideRoute");
                    voisin = OperateurVoisinage.crossArreteInsideRoute(s);
                    break;
                case 0:
                    //System.out.println("inversePointsArretes");
                    voisin = OperateurVoisinage.inversePointsArretes(s);
                    break;
            }**/
        }
        return voisin;
    }

}
