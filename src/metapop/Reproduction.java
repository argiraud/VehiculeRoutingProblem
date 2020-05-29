package metapop;

import metavoisinage.Solution;

import java.util.*;

public class Reproduction {
    //TODO P1 : fit = 2090
    //P2 : Fit = 1790
    //P3 : Fit = 2460
    //P4 : Fit = 1640
    //P1 : Ratio : 2090 / (2090+1790+2460+1640) = 0,26
    //P2 : Ratio = ...
    //P1 : Ratio = 1 - 0,26 = 0,74
    //Ration + roulette pour choisir les solutions a garder pour le croisement
    public static List<Solution> getSelectedSolutions(List<Solution> solutionList) {
        int fitnessTotal = solutionList.stream().mapToInt(solution -> (int) solution.getDistanceTotal()).sum();
        Map<Solution, Float> ratios = new HashMap<>();
        solutionList.forEach(solution -> {
            if (ratios.isEmpty()) {
                ratios.put(solution,(float) (1 - (solution.getDistanceTotal() / fitnessTotal)));
            } else {
                ratios.put(solution, (float) (1 - (solution.getDistanceTotal() / fitnessTotal)) + ratios.get(ratios.size() - 1));
            }
        });
        return getRatioChoosenSolution(ratios);
    }

    private static List<Solution> getRatioChoosenSolution(Map<Solution,Float> ratios) {
        Random r = new Random();
        List<Solution> choosenSolutions = new ArrayList<>();
        for (int i = 0; i < ratios.size(); i++) {
            float rationAlea = r.nextFloat();
            ratios.forEach((solution, ratio) ->{
                if (rationAlea <= ratio) {
                    choosenSolutions.add(solution);
                }
            });
        }
        return choosenSolutions;
    }
}
