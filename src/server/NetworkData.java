//Michael Asplund
package server;
import java.io.Serializable;

public abstract class NetworkData implements Serializable
{
  public enum Type {Name, Msg, WATEROFFER, Quit, FlowWater};//CancelOrder, SellCrop
  public Type type;
}
