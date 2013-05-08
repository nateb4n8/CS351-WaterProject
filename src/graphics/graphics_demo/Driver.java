import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Driver implements ChangeListener, ActionListener
{
	private JSlider xMax;
	private JSlider yMax;
	private JSlider zMax;
	
	private JButton land;
	private JButton water;
	private JButton toggle;
	
	private DisplayFarm2 dp;

	public Driver()
	{
		JFrame frame = new JFrame("DisplayFarm2");
		frame.setSize(500, 500);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();

		this.dp = new DisplayFarm2(new Farm(), panel);
		dp.setRange(0, 49, 0, 29, 0, 49);
		panel.setSize(frame.getWidth(), frame.getHeight());
		frame.add(panel);
		
		JFrame frame2 = new JFrame("Controls");
		frame2.setSize(500, 300);
		frame2.setLocation(700, 100);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = frame2.getContentPane();
		

		this.xMax = new JSlider(JSlider.HORIZONTAL, 0, 49, 49);
		xMax.setMajorTickSpacing(10);
		xMax.setSnapToTicks(true);
		xMax.setPaintTrack(true);
		xMax.setPaintLabels(true);
		
		this.xMax.addChangeListener(this);
		
		this.yMax = new JSlider(JSlider.HORIZONTAL, 0, 29, 29);
		yMax.setMajorTickSpacing(10);
		yMax.setSnapToTicks(true);
		yMax.setPaintTrack(true);
		yMax.setPaintLabels(true);
		
		this.yMax.addChangeListener(this);
		
		this.zMax = new JSlider(JSlider.HORIZONTAL, 0, 49, 49);
		zMax.setMajorTickSpacing(10);
		zMax.setSnapToTicks(true);
		zMax.setPaintTrack(true);
		zMax.setPaintLabels(true);
		
		this.zMax.addChangeListener(this);

		content.add(xMax, BorderLayout.NORTH);
		content.add(yMax, BorderLayout.CENTER);
		content.add(zMax, BorderLayout.SOUTH);
		
		JFrame frame3 = new JFrame("Controls");
		frame3.setSize(500, 300);
		frame3.setLocation(900, 100);
		frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content3 = frame3.getContentPane();
		
		this.land = new JButton("Land View");
		content3.add(this.land, BorderLayout.NORTH);
		this.land.addActionListener(this);
		
		this.water = new JButton("Water View");
		content3.add(this.water, BorderLayout.CENTER);
		this.water.addActionListener(this);
		
		this.toggle = new JButton("Toggle View");
		content3.add(this.toggle, BorderLayout.SOUTH);
		this.toggle.addActionListener(this);
		
		
		frame3.setVisible(true);
		
		
		frame2.setVisible(true);
		frame.setVisible(true);
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
	    JSlider source = (JSlider)e.getSource();
	    
	    if(source.getValueIsAdjusting() == true) 
	    {
	    	this.dp.setRange(0, this.xMax.getValue(), 0, this.yMax.getValue(), 0, this.zMax.getValue());
	    	//System.out.println("changing");
	    }		
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		
		if(source.equals(land))
		{
			this.dp.setLandView(true);
		}
		else if(source.equals(water))
		{
			this.dp.setWaterView(true);
		}
		else if(source.equals(toggle))
		{
			this.dp.setToggleView();
		}
		
	}
	
	
	public static void main(String[] args)
	{
		Driver d = new Driver();

	}







}
