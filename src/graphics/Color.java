package graphics;

public class Color
{
  public double red = 0;
	public double green = 0;
	public double blue = 0;
	
	public Color(double red, double green, double blue)
	{
		this.red = red/255.0;
		this.green = green/255.0;
		this.blue  = blue/255.0;
	}
}
