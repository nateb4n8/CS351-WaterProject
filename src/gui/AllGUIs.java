package gui;


public class AllGUIs 
{
  static WaterProjectGUI[] guis = new WaterProjectGUI[20];
  private static int counter = 0;
public static void addGUI(WaterProjectGUI g)
{
  guis[counter] = g;
  counter++;
}
public static void messageGUIs(String s)
{
  for(int i = 0; i < counter;i++)
  {
    if(guis[i] != null)
    {
      guis[i].getListModel().addElement(s);	
    }
  }
}
public static void removeSellOffer(int index)
{
  for(int i = 0; i < counter;i++) 
  {
    if(guis[i] != null)
	{
	  guis[i].getListModel().remove(index);	
	}
  }	
}
public static void putMoney(int GuiNo, double amount)
{ 
  
  if(guis[GuiNo]!= null)
  {
    //guis[GuiNo].addMoney(amount);
	double total = guis[GuiNo].getMoney()+amount;
	guis[GuiNo].setMoney(total);
    guis[GuiNo].getMoneyLabel().setText("Money:("+guis[GuiNo].getMoney()+")");
  }
}
public static void subMoney(int GuiNo, double amount)
{
  if(guis[GuiNo] != null)
  {
    //guis[GuiNo].subtractMoney(amount);
	double total = guis[GuiNo].getMoney()-amount;
	guis[GuiNo].setMoney(total);
    guis[GuiNo].getMoneyLabel().setText("Money:("+guis[GuiNo].getMoney()+")");
  }
}
public static void addQ(int GuiNo, int amount)
{
  if(guis[GuiNo] != null)
  {
    //guis[GuiNo].addQuantity(amount);
	int total = guis[GuiNo].getQuantity() + amount;
	guis[GuiNo].setQuantity(total);
    guis[GuiNo].getQuantityLabel().setText("Quantity:("+guis[GuiNo].getQuantity()+")");
  }
}
public static void subQ(int GuiNo, int amount)
{
  if(guis[GuiNo] != null)
  {
    //guis[GuiNo].subQuantity(amount);
	int total = guis[GuiNo].getQuantity() - amount;
	guis[GuiNo].setQuantity(total);
	guis[GuiNo].getQuantityLabel().setText("Quantity:("+guis[GuiNo].getQuantity()+")");
  } 	
}
}

