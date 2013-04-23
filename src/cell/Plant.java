/**
 * Author: Robert Trujillo & Nathan Acosta
 * Date: Mar 20, 2013 , Modified by Robert on 04.22.13
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
  (2.5, 3, 114.3, 10.16, 3.81, 2.65),
  
  SUNFLOWER
  (3.2, 13, 25.4, 30, 12, 2.65),
  
  AMARANTH
  (1.4, 2, 17.78, 4, 2, 1.33),
  
  CHILE
  (7.2, 17, 60.96, 5, .5, 2.65),
  
  SWEETCORN
  (1.65, 11, 45.72, 20, 3, 3.98),
  
  SUMMERSQUASH
  (3.5, 7, 91.44, 45, 2.5, 2.65),
  
  WINTERSQUASH
  (3.5, 16, 121.92, 120, 2.5, 2.65),
  
  POTATOES
  (4.2, 17, 45.72, 30, 15, 2.65),
  
  SWEETPEPPER
  (8.2, 80, 30.48, 2.5, .64, 2.65);
  
  
  //private double deltaHydraulicConductivity; //Unable to get specific data, will need to wait for plant expert
  //private double deltaInfiltrationRate; //Measured in
  //private double deltaEvaporationRate; //Measured in
  
  private double _transpiration; 
  private double _maturationTime;
  private double _matureDepth; 
  private double _distanceBetweenSeeds;
  private double _depthOfSeed;
  private double _waterRequirements;
  
  /**
   * Sets the constant values of a Plant type.
   * @param transpiration in mL/Week
   * @param maturationTime in Weeks
   * @param matureDepth in cm
   * @param distanceBetweenSeeds in cm
   * @param depthOfSeed in cm
   * @param waterRequirements in mL/week
   */
  private Plant (double transpiration, double maturationTime, double matureDepth,
      double distanceBetweenSeeds, double depthOfSeed, double waterRequirements)
  {
    this.transpiration = transpiration;
    this._maturationTime = maturationTime;
    this._matureDepth = matureDepth;
    this._distanceBetweenSeeds = distanceBetweenSeeds;
    this._depthOfSeed = depthOfSeed;    
    this._waterRequirements = waterRequirements;
  }
  
  /**
   * @return Returns the Transporation of this Plant in mL/Week.
   */
  public double getTranspiration()
  { return this._transpiration;
  }
  
  /**
   * @return Returns the Maturation Time of this Plant in Weeks.
   */
  public double getMaturationTimee()
  { return this._maturationTime;
  }

  /**
   * @return Returns the Mature Root Depth of this Plant in cm.
   */
  public double getMatureDepth()
  { return this._matureDepth;
  }
  
  /**
   * @return Returns the Distance needed between seeds of this Plant in cm.
   */
  public double getDistanceBetweenSeeds()
  { return this._distanceBetweenSeeds;
  }
  
  /**
   * @return Returns the Depth needed for the seed of this Plant in cm.
   */
  public double getDepthOfSeed()
  { return this._depthOfSeed;
  }
  
  /**
   * @return Returns the Water Requirements of this Plant in mL/week.
   */
  public double getWaterRequirements()
  { return this._waterRequirements;
  
  }
  
}
