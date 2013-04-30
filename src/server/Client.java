package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client
{
  private Socket clientSocket;
  private ObjectOutputStream outputStream;
  private ObjectInputStream inputStream;
  private long startNanoSec;
  private Scanner keyboard;
  private ClientListener listener;

  private volatile int sneedsInStore;

  public Client(String host, int portNumber)
  {
    startNanoSec = System.nanoTime();
    System.out.println("Starting Client: " + timeDiff());

    keyboard = new Scanner(System.in);

    while (!openConnection(host, portNumber))
    {
    }
    
    listener = new ClientListener();
    System.out.println("Client(): Starting listener = : " + listener);
    listener.start();

    listenToUserRequests();

    closeAll();

  }


  private boolean openConnection(String host, int portNumber)
  {

    try
    {
      clientSocket = new Socket(host, portNumber);
    }
    catch (UnknownHostException e)
    {
      System.err.println("Client Error: Unknown Host " + host);
      e.printStackTrace();
      return false;
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open connection to " + host
          + " on port " + portNumber);
      e.printStackTrace();
      return false;
    }

    try
    {
      outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open output stream");
      e.printStackTrace();
      return false;
    }
    try
    {
      inputStream = new ObjectInputStream(clientSocket.getInputStream());
    }
    catch (IOException e)
    {
      System.err.println("Client Error: Could not open input stream");
      e.printStackTrace();
      return false;
    }
    return true;

  }

  private void listenToUserRequests()
  {
    while (true)
    {
      //System.out.println("Sneeds in Inventory = " + sneedsInStore);
      System.out.println("Enter Command [Buy: # : $] | [Sell: # : $]");
      String cmd = keyboard.nextLine();
      if (cmd == null) continue;
      if (cmd.length() < 1) continue;

      char c = cmd.charAt(0);
      if (c == 'q') break;
      
      if (cmd.equalsIgnoreCase("send"))
      {
        try { outputStream.writeObject(new Offer("Test", 112358, 0.25)); }
        catch (IOException e) { e.printStackTrace(); }
      }
    }
  }
  
  public void closeAll()
  {
//    System.out.println("Client.closeAll()");
//
//    System.exit(0);
//    if (outputStream != null) outputStream.close();
//    if (inputStream != null)
//    {
//      try
//      {
//        clientSocket.close();
//        inputStream.close();
//      }
//      catch (IOException e)
//      {
//        System.err.println("Client Error: Could not close");
//        e.printStackTrace();
//      }
//    }

  }

  private String timeDiff()
  {
    long namoSecDiff = System.nanoTime() - startNanoSec;
    double secDiff = (double) namoSecDiff / 1000000000.0;
    return String.format("%.6f", secDiff);

  }

  public static void main(String[] args)
  {
    
    String host = "sycorax.cs.unm.edu";
    int port = 0;
   
    try
    {
      host = args[0];
      port = Integer.parseInt(args[1]);
      if (port < 1) throw new Exception();
    }
    catch (Exception e)
    {
      System.out.println("Usage: Client host portNumber");
      System.exit(0);
    }
    new Client(host, port);

  }
  
  class ClientListener extends Thread
  {
    public void run()
    {
      System.out.println("ClientListener.run()");
      while (true)
      {
        if (clientSocket.isClosed()) break;
        read();
      }

    }

    private void read()
    {
      try
      {
        System.out.println("Client: listening to socket");
        try 
        { Offer offer = (Offer)(inputStream.readObject());
          System.out.println("Offer: "+offer.merchant+" : "+offer.quantity+" : "+offer.unitPrice);
        }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }

  }

}
