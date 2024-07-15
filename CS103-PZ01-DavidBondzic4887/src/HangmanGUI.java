import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static javafx.application.Application.launch;

public class HangmanGUI extends Application {
    private List<String> wordList = Arrays.asList("metropolitan", "projekat", "programiranje");
    private String currentWord;
    private List<Character> guessedLetters;
    private int remainingAttempts;
    private Canvas canvas;
    private GraphicsContext gc;
    private TextField letterInput;
    private Label wordLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hangman");

        BorderPane root = new BorderPane();

        canvas = new Canvas(300, 300);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        HBox inputBox = new HBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);

        Label promptLabel = new Label("Ukucaj slovo:");
        letterInput = new TextField();
        Button submitButton = new Button("Pogodi");
        submitButton.setOnAction(e -> checkLetter());

        inputBox.getChildren().addAll(promptLabel, letterInput, submitButton);
        root.setBottom(inputBox);

        wordLabel = new Label();
        wordLabel.setAlignment(Pos.CENTER);
        wordLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.setTop(wordLabel);

        newGame();

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void newGame() {
        Random random = new Random();
        currentWord = wordList.get(random.nextInt(wordList.size()));
        guessedLetters = new ArrayList<>();
        remainingAttempts = 7;
        updateWordLabel();
        clearCanvas();
    }

    private void checkLetter() {
        String input = letterInput.getText().toLowerCase();
        letterInput.clear();

        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            showWarning("Nepravilan unos", "Ukucaj jedno slovo");
            return;
        }

        char letter = input.charAt(0);

        if (guessedLetters.contains(letter)) {
            showMessage("Slovo je vec probano", "Slovo vec pogodjeno");
            return;
        }

        guessedLetters.add(letter);
        updateWordLabel();

        if (currentWord.indexOf(letter) == -1) {
            remainingAttempts--;
            drawHangman();
        }

        if (remainingAttempts == 0) {
            showInfo("Kraj igre", "Izgubili ste, rec je bila: " + currentWord);
            newGame();
        } else if (checkWordComplete()) {
            showInfo("Rec pogodjena", "Cestitamo! Pogodili ste rec: " + currentWord);
            newGame();
        }
    }

    private void updateWordLabel() {
        StringBuilder displayedWord = new StringBuilder();
        for (char letter : currentWord.toCharArray()) {
            if (guessedLetters.contains(letter)) {
                displayedWord.append(letter).append(" ");
            } else {
                displayedWord.append("_ ");
            }
        }
        wordLabel.setText(displayedWord.toString());
    }

    private boolean checkWordComplete() {
        for (char letter : currentWord.toCharArray()) {
            if (!guessedLetters.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    private void drawHangman() {
        clearCanvas();

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0);

        if (remainingAttempts < 7) {
            // Draw gallows
            gc.strokeLine(20, 280, 120, 280);
            gc.strokeLine(70, 280, 70, 20);
            gc.strokeLine(70, 20, 180, 20);
            gc.strokeLine(180, 20, 180, 50);
        }

        if (remainingAttempts < 6) {
            // Draw head
            gc.strokeOval(160, 50, 40, 40);
        }

        if (remainingAttempts < 5) {
            // Draw body
            gc.strokeLine(180, 90, 180, 200);
        }

        if (remainingAttempts < 4) {
            // Draw left arm
            gc.strokeLine(180, 120, 140, 160);
        }

        if (remainingAttempts < 3) {
            // Draw right arm
            gc.strokeLine(180, 120, 220, 160);
        }

        if (remainingAttempts < 2) {
            // Draw left leg
            gc.strokeLine(180, 200, 140, 240);
        }

        if (remainingAttempts < 1) {
            // Draw right leg
            gc.strokeLine(180, 200, 220, 240);
        }
    }

    private void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
