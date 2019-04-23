package application;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

public class XYChartSeriesDataStorage implements DataStorage {

	private XYChart.Series<Integer, Long> series;
	
	public XYChartSeriesDataStorage() {
		series = new XYChart.Series<>();
	}
	
	@Override
	public void put(int seconds, long dataPoint) {
		series.getData().add(new XYChart.Data<>(seconds, dataPoint));
	}

	public XYChart.Series<Integer, Long> getSeries(){
		return series;
	}
	
	public String toString() {
		String outputString = "";
		ObservableList<Data<Integer, Long>> data = series.getData();
		for(XYChart.Data<Integer, Long> point: data) {
			outputString += point.toString();
		}
		return outputString;
	}
}
