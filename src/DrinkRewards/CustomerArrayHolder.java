//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

import java.io.FileNotFoundException;

public class CustomerArrayHolder 
{
	private CustomerArray customerArray = null;
	private CustomerArray preferredArray = null;
	private static final float PLATINUM_THRESHOLD = 200.00f, GOLD_THRESHOLD = 50.00f;
	
	//constructor
	public CustomerArrayHolder(String customerFileName, String preferredCustomerFileName) throws FileNotFoundException
	{
			customerArray = new CustomerArray(customerFileName);
			preferredArray = new PreferredArray(preferredCustomerFileName);
	}
	
	//check for the existence of a particular customer
	public boolean customerExists(String guestID) 
	{
		if(customerArray.customerExists(guestID))
		{
			//the customer exists in the customerArray
			return true;
		}
		//the customer does not exist in customerArray
		if(preferredArray == null)
		{
			//the customer can't exist in the preferred array because it doesn't exist, so the customer doesn't exist
			return false;
		}
		else if (preferredArray.customerExists(guestID))
		{
			//the customer exists in the preferred array
			return true;
		}
		
		//the customer doesn't exist in either array
		return false;
	}
	
	//process order
	public void processOrder(Order order) //determines the type of order to be processed and then calls the respective function
	{
		//get the required order data
		String guestID = order.getGuestID();
		float orderTotal = order.price();
		
		//find the guest and then process order based on customer type
		int index = customerArray.findCustomer(guestID);
		if(index != -1)
		{
			//customer was found
			processOrder(index, orderTotal);
		}
		else
		{
			index = preferredArray.findCustomer(guestID);
			if(index != -1)
			{
				//preferred customer was found
				processPreferredOrder(index, orderTotal);
			}
			else 
			{
				//the customer wasn't found. This means that there was an error in the order processing and somehow a bad order was added to the list, or the searching function is busted
				int status = 6;
				System.exit(status);
			}
		}
	}
	private void processOrder(int index, float orderTotal) //updates the customer's amount spent at the specified index and promotes if necessary
	{
		if(customerArray != null)
		{
			try 
			{
				//process order on the customer object
				float potentialAmountSpent = customerArray.get(index).processOrder(orderTotal);
				
				//check to see if the customer will be promoted to gold
				if (potentialAmountSpent >= GOLD_THRESHOLD)
				{
					int newIndex = promoteCustomer(index);
					processPreferredOrder(newIndex, orderTotal);
				}
				else
				{
					customerArray.get(index).setAmountSpent(potentialAmountSpent);
				}
			}
			catch(CustomerArrayIsNullException e)
			{
				//it should  be impossible for this exception to occur because we already tested customerArray. If this exception comes anyway we need to exit probably
				int status = 4;
				System.exit(status);
			}
		}
		else
		{
			//the customerArray is null, not empty. This means the customer file was not read in properly, so abort
			int status = 5;
			System.exit(status);
		}
	}
	private void processPreferredOrder(int index, float orderTotal) //updates the preferred customer's amount spent at the specified index and promotes if necessary
	{
		try 
		{
			Customer preferredCustomer = preferredArray.get(index);
			//process order on the preferred customer object
			preferredCustomer.processOrder(orderTotal);
			
			//if customer is gold, check to see if they should be promoted
			if(preferredCustomer instanceof GoldCustomer)
			{
				//check to see if the gold customer will be promoted to platinum
				if (preferredArray.get(index).getAmountSpent() >= PLATINUM_THRESHOLD)
				{
					//promote the customer
					promoteGoldCustomer(index);
				}
			}
		}
		catch(CustomerArrayIsNullException e)
		{
			//this should be impossible, because this method should only be called if the preferred array exists
			int status = 3;
			System.exit(status);
		}
	}
	
	//promotions
	private int promoteCustomer(int index) throws CustomerArrayIsNullException//promotes the normal customer at the specified index to a gold customer and returns the index of the promoted customer
	{
		GoldCustomer goldCustomer = new GoldCustomer(customerArray.get(index));
		int newIndex = preferredArray.append(goldCustomer);
		customerArray.remove(index);
		return newIndex;
		
	}
	private int promoteGoldCustomer(int index) throws CustomerArrayIsNullException //promotes the gold customer at the specified index to platinum and returns the index of the promoted customer
	{
		PlatinumCustomer platinumCustomer = new PlatinumCustomer(preferredArray.get(index));
		preferredArray.replace(platinumCustomer, index);
		return index;
	}

	//update files
	public void writeToFile() //saves updated info to the files
	{
		customerArray.writeToFile();
		preferredArray.writeToFile();
	}
}

class CustomerNotFoundException extends Exception
{
	 private static final long serialVersionUID = 1L;
	 protected CustomerNotFoundException()
	 {
		 super();
	 }
}
