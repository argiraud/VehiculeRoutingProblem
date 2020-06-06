package metapop;

import metavoisinage.OperateurVoisinage;
import metavoisinage.Solution;

import java.util.*;

public class GeneticAlgorithm {

    Random r;

    public GeneticAlgorithm() {
        r = new Random();
    }

    public Solution executeGeneticAlgorithm(Solution solution, int nbSol) {
        double probaCross = 0.7;
        List<Solution> solutions = new ArrayList<>();
        solutions = generateXSolutions(solution, nbSol);
        for (int i = 0; i < 1000; i++) {
            solutions = Reproduction.getSelectedSolutions(solutions);
            double j = r.nextDouble();
            if (j < probaCross) {
                //croisement
                Croisement.crossSolutions(solutions);
            } else {
                //mutation
                //Mutation.Mutation(solutions);
            }

        }
        solution = solutions.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                .orElseThrow(NoSuchElementException::new);
        return solution;
    }

    private List<Solution> generateXSolutions(Solution solution, Integer nbPop) {
        List<Solution> sol = new ArrayList<>();
        sol.add(solution);

        for (int i = 0; i < nbPop - 1; i++) {
            Solution nouvSol = null;
            while (nouvSol == null || sol.contains(nouvSol)) {
                nouvSol = lancerUnOperateurAleatoire(new Solution(solution), 100);
            }
            sol.add(nouvSol);
        }

        return sol;
    }

    private Solution lancerUnOperateurAleatoire(Solution s, int chargeMax) {
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
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
