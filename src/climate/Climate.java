package climate;

/**
 * Author: Robert Trujillo  
 * Date: Created by Robert on 05.1.2013 
 * A Climate class that depicts Albuquerque New Mexico climate averages
 */

public enum Climate {

	// Monthly Climates defined
	JANUARY(1, 31, 34.2, 46.8, 21.7, .4, 4, 8.0, 53.0, false), 
	FEBRUARY(2, 28, 40.0,53.5, 26.4, .5, 4, 8.8, 51.5, false),
	MARCH(3, 31, 46.9, 61.4, 32.2, .5,5, 9.9, 43.5, true),
  APRIL(4, 30, 55.2, 70.8, 39.6, .5, 5, 10.7, 36.0, true),
	MAY(5, 31, 64.2, 79.7, 48.6, .5, 4, 10.5, 36.0, true),
  JUNE(6, 30, 74.2,90.0, 58.3, .6, 4, 9.8, 32.5, true),
  JULY(7, 31, 78.5, 92.5, 64.4, 1.4,9, 8.9, 38.5, true),
  AUGUST(8, 31, 75.9, 89.0, 62.6, 1.6, 10, 8.1, 46.5, true),
  SEPTEMBER(9, 30, 68.6, 81.9, 55.2, 1.0, 6, 8.4, 46.5, true),
  OCTOBER(10, 31,57.0, 71.0, 43.0, .9, 5, 8.2, 45.5, false),
  NOVEMBER(11, 31, 44.3, 57.3,31.2, .4, 4, 7.9, 46.5, false),
  DECEMBER(12, 31, 35.3, 47.5, 23.1, .5,4, 7.6, 53.0, false);
   
	//Monthly climate attributes
	private int _monthNumber;
	private int _duration;
	private double _avgTemp;
	private double _avgHighTemp;
	private double _avgLowTemp;
	private double _avgPrecip;
	private int _avgDaysWithPrecip;
	private double _avgWindSpeed;
	private double _avgHumidity;
	private boolean _growingSeason;

	/**
	 * Sets the constant values of a Climate type.
	 * 
	 * @param monthNumber is an integer 1-12
	 * @param duration is an integer number of days in the month/climate
	 * @param avgTemp is the average daily temperature during the month in degrees Farenheit
	 * @param avgHighTemp is the average daily high temperature in degrees Farenheit
	 * @param avgLowTemp is the average daily low temperature in degrees Farenheit
	 * @param avgPrecip is the average daily precipitation in inches
	 * @param avgDaysWithPrecip is the average days where there is precipitation
	 * @param avgWindSpeed is the average daily wind speed in mph
	 * @param avgHumidity is the average daily humidity percentage
	 */
	Climate (int monthNumber, int duration, double avgTemp, double avgHighTemp, double avgLowTemp, double avgPrecip, int avgDaysWithPrecip, double avgWindSpeed, double avgHumidity, boolean growingSeason) {
		this._monthNumber = monthNumber;
		this._duration = duration;
		this._avgTemp = avgTemp;
		this._avgHighTemp = avgHighTemp;
		this._avgLowTemp = avgLowTemp;
		this._avgPrecip= avgPrecip;
		this._avgDaysWithPrecip= avgDaysWithPrecip;
		this._avgWindSpeed = avgWindSpeed;
		this._avgHumidity = avgHumidity;
		this._growingSeason = growingSeason;
	}

	/**
	 * @return corresponding month number (1-12)
	 */
	public int getMonthNumber() {
		return _monthNumber;
	}

	/**
	 * @return duration of month/climate
	 */
	public int getDuration() {
		return _duration;
	}

	/**
	 * @return average temp in fahrenheit
	 */
	public double getAvgTemp() {
		return _avgTemp;
	}

	/**
	 * @return average high temp in fahrenheit
	 */
	public double getAvgHighTemp() {
		return _avgHighTemp;
	}

	/**
	 * @return average low temp in fahrenheit
	 */
	public double getAvgLowTemp() {
		return _avgLowTemp;
	}

	/**
	 * @return average precipitation in inches
	 */
	public double getAvgPrecip() {
		return _avgPrecip;
	}

	/**
	 * @return average Days with Precip
	 */
	public int getAvgDaysWithPrecip() {
		return _avgDaysWithPrecip;
	}

	/**
	 * @return average windspeed in mph
	 */
	public double getAvgWindSpeed() {
		return _avgWindSpeed;
	}

	/**
	 * @return average humidity percentage
	 */
	public double getAvgHumidity() {
		return _avgHumidity;
	}
	
	
	/**
	 * @return true if growing season, else false
	 */
	public boolean isGrowingSeason() {
		return _growingSeason;
	}

	/**
	 * @return Returns the precipitation Daily in mL/cell.
	 */
	public double getDailyPrecip() {
		
		double mlRainPerCell = (_avgPrecip * 254)/_duration;
		
		return mlRainPerCell;
	}
	
	/**
	 * @return Returns the precipitation Daily in mL/cell.
	 */
	public int getPrecipFrequency() {
		
		int frequencyInDays = (int) (_duration/_avgDaysWithPrecip);
		
		return frequencyInDays;
	}
	
	

}
