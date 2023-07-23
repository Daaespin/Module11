package cen3024;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
    @Override
    public void start(Stage primaryStage) {
        TextArea ta = new TextArea();
        Scene scene = new Scene(new ScrollPane(ta), 500, 250);
        primaryStage.setTitle("Server"); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() ->
                        ta.appendText("Server started at " + new Date() + '\n'));

                while (true) {
                    Socket socket = serverSocket.accept();
                    Platform.runLater(() -> ta.appendText("Connected to a client.\n"));
                    DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
                    new Thread(() -> {
                        try {
                            while (true) {
                                int number = inputFromClient.readInt();
                                Platform.runLater(() -> {
                                    ta.appendText("Received number from client: " + number + "\n");
                                });
                                boolean isPrime = Prime(number);
                                outputToClient.writeBoolean(isPrime);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    private boolean Prime(int number) {
        if (number <= 1)
            return false;
        if (number <= 3)
            return true;
        if (number % 2 == 0 || number % 3 == 0)
            return false;
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0)
                return false;
        }
        return true;
    }
    public static void main(String[] args) {
        launch(args);
    }
}

