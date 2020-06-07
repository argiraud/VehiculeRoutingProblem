package metapop;

import metavoisinage.Arrete;
import metavoisinage.Client;
import metavoisinage.Route;
import metavoisinage.Solution;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GeneticAlgorithm {

    Random r;

    public GeneticAlgorithm() {
        r = new Random();
    }

    public Solution executeGeneticAlgorithm(Solution solution, int nbSol, double mutPercent) {
        double probaCross = 0.7;
        List<Solution> solutions = generateXSolutions(solution, nbSol);
        for (int i = 0; i <= 10000; i++) {
            solutions = Reproduction.getSelectedSolutions(solutions);
            double j = r.nextDouble();
            List<Map<Client, Integer>> mapToBuild;
            if (j < probaCross) {
                //croisement
                mapToBuild = Croisement.crossSolutions(solutions);
            } else {
                //mutation
                mapToBuild = Mutation.Mutation(solutions, mutPercent);
            }
            solutions.clear();
            List<Solution> finalSolutions = new ArrayList<>();
            mapToBuild.forEach(clientIntegerMap -> {
                List<Integer> nbRoutes = new ArrayList<>();
                clientIntegerMap.forEach((key, value) -> {
                    if (!nbRoutes.contains(value)) {
                        nbRoutes.add(value);
                    }
                });
                finalSolutions.add(rebuild(clientIntegerMap, nbRoutes));
            });
            solutions.addAll(finalSolutions);
        }
        solution = solutions.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                .orElseThrow(NoSuchElementException::new);
        return solution;
    }

    public Solution executeGeneticAlgorithmForTest(Solution solution, int nbSol, double mutPercent, String dataName) {
        String fileContent = "Data; nombre client; nb generation; taille population; taux mutation; fin cout; cout dep; temps\r\n";
        double probaCross = 0.7;
        double coutDep = solution.getDistanceTotal();
        int nbGen = 0;
        Solution bestsolution = new Solution(solution);
        for (int nbIter = 0; nbIter < 10; nbIter++) {
            System.out.println(nbIter);

            long startTime = System.nanoTime();
            List<Solution> solutions = generateXSolutions(solution, 100);
            for (int i = 0; i <= 10000; i++) {
                nbGen = i;
                solutions = Reproduction.getSelectedSolutions(solutions);
                double j = r.nextDouble();
                List<Map<Client, Integer>> mapToBuild = new ArrayList<>();
                if (j < probaCross) {
                    //croisement
                    mapToBuild = Croisement.crossSolutions(solutions);
                } else {
                    //mutation
                    mapToBuild = Mutation.Mutation(solutions, 0.15);
                }
                solutions.clear();
                List<Solution> finalSolutions = new ArrayList<>();
                mapToBuild.forEach(clientIntegerMap -> {
                    List<Integer> nbRoutes = new ArrayList<>();
                    clientIntegerMap.forEach((key, value) -> {
                        if (!nbRoutes.contains(value)) {
                            nbRoutes.add(value);
                        }
                    });
                    finalSolutions.add(rebuild(clientIntegerMap, nbRoutes));
                });
                solutions.addAll(finalSolutions);
            }
            bestsolution = solutions.stream().min(Comparator.comparing(Solution::getDistanceTotal))
                    .orElseThrow(NoSuchElementException::new);
            long stopTime = System.nanoTime();
            double executionTime = (stopTime - startTime) / 1_000_000_000.0;
            double muta = 0.1;
            fileContent += dataName + "; " + bestsolution.getAllClients().size() + "; " + nbGen + "; " + 100 + "; " + 0.15 + ";" + (int) bestsolution.getDistanceTotal() + ";" + (int) coutDep + ";" + executionTime + "\r\n";
        }
        writeGenResult(fileContent, dataName);
        return bestsolution;
    }

    private List<Solution> generateXSolutions(Solution solution, Integer nbPop) {
        List<Solution> sol = new ArrayList<>();
        sol.add(solution);

        for (int i = 0; i < nbPop - 1; i++) {
            Comparator<Client> compareById = (Client o1, Client o2) -> o1.getId().compareTo(o2.getId());
            List<Client> clientList = solution.getAllClients();
            clientList.sort(compareById);
            sol.add(routesCreation(solution.getAllClients()));
        }

        return sol;
    }

    private Solution rebuild(Map<Client, Integer> clients, List<Integer> nbRoute) {
        List<Route> routes = new ArrayList<>();
        Client depot = clients.entrySet().stream().filter(clientIntegerEntry -> clientIntegerEntry.getKey().getId() == 0).findFirst().get().getKey();
        clients.remove(depot);
        List<Client> clientsSansRoute = new ArrayList<>();
        for (Integer i : nbRoute) {
            int finalI = i;
            List<Client> clientsRoute = new ArrayList<>();
            clients.entrySet().stream().filter(clientIntegerEntry -> clientIntegerEntry.getValue() == finalI).forEach(
                    c -> clientsRoute.add(c.getKey())
            );
            //tant que la somme des qtt est > à 100 on en vire un et on le mets dans clientsSansRoute
            Double sommeTot = sommeTotQttListClient(clientsRoute);
            while (sommeTot > 100) {
                clientsSansRoute.add(clientsRoute.get(clientsRoute.size() - 1));
                clientsRoute.remove(clientsSansRoute.get(clientsSansRoute.size() - 1));
                sommeTot = sommeTotQttListClient(clientsRoute);
            }
            //Après on construit la route avec la liste des clients dans l'ordre cool
            routes.add(buildRouteWithoutDepotAtTheEnd(getClientOrdreDistance(clientsRoute, depot), i));
        }
        List<Route> copyRoutes = new ArrayList<>(routes);
        routes.forEach(route -> {
            if (route.getArretes().size() < 1) {
                copyRoutes.remove(route);
            }
        });
        routes = copyRoutes;
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
            if (!clientsSansRoute.isEmpty()) {
                routes.add(buildRouteWithoutDepotAtTheEnd(getClientOrdreDistance(clientsSansRoute, depot), routes.size()));
            }
        }
        routes.forEach(route -> {
            route.addArrete(new Arrete(route.getArretesById(route.getArretes().size() - 1).getClientFinal(), depot));
        });
        return new Solution(routes);
    }

    private List<Client> getClientOrdreDistance(List<Client> clients, Client depot) {
        List<Client> clientListOrdre = new ArrayList<>();
        clientListOrdre.add(depot);
        for (int i = 0; i < clients.size(); i++) {
            Double distanceMin[] = new Double[1];
            Integer idClientSelected[] = new Integer[1];
            distanceMin[0] = Double.MAX_VALUE;
            clients.stream().filter(client -> !clientListOrdre.contains(client)).forEach(client -> {
                        double distanceCalc = Math.sqrt(Math.pow(
                                (client.getX() - clientListOrdre.get(clientListOrdre.size() - 1).getX()), 2)
                                + Math.pow((client.getY() - clientListOrdre.get(clientListOrdre.size() - 1).getY()), 2));
                        if (distanceCalc < distanceMin[0]) {
                            distanceMin[0] = distanceCalc;
                            idClientSelected[0] = clients.indexOf(client);
                        }
                    }
            );
            clientListOrdre.add(clients.get(idClientSelected[0]));
        }
        return clientListOrdre;
    }

    private Double sommeTotQttListClient(List<Client> clients) {
        Double qttTot = 0d;
        for (int i = 0; i < clients.size(); i++) {
            qttTot += clients.get(i).getQuantite();
        }
        return qttTot;
    }

    private Route buildRouteWithoutDepotAtTheEnd(List<Client> clients, Integer idRoute) {
        List<Arrete> arretes = new ArrayList<>();
        for (int i = 0; i < clients.size() - 1; i++) {
            arretes.add(new Arrete(clients.get(i), clients.get(i + 1)));
        }
        return new Route(idRoute, arretes);
    }

    private Solution routesCreation(List<Client> clients) {
        Integer nbClient = clients.size();
        Comparator<Client> compareById = (Client o1, Client o2) -> o1.getId().compareTo(o2.getId());
        clients.sort(compareById);
        Solution solution = new Solution(new ArrayList<>());
        int nbVille = r.nextInt(4) + 3;
        Client depot = clients.get(0);
        clients.remove(0);
        int nbRoute = 0;
        while (!clients.isEmpty()) {
            int chargeActuelle = 0;
            Route route = new Route(nbRoute, new ArrayList<>());
            int i = r.nextInt(clients.size());
            Client clientActuel = clients.get(i);
            chargeActuelle = chargeActuelle + clientActuel.getQuantite();
            route.addArrete(new Arrete(depot, clientActuel));
            clients.remove(i);
            while (chargeActuelle < 100 && !clients.isEmpty() && route.getArretes().size() < nbVille) {
                i = r.nextInt(clients.size());
                if (chargeActuelle + clients.get(i).getQuantite() <= 100) {
                    route.addArrete(new Arrete(clientActuel, clients.get(i)));
                    clientActuel = clients.get(i);
                    chargeActuelle = chargeActuelle + clientActuel.getQuantite();
                    clients.remove(i);
                } else {
                    break;
                }
            }
            route.addArrete(new Arrete(clientActuel, depot));
            solution.getRoutes().add(route);
            nbRoute++;
        }
        return solution;
    }


    public void writeGenResult(String s, String dataName) {

        try {
            PrintWriter writer = new PrintWriter("GenResults/" + dataName + "test.csv");
            writer.println(s);
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
