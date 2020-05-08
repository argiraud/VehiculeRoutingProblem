package metavoisinage;

import java.util.*;

public class Tabou {

    private final Random r;

    public Tabou() {
        r = new Random();
    }

    public Solution methodeTabou(Solution routes, Integer chargeMax, Integer opVois, Integer nbVoisins, Integer nbExecutions, Integer tailleListe) {
        LinkedList<Solution> tabouList = new LinkedList<>();
        Solution meilleureSolution = new Solution(routes);
        Solution precSol = new Solution(routes);
        for (int i = 0; i < nbExecutions; i++) {
            List<Solution> voisins = genererVoisins(precSol, chargeMax, opVois, nbVoisins);
            Solution solActuelle = voisins.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                    .orElseThrow(NoSuchElementException::new);
            while (tabouList.stream().anyMatch(solActuelle::equals)) {
                voisins.remove(solActuelle);
                solActuelle = voisins.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                        .orElseThrow(NoSuchElementException::new);
            }
            if (solActuelle.getDistanceTotal() < precSol.getDistanceTotal()) {
                tabouList.add(solActuelle);
            }
            precSol = solActuelle;
            if (tabouList.size() >= tailleListe) {
                tabouList.remove();
            }
            if (precSol.getDistanceTotal() < meilleureSolution.getDistanceTotal()) {
                meilleureSolution = precSol;
            }
        }
        return meilleureSolution;
    }

    private List<Solution> genererVoisins(Solution routes, Integer chargeMax, Integer opVois, Integer nbVoisins) {
        List<Solution> voisins = new ArrayList<>();
        Solution voisin;
        for (int i = 0; i < nbVoisins; i++) {
            Solution s = new Solution(routes);
            switch (opVois) {
                case 0:
                    voisin = OperateurVoisinage.crossArreteBetweenRoutes(s, chargeMax);
                    break;
                case 1:
                    voisin = OperateurVoisinage.echangePointsBetweenRoutes(s, chargeMax);
                    break;
                case 2:
                    voisin = OperateurVoisinage.crossArreteInsideRoute(s);
                    break;
                case 3:
                    voisin = OperateurVoisinage.inversePointsArretes(s);
                    break;
                case 4:
                    voisin = OperateurVoisinage.enleverUnPoint(s, chargeMax);
                    break;
                default:
                    voisin = lancerUnOperateurAleatoire(s, chargeMax);
                    break;
            }
            if (voisin != null) {
                if (!voisins.contains(voisin)) {
                    voisins.add(voisin);
                } else {
                    i--;
                }
            } else i--;
            System.out.println(voisins.size());//5522
        }
        return voisins;
    }

    private Solution lancerUnOperateurAleatoire(Solution s, int chargeMax) {
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
