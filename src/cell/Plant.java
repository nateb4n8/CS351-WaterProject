package cell;

/**
 * Author: Robert Trujillo & Nathan Acosta
 * Date: Mar 20, 2013 , Modified by Robert on 04.22.13
 * A Plant contains properties pertaining to its interaction
 * with water and physical structure.
 */
public class PlantGamePlay
{   
  private double _WeeksOfInsufficentWater; 
  private double _CropSize;
  
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
