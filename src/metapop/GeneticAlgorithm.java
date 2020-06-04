package metapop;

import com.sun.deploy.security.SelectableSecurityManager;
import metavoisinage.OperateurVoisinage;
import metavoisinage.Solution;
import sun.security.x509.OtherName;

import java.util.*;

public class GeneticAlgorithm {

    public GeneticAlgorithm() {
    }

    public Solution executeGeneticAlgorithm(Solution solution, int nbSol) {
        double probaCross = 0.7;
        Random r = new Random();
        double j;

        //Initialisation
        List<Solution> bestSol =  generateXSolutions(solution, nbSol);
        System.out.println("Reproduction");
        List<Solution> geneticReprod = new ArrayList<>();

        for (int i = 0; i < 7; i++) { // a changer avec nbGen

            geneticReprod = Reproduction.getSelectedSolutions(bestSol);

            j = r.nextDouble();
            if (j < probaCross) {
                //croisement
            } else {
                //mutation
                Mutation.Mutation(geneticReprod);
            }
            bestSol = geneticReprod;
        }

        Solution bestSolAretourner = new Solution();
        bestSolAretourner = bestSol.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                .orElseThrow(NoSuchElementException::new);

        return bestSolAretourner;
    }

    private List<Solution> generateXSolutions(Solution solution, Integer nbPop) {
        List<Solution> sol = new ArrayList<>();
        sol.add(solution);
        System.out.println(solution.toString());

        for(int i = 0; i < nbPop - 1; i++)
        {
            Solution nouvSol = lancerUnOperateurAleatoire(new Solution(solution), 100);
            sol.add(nouvSol);
            System.out.println(nouvSol.toString());
        }

        return sol;
    }

    private Solution lancerUnOperateurAleatoire(Solution s, int chargeMax) {
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
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
        return s;
    }
}
