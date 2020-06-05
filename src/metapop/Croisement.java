package metapop;

import metavoisinage.Arrete;
import metavoisinage.Client;
import metavoisinage.Route;
import metavoisinage.Solution;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Croisement {

    private static final Random r = new Random();

    //TODO - diviser les solutions en 2 partie
    //TODO - On mets la partie gauche de P1 dans P2 et inversement
    public static List<Solution> crossSolutions(List<Solution> solutions) {
        List<Solution> crossSolutions = new ArrayList<>();
        for (int i = 0; i < solutions.size(); i += 2) {
            crossSolutions.addAll(cross2Solutions(solutions.get(i), solutions.get(i + 1)));
        }
        return crossSolutions;
    }

    private static List<Solution> cross2Solutions(Solution p1, Solution p2) {
        Map<Client, Integer> p1Clients = p1.getAllClients();
        Map<Client, Integer> p2Clients = p2.getAllClients();

        Integer pivot = r.nextInt(p1.getAllClients().size());
        Map<Client, Integer> e1 = new HashMap<>();
        Map<Client, Integer> e2 = new HashMap<>();

        p1Clients.forEach((k, v) -> {
            if (k.getId() > pivot) {
                e1.put(k, v);
            } else {
                e2.put(k, v);
            }
        });

        p2Clients.forEach((k, v) -> {
            if (k.getId() > pivot) {
                e2.put(k, v);
            } else {
                e1.put(k, v);
            }
        });

        List<Solution> solutions = new ArrayList<>();
        solutions.add(rebuildSolution(e1, p1.getRoutes().size()));
        solutions.add(rebuildSolution(e2, p2.getRoutes().size()));
        System.out.println(solutions);
        return solutions;
    }

    //TODO - On recrée les deux solutions obtenues à partir des id route de chaque client
    private static Solution rebuildSolution(Map<Client, Integer> clients, Integer nbRoute) {
        List<Route> routes = new ArrayList<>();
        Client depot = clients.entrySet().stream().filter(clientIntegerEntry -> clientIntegerEntry.getKey().getId() == 0).findFirst().get().getKey();
        clients.remove(depot);
        List<Client> clientsSansRoute = new ArrayList<>();
        for (int i = 0; i < nbRoute; i++) {
            int finalI = i;
            List<Client> clientsRoute = new ArrayList<>();
            clients.entrySet().stream().filter(clientIntegerEntry -> clientIntegerEntry.getValue() == finalI).forEach(
                    c -> clientsRoute.add(c.getKey())
            );
            List<Arrete> arretes = new ArrayList<>();
            double poidRoute = 0;
            for (int j = 0; j < clientsRoute.size(); j++) {
                poidRoute += clientsRoute.get(j).getQuantite();
                if (poidRoute > 100) {
                    poidRoute -= clientsRoute.get(j).getQuantite();
                    clientsSansRoute.add(clientsRoute.get(j));
                } else {
                    //si premier on ajoute depot -- premier liste
                    //sinon on ajoute j-1 -- j
                    //possible autre cas
                    if (j == 0) {
                        arretes.add(new Arrete(depot, clientsRoute.get(j)));
                    } else {
                        arretes.add(new Arrete(clientsRoute.get(j - 1), clientsRoute.get(j)));
                    }
                }
            }
            routes.add(new Route(i, arretes));
        }
        if (!clientsSansRoute.isEmpty()) {
            List<Client> clients1 = new ArrayList<>(clientsSansRoute);
            List<Route> finalRoutes = routes;
            clientsSansRoute.forEach(client -> {
                AtomicBoolean check = new AtomicBoolean(false);
                finalRoutes.forEach(route -> {
                    if (route.getChargeTotal() + client.getQuantite() <= 100 && !check.get()) {
                        route.addArrete(new Arrete(route.getArretesById(route.getArretes().size() - 1).getClientFinal(), client));
                        check.set(true);
                        clients1.remove(client);
                    }
                });
            });
            clientsSansRoute = clients1;
            while (!clientsSansRoute.isEmpty()) {
                int poidRoute = 0;
                List<Arrete> arretes = new ArrayList<>();
                for (int j = 0; j < clientsSansRoute.size(); j++) {
                    poidRoute += clientsSansRoute.get(j).getQuantite();
                    if (poidRoute > 100) {
                        poidRoute -= clientsSansRoute.get(j).getQuantite();
                    } else {
                        //si premier on ajoute depot -- premier liste
                        //sinon on ajoute j-1 -- j
                        //possible autre cas
                        if (j == 0) {
                            arretes.add(new Arrete(depot, clientsSansRoute.get(j)));
                        } else {
                            arretes.add(new Arrete(clientsSansRoute.get(j - 1), clientsSansRoute.get(j)));
                        }
                        clientsSansRoute.remove(j);
                    }
                }
            }
        }
        List<Route> copyRoutes = new ArrayList<>(routes);
        routes.forEach(route -> {
            if (route.getArretes().size()<1){
                copyRoutes.remove(route);
            }
        });
        routes=copyRoutes;
        routes.forEach(route -> {
            if (route.getArretes().size() <1){
                System.out.println(route);
            }
            route.addArrete(new Arrete(route.getArretesById(route.getArretes().size() - 1).getClientFinal(), depot));});
        return new Solution(routes);
    }
}
