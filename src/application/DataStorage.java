
package application;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 * Interface for an object that stores data that is associated with time.
 * 
 * @author Brendan Nenninger
 *
 */
public interface DataStorage {
	/**
	 * The time that the program started, in seconds
	 */
	// int should be sufficient, goes up to ~60 years
	public static final int PROGRAM_START_TIME = (int) (System.currentTimeMillis() / 1000);

	/**
	 * Adds a data point to the DataStorage object.
	 * 
	 * @param seconds   the time of the data point, in seconds
	 * @param dataPoint the data point
	 */
	public void put(int seconds, long dataPoint);

	/**
	 * Adds a data point to the DataStorage object, labeled at the current time.
	 * 
	 * @param dataPoint the data point
	 */
	public default void put(long dataPoint) {
		put((int) (System.currentTimeMillis() / 1000) - PROGRAM_START_TIME, dataPoint);
	}

	
}
