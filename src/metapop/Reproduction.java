package metapop;

import metavoisinage.Solution;

import java.util.*;

public class Reproduction {

    public static final Random r = new Random();

    public static List<Solution> getSelectedSolutions(List<Solution> solutionList) {
        int fitnessTotal = solutionList.stream().mapToInt(solution -> (int) solution.getDistanceTotal()).sum();
        Map<Solution, Float> ratios = new HashMap<>();
        solutionList.forEach(solution -> {
            if (ratios.isEmpty()) {
                ratios.put(solution, (float) (1 - (solution.getDistanceTotal() / fitnessTotal)));
            } else {
                ratios.put(solution, (float) (1 - (solution.getDistanceTotal() / fitnessTotal))
                        + ratios.get(solutionList.get(ratios.size() - 1)));
            }
        });
        return getRatioChoosenSolution(ratios);
    }

    private static List<Solution> getRatioChoosenSolution(Map<Solution, Float> ratios) {
        List<Solution> choosenSolutions = new ArrayList<>();
        for (int i = 0; i < ratios.size(); i++) {
            float rationAlea = r.nextFloat();
            ratios.forEach((solution, ratio) -> {
                if (rationAlea <= ratio) {
                    choosenSolutions.add(solution);
                }
            });
        }
        return choosenSolutions;
    }
}
