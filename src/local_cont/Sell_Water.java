package local_cont;

import server.Offer;

public class Sell_Water implements Client_Msg {

	Offer sell;
	@Override
	public String msg_type() {
		// TODO Auto-generated method stub
		return "Sell Water";
	}
	public Sell_Water(Offer o){
		sell = o;
	}
	public Offer get_offer(){
		return sell;
	}

}
