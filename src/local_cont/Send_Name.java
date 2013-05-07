package local_cont;
import server.Name;
public class Send_Name implements Client_Msg 
{

	public Name name;
	
	public Send_Name(Name n)
	{
	  name = n;	
	}
	@Override
	public String msg_type() 
	{
		
		return "name";
	}
	public Name getName()
	{
	  return name;
	}
	

}
