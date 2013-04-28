package local_cont;

public class Receive_Money implements Client_Msg {

	private double amt;
	@Override
	public String msg_type() {
		return "Receive Money";
	}
	public void set_amt(double amt){
		this.amt = amt;
	}
	public double get_amt(){
		return amt;
	}

}
