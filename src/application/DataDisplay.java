package application;

import java.net.URL;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DataDisplay {

	public static final int MAX_DATA_POINTS = 60;
	public static final double VOLUME_PER_TICK = 1.0 / 330.0;

	@FXML
	private LineChart<String, Double> rateChart;
	@FXML
	private Label volumeLabel;

	public DataDisplay() {
	}

	public void setupChart() {
		if (rateChart.getData().isEmpty()) {
			rateChart.getData().add(new XYChart.Series<String, Double>());
		}
		rateChart.setAnimated(false);
		rateChart.setCreateSymbols(false);
	}

	public void add(int time, long ticks) {
		Double flowRate = tickInputToFlowRate(ticks);
		Platform.runLater(() -> {
			ObservableList<XYChart.Data<String, Double>> list = rateChart.getData().get(0).getData();
			if (list.size() >= MAX_DATA_POINTS) {
				list.remove(0);
			}
			list.add(new XYChart.Data<String, Double>(Integer.toString(time), flowRate));
			updateVolumeLabel(getVolume(ticks));
		});
	}

	private long prevTime = System.nanoTime();
	private long prevTicks = 0;

	/**
	 * 
	 * @param currentTicks
	 * @return the flow rate in liters per minute
	 */
	private double tickInputToFlowRate(long currentTicks) {
		long currentTime = System.nanoTime();
		double timeDifference = (currentTime - prevTime) * 1e-9;//converted to seconds
		long tickDifference = currentTicks - prevTicks;
		double flowRate = getVolume(tickDifference) / timeDifference * 60.0;
		prevTime = currentTime;
		prevTicks = currentTicks;
		return flowRate;
	}

	private double getVolume(long ticks) {
		return VOLUME_PER_TICK * ticks;
	}

	private void updateVolumeLabel(double liters) {
		volumeLabel.setText(String.format("Total volume: %.2f liters", liters));
	}

	public static DataDisplay load(Stage primaryStage) {
		FXMLLoader loader = null;
		try {
			URL filePath = DataDisplay.class.getResource("PrimaryWindow.fxml");
			loader = new FXMLLoader(filePath);
			DataDisplay display = new DataDisplay();
			loader.setController(display);
			Parent root = loader.load();
			display.setupChart();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			// makes whole program close when x pressed. Probably a janky solution.
			primaryStage.setOnCloseRequest(e -> System.exit(0));
			primaryStage.show();
			return display;
		} catch (Exception e) {
			System.out.println("PrimaryWindow.fxml could not be loaded");
			e.printStackTrace();
			return null;
		}
	}

	public static final long PROGRAM_START_TIME = System.currentTimeMillis();

	/**
	 * Returns the time the program has been running in seconds
	 * 
	 * @return program runtime in seconds
	 */
	public static int getCurrentTime() {
		long timeDifference = System.currentTimeMillis() - PROGRAM_START_TIME;
		return (int) (timeDifference / 1000);
	}
}
