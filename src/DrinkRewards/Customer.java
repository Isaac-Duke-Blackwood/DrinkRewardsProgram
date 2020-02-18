//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

class Customer
{
	private String firstName;
	private String lastName;
	private String guestID;
	private float amountSpent;
	
	//constructor
	public Customer(String firstName, String lastName, String guestID, float amountSpent)
	{
		//no input validation required by project description
		setFirstName(firstName);
		setLastName(lastName);
		setGuestID(guestID);
		setAmountSpent(amountSpent);
	}
	
	//accessors
	protected String getFirstName()
	{
		return firstName;
	}
	protected String getLastName()
	{
		return lastName;
	}
	protected String getGuestID()
	{
		return guestID;
	}
	protected float getAmountSpent()
	{
		return amountSpent;
	}
	
	//mutators
	protected void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	protected void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	protected void setGuestID(String guestID)
	{
		this.guestID = guestID;
	}
	protected void setAmountSpent(float amountSpent)
	{
		this.amountSpent = amountSpent;
	}
	
	//write to data file (fileName should always be "customer.dat")
	protected void writeToFile(String fileName, boolean append) throws FileNotFoundException, IOException //append: if true, customer data will be written to the end of the file
	{
		//open the data file and a writer
		File file = new File(fileName);
		FileWriter customerWriter = new FileWriter(file, append);
		
		//write the data in the format specified in the project description
		customerWriter.write(guestID + "\n" + firstName + "\n" + lastName + "\n" + Float.toString(amountSpent) + "\n\n");
				
		//close the writer
		customerWriter.close();
	}

	//process order (and check if promoted)
	protected float processOrder(float orderTotal)
	{
		float potentialAmountSpent = amountSpent + orderTotal;
		return potentialAmountSpent;
	}
}
