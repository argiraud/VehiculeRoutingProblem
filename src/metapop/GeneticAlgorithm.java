package metapop;

import metavoisinage.OperateurVoisinage;
import metavoisinage.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    public GeneticAlgorithm() {
    }

    public Solution executeGeneticAlgorithm(Solution solution) {
        List<Solution> geneticSol = new ArrayList<>();
        geneticSol =  generateXSolutions(solution, 8);
        System.out.println("Reproduction");

        List<Solution> geneticReprod = new ArrayList<>();
        geneticReprod = Reproduction.getSelectedSolutions(geneticSol);
        //Mutation.Mutation(Croisement.crossSolutions(Reproduction.getSelectedSolutions(generateXSolutions(solution))));
        return null;
    }

    private List<Solution> generateXSolutions(Solution solution, Integer nbPop) {
        List<Solution> sol = new ArrayList<>();
        sol.add(solution);

        for(int i = 0; i < nbPop - 1; i++)
        {
            Solution nouvSol = lancerUnOperateurAleatoire(new Solution(solution), 100);
            sol.add(nouvSol);
        }

        return sol;
    }

    private Solution lancerUnOperateurAleatoire(Solution s, int chargeMax) {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
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
