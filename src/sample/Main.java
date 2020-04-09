package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import metavoisinage.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Title");
        Group root = new Group();
        Scene scene = new Scene(root, 600, 330);


    }


    public static void main(String[] args) throws IOException {
        //Fenetre fen = new Fenetre();
        //Application.launch(args);
        //List<Client> clients = dataFileToCLientList("Ressources/data01.txt");
        ArrayList<Client> client2 = dataFileToCLientList("Ressources/A3205.txt");
        Solution routes = routesCreation(client2, 100);
        Solution routesInit = new Solution(routes);
        System.out.println(routesInit);
        Tabou tabou = new Tabou();
        RecuitSimule rs = new RecuitSimule();

        //Tabou
        //routes = tabou.methodeTabou(routes);

        //Recuit Simulé
        routes = rs.MethodeRecuit(routes, 100);
        //OperateurVoisinage.echangePointsBetweenRoutes(routes);
        System.out.println("final " + routes);
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

    private static Solution routesCreation(ArrayList<Client> clients, int chargeMax) throws IOException {
        Solution routes = new Solution(new ArrayList<>());
        Random r = new Random();
        Client depot = clients.get(0);
        clients.remove(0);
        while (clients.size() > 1) {
            int chargeActuelle = 0;
            Route route = new Route(new ArrayList<>());
            int i = r.nextInt(clients.size());
            Client clientActuel = clients.get(i);
            chargeActuelle = chargeActuelle + clientActuel.getQuantite();
            route.addArrete(new Arrete(depot, clientActuel));
            clients.remove(i);
            while (chargeActuelle < chargeMax && clients.size() > 1) {
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
            routes.getRoutes().add(route);
        }
        return routes;
    }


}
