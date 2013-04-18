package topo;

import cell.Cell;
import cell.Farm;
import cell.Point3D;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Topography is a class that is only used to generate the shape of a piece of land.
 *
 * @author Max Ottesen
 */
public class Topography {
	private static final double MAX_RELIEF    = 13; //meters. The most the topography over the entire grid is allowed to vary
	private static final double TOLERANCE     = 0.75; //meters. Changes larger this amount will not be accepted
	private static final int    SIZE          = 64; //decimeters. length and width
	private static final Random rand          = new Random();


	/**
	 * Takes a latitude and longitude that correspond to a piece of land and shapes a {@link Farm}
	 * so that its topography mimics that piece of land. As of right now, this doesn't actually
	 * mimic the land with the given lat/lon. This produces the land randomly since I don't have
	 * the topo data that I need.
	 *
	 * @param latitude  - the latitude of the piece of land that the returned Farm will mimic
	 * @param longitude - the latitude of the piece of land that the returned Farm will mimic
	 *
	 * @return a Farm that has been given topographic shape
	 */
	public static Farm createFarm(double latitude, double longitude) {
		double[] minmax = {10.0, 0.0};
		double[][] deviation;

		//Generates a 2D array of doubles to correspond to heights of a specific i,j column. This is essentially the shape
		// of the land that the program will run on. It's a random, but smooth, topography.
		deviation = getDeviations(minmax);

//		ElevationData ed = new ElevationData(longitude, latitude);
//		deviation = ed.getElevations();

		//For debugging. MATLAB matrix so I can call bar3(A) and see what my land looks like
		//try {
		//	PrintWriter out = new PrintWriter(new FileWriter("C:/Users/Max/Desktop/file.txt"));
		//	out.print("A = [");
		//	for(int j = 0; j < SIZE; j++) {
		//		for(int i = 0; i < SIZE; i++) {
		//			out.print((deviation[i][j]+25) + " ");
		//		}
		//		out.print("; ");
		//	}
		//	out.println("];");
		//	out.close();
		//	System.out.print("done");
		//} catch(IOException e) {
		//	e.printStackTrace();
		//};

		Cell[][][] grid = new Cell[SIZE][SIZE][126+(int)(minmax[1]*100)];

		//Sets the top to air (null)
		for(int j = 0; j < SIZE; j++) {
			for(int i = 0; i < SIZE; i++) {
				grid[i][j][125+(int)(minmax[1]*100)] = null;
			}
		}

		//Goes cell by cell and sets the height, depth, and coordinates of each cell.
		//If you have a surface or air cell, set it to be one.
		for(int k = 0; k < 125+(int)(minmax[1]*100) ; k++) {
			for(int j = 0; j < SIZE; j++) {
				for(int i = 0; i < SIZE; i++) {
					if(k >= 0 && k < 46) {
						grid[i][j][k] = new Cell(50*(k+1), getDepth(i, j, k, deviation), new Point3D(i, j, k));
						grid[i][j][k].setSurface(false);
					}
					else if(k >= 46 && k < 76) {
						grid[i][j][k] = new Cell(2300 + 5*(k-45), getDepth(i, j, k, deviation), new Point3D(i, j, k));
						grid[i][j][k].setSurface(false);
					}
					else if(k >= 76 && k < 126) {
						grid[i][j][k] = new Cell(2450 + (k-75), getDepth(i, j, k, deviation), new Point3D(i, j, k));
						if(k == 125 && deviation[i][j] == 0.0) {
							grid[i][j][k].setSurface(true);
						}
						else {
							grid[i][j][k].setSurface(false);
						}
					}
					else {
						double d = getDepth(i, j, k, deviation);
						if(d != -1)
						{
							grid[i][j][k] = new Cell(2500 + k, d, new Point3D(i, j, k));

							if(d == 0) {
								grid[i][j][k].setSurface(true);
							}
							else {
								grid[i][j][k].setSurface(false);
							}
						}
						else {
							grid[i][j][k] = null;
						}
					}

				}
			}
		}

		Farm farm = new Farm();
		farm.setLatitude(latitude);
		farm.setLongitude(longitude);
		farm.setRelief(minmax[1]);
		farm.setGrid(grid);

		return farm;
	}

	/**
	 * Generates a random, but smooth topography using the given MAX_RELIEF, TOLERANCE, and SIZE. The MAX_RELIEF is the total
	 *  amount of difference there is allowed to be in the elevations. The TOLERANCE is the largest height difference between
	 *  two blocks that will be accepted. The SIZE is the length and width of the land
	 * @param minmax - the min and max heights
	 * @return - a 2D array of doubles that correspond to heights
	 */
	private static double[][] getDeviations(double[] minmax) {
		double chance; //that this height will used
		double previous2, previous1; //the heights of surrounding cells
		double num; //randomly generated number
		double[][] deviation = new double[SIZE][SIZE]; //the heights of each column above the lowest point

		for(int j = 0; j < SIZE; j++) {
			for(int i = 0; i < SIZE; i++) {
				chance = .5; //50/50 chance of taking the generated number
				num = rand.nextDouble() * MAX_RELIEF; // 0 <= num < MAX_RELIEF

				//Looks at cells to left to help decide whether or not the new number will be kept
				if(i - 2 >= 0) {
					previous2 = deviation[i - 2][j];
					previous1 = deviation[i - 1][j];
					chance += chance(previous1, previous2, num);
				}
				else if(i - 1 >= 0) {
					previous1 = deviation[i - 1][j];
					chance += chance(previous1, num);
				}

				//Looks at cells above to help decide whether or not the new number will be kept
				if(j - 2 >= 0) {
					previous2 = deviation[i][j - 2];
					previous1 = deviation[i][j - 1];
					chance += chance(previous1, previous2, num);
				}
				else if(j - 1 >= 0) {
					previous1 = deviation[i][j - 1];
					chance += chance(previous1, num);
				}

				//Looks at cells diagonally up and left to help decide whether or not the new number will be kept
				if(i - 2 >= 0 && j - 2 >= 0) {
					previous2 = deviation[i - 2][j - 2];
					previous1 = deviation[i - 1][j - 1];
					chance += chance(previous1, previous2, num);
				}
				else if(i - 1 >= 0 && j - 1 >= 0) {
					previous1 = deviation[i - 1][j - 1];
					chance += chance(previous1, num);
				}

				//Roll and see if you come up successful. If so, keep the value.
				if(rand.nextDouble() <= chance) {
					deviation[i][j] = num;
					if(num < minmax[0]) {
						minmax[0] = num;
					}
					if(num > minmax[1]) {
						minmax[1] = num;
					}
				}
				//Otherwise, try again for this cell.
				else {
					i--;
					continue;
				}
			}
		}

		for(int j = 0; j < SIZE; j++) {
			for(int i = 0; i < SIZE; i++) {
				deviation[i][j] -= minmax[0];
				deviation[i][j] = (int)(100*deviation[i][j])/100.0;
			}
		}

		minmax[1] -= minmax[0];
		minmax[0] = 0;

		return deviation;
	}

	private static double getDepth(int i, int j, int k, double[][] deviations) {
		double depth = 0;

		//if k is above the height of a given column, just set the depth to -1 to indicate air
		if(k > (126 + deviations[i][j]*100)) {
			depth = -1;
		}

		//Up until the point where you start seeing topography, just add the depth of the solid block above + the topography
		if(k >= 0 && k < 46) {
			depth += (45-k)*50.0;
			depth += deviations[i][j] * 100;
		}
		else if(k >= 46 && k < 76) {
			depth += 2250;
			depth += (75-k)*5;
			depth += deviations[i][j] * 100;
		}
		else if(k >= 76 && k < 126) {
			depth += 2395;
			depth += (125-k);
			depth += deviations[i][j] * 100;
		}
		//Once you start hitting the topography, only add the depth of the topography above
		else {
			depth +=  deviations[i][j]*100 - (k-126);
		}

		return depth;
	}

	private static double chance(double previous1, double previous2, double num) {
		double chance = 0;
		double change1 = previous1 - previous2;
		double change2 = num - previous1;

		//If the change being considered is smaller than change of the previous cell, the change being considered has a higher chance of
		// being picked. This helps smooth some of the jerkiness (because the numbers are random) out.
		if(Math.abs(change1) <= Math.abs(change2)) {
			chance += .1;
		}

		//If the change being considered is larger than change of the previous cell, the change being considered has a lower chance of
		// being picked. This again helps smooth jerkiness out.
		else {
			chance -= .1;

			//If the slope of the change being considered and the change of the previous cell are both pointing the same way,
			// the change being considered has a higher chance of being picked. This means that hills have a higher chance of
			// occurring rather than just small bumps everywhere.
			if(change1*change2 > 0) {
				chance += .025;
			}

			//If the slope of the change being considered and the change of the previous cell are pointing in different directions,
			// the change being considered has a lower chance of being picked. Again, this encourages hills.
			else if(change1*change2 < 0) {
				chance -= .025;
			}
		}

		//If the change being considered is outside the range of TOLERANCE, then the chance of being picked is 0.
		if(Math.abs(change2) > TOLERANCE) {
			return -5.0;
		}

		return chance;
	}

	//If there is only 1 cell neighboring a cell you are calculating the height for (in a given direction), then simply
	// pick a height that is within the range TOLERANCE
	private static double chance(double previous, double num) {
		if(Math.abs(num - previous) <= TOLERANCE) {
			return .1;
		}
		return -5.0;
	}

	public static void main(String[] args) {
		createFarm(0, 0);
	}
}