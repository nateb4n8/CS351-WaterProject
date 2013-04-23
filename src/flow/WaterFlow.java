package flow;

import cell.Farm;

/**
 * WaterFlow is a class that computes how water should flow from cell to cell. This is just a prototype right now.
 * @author: Max Ottesen
 */
public class WaterFlow {
	private double timeStep = 1; //seconds
	private Farm   farm;

	public WaterFlow(Farm farm) {
		this.farm = farm;
	}


	/**
	 * Runs the model for a given number of seconds
	 */
	public void update(double seconds) {
		for(double i = 0; i < seconds; i += this.timeStep) {
			this.update();
		}
	}


	/**
	 * Runs the model for one time step
	 */
	private void update() {

	}


}
