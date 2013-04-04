package gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class WaterProjectGUI extends JFrame implements ActionListener, ChangeListener
{
  JFrame frame;
  JPanel panel;
  private boolean adjusted = false;
  private Timer myTimer;
  private int valX1 = 0;
  private int valX2 = 100;
  private int valY1 = 0;
  private int valY2 = 100;
  private int valZ1 = 0;
  private int valZ2 = 100;
  
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
  //Create the start button that also acts as the pause button.
  private JButton startButton;
  private JButton loadButton;
  private JButton saveButton;
  private JButton restoreButton;
  private JButton createButton;
  
  public WaterProjectGUI(int x, int y, int width, int height)
  {
	  
	//rowHeight is used for the height to make the text and sliders.
	int rowHeight = 25;
	//button Width is for the width of the JButtons, like start.
	int buttonWidth = 90;
	//sliderWidth is how long to make the sliders.
	int sliderWidth = 200;
    frame = new JFrame("Water GUI");
    frame.setSize(width+230,height+230);
    
    panel = new JPanel(); //Graphics panel
    panel.setLayout(null);
    panel.setSize(width,height);
    panel.setBackground(Color.WHITE);
    frame.setLayout(null);
    
    frame.add(panel);
    
    frame.setVisible(true);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //JButton test = new JButton("Test");
    //test.setBounds(width-100,height-50,90,25);
    startButton = new JButton("Start");
    startButton.setBounds(width+10,5,buttonWidth,rowHeight);
    
    createButton = new JButton("Create Farm");
    createButton.setBounds(width+10,35,buttonWidth+30,rowHeight);
    
    loadButton = new JButton("Load");
    loadButton.setBounds(width+10,65,buttonWidth,rowHeight);
    
    saveButton = new JButton("Save");
    saveButton.setBounds(width+10,95,buttonWidth,rowHeight);
    
    restoreButton = new JButton("Restore");
    restoreButton.setBounds(width+10,125,buttonWidth,rowHeight);
    
    frame.add(startButton);
    frame.add(createButton);
    frame.add(loadButton);
    frame.add(saveButton);
    frame.add(restoreButton);
    //panel.add(test);
    
    slider_num1 = new JLabel("Initial water content of soil (50)");
    slider_num2 = new JLabel("Depth of roots (10)");
    //slider_num3 = new JLabel("Slider value (0)");
    x_label1 = new JLabel("X value (0) ");
    x_label2 = new JLabel("X value (100)");
    
    y_label1 = new JLabel("Y value (0)");
    y_label2 = new JLabel("Y value (100)");
    
    z_label1 = new JLabel("Z value (0)");
    z_label2 = new JLabel("Z value (100)");
    
    lat_label = new JLabel("Latitude (in decimal) = ");
    long_label = new JLabel("Longitude (in decimal) = ");
    frame.add(slider_num1);
    frame.add(slider_num2);
    //frame.add(slider_num3);
    
    frame.add(x_label1);
    frame.add(x_label2);
    frame.add(y_label1);
    frame.add(y_label2);
    frame.add(z_label1);
    frame.add(z_label2);
    frame.add(lat_label);
    frame.add(long_label);
    slider_num1.setBounds(width-145,height+10,sliderWidth+30,rowHeight);
    slider_num2.setBounds(width-145,height+30,sliderWidth,rowHeight);
    //slider_num3.setBounds(width-145,height+50,sliderWidth,rowHeight);
    x_label1.setBounds(5,height+10,sliderWidth,rowHeight);
    x_label2.setBounds(5,height+35,sliderWidth,rowHeight);
    y_label1.setBounds(5,height+60,sliderWidth,rowHeight);
    y_label2.setBounds(5,height+85,sliderWidth,rowHeight);
    z_label1.setBounds(5,height+110,sliderWidth,rowHeight);
    z_label2.setBounds(5,height+135,sliderWidth,rowHeight);
    lat_label.setBounds(width+10,155,buttonWidth+80,rowHeight);
    long_label.setBounds(width+10,185,buttonWidth+80,rowHeight);
    
    //Create the sliders corresponding to each of the slider labels. These sliders are the ones
    //that you move and pick a value.
    slider_1 = new JSlider(JSlider.HORIZONTAL,0,100,50);
    slider_2 = new JSlider(JSlider.HORIZONTAL,1, 30,10);
    //slider_3 = new JSlider(JSlider.HORIZONTAL,0,100,0);
    slider_X1 = new JSlider(JSlider.HORIZONTAL,0,100,0);
    slider_X2 = new JSlider(JSlider.HORIZONTAL,0,100,100);
    slider_Y1 = new JSlider(JSlider.HORIZONTAL,0,100,0);
    slider_Y2 = new JSlider(JSlider.HORIZONTAL,0,100,100);
    slider_Z1 = new JSlider(JSlider.HORIZONTAL,0,100,0);
    slider_Z2 = new JSlider(JSlider.HORIZONTAL,0,100,100);
    
    frame.add(slider_1);
    frame.add(slider_2);
    //frame.add(slider_3);
    frame.add(slider_X1);
    frame.add(slider_X2);
    frame.add(slider_Y1);
    frame.add(slider_Y2);
    frame.add(slider_Z1);
    frame.add(slider_Z2);
    
    slider_1.setBounds(width+30,height+10,sliderWidth,rowHeight);
    slider_2.setBounds(width+30,height+30,sliderWidth,rowHeight);
    //slider_3.setBounds(width+30,height+55,sliderWidth,rowHeight);
    slider_X1.setBounds(120,height+10,sliderWidth,rowHeight);
    slider_X2.setBounds(120,height+35,sliderWidth,rowHeight);
    slider_Y1.setBounds(120,height+60,sliderWidth,rowHeight);
    slider_Y2.setBounds(120,height+85,sliderWidth,rowHeight);
    slider_Z1.setBounds(120,height+110,sliderWidth,rowHeight);
    slider_Z2.setBounds(120,height+135,sliderWidth,rowHeight);
    
    myTimer = new Timer(100,this);
    
    startButton.addActionListener(this);
    createButton.addActionListener(this);
    loadButton.addActionListener(this);
    saveButton.addActionListener(this);
    restoreButton.addActionListener(this);
    
    slider_1.addChangeListener(this);
    slider_2.addChangeListener(this);
    //slider_3.addChangeListener(this);
    slider_X1.addChangeListener(this);
    slider_X2.addChangeListener(this);
    slider_Y1.addChangeListener(this);
    slider_Y2.addChangeListener(this);
    slider_Z1.addChangeListener(this);
    slider_Z2.addChangeListener(this);
    
    
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
	  CreateFarmGUI createFarmG = new  CreateFarmGUI(this);
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
	  }
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
    x_label1.setText("X value ("+valX1+")");
  }
  else if(source == slider_X2)
  {
	adjusted = true;
    valX2 = slider_X2.getValue();
    if(valX2 <= valX1)
    {
      slider_X2.setValue(valX1+1);
      valX2 = slider_X2.getValue();
      
    }
    x_label2.setText("X value ("+valX2+")");
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
    y_label1.setText("Y value ("+valY1+")");
  }
  else if(source == slider_Y2)
  {
	adjusted = true;
	if(valY2 <= valY1)
	{
	  slider_Y2.setValue(valY1+1);
	  valY2 = slider_Y2.getValue();
	}
    valY2 = slider_Y2.getValue();
    y_label2.setText("Y value ("+valY2+")");
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
    z_label1.setText("Z value ("+valZ1+")");
  }
  else if(source == slider_Z2)
  {
	adjusted = true;
    valZ2 = slider_Z2.getValue();
    if(valZ2 <= valZ1)
	{
	  slider_Z2.setValue(valZ2+1);
	  valZ2 = slider_Z2.getValue();
	}
    z_label2.setText("Z value ("+valZ2+")");
  }
  if(adjusted == true)
  {
    adjusted = false;
    //Pass all the X1,X2,Y1,Y2,Z1,Z2 values in order to update graphicsView.
  }
}
  
  
}
