package metapop;

import metavoisinage.OperateurVoisinage;
import metavoisinage.Solution;

import java.util.List;
import java.util.Random;

public class Mutation {
    //TODO Changer un client de tournée aléatoirement

    public static List<Solution> Mutation(List<Solution> sol) {

        Random r = new Random();
        int ind = r.nextInt(sol.size() - 1);
        sol.remove(ind);

        Solution nouvSol = lancerUnOperateurAleatoire(sol.get(ind), 100);

        sol.add(nouvSol);

        return sol;
    }


    private static Solution lancerUnOperateurAleatoire(Solution s, int chargeMax) {
        Random r = new Random();
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
