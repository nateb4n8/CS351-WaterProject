/**
 * Author: Nathan Acosta
 * Date: Feb 16, 2013
 */
package cell;

/**
 * @author nacosta
 * A CellOLD specifies an individual region within a 3D space.
 */
public class CellOLD
{
  private static double cellSize; // Length = width in centimeters
  private double height; // In centimeters
  private boolean surface; // True if cell is a surface cell
  private double depth; // Distance to surface in centimeters
  private double waterVolume; // Water within cell in milliliters
  private Point3D coordinate; // 3D array x, y, and z indexes

  private Soil soil; // Name of soil type
  private Plant plant; // Name of plant type

  /**
   * Creates a CellOLD with the specified parameters as attributes.
   * 
   * @param height The height of the cell.
   * @param type The type of soil within the cell.
   * @param saturation The initial percentage of saturation.
   * @param point The location of the cell.
   */
  public CellOLD(double height, Soil type, double saturation, Point3D point)
  {
    this.height = height;
    this.soil = type;
    this.coordinate = point;
  }

  /**
   * Generates a Point3D with the same coordinates except
   * for z increased.
   * @return the coordinates of the cell above
   */
  public Point3D getCellAboveCoordinates()
  {
    int x = this.coordinate.x;
    int y = this.coordinate.y;
    int z = this.coordinate.z + 1;
    return new Point3D(x,y,z);
  }
  
  /**
   * Generates a Point3D with the same coordinates except
   * for z decreased.
   * @return the coordinates of the cell below
   */
  public Point3D getCellBelowCoordinates()
  {
    int x = this.coordinate.x;
    int y = this.coordinate.y;
    int z = this.coordinate.z - 1;
    return new Point3D(x,y,z);
  }
  
  /**
   * Generates a Point3D with the same coordinates except
   * for y increased.
   * @return the coordinates of the cell in front
   */
  public Point3D getCellNorthCoordinates()
  {
    int x = this.coordinate.x;
    int y = this.coordinate.y + 1;
    int z = this.coordinate.z;
    return new Point3D(x,y,z);
  }
  
  /**
   * Generates a Point3D with the same coordinates except
   * for z increased.
   * @return the coordinates of the right cell
   */
  public Point3D getCellEastCoordinates()
  {
    int x = this.coordinate.x + 1;
    int y = this.coordinate.y;
    int z = this.coordinate.z;
    return new Point3D(x,y,z);
  }
  
  /**
   * Generates a Point3D with the same coordinates except
   * for y decreased.
   * @return the coordinates of the cell in back
   */
  public Point3D getCellSouthCoordinates()
  {
    int x = this.coordinate.x;
    int y = this.coordinate.y - 1;
    int z = this.coordinate.z;
    return new Point3D(x,y,z);
  }

  /**
   * Generates a Point3D with the same coordinates except
   * for x decreased.
   * @return the coordinates of the left cell
   */
  public Point3D getCellWestCoordinates()
  {
    int x = this.coordinate.x - 1;
    int y = this.coordinate.y;
    int z = this.coordinate.z;
    return new Point3D(x,y,z);
  }

  /**
   * @return the height
   */
  public double getHeight()
  {
    return height;
  }

  /**
   * @param height the height to set
   */
  public void setHeight(double height)
  {
    this.height = height;
  }

  /**
   * @return the coordinates
   */
  public Point3D getCoordinates()
  {
    return coordinate;
  }

  /**
   * @param coordinates the coordinates to set
   */
  public void setCoordinates(Point3D coordinates)
  {
    this.coordinate = coordinates;
  }

  /**
   * @return the soil
   */
  public Soil getSoil()
  {
    return soil;
  }

  /**
   * @param soil the soil to set
   */
  public void setSoil(Soil soil)
  {
    this.soil = soil;
  }

  /**
   * @return the plant
   */
  public Plant getPlant()
  {
    return plant;
  }

  /**
   * @param plant the plant to set
   */
  public void setPlant(Plant plant)
  {
    this.plant = plant;
  }
  
}