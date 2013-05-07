package gui;

import graphics.DisplayFarm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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

import server.Catalog;
import server.Name;
import server.NetworkData;
import server.Offer;

import local_cont.Local_Control;

import XML_Handler.XML_Handler;

import cell.Crops;
import cell.Farm;
import cell.Plant;


/**
 * 
 * @author Inderpreet Singh
 * WaterProjectGUI displays the user's farm, money value, quantity value, and
 * has different options to use to interact with the farm, and plant crops.
 */
public class WaterProjectGUI extends JFrame implements ActionListener, ChangeListener
{
  
	
  private static final long serialVersionUID = 1L;
  
  
  JFrame frame;
  JPanel panel;
  JPanel border;
  JPanel border2;
  
  //JList lst, will display the list of offers available to buy.
  JList lst;
  //JScrollPane JSP, is just to scroll up and down the list of offers when there are
  //too many offers.
  JScrollPane JSP;
  //DisplayFarm display is the copy of the DisplayFarm, used to update
  //the display when the slider values change.
  DisplayFarm display;
  //Create a new default list model to be used for the offers list.
  DefaultListModel listModel = new DefaultListModel();
  
  //Local_Control cont is used to buy, sell "Offers" and to make money
  //from the crops that are planted in each of the four quadrants.
  private Local_Control cont;
 
  //sellInput1 is where the user will specify the unitPrice for each quantity of water.
  private JTextField sellInput1;
  //sellInput2 is where the user specifies the quantity he wishes to sell.
  private JTextField sellInput2;
  //butInput is where the buyer specifies how many quantities he wishes to purchase.
  private JTextField buyInput;
  
  //Create the JLabels that will be used to display text.
  private JLabel sellWaterLabel;
  private JLabel sellAmount;
  private JLabel sellQuantity;
  private JLabel buyQuantity;
  private JLabel moneyLabel;
  private JLabel quantityLabel;
  private JLabel monthLabel;
  //Create the JButtons users can press.
  private JButton sellButton;
  private JButton buyButton;
  private JButton plantButton;
  
  private JLabel plantQ1;
  private JLabel plantQ2;
  private JLabel plantQ3;
  private JLabel plantQ4;
  
  //These Combo Boxes will be used to pick a plant to crop in each quadrant.
  private JComboBox Q1;
  private JComboBox Q2;
  private JComboBox Q3;
  private JComboBox Q4;
  
  //Farm f, will hold the farm created when Create Farm button is pressed.
  Farm f;
  //adjusted boolean will be used to tell the displayFarm to update the axes.
  private boolean adjusted = false;
  private Timer myTimer;
  //Initialize the maxes and mins, but after create farm is pressed
  //it will use the farm's x,y,z axis. 
  private int maxX = 70;
  private int maxY = 70;
  private int maxZ = 12;
  private int valX1 = 0;
  private int valX2 = maxX;
  private int valY1 = 0;
  private int valY2 = maxY;
  private int valZ1 = 0;
  private int valZ2 = maxZ;
  
  
  
  //All the different String file paths needed for restore, load and save.
  private String loadPath;
  private String savePath;
  private String restorePath;
  
  //Create the sliders for the GUI. For all the axes, X, Y, and Z.
  JSlider slider_X1;
  JSlider slider_X2;
  JSlider slider_Y1;
  JSlider slider_Y2;
  JSlider slider_Z1;
  JSlider slider_Z2;
  
  //Create the JLabels for each of the sliders 
  //to show what each slider does and display the value of each slider.
  private JLabel x_label1;
  private JLabel x_label2;
  private JLabel y_label1;
  private JLabel y_label2;
  private JLabel z_label1;
  private JLabel z_label2;
  //Latitude and Longitude will display the latitude and longitude.
  private JLabel lat_label;
  private JLabel long_label;
  private JLabel sell_offers;
  //Create the start button that also acts as the pause button.
  private JButton startButton;
  //Load button will load a saved file.
  private JButton loadButton;
  //Save current Farm configuration.
  private JButton saveButton;
  //Restore the file 
  private JButton restoreButton;
  //Create Button will call Create Farm gui to enter in the latitude, longitude, 
  //and browse to choose the farm setup file.
  private JButton createButton;
  //All the plant types user can plant on the farm.
  private String[] plantTypes = {"None","Pintobeans","Sunflower","Amaranth","Chile",
		  "Sweetcorn","Summersquash","Wintersquash","Potatoes","Sweetpepper"}; 
  //Height and width of the gui.
  private int height;
  private int width;
  //The Gui's name, User specifies name when they create a WaterProjectGUI.
  private String GuiName;
  //Name will be the same as GuiName given by user.
  private Name clientName;
/**
 * WaterProjectGUI constructor creates the GUI with a jpanel for the displayFarm,
 * sliders to interact with the displayFarm, and various options to make sell offers
 * or plant crops to make money, or buy water offers available.
 * @param l (Local_Control)
 * @param name (Name user wants for the GUI)
 */
public WaterProjectGUI(Local_Control l)
{
	cont = l;
	
	/******************Insert Server Info Here*************************/
	cont.openConnection("ServerIP", 9999);
	/******************************************************************/
	
	width = 500;
	height = 500;
	
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	int x = (dim.width-width)/2;
	
	
	//rowHeight is used for the height to make the text and sliders.
	int rowHeight = 25;
	//button Width is for the width of the JButtons, like start.
	int buttonWidth = 90;
	//sliderWidth is how long to make the sliders.
	int sliderWidth = 200;
	//An input box comes up, in which the user inputs a name.
	//The name is then made into a type "Name", which will be sent to the
	//Server to connect. 
	String name = JOptionPane.showInputDialog("Please enter in a name.");
	GuiName = name;
	clientName = new Name(name);
	
	//Create a new JFrame that displays the GUI's specified name.
    frame = new JFrame("Water GUI: Name: "+GuiName);
    //Set the size, and set layout to null, so we can specify the bounds for all the
    //components.
    frame.setSize(width+300,height+255);
    frame.setLayout(null);
    //Set location on the screen, will open in the middle of screen. 
    frame.setLocation(x,0);
    
  
  	//Construct the list component that will display the offers of water rights.
  	lst = new JList (listModel);
  	JSP = new JScrollPane(lst);
  	lst.setMinimumSize(new Dimension(150,100));
  	//adjust size and set layout
  	setPreferredSize (new Dimension (329, 124));
  	setLayout(null);
  	//add list components
  	add (lst);
  	add (JSP);
  	JSP.getViewport().add(lst);
  	//set component bounds (only needed by Absolute Positioning)
  	JSP.setBounds (width+10, 230, 240, 220);
  	lst.setBounds (width+10, 220, 240, 220);
    
    
    //Add the list display to the frame.
    frame.add(JSP);
    //panel will be the area used by DisplayFarm.
    panel = new JPanel();
    //Needed borders to set up border outlines over the sliders and options.
    border = new JPanel();
    border2 = new JPanel();
   
    //Set up the view border outline, that will hold the sliders.
    border.setBorder(new TitledBorder("View"));
    this.getBorder().setLayout(null);
    this.getBorder().setBounds(5,height+1,348,170);
    this.getBorder().setBackground(new Color(238,238,238));
    //Set up the Options border outline, will hold the options to sell and plant crops.
    border2.setBorder(new TitledBorder("Options"));
    border2.setLayout(null);
    border2.setBounds(353,height+1,405,170);
    border2.setBackground(new Color(238,238,238));
    //Set the panel up.
    panel.setLayout(null);
    panel.setSize(width,height);
    panel.setBackground(Color.WHITE);
    
    
    
    
    //Dispose of the frame when user clicks on the "X".
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    //Create and define the bounds for the start button.
    startButton = new JButton("Start");
    startButton.setBounds(width+10,5,buttonWidth+10,rowHeight);
    
    //Create and set up create Farm button, and set the font smaller so it fits 
    //on the size of button we are using.
    createButton = new JButton("Create Farm");
    Font f = new Font(createButton.getFont().getName(),createButton.getFont().getStyle(),9);
    Font f2 = new Font(createButton.getFont().getName(),createButton.getFont().getStyle(),10);
   
    createButton.setFont(f);
    createButton.setBounds(width+10,35,buttonWidth+10,rowHeight);
    
    //Create and set up load, save, and restore buttons.
    loadButton = new JButton("Load");
    loadButton.setBounds(width+10,65,buttonWidth+10,rowHeight);
    
    saveButton = new JButton("Save");
    saveButton.setBounds(width+10,95,buttonWidth+10,rowHeight);
    
    restoreButton = new JButton("Restore");
    restoreButton.setBounds(width+10,125,buttonWidth+10,rowHeight);
    //Set up sell, buy, and plant buttons.
    sellButton = new JButton("Sell");
    sellButton.setBounds(683,height+15,55,rowHeight);
    
    buyButton = new JButton("Buy");
    buyButton.setBounds(width+10,480,55,rowHeight);
    
    plantButton = new JButton("Plant");
    plantButton.setBounds(585,height+88,65,rowHeight);
    
    //Add all the created buttons to the frame.
    frame.add(startButton);
    frame.add(createButton);
    frame.add(loadButton);
    frame.add(saveButton);
    frame.add(restoreButton);
    frame.add(sellButton);
    frame.add(buyButton);
    frame.add(plantButton);
    //Create the various labels to be displayed on the frame.
    sellWaterLabel = new JLabel("Sell Water");
    sellAmount = new JLabel("$ Amount/unit");
    sellQuantity = new JLabel("Quantity");
    sellAmount.setFont(f2);
    sellQuantity.setFont(f2);
    buyQuantity = new JLabel("Quantity");
    buyQuantity.setFont(f2);
    //Create the Jlabels that will show which combo box belongs to which quadrant.
    plantQ1 = new JLabel("Quadrant 1");
    plantQ2 = new JLabel("Quadrant 2");
    plantQ3 = new JLabel("Quadrant 3");
    plantQ4 = new JLabel("Quadrant 4");
    //Create the combo boxes that have a list of all plant types to choose from.
    Q1 = new JComboBox(plantTypes);
    Q2 = new JComboBox(plantTypes);
    Q3 = new JComboBox(plantTypes);
    Q4 = new JComboBox(plantTypes);
    
    //x,y,z labels will display the value of the sliders position.
    x_label1 = new JLabel("X min (0) ");
    x_label2 = new JLabel("X max ("+maxX+")");
    
    y_label1 = new JLabel("Y min (0)");
    y_label2 = new JLabel("Y max ("+maxY+")");
    
    z_label1 = new JLabel("Z min (0)");
    z_label2 = new JLabel("Z max ("+maxZ+")");
    
    //Display the lat, long, and create Sell Offers label to show where the list is.
    lat_label = new JLabel("Latitude (in decimal) = ");
    long_label = new JLabel("Longitude (in decimal) = ");
    sell_offers = new JLabel("Sell Offers");
    monthLabel = new JLabel("");
    //Money Label will display the money the user has currently.
    moneyLabel = new JLabel("Money: "+cont.getMoney());
    //Quantity Label displays the current quantity of water rights the user has.
    quantityLabel = new JLabel("Quantity: "+cont.getQuantity()+" ");
    moneyLabel.setFont(f2);
    quantityLabel.setFont(f2);
    lat_label.setFont(f2);
    long_label.setFont(f2);
    //Add the various labels, and combo boxes to the frame.
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
    frame.add(monthLabel);
    //Set bounds of all the labels and combo boxes, pick where you want them to be
    //on the frame. Parameters are (x position, y position, width of component, height of component.)
    sellWaterLabel.setBounds(355,height+15,sliderWidth+30,rowHeight);
    sellAmount.setBounds(521,height+15,100,rowHeight);
    sellQuantity.setBounds(629,height+15,55,rowHeight);
    
   
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
    monthLabel.setBounds(width+25+buttonWidth,5,155,rowHeight);
    //Create the sliders corresponding to each of the slider labels. These sliders are the ones
    //that you move and pick a value.
    
    slider_X1 = new JSlider(JSlider.HORIZONTAL,0,maxX,0);
    slider_X2 = new JSlider(JSlider.HORIZONTAL,0,maxX,0);
    slider_X2.setInverted(true);
    slider_Y1 = new JSlider(JSlider.HORIZONTAL,0,maxY,0);
    slider_Y2 = new JSlider(JSlider.HORIZONTAL,0,maxY,0);
    slider_Y2.setInverted(true);
    slider_Z1 = new JSlider(JSlider.HORIZONTAL,0,maxZ,0);
    slider_Z2 = new JSlider(JSlider.HORIZONTAL,0,maxZ,0);
    slider_Z2.setInverted(true);
    
    //Add the sliders and set the bounds for the sliders. 
    frame.add(slider_X1);
    frame.add(slider_X2);
    frame.add(slider_Y1);
    frame.add(slider_Y2);
    frame.add(slider_Z1);
    frame.add(slider_Z2);
    
    slider_X1.setBounds(120,height+10,sliderWidth,rowHeight);
    slider_X2.setBounds(120,height+35,sliderWidth,rowHeight);
    slider_Y1.setBounds(120,height+60,sliderWidth,rowHeight);
    slider_Y2.setBounds(120,height+85,sliderWidth,rowHeight);
    slider_Z1.setBounds(120,height+110,sliderWidth,rowHeight);
    slider_Z2.setBounds(120,height+135,sliderWidth,rowHeight);
    //JTextFields will be used so the user can specify the unit price and quantity
    //that he wishes to sell.
    sellInput1 = new JTextField();
    sellInput2 = new JTextField();
    //This JTextField is used to specify the quantity the buyer wishes to buy.
    buyInput = new JTextField();
    
    //Add the JTextFields to the frame, and set the bounds.
    frame.add(buyInput);
    frame.add(sellInput1);
    frame.add(sellInput2);
    sellInput1.setBounds(420,height+15,100,rowHeight);
    sellInput2.setBounds(599,height+15,30,rowHeight);
    buyInput.setBounds(width+70,480,30,rowHeight);
    buyQuantity.setBounds(width+105,480,45,rowHeight);
   
    //AddActionListener for the inputs.
    sellInput1.addActionListener(this);
    sellInput2.addActionListener(this);
    
    
    myTimer = new Timer(100,this);
    //Add action listeners to all the buttons, so it notifies us when they
    //are pressed and we can do something when they are.
    startButton.addActionListener(this);
    createButton.addActionListener(this);
    loadButton.addActionListener(this);
    saveButton.addActionListener(this);
    restoreButton.addActionListener(this);
    sellButton.addActionListener(this);
    buyButton.addActionListener(this);
    plantButton.addActionListener(this);
    
    //Add change listenes for the sliders so it notifies us when 
    //the sliders move.
    slider_X1.addChangeListener(this);
    slider_X2.addChangeListener(this);
    slider_Y1.addChangeListener(this);
    slider_Y2.addChangeListener(this);
    slider_Z1.addChangeListener(this);
    slider_Z2.addChangeListener(this);
    //Add the panel and borders to the frame.
    frame.add(border);
    frame.add(border2);
    frame.add(panel);
    
    //Finally, set the frame visibility to true. 
    frame.setVisible(true);
    
    }

//The method is used to check if the input the user supplies is actually a number.
public static boolean isNumeric(String str)
{
  NumberFormat formatter = NumberFormat.getInstance();
  ParsePosition pos = new ParsePosition(0);
  formatter.parse(str, pos);
  return str.length() == pos.getIndex();
}
//Set the maxX, maxY, maxZ for the sliders, will be called
//when the farm is created to set the maximum for each axis.
public void setMaxX(int max)
{
  maxX = max;	
}
public void setMaxY(int max)
{
  maxY = max;	
}
public void setMaxZ(int max)
{
  maxZ = max;	
}
//Create Farm Gui will set the initial values of valX1 & X2 valY1 & y2, and valZ1 & Z2.
public void setSliderValues(int X1, int X2, int Y1, int Y2, int Z1, int Z2)
{
  valX1 = X1;
  valX2 = X2;
  valY1 = Y1;
  valY2 = Y2;
  valZ1 = Z1;
  valZ2 = Z2;
}
//Create Farm Gui will set the initial text of the slider jlabels.
public void setSliderText(int minX, int maxX, int minY, int maxY, int minZ, int maxZ)
{
  x_label1.setText("X min ("+minX+")");
  x_label2.setText("X max ("+maxX+")");
  y_label1.setText("Y min ("+minY+")");
  y_label2.setText("Y max ("+maxY+")");
  z_label1.setText("Z min ("+minZ+")");
  z_label2.setText("Z max ("+maxZ+")");
}
//Get the gui's height, create farm gui needs to use it.
public int getH()
{
  return height;
}
//Return the frame, used by create farm gui.
public JFrame getFrame()
{
  return frame;
}
//Returns the gui's name specified by the person who creates the WaterProjectGui.
//Will be used to display the subtracted/added money to the right Gui.
public String getGuiName()
{
  return GuiName;	
}
//Returns the client's name, which is the same name as the GuiName,
//but just in "Name" type instead of a string.
public Name getClientName()
{
  return clientName;	
}
//Returns the listModel, to update or clear it.
public DefaultListModel getListModel()
{
  return listModel;	
}

public JLabel getMoneyLabel()
{
  return moneyLabel;	
}
//Set the money label to the specified double value.
//Will be called by Local_Control to update the money.
public void setMoneyLabel()
{
	this.getMoneyLabel().setText("Money: "+cont.getMoney()+" ");
}
public JLabel getQuantityLabel()
{
  return quantityLabel;	
}
//Sets the quantity label to the changed quantity in cont.
//Will be called by cont, to change.
public void setQuantityLabel()
{
  this.getQuantityLabel().setText("Quantity: "+cont.getQuantity());	
}
//Used to get the JTextField for the input where the buyer
//specifies the quantity he wishes to purchase.
public JTextField getBuyQuantity()
{
  return buyInput;	
}
//Method that returns the panel that DisplayFarm will use.
public JPanel getPanel()
{
  return panel;
}
//I used this method to get the border when I needed to.
public JPanel getBorder()
{
  return border;	
}
//This method will be used when the user can plant again.
//When the user hits plant it gets disabled until, the user
//can plant again. 
public void enablePlantButton()
{
  plantButton.setEnabled(true);	
}
public void setMonth(String month)
{
  monthLabel.setText("Month: "+month);	
}
//I needed to flip the values so that the 
//Slider would highlight from the correct side.
//So these are just private mehtods used by the sliders.
private int flipValueX(int val)
{
  return maxX - val;
}
private int flipValueY(int val)
{
  return maxY - val;	
}
private int flipValueZ(int val)
{
  return maxZ - val;	
}
//Create farm gui uses this method to give the
//Gui a copy of the farm created.
public void setFarm(Farm farm)
{
  f = farm;	
}
//Returns the farm.
public Farm getFarm()
{
  return f;	
}
//Returns the displayFarm, needed to tell Display farm to update axes.
public void setDisplayFarm(DisplayFarm dis)
{
  display = dis;	
}
//Used by create farm gui, will give it the user's input of the latitude and longitude.
public void displayLat(double latitude)
{
  lat_label.setText("Latitude (in decimal) = " + latitude);	
}
public void displayLong(double longitude)
{
  long_label.setText("Longitude (in decimal) = "+ longitude);	
}
//Get the paths chosen by the user, when load, save, or restore is pressed.
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
//This is what will update the list for each GUI. It takes in 
//the ArrayList<Offers> specified in Catalog class. It will read
//each offer and display it on the list of the GUI.
public void updateSellOffers(ArrayList<Offer> offers)
{
  this.getListModel().clear();
  for(int i=0;i<offers.size();i++)
  {
    if(offers.get(i) != null)
    {
      //Get the Offer at the array index.
      Offer of = offers.get(i);
      //Get the merchant, quantity, unitPrice of each Offer.
      String merchant = of.merchant;
      int quantity = of.quantity;
      double unitPrice = of.unitPrice;
      //Create the format of the string to be used, and add it to the Gui's list.
      String s = "Seller: "+merchant+", "+"UnitPrice: "+unitPrice+", Quantity:"+quantity;
      this.getListModel().addElement(s);
    }
    
  }
}
//displayMessage can be used for displaying messages such as "You bought the offer",
//Or "Buying offer failed!" and so on.
public void displayMessage(String message)
{
  JOptionPane.showMessageDialog(null, message,"Message",JOptionPane.INFORMATION_MESSAGE);
}
//Removes, the Offer at the specified index of the list. 
//The list and the ArrayList of Offers will have the same index.
public void removeSellOffer(int index)
{
  this.getListModel().remove(index);	
}
//ActionPerformed is used when the user presses aa certain button.
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
	       cont.loadFarm(loadPath);
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
	    
		String sell = sellInput1.getText();
		String sellQ = sellInput2.getText();
		int sellQuan = 0;
		if(isNumeric(sellQ) == true)
		{
		   sellQuan = Integer.parseInt(sellQ);
		}
		
	    if(isNumeric(sell) == true && isNumeric(sellQ) == true && sellQuan <= cont.getQuantity())
	    {
	      String merchant = getGuiName();
	      int quantity = Integer.parseInt(sellInput2.getText());
	      double unitPrice = Double.parseDouble(sellInput1.getText());
	      
	      
	      cont.sell_water(quantity,unitPrice,merchant);
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
	    
	  }
	}
	else if(obj == plantButton)
	{   //Get each plant selected in each quadrant and send to cont.plantCrops.
		String[] plants = new String[4];
		plants[0] = (String) Q1.getSelectedItem();
		plants[1] = (String) Q2.getSelectedItem();
		plants[2] = (String) Q3.getSelectedItem();
		plants[3] = (String) Q4.getSelectedItem();
		cont.plantCrops(plants);
	  //Disable the plant button, and will be enabled when the user can plant again.
      //Method to enable is enablePlantButton(). 
	  plantButton.setEnabled(false);
	}
	  	
}

//State changed is used for the sliders. 
//Lets us know when the sliders are changing.
public void stateChanged(ChangeEvent e) 
{
  Object source = e.getSource();
  //Whenever a slider is changed, adjusted is set to true.
  //At the end, it calls setRange() method in the Display farm
  //if adjusted is true. 
  if(source == slider_X1)
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
    valX2 = flipValueX(slider_X2.getValue());
    if(valX2 <= valX1)
    {
      slider_X2.setValue(flipValueX(valX1+1));
      valX2 = flipValueX(slider_X2.getValue());
      
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
	valY2 = flipValueY(slider_Y2.getValue());
	if(valY2 <= valY1)
	{
	  slider_Y2.setValue(flipValueY(valY1+1));
	  valY2 = flipValueY(slider_Y2.getValue());
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
    valZ2 = flipValueZ(slider_Z2.getValue());
    if(valZ2 <= valZ1)
	{
	  slider_Z2.setValue(flipValueZ(valZ2+1));
	  valZ2 = flipValueZ(slider_Z2.getValue());
	}
    z_label2.setText("Z max ("+valZ2+")");
  }
  if(adjusted == true)
  {
	//Set adjusted to false now, so it wont update again
	//until the sliders are changed again.
    adjusted = false;
    //Pass all the X1,X2,Y1,Y2,Z1,Z2 values in order to update graphics.
    if(display != null)
    {
      //Call setRange method if adjusted == true.
      display.setRange(valX1,valX2,valY1,valY2,valZ1,valZ2);
    }
  }
}



  
  
}

