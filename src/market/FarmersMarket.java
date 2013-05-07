package market;

import java.util.Date;
import java.util.Random;

import server.TimeKeeper;

import climate.Climate;

import cell.Farm;
import cell.Plant;

import server.SellProduce;

public class FarmersMarket {

	// TODO Create Enum for season
	private String season;
	private TimeKeeper timeKeeper;

	// The order of these seasonal prices and quantities correspond to the order
	// they are listed in
	// the Plant Enum
	private static final double[] SEASONALLOWS = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
			29, 30, 31, 32, 33, 34, 35, 36};
	private static final double[] SEASONALHIGHS = { 11, 12, 13, 14, 15, 16, 17, 18,
			19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
			36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46};

	private static int[] quantitySold = new int[9];
	private static int[] quantitySoldYesterday = {1, 1, 1, 1, 1, 1, 1, 1, 1};

	private static double[] prices = new double[9];
	private static double[] pricesYesterday = new double[9];
	private static double[] initialPrices = new double[9];
	
	private double dollars;

	public FarmersMarket(TimeKeeper timeKeeper) {
		
		this.timeKeeper = timeKeeper;
		for (int i = 0; i < initialPrices.length; i++) {
			switch (i) {
			case 0:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[0] - SEASONALLOWS[0]) + 1);
				initialPrices[i] = randomPriceGenerator.nextDouble()
						+ SEASONALLOWS[0];
				break;
			case 1:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[4] - SEASONALLOWS[4]) + 1);
				initialPrices[i] = randomPriceGenerator.nextDouble()
						+ SEASONALLOWS[4];
				break;
			case 2:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[8] - SEASONALLOWS[8]) + 1);
				initialPrices[i] = randomPriceGenerator.nextDouble()
						+ SEASONALLOWS[8];
				break;
			case 3:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[12] - SEASONALLOWS[12]) + 1);
				initialPrices[i] = randomPriceGenerator.nextDouble()
						+ SEASONALLOWS[12];
				break;
			case 4:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[16] - SEASONALLOWS[16]) + 1);
				initialPrices[i] = randomPriceGenerator.nextDouble()
						+ SEASONALLOWS[16];
				break;
			case 5:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[20] - SEASONALLOWS[20]) + 1);
				initialPrices[i] = randomPriceGenerator.nextDouble()
						+ SEASONALLOWS[20];
				break;
			case 6:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[24] - SEASONALLOWS[24]) + 1);
				initialPrices[i] = randomPriceGenerator.nextDouble()
						+ SEASONALLOWS[24];
				break;
			case 7:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[28] - SEASONALLOWS[28]) + 1);
				initialPrices[i] = randomPriceGenerator.nextDouble()
						+ SEASONALLOWS[28];
				break;
			case 8:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[32] - SEASONALLOWS[32]) + 1);
				initialPrices[i] = randomPriceGenerator.nextDouble()
						+ SEASONALLOWS[32];
				break;
			}

		}
		for (int i = 0; i < pricesYesterday.length; i++) {
			pricesYesterday[i] = initialPrices[i];
			getPrices()[i] = initialPrices[i];
		}
	}
	
	private void openFarmersMarket() {
		//this.setGameDate(gameDate);
		timeKeeper.getCurrentDay();
	}
	
	public SellProduce SellProduce(Plant plant, int quantity) {
		// use new setBalance method in farm class to update the farms balance
		dollars = 0;
		switch (plant) {
		case PINTOBEANS:
			dollars = FarmersMarket.getPrices()[0] * quantity;
			FarmersMarket.getQuantitySold()[0] += quantity;
			break;
		case SUNFLOWER:
			dollars = FarmersMarket.getPrices()[1] * quantity;
			FarmersMarket.getQuantitySold()[1] += quantity;
			break;
		case AMARANTH:
			dollars = FarmersMarket.getPrices()[2] * quantity;
			FarmersMarket.getQuantitySold()[2] += quantity;
			break;
		case CHILE:
			dollars = FarmersMarket.getPrices()[3] * quantity;
			FarmersMarket.getQuantitySold()[3] += quantity;
			break;
		case SWEETCORN:
			dollars = FarmersMarket.getPrices()[4] * quantity;
			FarmersMarket.getQuantitySold()[4] += quantity;
			break;
		case SUMMERSQUASH:
			dollars = FarmersMarket.getPrices()[5] * quantity;
			FarmersMarket.getQuantitySold()[5] += quantity;
			break;
		case WINTERSQUASH:
			dollars = FarmersMarket.getPrices()[6] * quantity;
			FarmersMarket.getQuantitySold()[6] += quantity;
			break;
		case POTATOES:
			dollars = FarmersMarket.getPrices()[7] * quantity;
			FarmersMarket.getQuantitySold()[7] += quantity;
			break;
		case SWEETPEPPER:
			dollars = FarmersMarket.getPrices()[8] * quantity;
			FarmersMarket.getQuantitySold()[8] += quantity;
			break;
		}
		
		SellProduce transaction = new SellProduce(dollars);
		return transaction;

	}

	// TODO Add price today/tomorrow

	private Random randomPriceGenerator;

	// TODO Add balance double to Farm object


	private void setSeason() {
		if (timeKeeper.getCurrentClimate().getMonthNumber() >= 1 && timeKeeper.getCurrentClimate().getMonthNumber() < 4) {
			season = "winter";
		} else if (timeKeeper.getCurrentClimate().getMonthNumber() >= 4 && timeKeeper.getCurrentClimate().getMonthNumber() < 7) {
			season = "spring";
		} else if (timeKeeper.getCurrentClimate().getMonthNumber() >= 7 && timeKeeper.getCurrentClimate().getMonthNumber() < 10) {
			season = "summer";
		} else if (timeKeeper.getCurrentClimate().getMonthNumber() >= 10) {
			season = "fall";
		}

	}


	public void calculatePrice(Plant plant) {
		this.setSeason(); 
		if (season == "winter") {
			switch (plant) {
			case PINTOBEANS:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[0] - SEASONALLOWS[0]) + 1);
				getPrices()[0] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[0])
						+ (pricesYesterday[0] * -(((getQuantitySold()[0] - quantitySoldYesterday[0]) / quantitySoldYesterday[0]) / 2.0));
				break;
			case SUNFLOWER:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[4] - SEASONALLOWS[4]) + 1);
				getPrices()[1] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[4])
						+ (pricesYesterday[1] * -(((getQuantitySold()[1] - quantitySoldYesterday[1]) / quantitySoldYesterday[1]) / 2.0));
				break;
			case AMARANTH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[8] - SEASONALLOWS[8]) + 1);
				getPrices()[2] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[8])
						+ (pricesYesterday[2] * -(((getQuantitySold()[2] - quantitySoldYesterday[2]) / quantitySoldYesterday[2]) / 2.0));
				break;
			case CHILE:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[12] - SEASONALLOWS[12]) + 1);
				getPrices()[3] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[12])
						+ (pricesYesterday[3] * -(((getQuantitySold()[3] - quantitySoldYesterday[3]) / quantitySoldYesterday[3]) / 2.0));
				break;
			case SWEETCORN:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[16] - SEASONALLOWS[16]) + 1);
				getPrices()[4] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[16])
						+ (pricesYesterday[4] * -(((getQuantitySold()[4] - quantitySoldYesterday[4]) / quantitySoldYesterday[4]) / 2.0));
				break;
			case SUMMERSQUASH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[20] - SEASONALLOWS[20]) + 1);
				getPrices()[5] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[20])
						+ (pricesYesterday[5] * -(((getQuantitySold()[5] - quantitySoldYesterday[5]) / quantitySoldYesterday[5]) / 2.0));
				break;
			case WINTERSQUASH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[24] - SEASONALLOWS[24]) + 1);
				getPrices()[6] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[24])
						+ (pricesYesterday[6] * -(((getQuantitySold()[6] - quantitySoldYesterday[6]) / quantitySoldYesterday[6]) / 2.0));
				break;
			case POTATOES:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[28] - SEASONALLOWS[28]) + 1);
				getPrices()[7] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[28])
						+ (pricesYesterday[7] * -(((getQuantitySold()[7] - quantitySoldYesterday[7]) / quantitySoldYesterday[7]) / 2.0));
				break;
			case SWEETPEPPER:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[32] - SEASONALLOWS[32]) + 1);
				getPrices()[8] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[32])
						+ (pricesYesterday[8] * -(((getQuantitySold()[8] - quantitySoldYesterday[8]) / quantitySoldYesterday[8]) / 2.0));
				break;
			}
		} else if (season == "spring") {
			switch (plant) {
			case PINTOBEANS:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[1] - SEASONALLOWS[1]) + 1);
				getPrices()[0] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[1])
						+ (pricesYesterday[0] * -(((getQuantitySold()[0] - quantitySoldYesterday[0]) / quantitySoldYesterday[0]) / 2.0));
				break;
			case SUNFLOWER:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[5] - SEASONALLOWS[5]) + 1);
				getPrices()[1] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[5])
						+ (pricesYesterday[1] * -(((getQuantitySold()[1] - quantitySoldYesterday[1]) / quantitySoldYesterday[1]) / 2.0));
				break;
			case AMARANTH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[9] - SEASONALLOWS[9]) + 1);
				getPrices()[2] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[9])
						+ (pricesYesterday[2] * -(((getQuantitySold()[2] - quantitySoldYesterday[2]) / quantitySoldYesterday[2]) / 2.0));
				break;
			case CHILE:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[13] - SEASONALLOWS[13]) + 1);
				getPrices()[3] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[13])
						+ (pricesYesterday[3] * -(((getQuantitySold()[3] - quantitySoldYesterday[3]) / quantitySoldYesterday[3]) / 2.0));
				break;
			case SWEETCORN:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[17] - SEASONALLOWS[17]) + 1);
				getPrices()[4] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[17])
						+ (pricesYesterday[4] * -(((getQuantitySold()[4] - quantitySoldYesterday[4]) / quantitySoldYesterday[4]) / 2.0));
				break;
			case SUMMERSQUASH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[21] - SEASONALLOWS[21]) + 1);
				getPrices()[5] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[21])
						+ (pricesYesterday[5] * -(((getQuantitySold()[5] - quantitySoldYesterday[5]) / quantitySoldYesterday[5]) / 2.0));
				break;
			case WINTERSQUASH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[25] - SEASONALLOWS[25]) + 1);
				getPrices()[6] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[25])
						+ (pricesYesterday[6] * -(((getQuantitySold()[6] - quantitySoldYesterday[6]) / quantitySoldYesterday[6]) / 2.0));
				break;
			case POTATOES:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[29] - SEASONALLOWS[29]) + 1);
				getPrices()[7] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[29])
						+ (pricesYesterday[7] * -(((getQuantitySold()[7] - quantitySoldYesterday[7]) / quantitySoldYesterday[7]) / 2.0));
				break;
			case SWEETPEPPER:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[33] - SEASONALLOWS[33]) + 1);
				getPrices()[8] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[33])
						+ (pricesYesterday[8] * -(((getQuantitySold()[8] - quantitySoldYesterday[8]) / quantitySoldYesterday[8]) / 2.0));
				break;
			}
		}

		else if (season == "summer") {
			switch (plant) {
			case PINTOBEANS:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[2] - SEASONALLOWS[2]) + 1);
				getPrices()[0] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[2])
						+ (pricesYesterday[0] * -(((getQuantitySold()[0] - quantitySoldYesterday[0]) / quantitySoldYesterday[0]) / 2.0));
				break;
			case SUNFLOWER:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[6] - SEASONALLOWS[6]) + 1);
				getPrices()[1] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[6])
						+ (pricesYesterday[1] * -(((getQuantitySold()[1] - quantitySoldYesterday[1]) / quantitySoldYesterday[1]) / 2.0));
				break;
			case AMARANTH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[10] - SEASONALLOWS[10]) + 1);
				getPrices()[2] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[10])
						+ (pricesYesterday[2] * -(((getQuantitySold()[2] - quantitySoldYesterday[2]) / quantitySoldYesterday[2]) / 2.0));
				break;
			case CHILE:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[14] - SEASONALLOWS[14]) + 1);
				getPrices()[3] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[14])
						+ (pricesYesterday[3] * -(((getQuantitySold()[3] - quantitySoldYesterday[3]) / quantitySoldYesterday[3]) / 2.0));
				break;
			case SWEETCORN:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[18] - SEASONALLOWS[18]) + 1);
				getPrices()[4] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[18])
						+ (pricesYesterday[4] * -(((getQuantitySold()[4] - quantitySoldYesterday[4]) / quantitySoldYesterday[4]) / 2.0));
				break;
			case SUMMERSQUASH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[22] - SEASONALLOWS[22]) + 1);
				getPrices()[5] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[22])
						+ (pricesYesterday[5] * -(((getQuantitySold()[5] - quantitySoldYesterday[5]) / quantitySoldYesterday[5]) / 2.0));
				break;
			case WINTERSQUASH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[26] - SEASONALLOWS[26]) + 1);
				getPrices()[6] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[26])
						+ (pricesYesterday[6] * -(((getQuantitySold()[6] - quantitySoldYesterday[6]) / quantitySoldYesterday[6]) / 2.0));
				break;
			case POTATOES:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[30] - SEASONALLOWS[30]) + 1);
				getPrices()[7] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[30])
						+ (pricesYesterday[7] * -(((getQuantitySold()[7] - quantitySoldYesterday[7]) / quantitySoldYesterday[7]) / 2.0));
				break;
			case SWEETPEPPER:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[34] - SEASONALLOWS[34]) + 1);
				getPrices()[8] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[34])
						+ (pricesYesterday[8] * -(((getQuantitySold()[8] - quantitySoldYesterday[8]) / quantitySoldYesterday[8]) / 2.0));
				break;
			}
		}

		else if (season == "fall") {
			switch (plant) {
			case PINTOBEANS:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[3] - SEASONALLOWS[3]) + 1);
				getPrices()[0] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[3])
						+ (pricesYesterday[0] * -(((getQuantitySold()[0] - quantitySoldYesterday[0]) / quantitySoldYesterday[0]) / 2.0));
				break;
			case SUNFLOWER:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[7] - SEASONALLOWS[7]) + 1);
				getPrices()[1] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[7])
						+ (pricesYesterday[1] * -(((getQuantitySold()[1] - quantitySoldYesterday[1]) / quantitySoldYesterday[1]) / 2.0));
				break;
			case AMARANTH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[11] - SEASONALLOWS[11]) + 1);
				getPrices()[2] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[11])
						+ (pricesYesterday[2] * -(((getQuantitySold()[2] - quantitySoldYesterday[2]) / quantitySoldYesterday[2]) / 2.0));
				break;
			case CHILE:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[15] - SEASONALLOWS[15]) + 1);
				getPrices()[3] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[15])
						+ (pricesYesterday[3] * -(((getQuantitySold()[3] - quantitySoldYesterday[3]) / quantitySoldYesterday[3]) / 2.0));
				break;
			case SWEETCORN:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[19] - SEASONALLOWS[19]) + 1);
				getPrices()[4] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[19])
						+ (pricesYesterday[4] * -(((getQuantitySold()[4] - quantitySoldYesterday[4]) / quantitySoldYesterday[4]) / 2.0));
				break;
			case SUMMERSQUASH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[23] - SEASONALLOWS[23]) + 1);
				getPrices()[5] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[23])
						+ (pricesYesterday[5] * -(((getQuantitySold()[5] - quantitySoldYesterday[5]) / quantitySoldYesterday[5]) / 2.0));
				break;
			case WINTERSQUASH:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[27] - SEASONALLOWS[27]) + 1);
				getPrices()[6] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[27])
						+ (pricesYesterday[6] * -(((getQuantitySold()[6] - quantitySoldYesterday[6]) / quantitySoldYesterday[6]) / 2.0));
				break;
			case POTATOES:
				randomPriceGenerator = new Random(
						(int) (SEASONALHIGHS[31] - SEASONALLOWS[31]) + 1);
				getPrices()[7] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[31])
						+ (pricesYesterday[7] * -(((getQuantitySold()[7] - quantitySoldYesterday[7]) / quantitySoldYesterday[7]) / 2.0));
				break;
			case SWEETPEPPER:
				randomPriceGenerator = new Random((int) (SEASONALHIGHS[35] - SEASONALLOWS[35]) + 1);
				getPrices()[8] = (randomPriceGenerator.nextDouble() + SEASONALLOWS[35])
						+ (pricesYesterday[8] * -(((getQuantitySold()[8] - quantitySoldYesterday[8]) / quantitySoldYesterday[8]) / 2.0));
				break;
			}
		}
	}

	public void closeFarmersMarket() {
		// TODO add logic here to reset quantities and to generate new prices
		for (int i = 0; i < getPrices().length; i++) {
			pricesYesterday[i] = getPrices()[i];
		}
		
		this.calculatePrice(Plant.PINTOBEANS);
		this.calculatePrice(Plant.SUNFLOWER);
		this.calculatePrice(Plant.AMARANTH);
		this.calculatePrice(Plant.CHILE);
		this.calculatePrice(Plant.SWEETCORN);
		this.calculatePrice(Plant.SUMMERSQUASH);
		this.calculatePrice(Plant.WINTERSQUASH);
		this.calculatePrice(Plant.POTATOES);
		this.calculatePrice(Plant.SWEETPEPPER);
		
		for (int i = 0; i < getQuantitySold().length; i++) {
			quantitySoldYesterday[i] = getQuantitySold()[i];
		}

	}

	/**
	 * @param gameDate the gameDate to set
	 */
//	public void setGameDate(Date gameDate) {
//		this.gameDate = gameDate;
//	}
	
	/**
	 * 
	 * @param indexA
	 * @param indexB
	 */
	private void generatePrice(int indexA, int indexB)
	{
	  int seed = (int) (SEASONALHIGHS[indexA] - SEASONALLOWS[indexA]) + 1;
	  randomPriceGenerator = new Random(seed);
	  
	  double a = randomPriceGenerator.nextDouble() + SEASONALLOWS[indexA];
	  double b = getQuantitySold()[indexB] - quantitySoldYesterday[indexB];
	  double c = (b / quantitySoldYesterday[indexB]) / 2.0;
	  
      getPrices()[indexB] = a + (pricesYesterday[indexB] * - c);
	}

	public static double[] getPrices() {
		return prices;
	}

	public static void setPrices(double[] prices) {
		FarmersMarket.prices = prices;
	}

	public static int[] getQuantitySold() {
		return quantitySold;
	}

	public static void setQuantitySold(int[] quantitySold) {
		FarmersMarket.quantitySold = quantitySold;
	}

}
