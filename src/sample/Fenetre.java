package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.Collection;

public class Fenetre extends JFrame {
    public Fenetre() throws IOException {

        this.setTitle("Ma première fenêtre Java");

        this.setSize(800, 800);

        this.setLocationRelativeTo(null);


        //Instanciation d'un objet JPanel

        JPanel pan = new JPanel();


        //Définition de sa couleur de fond

        pan.setBackground(Color.GRAY);

        //On prévient notre JFrame que notre JPanel sera son content pane
        Panneau test = new Panneau();
        test.dataFileToCLientList("Ressources/data01.txt");
        test.routesCreation(100);


        this.setContentPane(test);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setVisible(true);

        setResizable(false);

    }

    public void mouseReleased(MouseEvent e) {
        e.getX();//Location of the mouse on the X axis
    }





}
