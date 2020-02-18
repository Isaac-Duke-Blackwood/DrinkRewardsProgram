//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

class PlatinumCustomer extends PreferredCustomer
{
	private float bonusBucks = 0.00f;
	
	//constructors
	public PlatinumCustomer(Customer customer)
	{
		super(customer);
		bonusBucks = (((int)getAmountSpent() - 200)/5);
	}
	public PlatinumCustomer(Customer customer, float bonusBucks) 
	{
		super(customer);
		this.bonusBucks = bonusBucks; //no input validation required by project description
	}
	
	
	//process order
	@Override
	protected float processOrder(float orderTotal)
	{
		if(orderTotal > bonusBucks) //order is bigger than the number of bonus bucks so a new total will be spent
		{
			orderTotal -= bonusBucks;
			bonusBucks = 0.00f;
			
			//calculate new bonus bucks from purchase.
			float oldAmountSpent = getAmountSpent();
			setAmountSpent(oldAmountSpent + orderTotal);
			int totalBonusBucksEarnedBeforeThisPurchase = (((int)oldAmountSpent - 200)/5);
			int totalBonusBucksEarnedAfterThisPurchase = (((int)getAmountSpent() - 200)/5);
			int newBonusBucks = totalBonusBucksEarnedAfterThisPurchase - totalBonusBucksEarnedBeforeThisPurchase; //gives bonus bucks for each new multiple of 5 reached 
			bonusBucks = newBonusBucks;
		}
		else //bonus bucks are greater than or equal to the order total no new bonus bucks
		{
			bonusBucks -= orderTotal;
			orderTotal = 0.00f;
		}
		return orderTotal;
	}

	//write to data file (fileName should always be "preferred.dat")
	@Override
	protected void writeToFile(String fileName, boolean append) throws FileNotFoundException, IOException //append: if true, customer data will be written to the end of the file
	{
		//open the data file and a writer
		File file = new File(fileName);
		FileWriter customerWriter = new FileWriter(file, append);
		
		//write the data in the format specified in the project description
		customerWriter.write(getGuestID() + "\n" + getFirstName() + "\n" + getLastName() + "\n" + Float.toString(getAmountSpent()) + "\n" + Float.toString(bonusBucks) + "\n\n");
				
		//close the writer
		customerWriter.close();
	}
}
