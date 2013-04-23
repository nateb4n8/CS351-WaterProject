package cell;

/**
 * Author: Robert Trujillo
 * Date: 04.23.13
 * A Class used for the WaterFlow Gameplay so the Plant may interact with the user/farm
 */
public class Crops 
{
  
  private Plant _plant; //holds crop plant type
  private Farm _farm;//array of Cells where the crop is located 
  private int _quadrant;//Quandrant number where Farm may be plotted numbers 0-3 are valid (0-> NW, 1-> NE, 2-> SW, 3-> SE) 
  private int _weeksWithoutSufficentWater; //counter for weeks without water  
  private int _cropSize; //variable to keep cropsize in number of plants
  
  
  /**
   * Sets the constant values of a Plant type.
   * @param plantType type of plant for crop
   * @param cropLocation in array of cells within farm
   * @param cropSize number of plants in crop
   * @throws Exception 
   */
  private Crops (Plant plant,Farm farm, int quadrant) throws Exception
  {
    
	if(quadrant < 4){
		this._quadrant = quadrant;
	}
	else throw new Exception("Invalid Quadrant Entered");
	  
    this._plant = plant;
    this._farm = _farm;
    this._cropSize = 0;
    
    //plant seeds
    for(int x = 0; x<= _farm.SIZE; x+= _plant.getDistanceBetweenSeeds()){
    	for(int y = 0; y<= _farm.SIZE;y+=_plant.getDistanceBetweenSeeds()){
    		
    		_farm.getGrid()[x][y][_plant.getDepthOfSeed()].setPlant(_plant); 
    		_cropSize++;
    	}
    }
    
    this._weeksWithoutSufficentWater = 0;//initially set to zero
  }
  
  /**
   * @return Returns the PlantType of this crop
   */
  public Plant getPlant()
  { return this._plant;
  }
  
  /**
   * @return Returns the array of cells that this crop covers.
   */
  public Farm getFarm()
  
  { return this._farm;
  }  
  
  /**
   * @return Returns the CropSize in number of plants for this Crop
   */
  public double getCropSize()
  { return this._cropSize;
  }
  
 
  
}
