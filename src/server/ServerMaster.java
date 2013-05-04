package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerMaster
{
  private ServerSocket serverSocket;
  private LinkedList<ServerWorker> allConnections = new LinkedList<ServerWorker>();
  private long time; //Server start time, used for transaction times.
  private Catalog catalog;

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
    //Set server start time.
    this.time = System.nanoTime();
    
    this.catalog = new Catalog();
    
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
      }
      catch (IOException e)
      {
        System.err.println("Server error: Failed to connect to client.");
        e.printStackTrace();
      }
    }
  }

  public void cleanConnectionList()
  {
    allConnections.clear(); //Removes all ServerWorkers
  }
  
  /**
   * Removes the passed in ServerWorker thread from the list of connections.
   */
  public void removeWorkerFromList(ServerWorker sw)
  {
        System.out.println(sw.name + " has quit the game.");
        allConnections.remove(sw);     
  }
  
  /** @return The single Catalog **/
  public Catalog getCatalog() { return this.catalog; }
  
  /** @return The server starting time **/
  public long getServerTime() { return this.time; }

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
}
