package metapop;

import metavoisinage.Client;
import metavoisinage.OperateurVoisinage;
import metavoisinage.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Mutation {
    //TODO Changer un client de tournée aléatoirement

    public static List<Map<Client, Integer>> Mutation(List<Solution> p1, double MutPercent) {
        Random r = new Random();
        List<Map<Client,Integer>> maps = new ArrayList<>();
        for (int i = 0; i < p1.size(); i++)
        {
            Map<Client, Integer> p1Clients = p1.get(i).getAllClientsWithIdRoute();
            int nbRoutes = p1.get(i).getRoutes().size();

            p1Clients.forEach((k, v) -> {
                double j = r.nextDouble();
                if (j < MutPercent){
                    int newRoute = r.nextInt(nbRoutes);
                    v = newRoute;
                }
            });
            maps.add(p1Clients);
        }

        return maps ;
    }


    private static Solution lancerUnOperateurAleatoire(Solution s, int chargeMax) {
        Random r = new Random();
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
}
