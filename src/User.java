// Name: Alisa Uthikamporn
// Student ID: 6188025
// Section: 1

public class User
{
	public int uid;
	
	public User(int _id)
	{
		uid = _id;			
	}
	
	public int getID()
	{
		if(uid < 1)
			return -1;
		else
			return uid;
	}

}
