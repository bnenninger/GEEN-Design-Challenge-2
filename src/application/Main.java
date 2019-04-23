package application;

import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
//		launch(args);
		ArrayList<Long> list = new ArrayList<>();
		XYChartSeriesDataStorage dataStorage = new XYChartSeriesDataStorage();
		ArduinoSerialCommunicator serialComm = new ArduinoSerialCommunicator(dataStorage);
		while (true) {
			Thread.sleep(1000);
			System.out.println(dataStorage);
		}
	}
}
