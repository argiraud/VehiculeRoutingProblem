package sample;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //List<Client> clients = dataFileToCLientList("Ressources/data01.txt");
        ArrayList<Client> client2 = dataFileToCLientList("Ressources/data01.txt");
        List<Route> routes = routesCreation(client2, 100);
        System.out.println("test");
    }

    private static ArrayList<Client> dataFileToCLientList(String filename) throws IOException {
        ArrayList<Client> clients = new ArrayList<>();
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\n");
        while (sc.hasNext()) {
            String[] lines = sc.next().split(";");
            List<Integer> attributs = new ArrayList<>();
            for (String line : lines) {
                attributs.add(Integer.parseInt(line.trim()));
            }
            clients.add(new Client(attributs.get(0), attributs.get(1), attributs.get(2), attributs.get(3)));
        }
        return clients;
    }

    private static List<Route> routesCreation(ArrayList<Client> clients, int chargeMax) throws IOException {
        List<Route> routes = new ArrayList<>();
        Random r = new Random();
        Client depot = clients.get(0);
        clients.remove(0);
        while (clients.size() > 1) {
            int chargeActuelle = 0;
            Route route = new Route(new ArrayList<>());
            int i = r.nextInt(clients.size());
            Client clientActuel = clients.get(i);
            chargeActuelle = chargeActuelle + clientActuel.getQuantite();
            route.addArrete(new Arrete(depot, clientActuel, getDistance(depot, clientActuel)));
            clients.remove(i);
            while (chargeActuelle < chargeMax && clients.size() > 1) {
                i = r.nextInt(clients.size());
                route.addArrete(new Arrete(clientActuel, clients.get(i), getDistance(clientActuel, clients.get(i))));
                clientActuel = clients.get(i);
                chargeActuelle = chargeActuelle + clientActuel.getQuantite();
                clients.remove(i);
            }
            route.addArrete(new Arrete(clientActuel, depot, getDistance(clientActuel, depot)));
            routes.add(route);
            System.out.println(route.toString());
        }
        return routes;
    }

    private static double getDistance(Client client1, Client client2) {
        return Math.sqrt(Math.pow((client2.getX() - client1.getX()), 2) + Math.pow((client2.getY() - client1.getY()), 2));
    }
}

