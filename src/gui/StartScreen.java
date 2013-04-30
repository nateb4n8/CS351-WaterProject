/**
 * Author: Nathan Acosta
 * Date: Apr 29, 2013
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import local_cont.Local_Control;

/**
 * @author nacosta
 * The StartScreen is a simple JFrame to display the type of session to
 * start, either a client session or a server session.
 */
public class StartScreen extends JFrame
{
  private StartScreen startScreen; //JButton access

  public StartScreen()
  {
    this.startScreen = this;
    Dimension jfDim = new Dimension(300, 300);
    Dimension screenDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    
    this.setTitle("Select Session");
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    this.pack(); //Allows insets to be found.
    
    int leftRightInsets = this.getInsets().left + this.getInsets().right;
    int topBottomInsets = this.getInsets().top + this.getInsets().bottom;
    
    //Compensate for the window borders/insets.
    int windowWidth = jfDim.width + leftRightInsets;
    int windowHeight = jfDim.height + topBottomInsets;
    
    this.setPreferredSize(new Dimension(windowWidth, windowHeight));
    
    int xTLC = (int)((screenDim.width * 0.5) - (windowWidth * 0.5));
    int yTLC = (int)((screenDim.height * 0.5) - (windowHeight * 0.5));
    
    //Center window on screen.
    this.setBounds(xTLC, yTLC, windowWidth, windowHeight);
    
    Canvas canvas = new Canvas();
    this.add(canvas);
    
    this.setVisible(true);
  }
  
  /**
   * @author nacosta
   * Canvas creates to buttons to start sessions and closes the StartScreen
   * once a selection has been made.
   */
  private class Canvas extends JPanel
  {
    private JButton createClient;
    private JButton createServer;
    
    public Canvas()
    {
      this.createClient = new JButton("Create Client");
      this.createServer = new JButton("Create Server");
      
      createClient.addActionListener(new ActionListener()
      { @Override
        public void actionPerformed(ActionEvent e)
        {
          Local_Control lc = new Local_Control();
          lc.addWaterProjectGUI(new WaterProjectGUI(lc));
          startScreen.setVisible(false);
        }
      });
      
      createServer.addActionListener(new ActionListener()
      { @Override
        public void actionPerformed(ActionEvent e)
        {
          //Server_Control startScreen = new Server_Control();
          //startScreen.addWaterProjectGUI(new WaterProjectGUI(startScreen));
          startScreen.setVisible(false);
        }
      });
      
      this.add(createClient);
      this.add(createServer);
    }
  }
    
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    StartScreen start = new StartScreen();
  }

}
