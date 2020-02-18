//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

import java.io.FileNotFoundException;
import java.io.IOException;

//This class should specify that the customer will be saved in the preferred customer array
abstract class PreferredCustomer extends Customer
{
	public PreferredCustomer(Customer customer)
	{
		super(customer.getFirstName(), customer.getLastName(), customer.getGuestID(), customer.getAmountSpent());
	}
	
	//write to data file
	@Override
	protected abstract void writeToFile(String fileName, boolean append) throws FileNotFoundException, IOException;
}
