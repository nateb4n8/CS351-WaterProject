package local_cont;

import server.Offer;

public class Buy_Water implements Client_Msg {

	public Offer buy;
	public Buy_Water(Offer o){
		buy = o;
	}
	@Override
	public String msg_type() {
		return "Buy Water";
	}
	public Offer get_offer(){
		return buy;
	}

}
