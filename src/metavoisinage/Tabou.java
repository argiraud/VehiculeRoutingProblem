package metavoisinage;

import java.util.*;

public class Tabou {
//TODO  - générer les voisins d'un ensemble de tournées
//      - prendre le plus petit voisin
//      - on le mets dans la liste tabou
//      - puis on recommence


    public Tabou() {
    }

    public Solution methodeTabou(Solution routes, Integer chargeMax, Integer opVois) {
        LinkedList<Solution> tabouList = new LinkedList<>();
        Solution meilleureSolution = new Solution(routes);
        Solution precSol = new Solution(routes);
        for (int i = 0; i < 100; i++) {
            List<Solution> voisins = genererVoisins(precSol, chargeMax, opVois);
            Solution solActuelle = voisins.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                    .orElseThrow(NoSuchElementException::new);
            while (tabouList.stream().anyMatch(solActuelle::equals)) {
                voisins.remove(precSol);
                solActuelle = voisins.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                        .orElseThrow(NoSuchElementException::new);
            }
            if (solActuelle.getDistanceTotal() < precSol.getDistanceTotal()) {
                tabouList.add(solActuelle);
            }
            precSol = solActuelle;
            if (tabouList.size() >= 2) {
                tabouList.remove();
            }
            if (precSol.getDistanceTotal() < meilleureSolution.getDistanceTotal()) {
                meilleureSolution = precSol;
            }
        }
        return meilleureSolution;
    }

    private List<Solution> genererVoisins(Solution routes, Integer chargeMax, Integer opVois) {
        List<Solution> voisins = new ArrayList<>();
        Random r = new Random();
        Solution voisin = null;
        for (int i = 0; i < 100000; i++) {
            Solution s = new Solution(routes);
            int j = opVois;
            switch (j) {
                case 0:
                    System.out.println("crossArreteBetweenRoutes");
                    voisin = OperateurVoisinage.crossArreteBetweenRoutes(s, chargeMax);
                    break;
                case 1:
                    System.out.println("echangePointsBetweenRoutes");
                    voisin = OperateurVoisinage.echangePointsBetweenRoutes(s, chargeMax);
                    break;
                case 2:
                    System.out.println("crossArreteInsideRoute");
                    voisin = OperateurVoisinage.crossArreteInsideRoute(s);
                    break;
                case 3:
                    System.out.println("inversePointsArretes");
                    voisin = OperateurVoisinage.inversePointsArretes(s);
                    break;
                case 4:
                    System.out.println("enleverUnPoint");
                    voisin = OperateurVoisinage.enleverUnPoint(s, chargeMax);
            }
            if (!voisins.contains(voisin)) {
                voisins.add(voisin);
            }
        }
        return voisins;
    }
}
