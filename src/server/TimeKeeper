package server;

import java.util.Timer;
import java.util.TimerTask;

import climate.Climate;

/**
 * Author: Robert Trujillo  
 * Date: Created by Robert on 05.1.2013 
 * A Time keeper class used to calculate elapsed days and current climate
 */

public class TimeKeeper {
  
	private int _numberOfMilliSecsPerDay; 
	private int _currentDay;
	private int _currentYear;
	private Climate _currentClimate;
	private int _currentClimateDaysRemaining;
	private Timer _timeKeeperTimer;
	
	/**
	 * Creates TimeKeeper, should be one per game when Server created
	 * @param numberOfMilliSecsPerDay number of milliseconds equal to Day
	 */
	public TimeKeeper(int numberOfMilliSecsPerDay){
		this._numberOfMilliSecsPerDay = numberOfMilliSecsPerDay;
		this._currentClimate = Climate.JANUARY;
		this._currentDay = 0;
		this._currentYear = 0;
		this._currentClimateDaysRemaining = _currentClimate.getDuration();
		this._timeKeeperTimer = new Timer();
		this._timeKeeperTimer.schedule(new AdvanceDayTask(),0,_numberOfMilliSecsPerDay);		
	}

	/**
	 * 
	 * @return game setting of current millisecs equal to a day
	 */
	public int getNumberOfMilliSecsPerDay() {
		return _numberOfMilliSecsPerDay;
	}

	/**
	 * 
	 * @returns number of days elapsed so far
	 */
	public int getCurrentDay() {
		
		return _currentDay;
	}

	/**
	 * 
	 * @return current year count 
	 */
	public int get_currentYear() {
		return _currentYear;
	}

	/**
	 * 
	 * @return current game sessions current climate
	 */
	public Climate getCurrentClimate() {
		return _currentClimate;
	}

	/**
	 * advances the current day while checking to see if climate also needs to be advanced
	 */
	public void advanceCurrentDay() {
		if(this._currentClimateDaysRemaining >1){
		this._currentDay++;
		
		this._currentClimateDaysRemaining--;
		}
		else{
			if(this._currentClimate.toString().equals("DECEMBER")){
				this._currentYear++;
			}
			this._currentDay++;
			this.advanceCurrentClimate(this._currentClimate);
		}
		
	}


	/**
	 * advances current climate to the next. Called within the advanceCurrentDay method
	 * @param _currentClimate
	 */
	public void advanceCurrentClimate(Climate _currentClimate) {
		
		switch(_currentClimate.getMonthNumber()){
		case 1: this._currentClimate = Climate.FEBRUARY;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
				break;
		case 2: this._currentClimate = Climate.MARCH;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
			    break;		
		case 3: this._currentClimate = Climate.APRIL;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
				break;		
		case 4: this._currentClimate = Climate.MAY;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
				break;
		case 5: this._currentClimate = Climate.JUNE;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
				break;
		case 6: this._currentClimate = Climate.JULY;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
				break;		
		case 7: this._currentClimate = Climate.AUGUST;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
				break;
		case 8: this._currentClimate = Climate.SEPTEMBER;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
				break;		
		case 9: this._currentClimate = Climate.OCTOBER;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
				break;	
		case 10: this._currentClimate = Climate.NOVEMBER;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration(); 
				break;	
		case 11: this._currentClimate = Climate.DECEMBER;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration(); 
				break;		 
		default: this._currentClimate = Climate.JANUARY;
				this._currentClimateDaysRemaining = this._currentClimate.getDuration();
				break;				
		}
		
	
	}

	/**
	 * Class to advance day every _numberOfSeconds in Day 
	 * @author Robert Trujillo
	 *
	 */
	class AdvanceDayTask extends TimerTask {	        
        public void run() {
            	advanceCurrentDay(); 
        }
    }	

}
