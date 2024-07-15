/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pz02;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author User
 */

public class Krugovi extends Application {
    private Pane pane = new Pane();

    @Override
    public void start(Stage primaryStage) {
        Circle[] circles = createCircles();
        connectCircles(circles);

        Scene scene = new Scene(pane, 400, 400);
        primaryStage.setTitle("Krugovi");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Circle[] createCircles() {
        Circle[] circles = new Circle[16];
        double radius = 15;

        for (int i = 0; i < 16; i++) {
            circles[i] = new Circle(radius);
            circles[i].setStroke(Color.BLACK);
            pane.getChildren().add(circles[i]);
        }

        circles[0].setCenterX(200);
        circles[0].setCenterY(200);

        for (int i = 1; i < 8; i++) {
            circles[i].setCenterX(200 + (i * 20));
            circles[i].setCenterY(200);
        }

        for (int i = 8; i < 15; i++) {
            circles[i].setCenterX(200 + ((i - 7) * 20));
            circles[i].setCenterY(240);
        }

        circles[15].setCenterX(200);
        circles[15].setCenterY(280);

        return circles;
    }

    private void connectCircles(Circle[] circles) {
        List<Set<Circle>> connectedSets = new ArrayList<>();

        for (Circle circle : circles) {
            boolean isConnected = false;

            for (Set<Circle> connectedSet : connectedSets) {
                if (isConnectedToSet(circle, connectedSet)) {
                    connectedSet.add(circle);
                    isConnected = true;
                    break;
                }
            }

            if (!isConnected) {
                Set<Circle> newSet = new HashSet<>();
                newSet.add(circle);
                connectedSets.add(newSet);
            }
        }

        Random random = new Random();

        for (Set<Circle> connectedSet : connectedSets) {
            Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));

            for (Circle circle : connectedSet) {
                circle.setFill(color);
            }
        }
    }

    private boolean isConnectedToSet(Circle circle, Set<Circle> connectedSet) {
        for (Circle connectedCircle : connectedSet) {
            if (isConnected(circle, connectedCircle)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConnected(Circle c1, Circle c2) {
        double distance = Math.sqrt(Math.pow(c2.getCenterX() - c1.getCenterX(), 2) +
                Math.pow(c2.getCenterY() - c1.getCenterY(), 2));
        double radiusSum = c1.getRadius() + c2.getRadius();
        return distance <= radiusSum;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


