package flow;

import cell.Farm;
import cell.Cell;
import cell.Plant;
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
	
	private double       timeStep = 1; //seconds
	private Farm         farm;
	private Cell[][][]   grid;
	private Double[][][] change;
	private double[][][] hydraulicHead;
	private double[][][] percentSaturation;
	private FlowWorker[] workers;
	private int          finishedWorkers;

	public WaterFlow(Farm farm) {
		this.farm = farm;
		this.grid = farm.getGrid();
		this.change = new Double[Farm.SIZE][Farm.SIZE][farm.zCellCount];
		reset(change);
		this.finishedWorkers = 0;
		this.workers = new FlowWorker[4];
		this.hydraulicHead = new double[Farm.xCellCount][Farm.yCellCount][farm.zCellCount];
		this.percentSaturation = new double[Farm.xCellCount][Farm.yCellCount][farm.zCellCount];
		
		workers[0] = new FlowWorker(0, Farm.xCellCount/2, 0, Farm.yCellCount/2, 0, farm.zCellCount, this, grid, change, timeStep);
		workers[1] = new FlowWorker(Farm.xCellCount/2, Farm.xCellCount, 0, Farm.yCellCount/2, 0, farm.zCellCount, this, grid, change, timeStep);
		workers[2] = new FlowWorker(0, Farm.xCellCount/2, Farm.yCellCount/2, Farm.yCellCount, 0, farm.zCellCount, this, grid, change, timeStep);
		workers[3] = new FlowWorker(Farm.xCellCount/2, Farm.xCellCount, Farm.yCellCount, Farm.yCellCount, 0, farm.zCellCount, this, grid, change, timeStep);
		
		for(int i = 0; i < 4; i++) {
		  workers[i].start();
		}
	}


	/**
	 * Runs the model for a given number of seconds
	 */
	public void update(double seconds) {
		for(double i = 0; i < seconds; i += this.timeStep) {
			long time = System.currentTimeMillis();
			this.update();
			System.out.println("+1 time step : " + (System.currentTimeMillis() - time));
		}
	}


	/**
	 * Runs the model for one time step
	 */
	private void update() {

		double totalWater = 0;
		//Calculate all percent saturations
		//Calculate all hydraulic heads
		//Loops and lets plants do growing calculations
		//TODO: consider having worker threads do this
		//TODO: have plants remove water from system
		for(int k = farm.getZCellCount() - 1; k >= 0; k--) { //k's count down so that the hydraulic head calculations can be done in the same loop as the percent saturations
			for(int j = 0; j < Farm.yCellCount; j++) {
				for(int i = 0; i < Farm.xCellCount; i++) {
					if(grid[i][j][k] == null) {
						percentSaturation[i][j][k] = new Double(-1);
						hydraulicHead[i][j][k] = new Double(-1);
						continue;
					}

					totalWater += grid[i][j][k].getWaterVolume();

					percentSaturation[i][j][k] = new Double(percentSaturation(grid[i][j][k]));
					hydraulicHead[i][j][k] = new Double(hydraulicHead(grid[i][j][k]));

					Plant plant = grid[i][j][k].getPlant();

					if(plant == null) {
						continue;
					}
					if(!plant.isDeadOrAlive()) {
						continue;
					}

					double availableWater = 0;

					for(Point3D p : plant.get_rootCellCoordinates(new Point3D(i, j, k))) {
						availableWater += grid[p.x][p.y][p.z].getWaterVolume();
					}

					plant.grow(availableWater);
				}
			}
		}

		System.out.println("Total Water = " + totalWater);



		for(int i = 0; i < 4; i++) {
	    workers[i].startCalculations();
	  }
	  	  
	  while(finishedWorkers < 4) {
	    try{Thread.sleep(1);} 
	    catch (InterruptedException e){}
	  }
	  finishedWorkers = 0;


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
	}


	/**
	 * Computes the hydraulic head of the given cell
	 * @param c - the cell being considered
	 * @return the hydraulic head of the given cell
	 */
	private double hydraulicHead(Cell c) {
		double saturation = percentSaturation(c);
		double height = c.getHeight();
		int x = c.getCoordinate().x;
		int y = c.getCoordinate().y;
		int z = c.getCoordinate().z;
		
		
		//Adds the heights of all the cells above the given cell that are fully saturated
		double heightAbove = 0;
		double s;
		for(int i = 1; i < farm.zCellCount; i++) {
			s = percentSaturation[x][y][z+i];
			if(s > .99) {
				heightAbove += grid[x][y][z+i].getHeight();
			}
			else {
				break;
			}
		}
		
		//returns the hydraulic head
		return saturation*height + heightAbove;
	}
	
	/**
	 * Computes the percent saturation of the given cell
	 * @param c - the cell being considered
	 * @return the percent saturation of the given cell
	 */
	private double percentSaturation(Cell c) {
		return c.getWaterVolume()/c.getSoil().getWaterCapacity();
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
		System.out.print("  ...topography :");
		Farm farm = Topography.createFarm(0, 0);
		System.out.println((System.currentTimeMillis() - time));
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
		System.out.println((System.currentTimeMillis() - time));

		time = System.currentTimeMillis();

		System.out.print("  ...flow       : ");
		WaterFlow water = new WaterFlow(farm);
		System.out.println((System.currentTimeMillis() - time));

		System.out.println("\nUpdating model\n");
		water.update(25);

		System.out.println("\nWaiting");
		try {Thread.sleep(2500);}
		catch(InterruptedException e) {}

		water.kill();
		System.out.println("done");
	}
}
