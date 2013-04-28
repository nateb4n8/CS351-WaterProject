package local_cont;

import server.Offer;
import topo.Topography;
import XML_Handler.XML_Handler;
import gui.WaterProjectGUI;
import cell.Crops;
import cell.Farm;
import cell.Plant;

public class Local_Control {
	private Farm f;
	private static WaterProjectGUI gui;
	public double getMoney(){

		return (f==null)?0:f.getMoney();
	}
	public Farm newFarm(double latitude, double longitude, String path){
	  f = Topography.createFarm(latitude, longitude);
	  XML_Handler.initGround(f, path);
	  return f;
	}
	public void saveFarm(String path){
		try {
			XML_Handler.save(f, path);
		} catch (Exception e) {
		}
	}
	public Farm loadFarm(String path){
		f = XML_Handler.restore(path);
		return f;
	}
	public void plantCrops(String[] crops){
		int len = crops.length;
		for (int i=0;i<len;i++){
			if (!crops[i].equalsIgnoreCase("None")){
				try {
					f.setCrop(i, new Crops(Plant.getPlantType(crops[i]), f, i));
				} catch (Exception e) {
				}
			}
		}
	}
	public void addMoney(double amt){
		f.setMoney(f.getMoney()+amt);
		gui.setMoneyLabel();
	}
	public void subMoney(double amt){
		f.setMoney(f.getMoney()-amt);
		gui.setMoneyLabel();		
	}
	public Client_Msg[] sell_crops(){
		Client_Msg[] msgs = new Client_Msg[4];
		for (int i=0;i<4;i++){
			msgs[i] = new Sell_Plant(f.getCrop(i), f.getCropQty(i));
		}
		return msgs;
	}
	public static void main(String args[]){
		Local_Control me = new Local_Control();
		gui = new WaterProjectGUI(me);
	}
	public int getQuantity() {
		return (f==null)?10:f.getWaterQty();
	}
	public void addMoney(Receive_Money msg){
		double total = f.getMoney() + msg.get_amt();
		f.setMoney(total);
	}
	public void addQ(int amount)
	{
	    //guis[GuiNo].addQuantity(amount);
		int total = getQuantity() + amount;
		f.setWaterQty(total);
	    gui.getQuantityLabel().setText("Quantity:("+getQuantity()+")");
	}
	public void subQ(int amount)
	{
	    //guis[GuiNo].subQuantity(amount);
		int total = getQuantity() - amount;
		f.setWaterQty(total);
		gui.getQuantityLabel().setText("Quantity:("+getQuantity()+")");
	}
	public void sell_water(int qty, double amt, String name){
		Offer sell = new Offer(name, qty, amt);
		Client_Msg msg = new Sell_Water(sell);
		// send message to server here.
	}
	public void buy(String s, int buyQ, int idx) {
	    String amount = getSellingValue(s);
	    double sellAm = Double.parseDouble(amount);
	    System.out.println("Sell Amount= "+sellAm);
	    System.out.println("Amount: "+amount);
	    String quant = getSellingQuantity(s);
	    int sellQuant = Integer.parseInt(quant);
	    System.out.println("Quantity: "+quant);
	    String GuiNum = getGuiNofromStr(s);
	    int sellerNum = Integer.parseInt(GuiNum);
	    System.out.println("GUI Number is "+sellerNum);
	    double guisMon = getMoney();
	    int guisQuan = getQuantity();
	    int buyerNum = gui.getGuiNumber();
	    System.out.println("BuyerNum= "+buyerNum);
	    Offer buy = new Offer(Integer.toString(buyerNum),buyQ,sellAm);
	    double total = sellAm*sellQuant;
	    if (guisMon >= total)
	    {
		    if(buyQ == sellQuant)
		    {
		    	gui.removeSellOffer(idx);
		    }
		    else if(buyQ > sellQuant)
		    {
		    	gui.removeSellOffer(idx);
		      int newQuantity = buyQ - sellQuant;
		      
		    }
		    else if(buyQ < sellQuant)
		    {
		      int newQuantity = sellQuant - buyQ;
		      String newString = setQuantity(s,newQuantity,sellerNum);
		      gui.removeSellOffer(idx);
		    }
		    //Send Offer to server.
	    }

	}
	public String getSellingValue(String s)
	{
	  String amount = "";
	  for(int i=1;i<s.length();i++)
	  {
		char c = s.charAt(i);
		if(c == ' ')
		{
		  break;	
		}
		else
		{
		  amount = amount + c;
		}
	  }
	  return amount;
	}
	public String getSellingQuantity(String s)
	{
	  int start;
	 
	  for(start = 0;start<s.length();start++)
	  {
		char c = s.charAt(start);
		if(c == ':')
		{
		  break;
		}
	    
	    
	  }
	  String quantity = "";
	  start++;
	  for(int i = start;i<s.length();i++)
	  {
		char c = s.charAt(i);
		if(c == ';')
		{
		  break;	
		}
		else
		{
	      quantity = quantity + c;
	      i++;
		}
	  }
	  return quantity;
	}
	public String getGuiNofromStr(String str)
	{
	  String number = "";
	  char c;
	  int start;
	  
	  for(start = str.length()-4;start<str.length();start++)
	  {
	    c = str.charAt(start);
	    if(c == ':')
	    {
	      start++;	
	      break;	
	    }
	  }
	  for(int i=start;i<str.length();i++)
	  {
	    number = number + str.charAt(i);	  
	  }
	  return number;
	}
	public String setQuantity(String s, int quantity, int sellerNo)
	{
	  char c;
	  String string = "";
	  
	  for(int i=0;i<s.length();i++)
	  {
		c = s.charAt(i);
	    if(c == ':')
	    {
	      break;	
	    }
	    string = string + s.charAt(i);
	  }
	  string = string + ':';
	  string = string + quantity;
	  String addition = " ;GUI No:"+sellerNo;
	  string = string + addition;
	  return string;
	}

}
