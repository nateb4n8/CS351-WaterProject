package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker extends Thread
{
  private Socket client;
  private ObjectOutputStream clientOutStream;
  private ObjectInputStream clientInStream;
  private ServerMaster server; //To communicate with server.
  public String name;

  public ServerWorker(Socket client, ServerMaster server)
  {
    this.client = client;
    this.server = server;

    try
    {
      clientOutStream = new ObjectOutputStream(client.getOutputStream());
    }
    catch (IOException e)
    {
      System.err.println("Server Worker: Could not open output stream");
      e.printStackTrace();
    }
    try
    {
      clientInStream = new ObjectInputStream(client.getInputStream());
    }
    catch (IOException e)
    {
      System.err.println("Server Worker: Could not open input stream");
      e.printStackTrace();
    }
  }
  
  //Called by ServerMaster
  public void sendOffer(Offer offer)
  {
    System.out.println("ServerWorker.send( Offer )");
    try { clientOutStream.writeObject(offer); }
    catch (IOException e) { e.printStackTrace(); }
  }
  
  public void run()
  {
    while (true)
    {
      try
      {
        Offer offer = (Offer) (clientInStream.readObject());
        System.out.println("Offer: "+offer.merchant+" : "+offer.quantity+" : "+offer.unitPrice);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

}
