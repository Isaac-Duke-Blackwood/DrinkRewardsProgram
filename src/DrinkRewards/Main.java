//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

import java.io.FileNotFoundException;

public class Main 
{

	public static void main(String[] args) 
	{	
		final String customerFileName = "customers.dat";
		final String preferredCustomerFileName = "preferred.dat";
		final String ordersFileName = "orders.dat";
		
		//testing
		
		
		//Create customer array holder (automatically reads in both customer data files and initializes customer arrays if they exist. If the preferred customer file can't be found, then there is no array created. If the normal customer array can't be found the program exits)
		try 
		{
			//Create customer array holder (automatically reads in both customer data files and initializes customer arrays if they exist. If the preferred customer file can't be found, then there is no array created.)
			CustomerArrayHolder customerArrayHolder = new CustomerArrayHolder(customerFileName, preferredCustomerFileName);
		
			//Create the order list
			OrderList orderList = new OrderList(ordersFileName, customerArrayHolder);
		
			//Process the order list
			orderList.processOrders();
			
			//Save the updated customer information
			customerArrayHolder.writeToFile();
			
		}
		catch (FileNotFoundException e)
		{
			//either the normal customer file or the order file could not be found so end the program
			e.printStackTrace();
			int status = 7;
			System.exit(status);
		}
	}
}
