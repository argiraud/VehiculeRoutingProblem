package metapop;

import metavoisinage.Solution;

import java.util.*;

public class Croisement {

    public static List<Solution> getSolutionsAfterCrossover(List<Solution> solutionsToCrossover){

        return null;
    }

    //TODO - diviser les solutions en 2 partie
    //TODO - On mets la partie gaude de P1 dans P2 et inversement
    public static List<Solution> crossSolutions(List<Solution> solutions){
        return solutions;
    }

    //TODO - On recherche les doublons si doublon il y a on le remplace par un client manquant
    private static Solution lookForDuplicateClient(Solution P){
        return null;
    }

    //TODO - On recrée les deux solutions obtenues à partir des id route de chaque client
    private static Solution rebuildSolution(Solution P){
        return null;
    }

    public static List<Solution> Crossover(Solution s1, Solution s2)
    {
        List<Solution> retour = new ArrayList();

        Solution tempS1 = new Solution();
        Solution tempS2 = new Solution();

        Random r = new Random();
        int k = r.nextInt(10)+ 3;

        for(int j = 0; j < k; j++)
        {
            tempS1.getRoutes().add(s1.getRoutes().get(j));
            tempS2.getRoutes().add(s2.getRoutes().get(j));
        }

        for(int i = k; i < s1.getRoutes().size(); i++)
        {
            tempS2.getRoutes().add(s1.getRoutes().get(i));
            tempS1.getRoutes().add(s2.getRoutes().get(i));
        }

        retour.add(tempS1);
        retour.add(tempS2);

        return retour;

    }


}
