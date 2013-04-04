/**
 * Author: Nathan Acosta
 * Date: Mar 24, 2013
 */
package cell;

/**
 * @author nacosta
 * Farm contains a 3D array of Cells (grid) that has a
 * latitude, longitude, and a relief along with a string
 * to display errors (errorText). The x and y cellCounts are
 * set corresponding to ten centimeters so x and y evaluate
 * to 40960000 square centimeters, a bit over one acre.
 */
public class Farm
{
  public String errorText; //Error messages
  public static final int xCellCount = 640; //Width of the grid in cell amount 
  public static final int yCellCount = 640; //Length of the grid in cell amount
  public int zCellCount; //Height of the grid in cell amount.
  
  private double latitude, longitude, relief; //Chosen location
  private Cell[][][] grid; //Contains all cells above and below the surface.
  
  /**
   * @param zCount the amount of cells in the z axis.
   */
  public void setZCellCount(int zCount)
  { this.zCellCount = zCount;
  }
  
  /**
   * @return the zCellCount
   */
  public int getZCellCount()
  { return grid[0][0].length;
  }
  
  /**
   * @return the latitude
   */
  public double getLatitude()
  { return latitude;
  }
  
  /**
   * @param latitude the latitude to set
   */
  public void setLatitude(double latitude)
  { this.latitude = latitude;
  }
  
  /**
   * @return the longitude
   */
  public double getLongitude()
  { return longitude;
  }
  
  /**
   * @param longitude the longitude to set
   */
  public void setLongitude(double longitude)
  { this.longitude = longitude;
  }
  
  /**
   * @return the relief
   */
  public double getRelief()
  { return relief;
  }
  
  /**
   * @param relief the relief to set
   */
  public void setRelief(double relief)
  { this.relief = relief;
  }
  
  /**
   * @return the grid
   */
  public Cell[][][] getGrid()
  { return grid;
  }
  
  /**
   * @param grid the grid to set
   */
  public void setGrid(Cell[][][] grid)
  { this.grid = grid;
  }
  
}
