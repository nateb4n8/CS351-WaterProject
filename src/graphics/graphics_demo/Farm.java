
public class Farm 
{
	int sizeX = 50;
	int sizeY = 30;
	int sizeZ = 50;
	
	public Cell[][][] grid;
	
	public Farm()
	{		
		this.grid = new Cell[sizeX][sizeY][sizeZ];
		
		for(int i = 0; i < sizeX; i++)
		{
			for(int j = 0; j < sizeY; j++)
			{
				for(int k = 0; k < sizeZ; k++)
				{
					assert(grid[i][j][k] != null);
					
					grid[i][j][k] = new Cell();
					
					grid[i][j][k].x = i;
					grid[i][j][k].y = j;
					grid[i][j][k].z = k;
					
					
					
					if(j < 2)
					{
						if(i < 20)
						{
						grid[i][j][k].setPlantType(Plant.SWEETCORN);
						}
						else if(i < 40)
						{
							grid[i][j][k].setPlantType(Plant.SUNFLOWER);
						}
						else
						{
							grid[i][j][k].setPlantType(Plant.WINTERSQUASH);
						}
					}
					
					if(j < 10)
					{
						grid[i][j][k].setSoilType(Soil.GILASAND);
					}
					else if(j < 20)
					{
						grid[i][j][k].setSoilType(Soil.GILALOAM);
					}
					else
					{
						grid[i][j][k].setSoilType(Soil.GILACLAY);
					}
					
					if(grid[i][j][k].getPlantType() == null)
					{
						grid[i][j][k].setWater(1);
					}

				}
			}
		}
	}

	public Cell[][][] getGrid()
	{
		return this.grid;
	}
	

}
