package metavoisinage;

import java.util.*;

public class Tabou {
//TODO  - générer les voisins d'un ensemble de tournées
//      - prendre le plus petit voisin
//      - on le mets dans la liste tabou
//      - puis on recommence


    public Tabou() {
    }

    public Solution methodeTabou(Solution routes) {
        List<Solution> voisins = genererVoisins(routes);
        LinkedList<Solution> tabouList = new LinkedList<>();
        Solution meilleureSolution = null;
        for (int i = 0; i < 100; i++) {
            Solution minSol = voisins.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                    .orElseThrow(NoSuchElementException::new);
            meilleureSolution = minSol;
            while (tabouList.stream().anyMatch(minSol::equals)) {
                voisins.remove(minSol);
                minSol = voisins.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                        .orElseThrow(NoSuchElementException::new);
            }
            if (tabouList.size() >= 2) {
                tabouList.remove();
            }
            if (minSol.getDistanceTotal() < meilleureSolution.getDistanceTotal()) {
                meilleureSolution = minSol;
            }
            tabouList.add(minSol);
        }
        return meilleureSolution;
    }

    private List<Solution> genererVoisins(Solution routes) {
        List<Solution> voisins = new ArrayList<>();
        Random r = new Random();
        Solution voisin = null;
        for (int i = 0; i < 100; i++) {
            int j = r.nextInt(4);
            switch (j) {
                case 0:
                    voisin = OperateurVoisinage.crossArreteBetweenRoutes(routes);
                    break;
                case 1:
                    voisin = OperateurVoisinage.echangePointsBetweenRoutes(routes);
                    break;
                case 2:
                    voisin = OperateurVoisinage.crossArreteInsideRoute(routes);
                    break;
                case 3:
                    voisin = OperateurVoisinage.inversePointsArretes(routes);
                    break;
            }
            if(!voisins.contains(voisin)){
                voisins.add(voisin);
            }
        }
        return voisins;
    }
}
