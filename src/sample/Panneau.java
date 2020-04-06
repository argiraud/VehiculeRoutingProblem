package sample;

import metavoisinage.Arrete;
import metavoisinage.Client;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Panneau  extends JPanel {

    private static ArrayList<Client> test = new ArrayList<>();
    private static List<Route> routes = new ArrayList<>();

    public static List<Route> getRoutes() {
        return routes;
    }

    public static void setRoutes(List<Route> routes) {
        Panneau.routes = routes;
    }

    public void dataFileToCLientList(String filename) throws IOException {
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\n");
        while (sc.hasNext()) {
            String[] lines = sc.next().split(";");
            List<Integer> attributs = new ArrayList<>();
            for (String line : lines) {
                attributs.add(Integer.parseInt(line.trim()));
            }
            getTest().add(new Client(attributs.get(0), attributs.get(1), attributs.get(2), attributs.get(3)));
        }
    }

    public static ArrayList<Client> getTest() {
        return test;
    }

    public static void setTest(ArrayList<Client> test) {
        Panneau.test = test;
    }

    public void routesCreation(int chargeMax) throws IOException {
        ArrayList<Client> copyClient = (ArrayList<Client>) test.clone();
        Random r = new Random();
        Client depot = copyClient.get(0);
        copyClient.remove(0);
        while (copyClient.size() > 1) {
            int chargeActuelle = 0;
            Route route = new Route(new ArrayList<>());
            int i = r.nextInt(copyClient.size());
            Client clientActuel = copyClient.get(i);
            chargeActuelle = chargeActuelle + clientActuel.getQuantite();
            route.addArrete(new Arrete(depot, clientActuel));
            copyClient.remove(i);
            while (chargeActuelle < chargeMax && copyClient.size() > 1) {
                i = r.nextInt(copyClient.size());
                route.addArrete(new Arrete(clientActuel, copyClient.get(i)));
                clientActuel = copyClient.get(i);
                chargeActuelle = chargeActuelle + clientActuel.getQuantite();
                copyClient.remove(i);
            }
            route.addArrete(new Arrete(clientActuel, depot));
            routes.add(route);
        }
    }

    private static double getDistance(Client client1, Client client2) {
        return Math.sqrt(Math.pow((client2.getX() - client1.getX()), 2) + Math.pow((client2.getY() - client1.getY()), 2));
    }

    public void paintComponent(Graphics g) {
        for(int i = 0; i < getTest().size() - 1; i++)
        {
            Client clientActuel = getTest().get(i);
            //Vous verrez cette phrase chaque fois que la méthode sera invoquée
            g.drawString(clientActuel.getQuantite().toString(), clientActuel.getX(), clientActuel.getY() + 5);
            g.setColor(Color.black);
            g.fillOval(clientActuel.getX() + this.getWidth()/2,  clientActuel.getY() + this.getHeight()/2, 10, 10);
        }

        for(int j= 0; j < getRoutes().size() - 1; j++)
        {
            Route routeActuel = getRoutes().get(j);
            for (int k = 0; k < routeActuel.getArretes().size() - 1; k++)
            {
                Arrete cheminActuel = routeActuel.getArretesById(k);
                System.out.println("Je suis exécutée !");
                g.setColor(Color.green);
                g.drawLine(cheminActuel.getClientInitial().getX(), cheminActuel.getClientInitial().getY(), cheminActuel.getClientFinal().getX(), cheminActuel.getClientFinal().getY());
                //g.drawString(cheminActuel.valueOf(cheminActuel.getDistance()), cheminActuel.getClientInitial().getX() + cheminActuel.getClientFinal().getX() / 2, cheminActuel.getClientInitial().getY() + cheminActuel.getClientFinal().getY() / 2 + 6);
                }

            }


        }
    }

