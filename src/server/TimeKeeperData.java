package server;

import java.io.Serializable;

/**
 * @author Robert Trujillo 05.06.2013
 * Class to be used be controller to communicate the TimeKeeper info 
 * including current climate info
 */
public class TimeKeeperData  extends NetworkData implements Serializable{

	private static final long serialVersionUID = 1L;
	public final Type       type = Type.TimeKeeper;
	public final TimeKeeper gameTimeKeeper;

	public TimeKeeperData(TimeKeeper timeKeeper) {
		this.gameTimeKeeper = timeKeeper;
	}
}
