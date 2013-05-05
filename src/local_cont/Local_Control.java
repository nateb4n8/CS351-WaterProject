package local_cont;

import server.Catalog;
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
	Catalog catalog;
	
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
		gui = new WaterProjectGUI(me,"WGUI");
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
		gui.setQuantityLabel();
	    //gui.getQuantityLabel().setText("Quantity:("+getQuantity()+")");
	}
	public void subQ(int amount)
	{
	    //guis[GuiNo].subQuantity(amount);
		int total = getQuantity() - amount;
		f.setWaterQty(total);
		gui.setQuantityLabel();
		//gui.getQuantityLabel().setText("Quantity:("+getQuantity()+")");
	}
	public void sell_water(int qty, double amt, String name){
		Offer sell = new Offer(name, qty, amt);
		Client_Msg msg = new Sell_Water(sell);
		
		// send message to server here.
		if(catalog == null)
		{
		  catalog = new Catalog();	
		}
		catalog.addSellOffer(sell);
		gui.updateSellOffers(catalog.getOffersList());
	}
	public void buy(String s, int buyQ, int idx) {
	    String amount = getSellingValue(s);
	    double sellAm = Double.parseDouble(amount);
	    System.out.println("Sell Amount= "+sellAm);
	    System.out.println("Amount: "+amount);
	    String quant = getSellingQuantity(s);
	    int sellQuant = Integer.parseInt(quant);
	    System.out.println("Quantity: "+quant);
	    String sellerGuiName = getGuiNofromStr(s);
	    
	    System.out.println("GUI Name is "+sellerGuiName);
	    double guisMon = getMoney();
	    int guisQuan = getQuantity();
	    String buyer = gui.getGuiName();
	    System.out.println("BuyerNum= "+buyer);
	    Offer buy = new Offer(buyer,buyQ,sellAm);
	    System.out.println(gui.getGuiName()+":"+sellerGuiName);
	    boolean check = buyer.equals(sellerGuiName);
	    System.out.println("Boolean:"+check);
	    double total = sellAm*sellQuant;
	    if (guisMon >= total)
	    {
		    if(buyQ == sellQuant)
		    {
		    	//If the buyer is the seller, just remove the offer, cancels offer.
			    //No money or quantity is added or subtracted, offer is canceled.
			    if(buyer.equals(sellerGuiName))
			    {
			      catalog.removeOfferAt(idx);
			      gui.updateSellOffers(catalog.getOffersList());
			      gui.getBuyQuantity().setText("");
			    }
			    else
			    {
		    	  subMoney(total);
		    	  addQ(sellQuant);
		    	  //gui.removeSellOffer(idx);
		    	  catalog.removeOfferAt(idx);
		    	  gui.updateSellOffers(catalog.getOffersList());
		    	  gui.getBuyQuantity().setText("");
			    }
		    	
		    }
		    else if(buyQ > sellQuant)
		    {
		      //If the buyer is the seller, just remove the offer, cancels offer.
		      //No money or quantity is added or subtracted, offer is canceled.
			  if(buyer.equals(sellerGuiName))
			  {
			    catalog.removeOfferAt(idx);
			    gui.updateSellOffers(catalog.getOffersList());
			    gui.getBuyQuantity().setText("");
			  }
			  else
			  {
		        subMoney(total);
		        addQ(sellQuant);
		        //gui.removeSellOffer(idx);
		        catalog.removeOfferAt(idx);
		        int newQuantity = buyQ - sellQuant;
		        gui.getBuyQuantity().setText(Integer.toString(newQuantity));
		        gui.updateSellOffers(catalog.getOffersList());
			  }
		      
		    }
		    else if(buyQ < sellQuant)
		    {
		      int newQuantity = sellQuant - buyQ;
		      //If the buyer is the seller, just remove the offer, cancels offer.
		      //No money or quantity is added or subtracted, offer is canceled.
			  if(buyer.equals(sellerGuiName))
			  {
			    catalog.removeOfferAt(idx);
				gui.updateSellOffers(catalog.getOffersList());
				Offer of = new Offer(sellerGuiName,newQuantity,sellAm);
				catalog.addSellOffer(of);
				gui.updateSellOffers(catalog.getOffersList());
				gui.getBuyQuantity().setText(Integer.toString(newQuantity));
			  }
			  else
			  {
		        
		        //String newString = setQuantity(s,newQuantity,sellAm,sellerNum);
		        Offer of = new Offer(sellerGuiName,newQuantity,sellAm);
		        subMoney(total);
		        addQ(sellQuant);
		        catalog.removeOfferAt(idx);
		        catalog.addSellOffer(of);
		        //System.out.println(newString);
		        //gui.removeSellOffer(idx);
		        gui.updateSellOffers(catalog.getOffersList());
			  }
		      
		    }
		    
		    //Send Offer to server.
	    }

	}
	public String getSellingValue(String s)
	{
	  String amount = "";
	  int i;
	  for(i=s.length()-24;i<s.length();i++)
	  {
		char c = s.charAt(i);
		if(c == ':')
		{
		  break;	
		}
		//else
		//{
		  //amount = amount + c;
		//}
	  }
	  i= i+2;
	  
	  for(int j = i;j<s.length();j++)
	  {
	    char c = s.charAt(j);
	    if(c == ',')
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
	 
	  for(start = s.length()-5;start<s.length();start++)
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
		quantity = quantity + c;
	    i++;
		
	  }
	  return quantity;
	}
	public String getGuiNofromStr(String str)
	{
	  String number = "";
	  char c;
	  int start;
	  
	  for(start = 0;start<str.length();start++)
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
		char ch= str.charAt(i);
		if(ch == ',')
		{
		  break;	
		}
	    number = number + ch;
	    
	  }
	  return number.trim();
	}
	public String setQuantity(String s, int quantity,double unitPrice,int sellerNo)
	{
	  String result = "Seller: "+sellerNo+", "+"UnitPrice: "+unitPrice+", "+"Quantity"+":"+quantity;
	  return result;
	}

	/**
	 * Adds the passed in GUI to Local_Control if one desn't exist(null)
	 * @param gui
	 */
	public void addWaterProjectGUI(WaterProjectGUI gui)
	{
	  if (this.gui == null) this.gui = gui;
	}
}
