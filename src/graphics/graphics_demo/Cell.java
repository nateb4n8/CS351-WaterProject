
public class Cell 
{

	public int x;
	public int y;
	public int z;
	
	private Soil soilType;
	private double water;
	private Plant plant;

	public Soil getSoil()
	{
		return this.soilType;
	}
	
	public void setSoilType(Soil soil)
	{
		this.soilType = soil;
	}

	public double getWater()
	{
		return this.water;
	}
	
	public void setWater(double value)
	{
		this.water = value;
	}

	public Plant getPlantType()
	{
		return this.plant;
	}	
	
	public void setPlantType(Plant plant)
	{
		this.plant = plant;
	}
	
}
