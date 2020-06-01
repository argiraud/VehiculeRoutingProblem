package metapop;

import metavoisinage.Solution;

import java.util.List;

public class GeneticAlgorithm {

    public GeneticAlgorithm() {
    }

    public Solution executeGeneticAlgorithm(Solution solution) {

        Mutation.Mutation(Croisement.crossSolutions(Reproduction.getSelectedSolutions(generateXSolutions(solution))));
        return null;
    }

    private List<Solution> generateXSolutions(Solution solution) {
        return null;
    }
}
