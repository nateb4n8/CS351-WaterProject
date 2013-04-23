package cell;

/**
 * Author: Robert Trujillo 
 * Date: 04.23.13 
 * A Crops class to create a crop on a quadrant of a farm.
 */
public class Crops {

	private Plant _plant; // holds crop plant type
	private Farm _farm;// array of Cells where the crop is located
	private int _quadrant;// Quandrant number where Farm may be plotted numbers(0-> NW, 1-> NE, 2-> SW, 3-> SE)
	private int _cropSize; // variable to keep cropsize in number of plants

	/**
	 * Sets the constant values of a Plant type.
	 * 
	 * @param plantType
	 *            type of plant for crop
	 * @param cropLocation
	 *            in array of cells within farm
	 * @param cropSize
	 *            number of plants in crop
	 * @throws Exception
	 */
	private Crops(Plant plant, Farm farm, int quadrant) throws Exception {
		// check if valid Crop entered
		if (quadrant < 4) {
			this._quadrant = quadrant;
		} else
			throw new Exception("Invalid Quadrant Entered");

		this._plant = plant;
		this._farm = farm;
		this._cropSize = 0;

		// plant seeds by quadrant, size currently set at 150 which is not
		// divisible by 4 so hardcoded 75 for some quads
		if (_quadrant == 0) {
			for (int x = 0; x < 75; x += _plant.getDistanceBetweenSeeds()) {
				for (int y = 0; y < 75; y += _plant.getDistanceBetweenSeeds()) {

					_farm.getGrid()[x][y][_plant.getDepthOfSeed()]
							.setPlant(_plant);
					_cropSize++;
				}
			}
		} else if (_quadrant == 1) {
			for (int x = 75; x < _farm.SIZE; x += _plant
					.getDistanceBetweenSeeds()) {
				for (int y = 0; y < 75; y += _plant.getDistanceBetweenSeeds()) {

					_farm.getGrid()[x][y][_plant.getDepthOfSeed()]
							.setPlant(_plant);
					_cropSize++;
				}
			}
		} else if (_quadrant == 2) {
			for (int x = 0; x < 75; x += _plant.getDistanceBetweenSeeds()) {
				for (int y = 75; y < _farm.SIZE; y += _plant
						.getDistanceBetweenSeeds()) {

					_farm.getGrid()[x][y][_plant.getDepthOfSeed()]
							.setPlant(_plant);
					_cropSize++;
				}
			}
		} else {
			for (int x = 75; x < _farm.SIZE; x += _plant
					.getDistanceBetweenSeeds()) {
				for (int y = 75; y < _farm.SIZE; y += _plant
						.getDistanceBetweenSeeds()) {

					_farm.getGrid()[x][y][_plant.getDepthOfSeed()]
							.setPlant(_plant);
					_cropSize++;
				}
			}
		}
	}

	/**
	 * @return Returns the PlantType of this crop
	 */
	public Plant getPlant() {
		return this._plant;
	}

	/**
	 * @return Returns the array of cells that this crop covers.
	 */
	public Farm getFarm()

	{
		return this._farm;
	}

	/**
	 * @return Returns the CropSize in number of plants for this Crop
	 */
	public double getCropSize() {
		return this._cropSize;
	}

	/**
	 * @return Returns the quadrant of the farm that the crop is in
	 **/
	public int get_quadrant() {
		return _quadrant;
	}

}
