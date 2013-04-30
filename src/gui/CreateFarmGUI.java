package gui;
import graphics.DisplayFarm;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import local_cont.Local_Control;

import XML_Handler.XML_Handler;

import topo.Topography;

import cell.*;


public class CreateFarmGUI extends JFrame implements ActionListener, ChangeListener {
	
  //modded stuff
  
  private Farm farm;
  private JPanel panel;
  private DisplayFarm displayFarm;
  private Local_Control cont;
  
  JFrame createFarmFrame;
	JFileChooser fc;
	
	private double latitude;
	private double longitude;
	private String configPath;
	
	private JTextField latTextField;
	private JTextField longTextField;
	private JTextField configPathTextField;
	private JFileChooser chooser;
	
	private JLabel latitudeText;
	private JLabel longText;
	private JLabel filePath;
	
	private JButton create;
	private JButton browse;
	private WaterProjectGUI g;
	public CreateFarmGUI(WaterProjectGUI GUI, Local_Control l) {
		cont = l;
		createFarmFrame = new JFrame("Create Farm");
		//createFarmFrame.setTitle("Create Farm");
	    createFarmFrame.setSize(600, 200);
	    createFarmFrame.setVisible(true);
	    createFarmFrame.setResizable(false);
		createFarmFrame.setLayout(null);
		createFarmFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		g = GUI;
		latTextField = new JTextField();
		latTextField.setBounds(100, 10, 100, 20);
		longTextField = new JTextField();
		longTextField.setBounds(100, 40, 100, 20);
		configPathTextField = new JTextField();
		configPathTextField.setBounds(100, 70, 350, 20);
		
		create = new JButton("Create");
		create.setBounds(100,100,90,25);
		create.addActionListener(this);
		createFarmFrame.add(create);
		
		browse = new JButton("Browse");
		browse.setBounds(100,130,90,25);
		browse.addActionListener(this);
		createFarmFrame.add(browse);
		
		latitudeText = new JLabel("Lattitude");
		latitudeText.setBounds(10, 10, 90, 20);
		
		longText = new JLabel("Longitude");
		longText.setBounds(10, 40, 90, 20);
		
		filePath = new JLabel("File Path");
		filePath.setBounds(10, 70, 90, 20);
		
		
		
		
		createFarmFrame.add(latTextField);
		createFarmFrame.add(longTextField);
		createFarmFrame.add(configPathTextField);
		createFarmFrame.add(latitudeText);
		createFarmFrame.add(longText);
		createFarmFrame.add(filePath);

		
		
		
	}
	
	


	public double getLatitude() {
		return latitude;
	}




	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}




	public double getLongitude() {
		return longitude;
	}




	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}




	public String getConfigPath() {
		return configPath;
	}




	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

    public DisplayFarm getDisplayFarm()
    {
      return displayFarm;	
    }


	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	
	public void actionPerformed(ActionEvent e) 
	{
	  Object obj = e.getSource();
	  if(obj == create)
	  {
	    this.setLatitude(Double.parseDouble(latTextField.getText()));
	    this.setLongitude(Double.parseDouble(longTextField.getText()));
	    this.setConfigPath(configPathTextField.getText());
	    //System.out.println("Lat="+this.getLatitude());
	    //System.out.println("Long="+this.getLongitude());
	    //System.out.println("Path="+this.getConfigPath());
	    createFarmFrame.dispose();
	    g.displayLat(this.getLatitude());
	    g.displayLong(this.getLongitude());
	    this.farm = cont.newFarm(this.getLatitude(), this.getLongitude(), this.getConfigPath());
	    //this.farm = Topography.createFarm(this.getLatitude(), this.getLongitude());
	    //XML_Handler xmlHandler = new XML_Handler();
	    //xmlHandler.initGround(farm, this.getConfigPath());
	    
	    this.displayFarm = new DisplayFarm(this.farm, this.panel);
	    g.setDisplayFarm(this.displayFarm);
	    g.setMoneyLabel();
	    
	  }
	  else if(obj == browse)
	  {
		if(chooser == null)
		{
		  chooser = new JFileChooser();
		}
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
		  
		  String s = chooser.getSelectedFile().getPath();
		  configPathTextField.setText(s);
		  chooser.setCurrentDirectory(chooser.getSelectedFile());
	    }	  
	  }
	}
	
	

}
