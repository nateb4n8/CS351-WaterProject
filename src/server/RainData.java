package server;

import java.io.Serializable;

/**
 * @author Robert Trujillo 05.06.2013
 * Class to be used be controller to communicate the rainAmount to clients 
 */
public class RainData  extends NetworkData implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	public final Type type = Type.Rain;
	public final double rainAmount;

	public RainData(double rain) {
		this.rainAmount = rain;
	}
}
