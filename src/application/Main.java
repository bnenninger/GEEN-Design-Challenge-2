package application;

import dataTest.RandomDataGenerator;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static final boolean DEBUG_MODE = false;

	@Override
	public void start(Stage primaryStage) {
		try {
			DataDisplay display = DataDisplay.load(primaryStage);
			DataSource source = null;
			if (DEBUG_MODE) {
				source = new RandomDataGenerator(display, 100);
				Thread generator = new Thread((RandomDataGenerator) source);
				generator.start();
			}
			else {
				source = new ArduinoSerialCommunicator(display);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		launch(args);
	}
}
