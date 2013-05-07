//Michael Asplund

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

//import com.sun.org.apache.bcel.internal.generic.IINC;
//talk to michael if you really need this

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

  public void send(NetworkData data)
  {
    System.out.println("ServerWorker.send(data)");
    try
    {
      clientOutStream.writeObject(data);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public void run()
  {
    while (true)
    {
      try
      {
        Offer offer = (Offer) (clientInStream.readObject());
        System.out.println("Offer: "+offer.merchant+" : "+offer.quantity+" : "+offer.unitPrice);


        System.out.println("Worker: listening to socket");
        NetworkData nd = (NetworkData)clientInStream.readObject();
        
        //feel free to add more else if's before the else
        //for any Type that are added to NetworkData
        if (nd.type == NetworkData.Type.Msg)
        {
          Msg message = (Msg)nd;
          String msg = message.msg;
          System.out.println(msg);
        }
        else if (nd.type == NetworkData.Type.Name)
        {
          Name n = (Name)nd;
          this.name = n.name;
        }
        else if (nd.type == NetworkData.Type.Quit)
        {
          //System.exit(0);
          server.removeWorker(this);
          //this.interrupt();
          this.stop();
        }
        else if (nd.type == NetworkData.Type.WATEROFFER)
        {
          Offer inOffer = (Offer)(nd);
          String result = null;
          
          if (inOffer.offerType == null)
          { result = "Error: OfferType not specified.";
          }
          else if (this.name != inOffer.merchant)
          { result = "Error: Cannot work with other user offers.";
          }
          else if(inOffer.offerType == Offer.OfferType.ADDSELL)
          { result = this.server.getCatalog().addSellOffer(inOffer);
          }
          else if(inOffer.offerType == Offer.OfferType.REMOVESELL)
          { result = this.server.getCatalog().removeOffer(inOffer);
          }
          
          if (result.startsWith("Error"))
          {
            Msg message = new Msg(inOffer.merchant, result);
            message.error = NetworkData.Type.WATEROFFER;
            this.send(message);
          }
        }
        else if(nd.type == NetworkData.Type.FlowWater)
        {
          server.flowWater(this, (FlowData)nd);
        }
        else
        {
          System.out.println("Unrecognized message from client");
          Msg data = new Msg(name, "unknown type");
          data.error = nd.type;
          send(data);
        }

      }
      catch (Exception e)
      {
        System.out.println("Invalid command");
        NetworkData data = new Msg(name, "Invalid command");
        send(data);
        e.printStackTrace();
      }
    }
  }

}
