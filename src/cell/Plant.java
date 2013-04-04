/**
 * Author: Nathan Acosta
 * Date: Mar 20, 2013
 */
package cell;

/**
 * @author nacosta
 * A Plant contains properties pertaining to its interaction
 * with water and physical structure.
 */
public enum Plant
{
  PINTOBEANS
  (2.5, 0, 0, 0, 21, 45),
  
  SUNFLOWER
  (3.2, 0, 0, 0, 90, 10),
  
  AMARANTH
  (1.4, 0, 0, 0, 14, 7),
  
  CHILE
  (7.2, 0, 0, 0, 120, 24),
  
  SWEETCORN
  (1.65, 0, 0, 0, 80, 18),
  
  SUMMERSQUASH
  (3.5, 0, 0, 0, 50, 36),
  
  WINTERSQUASH
  (3.5, 0, 0, 0, 110, 48),
  
  POTATOES
  (4.2, 0, 0, 0, 120, 18),
  
  SWEETPEPPER
  (8.2, 0, 0, 0, 80, 12),
  
  ROOTS(0, 0, 0, 0, 0, 0);
  
  private double transporation; //Measured in ...
  private double deltaHydraulicConductivity; //Measured in ...
  private double deltaInfiltrationRate; //Measured in ...
  private double deltaEvaporationRate; //Measured in ...
  private double growthRate; //Measured in ...
  private double matureDepth; //Measured in ...
  
  /**
   * Sets the constant values of a Plant type.
   * @param t Transporation in ...
   * @param dhc Delta Hydraulic Conductivity in ...
   * @param dir Delta Infiltration Rate in ...
   * @param der Delta Evaporation Rate in ...
   * @param gr Growth Rate in ...
   * @param md Mature Depth in ...
   */
  private Plant (double t, double dhc, double dir,
      double der, double gr, double md)
  {
    this.transporation = t;
    this.deltaHydraulicConductivity = dhc;
    this.deltaInfiltrationRate = dir;
    this.deltaEvaporationRate = der;
    this.growthRate = gr;
    this.matureDepth = md;
  }
  
  /**
   * @return Returns the Transporation of this Plant.
   */
  public double getTransporation()
  { return this.transporation;
  }
  
  /**
   * @return Returns the Delta Hydraulic Conductivity of this Plant.
   */
  public double getDeltaHydraulicConductivity()
  { return this.deltaHydraulicConductivity;
  }

  /**
   * @return Returns the Delta Infiltration Rate of this Plant.
   */
  public double getDeltaInfiltrationRate()
  { return this.deltaInfiltrationRate;
  }
  
  /**
   * @return Returns the Delta Evaporation Rate of this Plant.
   */
  public double getDeltaEvaporationRate()
  { return this.deltaEvaporationRate;
  }
  
  /**
   * @return Returns the Growth Rate of this Plant.
   */
  public double getGrowthRate()
  { return this.growthRate;
  }
  
  /**
   * @return Returns the Mature Depth of this Plant.
   */
  public double getMatureDepth()
  { return this.matureDepth;
  }
  
}
