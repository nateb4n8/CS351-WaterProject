package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParsePosition;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import local_cont.Local_Control;

import XML_Handler.XML_Handler;

import cell.Crops;
import cell.Farm;
import cell.Plant;



public class WaterProjectGUI extends JFrame implements ActionListener, ChangeListener
{
  
	
  private static final long serialVersionUID = 1L;
  static int instanceCounter = 0;
  int counter = 0;
  
  JFrame frame;
  JPanel panel;
  JPanel border;
  JPanel border2;
  
  JList lst;

  DefaultListModel listModel = new DefaultListModel();
  private Local_Control cont;
  
  //private double money;
  //private int quantity;
  
  JScrollPane JSP;
  private JTextField sellInput1;
  private JTextField sellInput2;
  private JTextField buyInput;
  
  private JLabel sellWaterLabel;
  private JLabel sellAmount;
  private JLabel sellQuantity;
  private JLabel buyQuantity;
  private JLabel moneyLabel;
  private JLabel quantityLabel;
  
  private JButton sellButton;
  private JButton buyButton;
  private JButton plantButton;
  
  private JLabel plantQ1;
  private JLabel plantQ2;
  private JLabel plantQ3;
  private JLabel plantQ4;
  
  
  private JComboBox Q1;
  private JComboBox Q2;
  private JComboBox Q3;
  private JComboBox Q4;
  
  Farm f;
  private boolean adjusted = false;
  private Timer myTimer;
  private int max = 150;
  private int valX1 = 0;
  private int valX2 = max;
  private int valY1 = 0;
  private int valY2 = max;
  private int valZ1 = 0;
  private int valZ2 = max;
  
  
  
  //All the different String file paths needed for restore, load and save.
  private String loadPath;
  private String savePath;
  private String restorePath;
  
//Create the sliders for the GUI.
  private JSlider slider_1;
  private JSlider slider_2;
  //private JSlider slider_3;
  private JSlider slider_X1;
  private JSlider slider_X2;
  private JSlider slider_Y1;
  private JSlider slider_Y2;
  private JSlider slider_Z1;
  private JSlider slider_Z2;
  
  //Create the JLabels for each of the sliders to show what each slider does and display the value of each slider.
  private JLabel slider_num1;
  private JLabel slider_num2;
  //private JLabel slider_num3;
  private JLabel x_label1;
  private JLabel x_label2;
  private JLabel y_label1;
  private JLabel y_label2;
  private JLabel z_label1;
  private JLabel z_label2;
  private JLabel lat_label;
  private JLabel long_label;
  private JLabel sell_offers;
  //Create the start button that also acts as the pause button.
  private JButton startButton;
  private JButton loadButton;
  private JButton saveButton;
  private JButton restoreButton;
  private JButton createButton;
  private String[] plantTypes = {"None","Pintobeans","Sunflower","Amaranth","Chile",
		  "Sweetcorn","Summersquash","Wintersquash","Potatoes","Sweetpepper"}; 
  
 

public WaterProjectGUI(Local_Control l)
{
	  
	instanceCounter++;
	counter = instanceCounter;
	//money = 1000.00;
    //quantity = 10;
	cont = l;
	int width = 500;
	int height = 500;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	int x = (dim.width-width)/2;
	
	
	//rowHeight is used for the height to make the text and sliders.
	int rowHeight = 25;
	//button Width is for the width of the JButtons, like start.
	int buttonWidth = 90;
	//sliderWidth is how long to make the sliders.
	int sliderWidth = 200;
    frame = new JFrame("Water GUI Number: "+getGuiNumber());
    frame.setSize(width+265,height+255);
    frame.setLayout(null);
    frame.setLocation(x,0);
    
  
  	//construct components
  	lst = new JList (listModel);
  	JSP = new JScrollPane(lst);
  	lst.setMinimumSize(new Dimension(150,100));
  	//adjust size and set layout
  	setPreferredSize (new Dimension (329, 124));
  	setLayout (null);
  	//add components
  	add (lst);
  	add (JSP);
  	JSP.getViewport().add(lst);
  	//set component bounds (only needed by Absolute Positioning)
  	JSP.setBounds (width+10, 230, 180, 220);
  	lst.setBounds (width+10, 220, 180, 220);
    
    
    
    frame.add(JSP);
    
    panel = new JPanel();
    border = new JPanel();
    border2 = new JPanel();
   
    
    
   
    
    
    border.setBorder(new TitledBorder("View"));
    this.getBorder().setLayout(null);
    this.getBorder().setBounds(5,height+1,348,170);
    this.getBorder().setBackground(new Color(238,238,238));
  
    border2.setBorder(new TitledBorder("Options"));
    border2.setLayout(null);
    border2.setBounds(353,height+1,405,170);
    border2.setBackground(new Color(238,238,238));
    
    panel.setLayout(null);
    panel.setSize(width,height);
    panel.setBackground(Color.WHITE);
    
    
    
    
    //frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    //JButton test = new JButton("Test");
    //test.setBounds(width-100,height-50,90,25);
    startButton = new JButton("Start");
    startButton.setBounds(width+10,5,buttonWidth+10,rowHeight);
    
    createButton = new JButton("Create Farm");
    Font f = new Font(createButton.getFont().getName(),createButton.getFont().getStyle(),9);
    Font f2 = new Font(createButton.getFont().getName(),createButton.getFont().getStyle(),10);
    //System.out.println("Font:"+createButton.getFont());
    createButton.setFont(f);
    createButton.setBounds(width+10,35,buttonWidth+10,rowHeight);
    
    loadButton = new JButton("Load");
    loadButton.setBounds(width+10,65,buttonWidth+10,rowHeight);
    
    saveButton = new JButton("Save");
    saveButton.setBounds(width+10,95,buttonWidth+10,rowHeight);
    
    restoreButton = new JButton("Restore");
    restoreButton.setBounds(width+10,125,buttonWidth+10,rowHeight);
    
    sellButton = new JButton("Sell");
    sellButton.setBounds(683,height+15,55,rowHeight);
    
    buyButton = new JButton("Buy");
    buyButton.setBounds(width+10,480,55,rowHeight);
    
    plantButton = new JButton("Plant");
    plantButton.setBounds(585,height+88,65,rowHeight);
    
    frame.add(startButton);
    frame.add(createButton);
    frame.add(loadButton);
    frame.add(saveButton);
    frame.add(restoreButton);
    frame.add(sellButton);
    frame.add(buyButton);
    frame.add(plantButton);
    //panel.add(test);
    sellWaterLabel = new JLabel("Sell Water");
    sellAmount = new JLabel("$ Amount/unit");
    sellQuantity = new JLabel("Quantity");
    sellAmount.setFont(f2);
    sellQuantity.setFont(f2);
    buyQuantity = new JLabel("Quantity");
    buyQuantity.setFont(f2);
    
    plantQ1 = new JLabel("Quadrant 1");
    plantQ2 = new JLabel("Quadrant 2");
    plantQ3 = new JLabel("Quadrant 3");
    plantQ4 = new JLabel("Quadrant 4");
    Q1 = new JComboBox(plantTypes);
    Q2 = new JComboBox(plantTypes);
    Q3 = new JComboBox(plantTypes);
    Q4 = new JComboBox(plantTypes);
    
    
    slider_num1 = new JLabel("Initial water content of soil (50)");
    slider_num1.setFont(f2);
    slider_num2 = new JLabel("Depth of roots (10)");
    //slider_num3 = new JLabel("Slider value (0)");
    x_label1 = new JLabel("X min (0) ");
    x_label2 = new JLabel("X max (150)");
    
    y_label1 = new JLabel("Y min (0)");
    y_label2 = new JLabel("Y max (150)");
    
    z_label1 = new JLabel("Z min (0)");
    z_label2 = new JLabel("Z max (150)");
    
    lat_label = new JLabel("Latitude (in decimal) = ");
    long_label = new JLabel("Longitude (in decimal) = ");
    sell_offers = new JLabel("Sell Offers");
    
    moneyLabel = new JLabel("Money: "+cont.getMoney());
    quantityLabel = new JLabel("Quantity: "+cont.getQuantity()+" ");
    moneyLabel.setFont(f2);
    quantityLabel.setFont(f2);
    lat_label.setFont(f2);
    long_label.setFont(f2);
    //frame.add(slider_num1);
    //frame.add(slider_num2);
    //frame.add(slider_num3);
    frame.add(sellWaterLabel);
    frame.add(sellAmount);
    frame.add(sellQuantity);
    frame.add(buyQuantity);
    frame.add(x_label1);
    frame.add(x_label2);
    frame.add(y_label1);
    frame.add(y_label2);
    frame.add(z_label1);
    frame.add(z_label2);
    frame.add(lat_label);
    frame.add(long_label);
    frame.add(sell_offers);
    frame.add(plantQ1);
    frame.add(plantQ2);
    frame.add(plantQ3);
    frame.add(plantQ4);
    frame.add(Q1);
    frame.add(Q2);
    frame.add(Q3);
    frame.add(Q4);
    frame.add(quantityLabel);
    frame.add(moneyLabel);
    //System.out.println(width-145);
    sellWaterLabel.setBounds(355,height+15,sliderWidth+30,rowHeight);
    sellAmount.setBounds(521,height+15,100,rowHeight);
    sellQuantity.setBounds(629,height+15,55,rowHeight);
    
    //slider_num1.setBounds(355,height+20,sliderWidth+30,rowHeight);
    //slider_num2.setBounds(355,height+50,sliderWidth,rowHeight);
    //slider_num3.setBounds(width-145,height+50,sliderWidth,rowHeight);
    x_label1.setBounds(10,height+10,sliderWidth,rowHeight);
    x_label2.setBounds(10,height+35,sliderWidth,rowHeight);
    y_label1.setBounds(10,height+60,sliderWidth,rowHeight);
    y_label2.setBounds(10,height+85,sliderWidth,rowHeight);
    z_label1.setBounds(10,height+110,sliderWidth,rowHeight);
    z_label2.setBounds(10,height+135,sliderWidth,rowHeight);
    lat_label.setBounds(width+10,155,buttonWidth+80,rowHeight);
    long_label.setBounds(width+10,185,buttonWidth+80,rowHeight);
    sell_offers.setBounds(width+10, 210, buttonWidth, rowHeight);
    plantQ1.setBounds(355,height+45,70,rowHeight);
    plantQ2.setBounds(355,height+90,70,rowHeight);
    plantQ3.setBounds(470,height+45,70,rowHeight);
    plantQ4.setBounds(470,height+90,70,rowHeight);
    Q1.setBounds(355,height+65,110,rowHeight);
    Q2.setBounds(355,height+110,110,rowHeight);
    Q3.setBounds(470,height+65,110,rowHeight);
    Q4.setBounds(470,height+110,110,rowHeight);
    quantityLabel.setBounds(width+120,450,80,rowHeight);
    moneyLabel.setBounds(width+10,450,100,rowHeight);
    //Create the sliders corresponding to each of the slider labels. These sliders are the ones
    //that you move and pick a value.
    //slider_1 = new JSlider(JSlider.HORIZONTAL,0,100,50);
    //slider_2 = new JSlider(JSlider.HORIZONTAL,1, 30,10);
    //slider_3 = new JSlider(JSlider.HORIZONTAL,0,100,0);
    slider_X1 = new JSlider(JSlider.HORIZONTAL,0,150,0);
    slider_X2 = new JSlider(JSlider.HORIZONTAL,0,150,0);
    slider_X2.setInverted(true);
    slider_Y1 = new JSlider(JSlider.HORIZONTAL,0,150,0);
    slider_Y2 = new JSlider(JSlider.HORIZONTAL,0,150,0);
    slider_Y2.setInverted(true);
    slider_Z1 = new JSlider(JSlider.HORIZONTAL,0,150,0);
    slider_Z2 = new JSlider(JSlider.HORIZONTAL,0,150,0);
    slider_Z2.setInverted(true);
    
    //frame.add(slider_1);
    //frame.add(slider_2);
    //frame.add(slider_3);
    frame.add(slider_X1);
    frame.add(slider_X2);
    frame.add(slider_Y1);
    frame.add(slider_Y2);
    frame.add(slider_Z1);
    frame.add(slider_Z2);
    
    
    
    //slider_1.setBounds(355+sliderWidth,height+20,sliderWidth,rowHeight);
    //slider_2.setBounds(355+sliderWidth,height+50,sliderWidth,rowHeight);
    //slider_3.setBounds(width+30,height+55,sliderWidth,rowHeight);
    slider_X1.setBounds(120,height+10,sliderWidth,rowHeight);
    slider_X2.setBounds(120,height+35,sliderWidth,rowHeight);
    slider_Y1.setBounds(120,height+60,sliderWidth,rowHeight);
    slider_Y2.setBounds(120,height+85,sliderWidth,rowHeight);
    slider_Z1.setBounds(120,height+110,sliderWidth,rowHeight);
    slider_Z2.setBounds(120,height+135,sliderWidth,rowHeight);
    
    sellInput1 = new JTextField();
    sellInput2 = new JTextField();
    buyInput = new JTextField();
    
    frame.add(buyInput);
    frame.add(sellInput1);
    frame.add(sellInput2);
    sellInput1.setBounds(420,height+15,100,rowHeight);
    sellInput2.setBounds(599,height+15,30,rowHeight);
    buyInput.setBounds(width+70,480,30,rowHeight);
    buyQuantity.setBounds(width+105,480,45,rowHeight);
    //sellInput1.getDocument().addDocumentListener();
    //sellInput2.getDocument().addDocumentListener();
    
    sellInput1.addActionListener(this);
    sellInput2.addActionListener(this);
    
    myTimer = new Timer(100,this);
    
    startButton.addActionListener(this);
    createButton.addActionListener(this);
    loadButton.addActionListener(this);
    saveButton.addActionListener(this);
    restoreButton.addActionListener(this);
    sellButton.addActionListener(this);
    buyButton.addActionListener(this);
    plantButton.addActionListener(this);
    
    //slider_1.addChangeListener(this);
    //slider_2.addChangeListener(this);
    //slider_3.addChangeListener(this);
    slider_X1.addChangeListener(this);
    slider_X2.addChangeListener(this);
    slider_Y1.addChangeListener(this);
    slider_Y2.addChangeListener(this);
    slider_Z1.addChangeListener(this);
    slider_Z2.addChangeListener(this);
    
    frame.add(border);
    frame.add(border2);
    frame.add(panel);
   
    //frame.pack();
    frame.setVisible(true);
    //AllGUIs.addGUI(this);
    
    }

 
public static boolean isNumeric(String str)
{
  NumberFormat formatter = NumberFormat.getInstance();
  ParsePosition pos = new ParsePosition(0);
  formatter.parse(str, pos);
  return str.length() == pos.getIndex();
}

//public int getQuantity()
//{
//  return quantity;	
//}
//public double getMoney()
//{
//  return money;	
//}
//public void addMoney(double funds)
//{
//  money = money + funds;
//  
//}
//public void subtractMoney(double mon)
//{
//  money = money - mon;
// 
//}
//public void addQuantity(int quan)
//{
//  quantity = quantity + quan;
//  
//}
//public void subQuantity(int quan)
//{
//  quantity = quantity - quan;
//  
//}
//public void setMoney(double amount)
//{
//  money = amount;	
//}
//public void setQuantity(int amount)
//{
//  quantity = amount;	
//}
public int getGuiNumber()
{
  return counter;	
}
public DefaultListModel getListModel()
{
  return listModel;	
}
public JLabel getMoneyLabel()
{
  return moneyLabel;	
}
public void setMoneyLabel()
{
	this.getMoneyLabel().setText("Money: "+cont.getMoney()+" ");
}
public JLabel getQuantityLabel()
{
  return quantityLabel;	
}
public JPanel getPanel()
{
  return panel;
}
public JPanel getBorder()
{
  return border;	
}
//public String getSellingValue(String s)
//{
//  String amount = "";
//  for(int i=1;i<s.length();i++)
//  {
//	char c = s.charAt(i);
//	if(c == ' ')
//	{
//	  break;	
//	}
//	else
//	{
//	  amount = amount + c;
//	}
// }
//  return amount;
//}
//public String getSellingQuantity(String s)
//{
//  int start;
// 
//  for(start = 0;start<s.length();start++)
//  {
//	char c = s.charAt(start);
//	if(c == ':')
//	{
//	  break;
//	}
//    
//    
//  }
//  String quantity = "";
//  start++;
//  for(int i = start;i<s.length();i++)
//  {
//	char c = s.charAt(i);
//	if(c == ';')
//	{
//	  break;	
//	}
//	else
//	{
//      quantity = quantity + c;
//      i++;
//	}
//  }
//  return quantity;
//}
//public String setQuantity(String s, int quantity, int sellerNo)
//{
//  char c;
//  String string = "";
//  
//  for(int i=0;i<s.length();i++)
//  {
//	c = s.charAt(i);
//    if(c == ':')
//    {
//      break;	
//    }
//    string = string + s.charAt(i);
//  }
//  string = string + ':';
//  string = string + quantity;
//  String addition = " ;GUI No:"+sellerNo;
//  string = string + addition;
//  return string;
//}
//public String getGuiNofromStr(String str)
//{
//  String number = "";
//  char c;
//  int start;
//  
//  for(start = str.length()-4;start<str.length();start++)
//  {
//    c = str.charAt(start);
//    if(c == ':')
//    {
//      start++;	
//      break;	
//    }
//  }
//  for(int i=start;i<str.length();i++)
//  {
//    number = number + str.charAt(i);	  
//  }
//  return number;
//}
/*private static Plant getPlantType(String p)
{
  Plant plant = null;
  if(p.equalsIgnoreCase("Pintobeans"))
  {
    plant = Plant.PINTOBEANS;  	  
  }
  else if(p.equalsIgnoreCase("Sunflower"))
  {
    plant = Plant.SUNFLOWER;	  
  }
  else if(p.equalsIgnoreCase("Amaranth"))
  {
    plant = Plant.AMARANTH;	  
  }
  else if(p.equalsIgnoreCase("Chile"))
  {
    plant = Plant.CHILE;	  
  }
  else if(p.equalsIgnoreCase("Sweetcorn"))
  {
    plant = Plant.SWEETCORN;	  
  }
  else if(p.equalsIgnoreCase("Summersquash"))
  {
    plant = Plant.SUMMERSQUASH;	  
  }
  else if(p.equalsIgnoreCase("Wintersquash"))
  {
    plant = Plant.WINTERSQUASH;	  
  }
  else if(p.equalsIgnoreCase("Potatoes"))
  {
    plant = Plant.POTATOES;	  
  }
  else if(p.equalsIgnoreCase("Sweetpepper"))
  {
    plant = Plant.SWEETPEPPER;	  
  }
  return plant;
  
}*/
private int flipValue(int val)
{
  return max - val;
}
public void setFarm(Farm farm)
{
  f = farm;	
}
public Farm getFarm()
{
  return f;	
}
public void displayLat(double latitude)
{
  lat_label.setText("Latitude (in decimal) = " + latitude);	
}
public void displayLong(double longitude)
{
  long_label.setText("Longitude (in decimal) = "+ longitude);	
}
public String getLoadPath()
{
  return loadPath;	
}
public String getSavePath()
{
  return savePath;
}
public String getRestorePath()
{
  return restorePath;	
}
public void removeSellOffer(int index){
  this.getListModel().remove(index);	
}
 
public void actionPerformed(ActionEvent e) 
{
	Object obj = e.getSource();
	if(obj == startButton)
    {
	  
      if(myTimer.isRunning())
      {
        myTimer.stop();
        startButton.setText("Start");
      }
      //Else set the text to pause and timer will be running.
      else
      {
        startButton.setText("Pause");
        
        myTimer.start();
      }
    }
	else if(obj == loadButton)
	{
	  JFileChooser chooser = new JFileChooser();
	  int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) 
	    {
	       loadPath = chooser.getSelectedFile().getPath();
	       System.out.println("You chose to open this file: "+getLoadPath());
	       
	    }
	}
	else if(obj == createButton)
	{
	  CreateFarmGUI createFarmG = new  CreateFarmGUI(this,cont);
	  createFarmG.isVisible();
	  
	}
	else if(obj == saveButton)
	{
	  JFileChooser chooser = new JFileChooser();
	  int returnVal = chooser.showSaveDialog(this);
	  if(returnVal == JFileChooser.APPROVE_OPTION)
	  {
	    savePath = chooser.getSelectedFile().getPath();
	    System.out.println("Save Path is: "+getSavePath());
	    //XML_Handler xmlHandler = new XML_Handler();
	    cont.saveFarm(getSavePath());
	  }
	}
	else if(obj == restoreButton)
	{
	  JFileChooser chooser = new JFileChooser();
	  int returnVal = chooser.showOpenDialog(this);
	  if(returnVal == JFileChooser.APPROVE_OPTION)
	  {
	    restorePath = chooser.getSelectedFile().getPath();
	    System.out.println("Restore Path of file is: "+getRestorePath());
	    cont.loadFarm(getRestorePath());
	  }
	}
	else if(obj == sellButton)
	{
	  if(sellInput1.getText().equals("") || sellInput1.getText().equals(null)
		|| sellInput2.getText().equals("") || sellInput2.getText().equals(null))
	  {
		  
	  }
	  else
	  {
	    //System.out.println("Sell: "+sellInput1.getText());
	    //System.out.println("SellQ: "+sellInput2.getText());
		String sell = sellInput1.getText();
		String sellQ = sellInput2.getText();
		int sellQuan = Integer.parseInt(sellQ);
		
	    if(isNumeric(sell) == true && isNumeric(sellQ) == true && sellQuan <= cont.getQuantity())
	    {
		  //String s = "$"+sellInput1.getText()+" per unit; " +"Quantity:"+ sellInput2.getText()+" ;GUI No:"+getGuiNumber();
		  //AllGUIs.messageGUIs(s);
	      cont.sell_water(Integer.parseInt(sellInput2.getText()), 
	    		          Double.parseDouble(sellInput1.getText()),
	    		          Integer.toString(getGuiNumber()));
	    }
	    sellInput1.setText("");
	    sellInput2.setText("");
	  }
		
	}
	else if(obj == buyButton)
	{
	  int i = lst.getSelectedIndex();
	  int buyQ = 0;
	  String buyQuan = buyInput.getText();
	  if(buyQuan.equals("") || buyQuan.equals(null))
	  {
	    buyQuan = "0";
	  }
	  buyQ = Integer.parseInt(buyQuan);
	  // if(i == -1 || buyQuan.equals(""))  buyQuan cannnot equal "" because of 2 lines above.
	  if (i == -1)
	  {
	  	  
	  }
	  else
	  {
	    String s = (String) lst.getModel().getElementAt(i);
	    System.out.println(s);
	    cont.buy(s,buyQ,i);
	    /* String amount = getSellingValue(s);
	    double sellAm = Double.parseDouble(amount);
	    System.out.println("Sell Amount= "+sellAm);
	    System.out.println("Amount: "+amount);
	    String quant = getSellingQuantity(s);
	    int sellQuant = Integer.parseInt(quant);
	    System.out.println("Quantity: "+quant);
	    String GuiNum = getGuiNofromStr(s);
	    int sellerNum = Integer.parseInt(GuiNum);
	    System.out.println("GUI Number is "+sellerNum);
	    double guisMon = cont.getMoney();
	    int guisQuan = this.getQuantity();
	    int buyerNum = this.getGuiNumber();
	    System.out.println("BuyerNum= "+buyerNum);
	    double total = sellAm*sellQuant;
	    if(buyQ == sellQuant && guisMon >= total)
	    {
	      AllGUIs.removeSellOffer(i);
	      AllGUIs.subMoney(buyerNum,total);
	      AllGUIs.putMoney(sellerNum,total);
	    }
	    else if(buyQ > sellQuant && guisMon >= total)
	    {
	      AllGUIs.removeSellOffer(i);
	      int newQuantity = buyQ - sellQuant;
	      buyInput.setText(""+newQuantity);
	      AllGUIs.subMoney(getGuiNumber(), total);
	      AllGUIs.putMoney(sellerNum, total);
	      
	    }
	    else if(buyQ < sellQuant && guisMon >= total)
	    {
	      int newQuantity = sellQuant - buyQ;
	      String newString = setQuantity(s,newQuantity,sellerNum);
	      //AllGUIs.removeSellOffer(i);
	      //AllGUIs.messageGUIs(newString);
	      //AllGUIs.subMoney(getGuiNumber(), total);
	      //AllGUIs.putMoney(sellerNum, total);
	    } */
	  }
	}
	else if(obj == plantButton)
	{
		String[] plants = new String[4];
		plants[0] = (String) Q1.getSelectedItem();
		plants[1] = (String) Q2.getSelectedItem();
		plants[2] = (String) Q3.getSelectedItem();
		plants[3] = (String) Q4.getSelectedItem();
		cont.plantCrops(plants);
	 /* String plant1 = (String) Q1.getSelectedItem();
	  String plant2 = (String) Q2.getSelectedItem();
	  String plant3 = (String) Q3.getSelectedItem();
	  String plant4 = (String) Q4.getSelectedItem();
	  if(plant1.equalsIgnoreCase("None"))
	  {
	    	  
	  }
	  else
	  {
	    Plant p = getPlantType(plant1);
	    try {Crops c1 = new Crops(p,f,1);
		} catch (Exception e1) 
		{
		}
	  }
	  if(plant2.equalsIgnoreCase("None"))
	  {
	    	  
	  }
	  else
	  {
	    Plant p = getPlantType(plant2);
	    try {Crops c2 = new Crops(p,f,2);
		} catch (Exception e1){
		}
	  }
	  if(plant3.equalsIgnoreCase("None"))
	  {
	    
	  }
	  else
	  {
	    Plant p = getPlantType(plant3);
	    try {Crops c3 = new Crops(p,f,3);
		} catch (Exception e1){
		}
	  }
	  if(plant4.equalsIgnoreCase("None"))
	  {
	    	  
	  }
	  else
	  {
	    Plant p = getPlantType(plant4);
	    try {Crops c4 = new Crops(p,f,4);
		} catch (Exception e1){
		}
	  } */
	  plantButton.setEnabled(false);
	}
	  	
	
	
	
		
	    
	
	
}


public void stateChanged(ChangeEvent e) 
{
  Object source = e.getSource();
  if(source == slider_1)
  {
    int val = slider_1.getValue();
    slider_num1.setText("Initial water content of soil ("+val+")");
    
  }
  else if(source == slider_2)
  {
    int val = slider_2.getValue();
    slider_num2.setText("Depth of roots ("+val+")");
  }
  //else if(source == slider_3)
  //{
    //int val = slider_3.getValue();
    //slider_num3.setText("Slider value ("+val+")");
  //}
  else if(source == slider_X1)
  {
	adjusted = true;
    valX1 = slider_X1.getValue();
    if(valX1 >= valX2)
    {
      slider_X1.setValue(valX2-1);
      valX1 = slider_X1.getValue();
    }
    x_label1.setText("X min ("+valX1+")");
  }
  else if(source == slider_X2)
  {
	adjusted = true;
    valX2 = flipValue(slider_X2.getValue());
    if(valX2 <= valX1)
    {
      slider_X2.setValue(flipValue(valX1+1));
      valX2 = flipValue(slider_X2.getValue());
      
    }
    x_label2.setText("X max ("+valX2+")");
  }
  else if(source == slider_Y1)
  {
	adjusted = true;
    valY1 = slider_Y1.getValue();
    if(valY1 >= valY2)
    {
      slider_Y1.setValue(valY2-1);
      valY1 = slider_Y1.getValue();
    }
    y_label1.setText("Y min ("+valY1+")");
  }
  else if(source == slider_Y2)
  {
	adjusted = true;
	valY2 = flipValue(slider_Y2.getValue());
	if(valY2 <= valY1)
	{
	  slider_Y2.setValue(flipValue(valY1+1));
	  valY2 = flipValue(slider_Y2.getValue());
	}
    
    y_label2.setText("Y max ("+valY2+")");
  }
  else if(source == slider_Z1)
  {
	adjusted = true;
    valZ1 = slider_Z1.getValue();
    if(valZ1 >= valZ2)
	{
	  slider_Z1.setValue(valZ2-1);
	  valZ1 = slider_Z1.getValue();
	}
    z_label1.setText("Z min ("+valZ1+")");
  }
  else if(source == slider_Z2)
  {
	adjusted = true;
    valZ2 = flipValue(slider_Z2.getValue());
    if(valZ2 <= valZ1)
	{
	  slider_Z2.setValue(flipValue(valZ2+1));
	  valZ2 = flipValue(slider_Z2.getValue());
	}
    z_label2.setText("Z max ("+valZ2+")");
  }
  if(adjusted == true)
  {
    adjusted = false;
    //Pass all the X1,X2,Y1,Y2,Z1,Z2 values in order to update graphics.
  }
}



  
  
}

