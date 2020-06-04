package metapop;

import metavoisinage.Solution;

import java.util.*;

public class Reproduction {

    public static final Random r = new Random();

    public static List<Solution> getSelectedSolutions(List<Solution> solutionList) {
        int fitnessTotal = solutionList.stream().mapToInt(solution -> (int) solution.getDistanceTotal()).sum();
        Map<Solution, Double> ratios = new HashMap<>();
        Double[] lastRatio = new Double[2];
        solutionList.forEach(solution -> {
            if (ratios.isEmpty()) {
                ratios.put(solution, (1D - (solution.getDistanceTotal() / fitnessTotal)));
            } else {
                ratios.put(solution, (1D - (solution.getDistanceTotal() / fitnessTotal))
                        + ratios.get(solutionList.get(ratios.size() - 1)));
            }
            lastRatio[0] = ratios.get(solution);
        });
        return getRatioChoosenSolution(ratios, lastRatio);
    }

    private static List<Solution> getRatioChoosenSolution(Map<Solution, Double> ratios, Double[] lastRatio) {
        List<Solution> choosenSolutions = new ArrayList<>();
        for (int i = 0; i < ratios.size(); i++) {
            double rationAlea = r.nextDouble() * lastRatio[0];
            for (Map.Entry<Solution, Double> ratio : ratios.entrySet()) {
                if (rationAlea <= ratio.getValue()) {
                    choosenSolutions.add(ratio.getKey());
                    System.out.println(ratio.getKey().toString());
                    break;

                }
            }
        }
        return choosenSolutions;
    }
}
