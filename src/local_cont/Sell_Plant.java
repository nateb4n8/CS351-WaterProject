package local_cont;

import cell.Farm;
import cell.Plant;

public class Sell_Plant implements Client_Msg {

	Plant plant;
	double quantity;
	Farm farm;
	public Sell_Plant(){
		farm = null;
		quantity = 0;
		plant = null;
	}
	public Sell_Plant(Plant p, double qty){
		plant = p;
		quantity = qty;
	}
	@Override
	public String msg_type() {
		return "Sell Plant";
	}
	
	

}
