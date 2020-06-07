package affichage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import metapop.GeneticAlgorithm;
import metavoisinage.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    Label mutationLabel;

    @FXML
    javafx.scene.canvas.Canvas canvas;

    @FXML
    Slider temperatureSlid;

    @FXML
    Slider mutationSlid;

    @FXML
    TextField nbVoisinsField;

    @FXML
    TextField nbExecutionField;

    @FXML
    TextField nbSolField;

    @FXML
    Label temperatureLabel;

    @FXML
    Label nbVoisinsLabel;

    @FXML
    Label nbSolLabel;

    @FXML
    Label nbExecutionLabel;

    @FXML
    MenuItem tabouItem;

    @FXML
    MenuItem recuitItem;

    @FXML
    MenuItem geneticItem;

    @FXML
    Label tpsExecution;

    @FXML
    Label tailleListLabel;

    @FXML
    TextField tailleListField;

    Solution routes;

    public static final Integer CHARGE_MAX = 100;
    private static final String DISTANCE_TOTAL_MSG = "Distance Total: ";
    private static final String NOMBRE_VEHICULE_MSG = "Nombre de véhicule: ";

    Integer temperature = 100;
    Integer mutation = 0;
    Random rand;
    Integer nbVoisins = 1000;
    Integer nbExecutions = 100000;
    Integer tailleList = 31;
    Integer nbSol = 2;
    private String dataName;

    @FXML
    public void initialize() throws IOException {
        rand = new Random();
        File repertoire = new File("./Ressources");
        String[] files = repertoire.list();
        assert files != null;
        List<String> filesList = new ArrayList<>(Arrays.asList(files));
        temperatureSlid.setMin(100);
        temperatureSlid.setMax(1000);
        temperatureSlid.setValue(100);
        temperatureSlid.setShowTickLabels(true);
        temperatureSlid.setShowTickMarks(true);
        temperatureSlid.setMajorTickUnit(100);
        temperatureSlid.setMinorTickCount(100);
        temperatureSlid.setBlockIncrement(100);

        mutationSlid.setMin(0);
        mutationSlid.setMax(100);
        mutationSlid.setValue(0);
        mutationSlid.setShowTickLabels(true);
        mutationSlid.setShowTickMarks(true);
        mutationSlid.setMajorTickUnit(5);
        mutationSlid.setMinorTickCount(1);
        mutationSlid.setBlockIncrement(5);
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
        tailleListField.textProperty().addListener((v, oldValue, newValue) -> {
            try {
                tailleList = Integer.valueOf(newValue);
            } catch (Exception e) {
                tailleList = Integer.valueOf(oldValue);
            }
        });
        nbSolField.textProperty().addListener((v, oldValue, newValue) -> {
            try {
                nbSol = Integer.valueOf(newValue);
            } catch (Exception e) {
                nbSol = Integer.valueOf(oldValue);
            }
        });


        temperatureSlid.valueProperty().addListener((ov, oldVal, newVal) -> temperature = newVal.intValue());
        mutationSlid.valueProperty().addListener((ov, oldVal, newVal) -> mutation = newVal.intValue());
        nbVoisinsField.setText(nbVoisins.toString());
        nbExecutionField.setText(nbExecutions.toString());
        nbSolField.setText(nbSol.toString());
        tailleListField.setText(tailleList.toString());
        algobtn.getItems().forEach(i -> i.setOnAction(a -> algobtn.setText(i.getText())));
        opvoisbtn.getItems().forEach(i -> i.setOnAction(a -> opvoisbtn.setText(i.getText())));
        routes = routesCreation(dataFileToCLientList("Ressources/A3205.txt"));
        distance.setText(DISTANCE_TOTAL_MSG + Math.round(routes.getDistanceTotal()));
        nbVehicule.setText(NOMBRE_VEHICULE_MSG + Math.round(routes.getRoutes().size()));

        generateDraw(routes);

        tabouItem.setOnAction(event -> {
            algobtn.setText(tabouItem.getText());
            temperatureSlid.setVisible(false);
            temperatureLabel.setVisible(false);
            nbExecutionField.setVisible(true);
            nbExecutionLabel.setVisible(true);
            nbVoisinsField.setVisible(true);
            nbVoisinsLabel.setVisible(true);
            tailleListField.setVisible(true);
            tailleListLabel.setVisible(true);
            opvoisbtn.setVisible(true);
            nbSolLabel.setVisible(false);
            nbSolField.setVisible(false);
            mutationLabel.setVisible(false);
            mutationSlid.setVisible(false);
        });

        recuitItem.setOnAction(event -> {
            algobtn.setText(recuitItem.getText());
            temperatureSlid.setVisible(true);
            temperatureLabel.setVisible(true);
            nbExecutionField.setVisible(false);
            nbExecutionLabel.setVisible(false);
            nbVoisinsLabel.setVisible(false);
            nbVoisinsField.setVisible(false);
            tailleListLabel.setVisible(false);
            tailleListField.setVisible(false);
            opvoisbtn.setVisible(true);
            nbSolLabel.setVisible(false);
            nbSolField.setVisible(false);
            mutationLabel.setVisible(false);
            mutationSlid.setVisible(false);
        });

        geneticItem.setOnAction(event -> {
            algobtn.setText(geneticItem.getText());
            temperatureSlid.setVisible(false);
            temperatureLabel.setVisible(false);
            nbExecutionField.setVisible(false);
            nbExecutionLabel.setVisible(false);
            nbVoisinsLabel.setVisible(false);
            nbVoisinsField.setVisible(false);
            tailleListLabel.setVisible(false);
            tailleListField.setVisible(false);
            opvoisbtn.setVisible(false);
            nbSolLabel.setVisible(true);
            nbSolField.setVisible(true);
            mutationLabel.setVisible(true);
            mutationSlid.setVisible(true);
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
                    case "Tous les opérateurs":
                        routes = tabou.methodeTabou(routes, CHARGE_MAX, -1, nbVoisins, nbExecutions, tailleList);
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
                    case "Tous les opérateurs":
                        routes = rs.MethodeRecuit(routes, temperature, CHARGE_MAX, -1);
                        break;
                    default:
                        break;
                }
                break;
            case "Génétique":
                GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
                routes = geneticAlgorithm.executeGeneticAlgorithm(routes, nbSol, (mutation.doubleValue() / 100d));
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
        String bip = "musique-multiplex-canal.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        eraseall();
        generateDraw(routes);
        distance.setText(DISTANCE_TOTAL_MSG + Math.round(routes.getDistanceTotal()));
        nbVehicule.setText(NOMBRE_VEHICULE_MSG + Math.round(routes.getRoutes().size()));

    }

    public void charger() throws IOException {
        MultipleSelectionModel<String> selected = fichiers.getSelectionModel();
        routes = routesCreation(dataFileToCLientList("Ressources/" + selected.getSelectedItem()));
        distance.setText(DISTANCE_TOTAL_MSG + Math.round(routes.getDistanceTotal()));
        nbVehicule.setText(NOMBRE_VEHICULE_MSG + Math.round(routes.getRoutes().size()));
        eraseall();
        generateDraw(routes);
        dataName = selected.getSelectedItem().toString().substring(0, 5);
        System.out.println(dataName);
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
            Color color = generateColor();
            route.getArretes().forEach(a -> {
                gc.beginPath();
                gc.setFill(Color.BLUE);
                if (a.getClientInitial().getId() == 0) {
                    gc.setFill(Color.RED);
                    gc.fillOval(a.getClientInitial().getX() * 5.0, a.getClientInitial().getY() * 5.0, 10, 10);
                } else {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(a.getClientInitial().getX() * 5.0, a.getClientInitial().getY() * 5.0, 5, 5);
                }
                if (a.getClientFinal().getId() == 0) {
                    gc.setFill(Color.RED);
                    gc.fillOval(a.getClientFinal().getX() * 5.0, a.getClientFinal().getY() * 5.0, 10, 10);
                } else {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(a.getClientFinal().getX() * 5.0, a.getClientFinal().getY() * 5.0, 5, 5);
                }
                gc.setStroke(color);
                gc.moveTo(a.getClientInitial().getX() * 5.0, a.getClientInitial().getY() * 5.0);
                gc.lineTo(a.getClientFinal().getX() * 5.0, a.getClientFinal().getY() * 5.0);
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

    private Solution routesCreation(ArrayList<Client> clients) {
        Solution solution = new Solution(new ArrayList<>());
        int nbVille = rand.nextInt(4) + 3;
        Client depot = clients.get(0);
        clients.remove(0);
        int nbRoute = 0;
        while (!clients.isEmpty()) {
            int chargeActuelle = 0;
            Route route = new Route(nbRoute, new ArrayList<>());
            int i = rand.nextInt(clients.size());
            Client clientActuel = clients.get(i);
            chargeActuelle = chargeActuelle + clientActuel.getQuantite();
            route.addArrete(new Arrete(depot, clientActuel));
            clients.remove(i);
            while (chargeActuelle < CHARGE_MAX && !clients.isEmpty() && route.getArretes().size() < nbVille) {
                i = rand.nextInt(clients.size());
                if (chargeActuelle + clients.get(i).getQuantite() <= CHARGE_MAX) {
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
        Comparator<Client> compareById = (Client o1, Client o2) -> o1.getId().compareTo(o2.getId());
        List<Client> clientList = solution.getAllClients();
        clientList.sort(compareById);
        return solution;
    }

    private Color generateColor() {
        double redValue = rand.nextFloat();
        double greenValue = rand.nextFloat();
        double blueValue = rand.nextFloat() / 2f + 0.5;

        return new Color(redValue, greenValue, blueValue, 1);
    }


    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
}

