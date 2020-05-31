package metapop;

import metavoisinage.Solution;

import java.util.List;

public class GeneticAlgorithm {

    public GeneticAlgorithm() {
    }

    public Solution executeGeneticAlgorithm(List<Solution> solutions){
        Mutation.Mutation(Croisement.crossSolutions(Reproduction.getSelectedSolutions(solutions)));
        return null;
    }
}
