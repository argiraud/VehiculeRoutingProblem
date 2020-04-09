package metavoisinage;

import com.sun.deploy.security.SelectableSecurityManager;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RecuitSimule {

    public RecuitSimule() {

    }

    public Solution MethodeRecuit(Solution routesInit, Integer temperature ) {
        Solution meilleuresolution = new Solution(routesInit);
        Solution precSol = new Solution(routesInit);
        Route routeCour = meilleuresolution.getRoutes().get(0);
        double fitMin = routesInit.getDistanceTotal();
        double deltaFit = 0;
        double p;
        Random r = new Random();
        for (int k = 0; k < temperature; k++) {
            for(int l = 1; l < temperature; l++){
                Solution randsolution = genererVoisins(precSol);
                int j = r.nextInt(randsolution.getRoutes().size());

                deltaFit = precSol.getDistanceTotal() - randsolution.getDistanceTotal();
                if (deltaFit <= 0){
                    precSol = randsolution;
                    if (precSol.getDistanceTotal() < fitMin){
                        meilleuresolution = randsolution;
                        fitMin = meilleuresolution.getDistanceTotal();
                    }
                } else
                {
                    p = ThreadLocalRandom.current().nextDouble(0, 1);
                    if (p < Math.exp((deltaFit/temperature))) {
                        meilleuresolution = randsolution;
                        precSol = randsolution;
                    }else {
                        meilleuresolution = precSol;
                    }
                }
                }
            k = (int) (0.5 * k);
            }

        return meilleuresolution;
    }

    private Solution genererVoisins(Solution routes) {
        Random r = new Random();
        Solution voisin = null;
        int j = r.nextInt(4);
            switch (j) {
                case 0:
                    voisin = OperateurVoisinage.crossArreteBetweenRoutes(routes);
                    break;
                case 1:
                    voisin = OperateurVoisinage.echangePointsBetweenRoutes(routes);
                    break;
                case 2:
                    voisin = OperateurVoisinage.crossArreteInsideRoute(routes);
                    break;
                case 3:
                    voisin = OperateurVoisinage.inversePointsArretes(routes);
                    break;
        }
        return voisin;
    }

}
