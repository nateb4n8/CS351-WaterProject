package flow;

import cell.Cell;
import cell.Point3D;

/**
 * A FlowWorker is a thread used by WaterFlow in order to split up the ground water calculations
 * @author Max Ottesen
 */
public class FlowWorker extends Thread {
  private int          minX, maxX;
  private int          minY, maxY;
  private int          minZ, maxZ;
  private WaterFlow    m;
  private Cell[][][]   grid;
  private Double[][][] change;
  private boolean      calculate;
  private boolean      kill;
  private double       timeStep;
  
  
  public FlowWorker(int minX, int maxX, int minY, int maxY, int minZ, int maxZ, WaterFlow master, Cell[][][] grid, Double[][][] change, double timeStep) {
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
    this.minZ = minZ;
    this.maxZ = maxZ;
    this.grid = grid;
    this.change = change;
    this.m = master;
    this.timeStep = timeStep;
    this.calculate = false;
    this.kill = false;
  }
  
  
  public void run() {
    //Keep going until it's killed
    while(!kill) {
      //Both this thread and the master thread have to be ready before calculations begin
      if(!calculate || !m.ready()) {
        try {Thread.sleep(1);} 
        catch(InterruptedException e) {}
        continue;
      }
      
      //Flows water between all cells synchronously
      for(int k = minZ; k < maxZ; k++) {
        for(int j = minY; j < maxY; j++) {
          for(int i = minX; i < maxX; i++) {
            if(grid[i][j][k] == null) {
              continue;
            }

            flowWaterSide(grid[i][j][k], grid[i-1][j][k]);
            flowWaterSide(grid[i][j][k], grid[i+1][j][k]);
            flowWaterSide(grid[i][j][k], grid[i][j-1][k]);
            flowWaterSide(grid[i][j][k], grid[i][j+1][k]);
            flowWaterSide(grid[i][j][k], grid[i][j][k-1]);
            flowWaterUp(grid[i][j][k], grid[i][j][k+1]);

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
    double K = cellI.getSoil().getHydraulicConductivity()*cellX.getSoil().getHydraulicConductivity()/2;
    //The area of the face of the cell being flowed from
    double A = cellI.getHeight()*Cell.getCellSize();
    double min = Math.min(1, (m.getHydraulicHead(ci.x, ci.y, ci.z)-m.getHydraulicHead(cx.x, cx.y, cx.z))/Cell.getCellSize());
    
    double flowAmount = K * A * min * timeStep;
    
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
    double K = cellI.getSoil().getHydraulicConductivity()*cellX.getSoil().getHydraulicConductivity()/2;
    //The area of the face of the cell being flowed from
    double A = cellI.getHeight()*Cell.getCellSize();
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
