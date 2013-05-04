//Michael Asplund
//from client to server
//tells the server the name to associate with the client
//name must be unique
package server;
public class Name extends NetworkData
{
  private static final long serialVersionUID = 1L;
  String name;
  
  public Name(String name)
  {
    type = Type.Name;
    this.name = name;
  }
}
