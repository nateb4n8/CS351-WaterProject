package cell;

/**
 * Author: Robert Trujillo & Nathan Acosta
 * Date: 04.23.13
 * A Class used for the WaterFlow Gameplay so the Plant may interact with the user/farm
 */
public class Crop
{
  
  private Plant _plantType; //holds crop plant type
  private Cell [] _cropLocation;//array of Cells where the crop is located 
  private int _weeksWithoutSufficentWater; //counter for weeks without water  
  private int _cropSize; //variable to keep cropsize in number of plants
  
  
  /**
   * Sets the constant values of a Plant type.
   * @param plantType type of plant for crop
   * @param cropLocation in array of cells within farm
   * @param cropSize number of plants in crop
   */
  private Crop (Plant plantType,Cell[] cropLocation, double cropSize)
  {
    this._plantType = plantType;
    this._cropLocation = cropLocation;
    this._cropSize = cropSize;
    this._weeksWithoutSufficentWater = 0;//initially set to zero
  }
  
 
  
}
