package metapop;

import metavoisinage.Solution;

import java.util.*;

public class Reproduction {

    public static final Random r = new Random();

    public static List<Solution> getSelectedSolutions(List<Solution> solutionList) {
        int fitnessTotal = solutionList.stream().mapToInt(solution -> (int) solution.getDistanceTotal()).sum();
        Double[] lastRatio = new Double[2];
        List<Map<Solution, Double>> ratioss = new ArrayList<>();
        solutionList.forEach(solution -> {
            Map<Solution, Double> ratios = new HashMap<>();
            if (ratios.isEmpty()) {
                ratios.put(solution, (1D - (solution.getDistanceTotal() / fitnessTotal)));
            } else {
                ratios.put(solution, (1D - (solution.getDistanceTotal() / fitnessTotal))
                        + ratios.get(solutionList.get(ratios.size() - 1)));
            }
            lastRatio[0] = ratios.get(solution);
            ratioss.add(ratios);
        });
        return getRatioChoosenSolution(ratioss, lastRatio);
    }

    private static List<Solution> getRatioChoosenSolution(List<Map<Solution, Double>> ratios, Double[] lastRatio) {
        List<Solution> choosenSolutions = new ArrayList<>();
        for (int i = 0; i < ratios.size(); i++) {
            double rationAlea = r.nextDouble() * lastRatio[0];
            for (Map<Solution, Double> ratio : ratios) {
                if (rationAlea <= ratio.entrySet().stream().findFirst().get().getValue()) {
                    choosenSolutions.add(ratio.entrySet().stream().findFirst().get().getKey());
                    break;
                }
            }
        }
        return choosenSolutions;
    }
}
