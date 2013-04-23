package topo;

import cell.Farm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Robert Trujillo
 */
public class ElevationData {

	// elevation data 2d array
	public double[][] elevations = new double[Farm.SIZE][Farm.SIZE];

	/**
	 * Retrieves Elevation data from GOOGLE Elevation web service
	 *
	 * @param url containing up to 10 unique lattitude and longitudes
	 * @param y   Current Y coord of elevation array to be populated
	 */
	public void GetElevationsFromGoogle(String url, int y) {
		// Declare Variables
		URL googUrl;
		BufferedReader reader = null;
		String line;
		double currElevationReading;

		try {
			// create the HttpURLConnection
			googUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) googUrl.openConnection();

			// just want to do an HTTP GET here
			connection.setRequestMethod("GET");

			// give it 15 seconds to respond
			connection.setReadTimeout(15 * 1000);
			connection.connect();

			// read the output from the server
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		} catch(IOException e) {
			e.printStackTrace();
		}

		//Read Google Response and populate 10 rows of data in elevations array
		try {
			while((line = reader.readLine()) != null) {
				if(line.contains("elevation")) {
					if((line.substring(line.lastIndexOf(" "), line.indexOf(",") - 7).trim().length() > 1)) {
						currElevationReading = Double.parseDouble(line.substring(line.lastIndexOf(" "),
						                                                         line.indexOf(",") - 7).trim());

						for(int i = 0; i < Farm.SIZE; i++) {
							this.elevations[y][i] = currElevationReading;

						}

						y += 1;
					}

				}
			}

		} catch(IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Troubleshooting responses from google that are sometimes null, believe timing
	 * issue. Correct data by populating empty rows with elevation from previous row of data
	 */
	public void DataCorrection() {
		for(int i = 0; i < Farm.SIZE; i++) {
			for(int j = 0; j < Farm.SIZE; j++) {
				if(this.elevations[i][j] == 0.0) {
					this.elevations[i][j] = this.elevations[i - 1][j];
				}

			}
		}
	}

	/**
	 * Create Elevation Data Object
	 *
	 * @param longitude must be -180 to 180
	 * @param latitude must be -90 to 90
	 */
	public ElevationData(double longitude, double latitude) {

		int startLatitude = (int) ((Math.round(latitude * 1000)) - 960);
		int endLongitude = (int) ((Math.round(longitude * 1000)) + 960);
		int currLat = startLatitude, currLong = endLongitude;

		StringBuilder builder = new StringBuilder();

		int arryCnt = 0;

		for(int x = 0; x < Farm.SIZE; x += 10) {
			builder.setLength(0);
			builder.append("http://maps.googleapis.com/maps/api/elevation/json?locations=" + (currLong / 1000.0) + "," +
			               "" + (currLat / 1000.0));
			for(int y = 1; y < 10; y++) {
				currLat += 3;
				builder.append("|" + (currLong / 1000.0) + "," + (currLat / 1000.0));

			}
			builder.append("&sensor=true");
			GetElevationsFromGoogle(builder.toString(), arryCnt);
			currLat += 3;
			arryCnt += 10;
		}

		DataCorrection();
	}

	/**
	 * Retrieves Elevation data of Farm.SIZE x Farm.SIZE decimeter plot of land (1 acre)
	 *
	 * @return 2D Double Array
	 */
	public double[][] getElevations() {
		return elevations;
	}

}
