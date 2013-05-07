package market;

import java.io.Serializable;

import cell.Plant;

import server.NetworkData;
import server.NetworkData.Type;

public class SellProduce extends NetworkData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public final Type type = Type.SellProduce;
	public double total;

	private  SellProduce(Plant plant, int quantity) {
		// use new setBalance method in farm class to update the farms balance
		total = 0;
		switch (plant) {
		case PINTOBEANS:
			total = FarmersMarket.getPrices()[0] * quantity;
			FarmersMarket.getQuantitySold()[0] += quantity;
			break;
		case SUNFLOWER:
			total = FarmersMarket.getPrices()[1] * quantity;
			FarmersMarket.getQuantitySold()[1] += quantity;
			break;
		case AMARANTH:
			total = FarmersMarket.getPrices()[2] * quantity;
			FarmersMarket.getQuantitySold()[2] += quantity;
			break;
		case CHILE:
			total = FarmersMarket.getPrices()[3] * quantity;
			FarmersMarket.getQuantitySold()[3] += quantity;
			break;
		case SWEETCORN:
			total = FarmersMarket.getPrices()[4] * quantity;
			FarmersMarket.getQuantitySold()[4] += quantity;
			break;
		case SUMMERSQUASH:
			total = FarmersMarket.getPrices()[5] * quantity;
			FarmersMarket.getQuantitySold()[5] += quantity;
			break;
		case WINTERSQUASH:
			total = FarmersMarket.getPrices()[6] * quantity;
			FarmersMarket.getQuantitySold()[6] += quantity;
			break;
		case POTATOES:
			total = FarmersMarket.getPrices()[7] * quantity;
			FarmersMarket.getQuantitySold()[7] += quantity;
			break;
		case SWEETPEPPER:
			total = FarmersMarket.getPrices()[8] * quantity;
			FarmersMarket.getQuantitySold()[8] += quantity;
			break;
		}

	}

}
