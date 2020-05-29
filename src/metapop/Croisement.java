package metapop;

import metavoisinage.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Croisement {
    //TODO découpage des solutions en 1 point
    //injecte dans la premiere solution le découpage de la deuxième solution et inversement
    //TODO coisement en deux points pour plus tard

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
