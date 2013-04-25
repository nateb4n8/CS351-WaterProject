package flow;

import cell.Cell;
import cell.Farm;
import cell.Point3D;

/**
 * WaterFlow is a class that computes how water should flow from cell to cell.
 * @author Max Ottesen
 */
public class WaterFlow {
	
	private double       timeStep = 1; //seconds
	private Farm         farm;
	private Cell[][][]   grid;
	private double[][][] change;

	public WaterFlow(Farm farm) {
		this.farm = farm;
		this.grid = farm.getGrid();
		change = new double[Farm.SIZE][Farm.SIZE][farm.zCellCount];
	}


	/**
	 * Runs the model for a given number of seconds
	 */
	public void update(double seconds) {
		for(double i = 0; i < seconds; i += this.timeStep) {
			this.update();
		}
	}


	/**
	 * Runs the model for one time step
	 */
	private void update() {

		for(int k = 0; k < farm.getZCellCount(); k++) {
			for(int j = 0; j < Farm.yCellCount; j++) {
				for(int i = 0; i < Farm.xCellCount; i++) {
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
		
		for(int k = 0; k < farm.getZCellCount(); k++) {
			for(int j = 0; j < Farm.yCellCount; j++) {
				for(int i = 0; i < Farm.xCellCount; i++) {
					if(grid[i][j][k] == null) {
						continue;
					}
					grid[i][j][k].setWaterVolume(grid[i][j][k].getWaterVolume() + change[i][j][k]);
				}
			}
		}
		
		for(int k = 0; k < farm.getZCellCount(); k++) {
			for(int j = 0; j < Farm.yCellCount; j++) {
				for(int i = 0; i < Farm.xCellCount; i++) {
					change[i][j][k] = 0;
				}
			}
		}
	}



	private void flowWaterSide(Cell cellI, Cell cellX) {
		double iSatur = percentSaturation(cellI);
		
		//Only do calculations if...
		//Percent saturation is greater than percent adhesion
		if(iSatur <= cellI.getSoil().getWaterAdhesion()) {
			return;
		}
		Point3D ci = cellI.getCoordinate();
		Point3D cx = cellX.getCoordinate();
		//The hydraulic head of the cell is greater than the cell its flowing to
		if(hydraulicHead(cellI) <= hydraulicHead(cellX)) {
			return;
		}
		//The cell being flowed to isn't full
		if(percentSaturation(cellX) >= 99) {
			return;
		}
		
		double K = cellI.getSoil().getHydraulicConductivity()*cellX.getSoil().getHydraulicConductivity()/2;
		double A = cellI.getHeight()*Cell.getCellSize();
		double min = Math.min(1, (hydraulicHead(cellI)-hydraulicHead(cellX))/Cell.getCellSize());
		
		double flowAmount = K * A * min * timeStep;
		
		change[ci.x][ci.y][ci.z] -= flowAmount;
		change[cx.x][cx.y][cx.z] += flowAmount;	
	}
	
	private void flowWaterUp(Cell cellI, Cell cellX) {
		double iSatur = percentSaturation(cellI);
		double xSatur = percentSaturation(cellX);
		
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
		
		double K = cellI.getSoil().getHydraulicConductivity()*cellX.getSoil().getHydraulicConductivity()/2;
		double A = cellI.getHeight()*Cell.getCellSize();
		double satDif = (iSatur-xSatur)/Cell.getCellSize();
		
		double flowAmount = K * A * satDif * timeStep;
		
		Point3D ci = cellI.getCoordinate();
		Point3D cx = cellX.getCoordinate();
		change[ci.x][ci.y][ci.z] -= flowAmount;
		change[cx.x][cx.y][cx.z] += flowAmount;	
	}

	
	private double hydraulicHead(Cell c) {
		double saturation = percentSaturation(c);
		double height = c.getHeight();
		int x = c.getCoordinate().x;
		int y = c.getCoordinate().y;
		int z = c.getCoordinate().z;
		
		
		double heightAbove = 0;
		double s;
		for(int i = 1; i < farm.zCellCount; i++) {
			s = percentSaturation(grid[x][y][z]);
			if(s > 99) {
				heightAbove += grid[x][y][z].getHeight();
			}
			else {
				break;
			}
		}
		
		return saturation*height + heightAbove;
	}
	
	private  double percentSaturation(Cell c) {
		return c.getSoil().getWaterCapacity()/c.getWaterVolume();
	}

}
