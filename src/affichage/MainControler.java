package affichage;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import metavoisinage.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainControler {

    @FXML
    ListView<String> fichiers;

    @FXML
    MenuButton algobtn;

    @FXML
    Button charger;

    @FXML
    Label nbVehicule;

    @FXML
    Label distance;

    @FXML
    javafx.scene.canvas.Canvas canvas;

    @FXML
    TextField chargeMaxField;

    Solution routes;
    List<Color> colors;

    Integer chargeMax = 100;

    @FXML
    public void initialize() throws IOException {
        File repertoire = new File("./Ressources");
        String[] files = repertoire.list();
        List<String> filesList = new ArrayList<>();
        for (String file : files) {
            filesList.add(file);
        }
        chargeMaxField.setText("100");
        ObservableList<String> filesObs = FXCollections.observableArrayList();
        filesObs.addAll(filesList);
        fichiers.setItems(filesObs);
        chargeMaxField.textProperty().addListener((v, oldValue, newValue) -> {
            try {
                chargeMax = Integer.valueOf(newValue);
            } catch (Exception e) {
                chargeMax = Integer.valueOf(oldValue);
            }
            System.out.println(chargeMax);
        });

        algobtn.getItems().forEach(i -> i.setOnAction(a -> algobtn.setText(i.getText())));
        routes = routesCreation(dataFileToCLientList("Ressources/A3205.txt"), chargeMax);
        distance.setText("Distance Total: " + String.valueOf(Math.round(routes.getDistanceTotal())));
        nbVehicule.setText("Nombre de véhicule: " + String.valueOf(Math.round(routes.getRoutes().size())));

        generateDraw(routes);

    }

    public void executer() {
        switch (algobtn.getText()) {
            case "Tabou":
                Tabou tabou = new Tabou();
                routes = tabou.methodeTabou(routes, chargeMax);
                break;
            case "Recuit":
                RecuitSimule rs = new RecuitSimule();
                routes = rs.MethodeRecuit(routes, 100, chargeMax);
                break;
            default:
                break;
        }

        eraseall();
        generateDraw(routes);
        distance.setText("Distance Total: " + String.valueOf(Math.round(routes.getDistanceTotal())));
        nbVehicule.setText("Nombre de véhicule: " + String.valueOf(Math.round(routes.getRoutes().size())));

    }

    public void charger() throws IOException {
        MultipleSelectionModel<String> selected = fichiers.getSelectionModel();
        routes = routesCreation(dataFileToCLientList("Ressources/" + selected.getSelectedItem()), chargeMax);
        distance.setText("Distance Total: " + String.valueOf(Math.round(routes.getDistanceTotal())));
        nbVehicule.setText("Nombre de véhicule: " + String.valueOf(Math.round(routes.getRoutes().size())));
        eraseall();
        generateDraw(routes);
    }

    public void eraseall() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.beginPath();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, 500, 500);

    }

    private void generateDraw(Solution solution) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setLineWidth(1.0);
        colors = new ArrayList<>();
        colors.add(Color.BROWN);
        colors.add(Color.GREEN);
        colors.add(Color.ORANGE);
        colors.add(Color.VIOLET);
        colors.add(Color.BLACK);
        colors.add(Color.LIME);
        colors.add(Color.BROWN);
        colors.add(Color.GREEN);
        colors.add(Color.ORANGE);
        colors.add(Color.VIOLET);
        colors.add(Color.BLACK);
        colors.add(Color.LIME);
        colors.add(Color.BROWN);
        colors.add(Color.GREEN);
        colors.add(Color.ORANGE);
        colors.add(Color.VIOLET);
        colors.add(Color.BLACK);
        colors.add(Color.LIME);

        solution.getRoutes().forEach(route -> {
            System.out.println(route.getId());
            Color color = colors.get(route.getId());
            route.getArretes().forEach(a -> {
                gc.beginPath();
                gc.setFill(Color.BLUE);
                if (a.getClientInitial().getId() == 0) {
                    gc.setFill(Color.RED);
                    gc.fillOval(a.getClientInitial().getX() * 5, a.getClientInitial().getY() * 5, 10, 10);
                } else {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(a.getClientInitial().getX() * 5, a.getClientInitial().getY() * 5, 5, 5);
                }
                if (a.getClientFinal().getId() == 0) {
                    gc.setFill(Color.RED);
                    gc.fillOval(a.getClientFinal().getX() * 5, a.getClientFinal().getY() * 5, 10, 10);
                } else {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(a.getClientFinal().getX() * 5, a.getClientFinal().getY() * 5, 5, 5);
                }
                gc.setStroke(color);
                gc.moveTo(a.getClientInitial().getX() * 5, a.getClientInitial().getY() * 5);
                gc.lineTo(a.getClientFinal().getX() * 5, a.getClientFinal().getY() * 5);
                gc.stroke();
            });
        });

    }


    private static ArrayList<Client> dataFileToCLientList(String filename) throws IOException {
        ArrayList<Client> clients = new ArrayList<>();
        File file = new File(filename);
        try (Scanner sc = new Scanner(file)) {
            sc.useDelimiter("\n");
            while (sc.hasNext()) {
                String[] lines = sc.next().split(";");
                List<Integer> attributs = new ArrayList<>();
                for (String line : lines) {
                    attributs.add(Integer.parseInt(line.trim()));
                }
                clients.add(new Client(attributs.get(0), attributs.get(1), attributs.get(2), attributs.get(3)));
            }
        }
        return clients;
    }

    private static Solution routesCreation(ArrayList<Client> clients, int chargeMax) throws IOException {
        Solution routes = new Solution(new ArrayList<>());
        Random r = new Random();
        Client depot = clients.get(0);
        clients.remove(0);
        int nbRoute = 0;
        while (clients.size() > 1) {
            int chargeActuelle = 0;
            Route route = new Route(nbRoute, new ArrayList<>());
            int i = r.nextInt(clients.size());
            Client clientActuel = clients.get(i);
            chargeActuelle = chargeActuelle + clientActuel.getQuantite();
            route.addArrete(new Arrete(depot, clientActuel));
            clients.remove(i);
            while (chargeActuelle < chargeMax && clients.size() > 1) {
                i = r.nextInt(clients.size());
                if (chargeActuelle + clients.get(i).getQuantite() <= chargeMax) {
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
            nbRoute++;
        }
        return routes;
    }

    public void onExit() {
        Platform.exit();
    }
}

