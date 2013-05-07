package server;

import java.io.Serializable;

import market.FarmersMarket;

import cell.Plant;


public class SellProduce extends NetworkData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public final Type type = Type.SellProduce;
	public double dollars;
	
	
	public SellProduce(double total) {
		this.dollars = total;
	}
	
	

}
