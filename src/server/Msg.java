//Michael Asplund
//sent from server to client
package server;
public class Msg extends NetworkData
{
  private static final long serialVersionUID = 1L;
  String name;  //which client msg is for
  String msg;
  Type error = null; //if there was an error processing a request
  
  public Msg(String name, String msg)
  {
    type = Type.Msg;
    this.name = name;
    this.msg = msg;
  }
}
