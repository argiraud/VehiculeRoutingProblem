package affichage;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    MenuButton opvoisbtn;

    @FXML
    Button charger;

    @FXML
    Label nbVehicule;

    @FXML
    Label distance;

    @FXML
    javafx.scene.canvas.Canvas canvas;

    @FXML
    Slider temperatureSlid;

    @FXML
    TextField nbVoisinsField;

    @FXML
    TextField nbExecutionField;

    @FXML
    Label temperatureLabel;

    @FXML
    Label nbVoisinsLabel;

    @FXML
    Label nbExecutionLabel;

    @FXML
    MenuItem tabou;

    @FXML
    MenuItem recuit;

    @FXML
    Label tpsExecution;

    Solution routes;
    List<Color> colors;

    public static final Integer CHARGE_MAX = 100;
    Integer temperature = 100;
    Random rand;
    Integer nbVoisins = 10000;
    Integer nbExecutions = 100000;
    Integer tailleList = 31;

    @FXML
    public void initialize() throws IOException {
        rand = new Random();
        File repertoire = new File("./Ressources");
        String[] files = repertoire.list();
        List<String> filesList = new ArrayList<>();
        for (String file : files) {
            filesList.add(file);
        }
        temperatureSlid.setMin(100);
        temperatureSlid.setMax(1000);
        temperatureSlid.setValue(100);
        temperatureSlid.setShowTickLabels(true);
        temperatureSlid.setShowTickMarks(true);
        temperatureSlid.setMajorTickUnit(100);
        temperatureSlid.setMinorTickCount(100);
        temperatureSlid.setBlockIncrement(100);
        ObservableList<String> filesObs = FXCollections.observableArrayList();
        filesObs.addAll(filesList);
        fichiers.setItems(filesObs);
        nbVoisinsField.textProperty().addListener((v, oldValue, newValue) -> {
            try {
                nbVoisins = Integer.valueOf(newValue);
            } catch (Exception e) {
                nbVoisins = Integer.valueOf(oldValue);
            }
        });
        nbExecutionField.textProperty().addListener((v, oldValue, newValue) -> {
            try {
                nbExecutions = Integer.valueOf(newValue);
            } catch (Exception e) {
                nbExecutions = Integer.valueOf(oldValue);
            }
        });
        temperatureSlid.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                temperature = new_val.intValue();
            }
        });
        nbVoisinsField.setText(nbVoisins.toString());
        nbExecutionField.setText(nbExecutions.toString());
        algobtn.getItems().forEach(i -> i.setOnAction(a -> algobtn.setText(i.getText())));
        opvoisbtn.getItems().forEach(i -> i.setOnAction(a -> opvoisbtn.setText(i.getText())));
        routes = routesCreation(dataFileToCLientList("Ressources/A3205.txt"), CHARGE_MAX);
        distance.setText("Distance Total: " + String.valueOf(Math.round(routes.getDistanceTotal())));
        nbVehicule.setText("Nombre de véhicule: " + String.valueOf(Math.round(routes.getRoutes().size())));

        generateDraw(routes);

        tabou.setOnAction(event -> {
            algobtn.setText(tabou.getText());
            temperatureSlid.setVisible(false);
            temperatureLabel.setVisible(false);
            nbExecutionField.setVisible(true);
            nbExecutionLabel.setVisible(true);
            nbVoisinsField.setVisible(true);
            nbVoisinsLabel.setVisible(true);
        });

        recuit.setOnAction(event -> {
            algobtn.setText(recuit.getText());
            temperatureSlid.setVisible(true);
            temperatureLabel.setVisible(true);
            nbExecutionField.setVisible(false);
            nbExecutionLabel.setVisible(false);
            nbVoisinsLabel.setVisible(false);
            nbVoisinsField.setVisible(false);
        });
    }

    public void executer() {
        tpsExecution.setText("Tps Execution: Execution en cours...");
        long startTime = System.nanoTime();
        switch (algobtn.getText()) {
            case "Tabou":
                Tabou tabou = new Tabou();
                switch (opvoisbtn.getText()) {
                    case "Cross Exchange between route":
                        routes = tabou.methodeTabou(routes, CHARGE_MAX, 0, nbVoisins, nbExecutions, tailleList);
                        break;
                    case "Exchange Operator":
                        routes = tabou.methodeTabou(routes, CHARGE_MAX, 1, nbVoisins, nbExecutions, tailleList);
                        break;
                    case "Cross Exchange inside route":
                        routes = tabou.methodeTabou(routes, CHARGE_MAX, 2, nbVoisins, nbExecutions, tailleList);
                        break;
                    case "Inverse Points Arretes":
                        routes = tabou.methodeTabou(routes, CHARGE_MAX, 3, nbVoisins, nbExecutions, tailleList);
                        break;
                    case "Enlever un point":
                        routes = tabou.methodeTabou(routes, CHARGE_MAX, 4, nbVoisins, nbExecutions, tailleList);
                        break;
                    default:
                        break;
                }

                break;
            case "Recuit":
                RecuitSimule rs = new RecuitSimule();
                switch (opvoisbtn.getText()) {
                    case "Cross Exchange between route":
                        routes = rs.MethodeRecuit(routes, temperature, CHARGE_MAX, 0);
                        break;
                    case "Exchange Operator":
                        routes = rs.MethodeRecuit(routes, temperature, CHARGE_MAX, 1);
                        break;
                    case "Cross Exchange inside route":
                        routes = rs.MethodeRecuit(routes, temperature, CHARGE_MAX, 2);
                        break;
                    case "Inverse Points Arretes":
                        routes = rs.MethodeRecuit(routes, temperature, CHARGE_MAX, 3);
                        break;
                    case "Enlever un point":
                        routes = rs.MethodeRecuit(routes, temperature, CHARGE_MAX, 4);
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;

        }

        long stopTime = System.nanoTime();
        double executionTime = (stopTime - startTime) / 1_000_000_000.0;
        if (executionTime > 60) {
            executionTime = executionTime / 60;
            tpsExecution.setText("Temps Execution: " + Math.round(executionTime * 100.0) / 100.0 + " min");

        } else {
            tpsExecution.setText("Temps Execution: " + Math.round(executionTime * 100.0) / 100.0 + " s");
        }
        String bip = "la-cucaracha-horn-sound-effect.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        eraseall();
        generateDraw(routes);
        distance.setText("Distance Total: " + String.valueOf(Math.round(routes.getDistanceTotal())));
        nbVehicule.setText("Nombre de véhicule: " + String.valueOf(Math.round(routes.getRoutes().size())));

    }

    public void charger() throws IOException {
        MultipleSelectionModel<String> selected = fichiers.getSelectionModel();
        routes = routesCreation(dataFileToCLientList("Ressources/" + selected.getSelectedItem()), CHARGE_MAX);
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

        solution.getRoutes().forEach(route -> {
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
                gc.setStroke(generateColor());
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
        int nbVille = r.nextInt(4) + 3;
        ;
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
            while (chargeActuelle < chargeMax && clients.size() > 1 && route.getArretes().size() < nbVille) {
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

    private Color generateColor() {
        double redValue = rand.nextFloat() / 2f + 0.5;
        double greenValue = rand.nextFloat() / 2f + 0.5;
        double blueValue = rand.nextFloat() / 2f + 0.5;

        return new Color(redValue, greenValue, blueValue, 1);
    }

    public void onExit() {
        Platform.exit();
    }
}

