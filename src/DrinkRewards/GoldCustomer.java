//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

class GoldCustomer extends PreferredCustomer
{
	private static final float TEN_PERCENT_THRESHOLD = 100.00f, FIFTEEN_PERCENT_THRESHOLD = 150.00f;
	private static final float FIVE_PERCENT = .05f, TEN_PERCENT = .10f, FIFTEEN_PERCENT = .15f;
	private float discount = FIVE_PERCENT;
	
	public GoldCustomer(Customer customer)
	{
		super(customer);
		determineDiscount(getAmountSpent());
	}
	public GoldCustomer(Customer customer, float discount)
	{
		super(customer);
		this.discount = discount;
	}
	
	//determine the current discount
	private void determineDiscount(float amountSpent)
	{
		float newDiscount = discount;
		if(amountSpent >= FIFTEEN_PERCENT_THRESHOLD)
		{
			newDiscount = FIFTEEN_PERCENT;
		}
		else if(amountSpent >= TEN_PERCENT_THRESHOLD)
		{
			newDiscount = TEN_PERCENT;
		}
		
		//prevents discount rates from being lowered once they are given
		if(newDiscount > discount)
		{
			discount = newDiscount;
		}
	}

	//process order (and check if promoted)
	@Override
	protected float processOrder(float orderTotal)
	{
		float potentialAmountSpent = getAmountSpent() + orderTotal * (1 - discount);
		
		//update and apply the discount to the order
		determineDiscount(potentialAmountSpent);
		orderTotal *= (1 - discount);
		setAmountSpent(getAmountSpent() + orderTotal);
		
		//return the amount
		return getAmountSpent();
		
	}

	//write to data file (fileName should always be "preferred.dat"
	@Override
	protected void writeToFile(String fileName, boolean append) throws FileNotFoundException, IOException //append: if true, customer data will be written to the end of the file
	{
		//open the data file and a writer
		File file = new File(fileName);
		FileWriter customerWriter = new FileWriter(file, append);
		
		//write the data in the format specified in the project description
		customerWriter.write(getGuestID() + "\n" + getFirstName() + "\n" + getLastName() + "\n" + Float.toString(getAmountSpent()) + "\n" + Float.toString(discount) + "\n\n");
				
		//close the writer
		customerWriter.close();
	}
}
