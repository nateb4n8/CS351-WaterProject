package flow;

import cell.Farm;
import cell.Cell;
import cell.Point3D;
import cell.Soil;
import topo.Topography;
import java.util.Random;

/**
 * WaterFlow is a class that computes how water should flow from cell to cell.
 *
 * TODO: Account for water that plants remove
 * TODO: Extra-farm flow
 * 
 * @author Max Ottesen
 */
public class WaterFlow {
	
	private double       timeStep = 540; //seconds
	private Farm         farm;
	private Cell[][][]   grid;
	private Double[][][] change;
	private Double[][][] hydraulicHead;
	private Double[][][] percentSaturation;
	private FlowWorker[] workers;
	private int          finishedWorkers;
	private int          simulatedTime;

	public WaterFlow(Farm farm) {
		this.farm = farm;
		this.grid = farm.getGrid();
		this.change = new Double[Farm.SIZE][Farm.SIZE][farm.zCellCount];
    this.hydraulicHead = new Double[Farm.xCellCount][Farm.yCellCount][farm.zCellCount];
    this.percentSaturation = new Double[Farm.xCellCount][Farm.yCellCount][farm.zCellCount];
		reset(change);
		reset(hydraulicHead);
		reset(percentSaturation);
		this.finishedWorkers = 0;
		this.workers = new FlowWorker[4];
		this.simulatedTime = 0;
		
		workers[0] = new FlowWorker(0, Farm.xCellCount/2, 0, Farm.yCellCount/2, farm.zCellCount, this, grid, change, timeStep);
		workers[1] = new FlowWorker(Farm.xCellCount/2, Farm.xCellCount, 0, Farm.yCellCount/2, farm.zCellCount, this, grid, change, timeStep);
		workers[2] = new FlowWorker(0, Farm.xCellCount/2, Farm.yCellCount/2, Farm.yCellCount, farm.zCellCount, this, grid, change, timeStep);
		workers[3] = new FlowWorker(Farm.xCellCount/2, Farm.xCellCount, Farm.yCellCount/2, Farm.yCellCount, farm.zCellCount, this, grid, change, timeStep);
		
		for(int i = 0; i < 4; i++) {
		  workers[i].start();
		}
	}


	/**
	 * Runs the model for a given number of seconds
	 */
	public void update(double seconds) {
		for(double i = 0; i < seconds; i += this.timeStep) {
			//long time = System.currentTimeMillis();
			this.update();
			simulatedTime += this.timeStep;
			//System.out.println("+1 time step : " + (System.currentTimeMillis() - time));
		}
	}


	/**
	 * Runs the model for one time step
	 */
	private void update() {
		//TODO: have plants remove water from system
		for(int i = 0; i < 4; i++) {
			workers[i].startCalculations();
		}
		while(finishedWorkers < 4) {
			try{Thread.sleep(1);}
			catch(InterruptedException e){}
		}
		finishedWorkers = 0;

		
		for(int i = 0; i < 4; i++) {
	    workers[i].startCalculations();
	  }
	  while(finishedWorkers < 4) {
	    try{Thread.sleep(1);}
	    catch(InterruptedException e){}
	  }
	  finishedWorkers = 0;

	  double totalWater = 0;
	  for(int i = 0; i < 4; i++) {
	    totalWater += workers[i].getTotalWater();
	  }

		if(simulatedTime % (timeStep*100) == 0) System.out.println(totalWater + " " + simulatedTime);


		//Update the amount of water that all the cells have
		for(int k = 0; k < farm.getZCellCount(); k++) {
			for(int j = 0; j < Farm.yCellCount; j++) {
				for(int i = 0; i < Farm.xCellCount; i++) {
					if(grid[i][j][k] == null) {
						continue;
					}
					grid[i][j][k].setWaterVolume(grid[i][j][k].getWaterVolume() + change[i][j][k]);
				}
			}
		}
		
		//Zero out the change holder
		reset(change);
		reset(hydraulicHead);
		reset(percentSaturation);
	}

	
	/**
	 * Sets a Double[][][] array to all 0s
	 * @param array - the array to be reset
	 */
	private void reset(Double[][][] array) {
	   //Reset the change holder
    for(int k = 0; k < farm.getZCellCount(); k++) {
      for(int j = 0; j < Farm.yCellCount; j++) {
        for(int i = 0; i < Farm.xCellCount; i++) {
          array[i][j][k] = new Double(0);
        }
      }
    } 
	}
	
	/**
	 * Returns the percent saturation of a specified cell
	 * @param x - the X-coordinate of the cell
	 * @param y - the Y-coordinate of the cell
	 * @param z - the Z-coordinate of the cell
	 * @return the percent saturation of the cell
	 */
	public double getPercentSaturation(int x, int y, int z) {
	  return percentSaturation[x][y][z];
	}
	
	 /**
   * Sets a given cell to have a given percent saturation
   * @param x - x coordinate of cell
   * @param y - y coordinate of cell
   * @param z - z coordinate of cell
   * @param sat - percent saturation of cell
   */
	public void setPercentSaturation(int x, int y, int z, Double sat) {
	  synchronized(this.percentSaturation[x][y][z]) {
	    this.percentSaturation[x][y][z] = sat;
	  }
	}
	
	 /**
   * Returns the hydraulic head of a specified cell
   * @param x - the X-coordinate of the cell
   * @param y - the Y-coordinate of the cell
   * @param z - the Z-coordinate of the cell
   * @return the hydraulic head of the cell
   */
	public double getHydraulicHead(int x, int y, int z) {
	  return hydraulicHead[x][y][z];
	}
	
	/**
	 * Sets a given cell to have a given hydraulic head
	 * @param x - x coordinate of cell
	 * @param y - y coordinate of cell
	 * @param z - z coordinate of cell
	 * @param head - hydraulic head of cell
	 */
	public void setHydraulicHead(int x, int y, int z, Double head) {
	  synchronized(this.hydraulicHead[x][y][z]) {
	    this.hydraulicHead[x][y][z] = head;
	  }
	}
	
	/**
	 * Lets a worker thread tell the master that it's done
	 */
	public synchronized void workerDone() {
	  finishedWorkers++;
	}

	/**
	 * Lets all the worker threads die
	 */
	public void kill() {
		for(int i = 0; i < workers.length; i++) {
			workers[i].kill();
		}
	}





	public static void main(String[] args) {
		long time = System.currentTimeMillis();

		System.out.println("INITIALIZATIONS");
		System.out.print("  ...topography : ");
		Farm farm = Topography.createFarm(0, 0);
		System.out.println((System.currentTimeMillis() - time) + " ms");
		System.out.println("    " + (farm.getZCellCount() * Farm.xCellCount * Farm.yCellCount) + " cells");

		time = System.currentTimeMillis();

		Random rand = new Random();

		System.out.print("  ...ground     : ");
		//XML_Handler.initGround(farm, "C:/Program Files (x86)/JetBrains/IntelliJ IDEA 12.1.1/IDEA/Java/Groundwater_Flow/src/XML_Handler/FarmSetup.xml");
		Cell[][][] grid = farm.getGrid();
		for(int k = 0; k < farm.zCellCount; k++) {
			for(int j = 0; j < Farm.yCellCount; j++) {
				for(int i = 0; i < Farm.xCellCount; i++) {
					if(grid[i][j][k] != null) {
						grid[i][j][k].setSoil(Soil.GILASAND);
						if(rand.nextDouble() < .75) {
							grid[i][j][k].setWaterVolume(rand.nextInt(10));
						}
					}
				}
			}
		}
		System.out.println((System.currentTimeMillis() - time) + " ms");

		time = System.currentTimeMillis();

		System.out.print("  ...flow       : ");
		WaterFlow water = new WaterFlow(farm);
		System.out.println((System.currentTimeMillis() - time) + " ms");

		time = System.currentTimeMillis();
		System.out.println("\nUpdating model\n");
		water.update(15778500);
		System.out.println("Simulated " + water.simulatedTime + " seconds in " + (System.currentTimeMillis() - time)/1000 + " seconds");

		System.out.println("\nWaiting");
		try {Thread.sleep(2500);}
		catch(InterruptedException e) {}

		water.kill();
		System.out.println("done");
	}
}
