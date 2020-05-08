package metavoisinage;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RecuitSimule {

    private final Random r;

    public RecuitSimule() {
        r = new Random();
    }

    public Solution MethodeRecuit(Solution routesInit, Integer temperature, Integer chargeMax, Integer opVois) {
        Solution meilleuresolution = new Solution(routesInit);
        Solution precSol = new Solution(routesInit);
        Route routeCour = meilleuresolution.getRoutes().get(0);
        double fitMin = routesInit.getDistanceTotal();
        double deltaFit = 0;
        double p;
        for (int k = 0; k < temperature; k++) {
            for (int l = 1; l < temperature; l++) {
                Solution randsolution = genererVoisins(precSol, chargeMax, opVois);
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
                    double t = Math.exp((-deltaFit) / temperature);
                    if (p < t) {
                        meilleuresolution = randsolution;
                        precSol = randsolution;
                    } else {
                        meilleuresolution = precSol;
                    }
                }
            }
            temperature = (int) (0.9 * temperature); //0.9 correspond au mu
        }

        return meilleuresolution;
    }

    private Solution genererVoisins(Solution routes, Integer chargeMax, Integer opVois) {
        Solution voisin = null;
        for (int i = 0; i < 100; i++) {
            Solution s = new Solution(routes);
            switch (opVois) {
                case 0:
                    voisin = OperateurVoisinage.crossArreteBetweenRoutes(s, chargeMax);
                    break;
                case 1:
                    voisin = OperateurVoisinage.echangePointsBetweenRoutes(s, chargeMax);
                    break;
                case 2:
                    voisin = OperateurVoisinage.crossArreteInsideRoute(s);
                    break;
                case 3:
                    voisin = OperateurVoisinage.inversePointsArretes(s);
                    break;
                case 4:
                    voisin = OperateurVoisinage.enleverUnPoint(s, chargeMax);
                    break;
                default:
                    voisin = lancerUnOperateurAleatoire(s, chargeMax);
                    break;
            }
        }
        return voisin;
    }

    private Solution lancerUnOperateurAleatoire(Solution s, int chargeMax) {
        int j = r.nextInt(5);
        switch (j) {
            case 0:
                return OperateurVoisinage.crossArreteBetweenRoutes(s, chargeMax);
            case 1:
                return OperateurVoisinage.echangePointsBetweenRoutes(s, chargeMax);
            case 2:
                return OperateurVoisinage.crossArreteInsideRoute(s);
            case 3:
                return OperateurVoisinage.inversePointsArretes(s);
            case 4:
                return OperateurVoisinage.enleverUnPoint(s, chargeMax);
            default:
                return s;
        }
    }

}
