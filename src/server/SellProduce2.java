package server;

import java.io.Serializable;

import market.FarmersMarket;

import cell.Plant;


public class SellProduce2 extends NetworkData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public final Type type = Type.SellProduce;
	public int quantity;
	public Plant plant;
	
	
	public SellProduce2(int quantity, Plant plant) {
		this.quantity = quantity;
		this.plant = plant;
	}
	
	

}