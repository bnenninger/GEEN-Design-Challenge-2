package application;

public abstract class DataSource {
	private DataDisplay display;

	protected DataSource(DataDisplay display) {
		this.display = display;
	}
	
	protected void add(int time, long ticks) {
		display.add(time, ticks);
	}
}
