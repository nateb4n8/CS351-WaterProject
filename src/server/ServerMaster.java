//Michael Asplund

package server;


//import ServerWorker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import server.NetworkData.Type;
import timeKeeper.TimeKeeper;

public class ServerMaster implements KeyListener
{
  private ServerSocket serverSocket;
  private LinkedList<ServerWorker> allConnections = new LinkedList<ServerWorker>();
  private long stime; //Server start time, used for transaction times.
  private Catalog catalog;
  private TimeKeeper timeKeeper;
  private ServerWorker[][] farmgrid = new ServerWorker[2][2];
  private int farmcount = 0;
  
  public ServerMaster(int portNumber)
  {
    try
    {
      serverSocket = new ServerSocket(portNumber);
    }
    catch (IOException e)
    {
      System.err.println("Server error: Opening socket failed.");
      e.printStackTrace();
      System.exit(-1);
    }
    
    
    this.catalog = new Catalog();
    
    
    System.out.println("Press s to start");
    waitForConnection();
  }

  public void waitForConnection()
  { 
    while (true)
    {
      System.out.println("ServerMaster: waiting for Connection");
      try
      {
        Socket client = serverSocket.accept();
        ServerWorker worker = new ServerWorker(client, this);
        worker.start();
        System.out.println("ServerMaster: New Client Connection");
        allConnections.add(worker);
        makeGrid(worker);
      }
      catch (IOException e)
      {
        System.err.println("Server error: Failed to connect to client.");
        e.printStackTrace();
      }
    }
  }

  private void makeGrid(ServerWorker farm)
  {
    if(farmcount > farmgrid.length * farmgrid[0].length)
    {
      if(farmgrid.length > farmgrid[0].length)
      {
        farmgrid = new ServerWorker[farmgrid.length][farmgrid[0].length + 1];
      }
      else
      {
        farmgrid = new ServerWorker[farmgrid.length + 1][farmgrid[0].length];
      }
      int x = 0,y=0;
      for (ServerWorker workers : allConnections)
      {
        if(x >= farmgrid.length)
        {
          x = 0;
          y++;
        }
        farmgrid[x][y] = workers;
        x++;
      }
    }
    else
    {
      for(int x= 0; x < farmgrid.length; x++)
      {
        for(int y= 0; y < farmgrid[0].length; y++)
        {
          if(farmgrid[x][y] == null)
          {
            farmgrid[x][y] = farm;
            x = farmgrid.length;
            y = farmgrid[0].length;
          }
        }
      }
    }
    farmcount++;
  }
  
  public void cleanConnectionList()
  {
    allConnections.clear(); //Removes all ServerWorkers
  }
  
  /**
   * Removes the passed in ServerWorker thread from the list of connections.
   */
  public void removeWorker(ServerWorker sw)
  {
        System.out.println(sw.name + " has quit the game.");
        allConnections.remove(sw);
        int x=0, y=0;
        farmgrid = new ServerWorker[farmgrid.length][farmgrid[0].length];
        for (ServerWorker workers : allConnections)
        {
          if(x >= farmgrid.length)
          {
            x = 0;
            y++;
          }
          farmgrid[x][y] = workers;
          x++;
        }
  }
  
  public void broadcast(NetworkData data)
  {
    for (ServerWorker workers : allConnections)
    {
      workers.send(data);
    }
  }
  
  /** @return The single Catalog **/
  public Catalog getCatalog() { return this.catalog; }
  
  /** @return The server starting time **/
  public long getServerTime() { return this.stime; }

  //returns time server has been running
  private double timeDiff()
  {
    long time = System.nanoTime() - stime;
    double secDiff = (double) time / 1000000000.0;
    return secDiff;
  }

  /**
   * @return ServerMaster game's TimeKeeper
   */
  public TimeKeeper getTimeKeeper() {
    return timeKeeper;
}

public static void main(String args[])
  {
    int port = 0;
    try
    {
      port = Integer.parseInt(args[0]);
      if (port < 1) throw new Exception();
    }
    catch (Exception e)
    {
      System.out.println("Usage: ServerMaster portNumber");
      System.exit(0);
    }

    new ServerMaster(port);
  }

@Override
public void keyPressed(KeyEvent arg0)
{
  if(arg0.getKeyChar() == 's' || arg0.getKeyChar() == 'S')
  {
    //Set server start time.
    this.stime = System.nanoTime();
    this.timeKeeper = new TimeKeeper(822);//create new timeKeeper with 822ms = 1 day or 5min = 1yr
    NetworkData go = new Msg("me", "hi");
    go.type = Type.Start;
    broadcast(go);
  }
}

@Override
public void keyReleased(KeyEvent arg0)
{
  // TODO Auto-generated method stub
  
}

@Override
public void keyTyped(KeyEvent arg0)
{
  // TODO Auto-generated method stub
  
}
}

