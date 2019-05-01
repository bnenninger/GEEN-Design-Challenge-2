package dataTest;

import java.util.Random;

import application.DataDisplay;
import application.DataSource;

public class RandomDataGenerator extends DataSource implements Runnable{

	public static final int TIME_DELAY = 1000;//ms time delay
	
	private Random r;
	private int maxIncrease;
	private long previousValue;

	public RandomDataGenerator(DataDisplay display, int maxIncrease) {
		super(display);
		r = new Random();
		this.maxIncrease = maxIncrease;
		previousValue = 0;
	}

	public long getData() {
		previousValue += r.nextInt(maxIncrease);
		return previousValue;
	}
	
	@Override
	public void run() {
		while (true) {
			super.add(DataDisplay.getCurrentTime(), getData());
			try {
				Thread.sleep(TIME_DELAY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
