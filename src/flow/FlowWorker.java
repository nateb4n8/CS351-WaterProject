package flow;

import cell.Cell;
import cell.Farm;
import cell.Plant;
import cell.Point3D;

/**
 * A FlowWorker is a thread used by WaterFlow in order to split up the ground water calculations
 * @author Max Ottesen
 */
public class FlowWorker extends Thread {
  private int          minX, maxX;
  private int          minY, maxY;
	private int          zCellCount;
  private WaterFlow    m;
  private Cell[][][]   grid;
  private Double[][][] change;
  private boolean      calculate;
  private boolean      kill;
  private double       timeStep;
	private Double       totalWater;
  
  
  public FlowWorker(int minX, int maxX, int minY, int maxY, int zCellCount, WaterFlow master, Cell[][][] grid, Double[][][] change, double timeStep) {
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
    this.grid = grid;
    this.change = change;
    this.m = master;
    this.timeStep = timeStep;
    this.calculate = false;
    this.kill = false;
	  this.zCellCount = zCellCount;
	  this.totalWater = new Double(0);
  }
  
  
  public void run() {
    //Keep going until it's killed
    while(!kill) {
      //The master has to tell this thread to calculate before it will
      if(!calculate) {
        try {Thread.sleep(1);}
        catch(InterruptedException e) {}
        continue;
      }


	    synchronized(this) {
		    this.totalWater = new Double(0);
		    for(int k = zCellCount - 1; k >= 0; k--) { //k's count down so that the hydraulic head calculations can be done in the same loop as the percent saturations
			    for(int j = minY; j < maxY; j++) {
				    for(int i = minX; i < maxX; i++) {
					    if(grid[i][j][k] == null) {
						    m.setPercentSaturation(i, j, k, new Double(-1));;
						    m.setHydraulicHead(i, j, k, new Double(-1));
						    continue;
					    }

					    m.setPercentSaturation(i, j, k, new Double(percentSaturation(grid[i][j][k])));
					    m.setHydraulicHead(i, j, k, new Double(hydraulicHead(grid[i][j][k])));

					    this.totalWater += grid[i][j][k].getWaterVolume();

					    //Plant plant = grid[i][j][k].getPlant();
					    //
					    //if(plant == null) {
						   // continue;
					    //}
					    //if(!plant.isDeadOrAlive()) {
						   // continue;
					    //}
					    //
					    //double availableWater = 0;
					    //
					    //for(Point3D p : plant.get_rootCellCoordinates(new Point3D(i, j, k))) {
						   // availableWater += grid[p.x][p.y][p.z].getWaterVolume();
					    //}
					    //
					    //plant.grow(availableWater);
				    }
			    }
		    }
	    }

	    //Sets itself up so the master thread has to tell it to start before it does more calculations
	    this.calculate = false;
	    m.workerDone();


      while(!calculate) {
	      if(kill) {return;}
	      try {Thread.sleep(1);}
	      catch(InterruptedException e) {}
      }
      
      //Flows water between all cells synchronously
      for(int k = 0; k < zCellCount; k++) {
        for(int j = minY; j < maxY; j++) {
          for(int i = minX; i < maxX; i++) {
            if(grid[i][j][k] == null) {
              continue;
            }
	          if(grid[i][j][k].getWaterVolume() == 0) {
		          continue;
	          }

	          if(i != 0) {
		          flowWaterSide(grid[i][j][k], grid[i-1][j][k]);
	          }
	          if(i != Farm.xCellCount - 1) {
		          flowWaterSide(grid[i][j][k], grid[i+1][j][k]);
	          }

	          if(j != 0) {
		          flowWaterSide(grid[i][j][k], grid[i][j-1][k]);
	          }
	          if(j != Farm.yCellCount - 1) {
		          flowWaterSide(grid[i][j][k], grid[i][j+1][k]);
	          }

	          if(k != 0) {
		          flowWaterSide(grid[i][j][k], grid[i][j][k-1]);
	          }
	          if(k != zCellCount - 1) {
		          flowWaterUp(grid[i][j][k], grid[i][j][k+1]);
	          }

          }
        }
      }
      
      //Sets itself up so the master thread has to tell it to start before it does more calculations
      this.calculate = false;
      m.workerDone();
    }
  }
  
  
  /**
   * Calculates the amount of water that should flow from one cell to another. This
   *  should not be used to calculate water flowing upward!
   *  
   * @param cellI - the cell to flow water from
   * @param cellX - the cell to flow water to
   */
  private void flowWaterSide(Cell cellI, Cell cellX) {
    if(cellX == null) {
      return;
    }
    Point3D ci = cellI.getCoordinate();
    Point3D cx = cellX.getCoordinate();
    
    //The saturation of the giving cell
    double iSatur = m.getPercentSaturation(ci.x, ci.y, ci.z);
      
    //Only do calculations if...
    //Percent saturation is greater than percent adhesion
    if(iSatur <= cellI.getSoil().getWaterAdhesion()) {
      return;
    }
    //The hydraulic head of the cell is greater than the cell its flowing to
    if(m.getHydraulicHead(ci.x, ci.y, ci.z) <= m.getHydraulicHead(cx.x, cx.y, cx.z)) {
      return;
    }
    //The cell being flowed to isn't full
    if(m.getPercentSaturation(cx.x, cx.y, cx.z) >= 99) {
      return;
    }
      
    //The average hydraulic conductivity
    double K = (cellI.getSoil().getHydraulicConductivity()+cellX.getSoil().getHydraulicConductivity())/2;
    //The area of the face of the cell being flowed from
    double A = cellI.getHeight()*Cell.getCellSize();
    double min = Math.min(1, (m.getHydraulicHead(ci.x, ci.y, ci.z)-m.getHydraulicHead(cx.x, cx.y, cx.z)));
    
    double flowAmount = K * A * min * timeStep / Cell.getCellSize();
    
    synchronized(change[ci.x][ci.y][ci.z]) {
      synchronized(change[cx.x][cx.y][cx.z]) {
        change[ci.x][ci.y][ci.z] -= flowAmount;
        change[cx.x][cx.y][cx.z] += flowAmount; 
      }
    }
  }
  
   /**
   * Calculates the amount of water that should flow from one cell to another. This
   *  should only be used for water flowing upwards!
   *  
   * @param cellI - the cell to flow water from
   * @param cellX - the cell to flow water to
   */
  private void flowWaterUp(Cell cellI, Cell cellX) {
    if(cellX == null) {
      return;
    }
    Point3D ci = cellI.getCoordinate();
    Point3D cx = cellX.getCoordinate();
    

    
    //The percent saturations of each cell
    double iSatur = m.getPercentSaturation(ci.x, ci.y, ci.z);
    double xSatur = m.getPercentSaturation(cx.x, cx.y, cx.z);
    
    //Only do calculations if...
    //Percent saturation is greater than percent adhesion in giving cell
    if(iSatur <= cellI.getSoil().getWaterAdhesion()) {
      return;
    }
    //Percent saturation is less than percent adhesion in receiving cell
    if(xSatur > cellX.getSoil().getWaterAdhesion()) {
      return;
    }
    //Cell i is more saturated than cell x
    if(iSatur <= xSatur) {
      return;
    }
    
    
    //The average hydraulic conductivity
    double K = (cellI.getSoil().getHydraulicConductivity()+cellX.getSoil().getHydraulicConductivity())/2;
    //The area of the face of the cell being flowed from
    double A = Cell.getCellSize()*Cell.getCellSize();
    double satDif = (iSatur-xSatur)/Cell.getCellSize();
    
    double flowAmount = K * A * satDif * timeStep;
    
    synchronized(change[ci.x][ci.y][ci.z]) {
      synchronized(change[cx.x][cx.y][cx.z]) {
        change[ci.x][ci.y][ci.z] -= flowAmount;
        change[cx.x][cx.y][cx.z] += flowAmount; 
      }
    }
  }
  
  
  
  /**
   * Computes the hydraulic head of the given cell
   * @param c - the cell being considered
   * @return the hydraulic head of the given cell
   */
  private double hydraulicHead(Cell c) {
    double height = c.getHeight();
    int x = c.getCoordinate().x;
    int y = c.getCoordinate().y;
    int z = c.getCoordinate().z;
    double saturation = m.getPercentSaturation(x, y, z);
    
    
    //Adds the heights of all the cells above the given cell that are fully saturated
    double heightAbove = 0;
    double s;
    for(int i = 1; i < zCellCount; i++) {
      s = m.getPercentSaturation(x, y, z+i);
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
	 * @return the total amount of water in this worker's system
	 */
	public Double getTotalWater() {
		synchronized(this) {
			return this.totalWater;
		}
	}
  
  
  /**
   * Lets this thread now that it's OK to start doing its calculations
   */
  public void startCalculations() {
    this.calculate = true;
  }
  
  /**
   * Kills this thread by letting its run() loop end
   */
  public void kill() {
    this.kill = true;
  }
}
