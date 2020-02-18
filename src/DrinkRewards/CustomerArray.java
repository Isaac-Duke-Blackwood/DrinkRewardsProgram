//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CustomerArray 
{
	private Customer[] customerArray = null;
	protected String fileName = null;
	
	//constructors
	protected CustomerArray() {} //constructor for an empty list, should only be called by the subclass, because the directions say if there is no normal customer file to abort
	public CustomerArray(String fileName) throws FileNotFoundException
	{
		this.fileName = fileName;
		try 
		{
			//create a scanner to traverse the file with a loop and add each element to the array
			File file = new File(fileName);
			Scanner scanner = new Scanner(file); 
			
			//loop through the file until end
			boolean endOfFile = !scanner.hasNextLine();
			while(!endOfFile)
			{
				//there is customer data here so gather it and append a new customer
				gatherAndAppend(scanner);
				
				//check again for end of file
				if(scanner.hasNextLine())
				{
					//there should be an empty line here, so skip it
					scanner.nextLine();
					
					//check again for end of file
					endOfFile = !scanner.hasNextLine();				
				}
				else
				{
					endOfFile = true;
				}
			}
			scanner.close();
		}
		catch (NoSuchElementException e) 
		{
			//there wasn't any data in the file, or the data was in the wrong format
			customerArray = new Customer[0];
		}
	}
	
	//gather data from the passed scanner to create a new customer object and then append it
	private void gatherAndAppend(Scanner scanner) // probably should only be called from the constructor
	{
		String guestID = scanner.nextLine();
		String firstName = scanner.nextLine();
		String lastName = scanner.nextLine();
		float amountSpent = Float.parseFloat(scanner.nextLine());
		
		//add the new customer to the end of the array
		append(new Customer(firstName, lastName, guestID, amountSpent));
	}
	
	//mutators
	protected int append(Customer customer) //appends the passed customer to the end of the list and resizes it. returns the index of the new customer
	{
		if(null == customerArray) 
		{
			//create the array because it doesn't exist yet
			customerArray = new Customer[1];
			
			//add the element
			customerArray[0] = customer;
			
			return 0;
		}
		else
		{
			//create a new customer array that is the same as the old one except it has an additional element at the end
			Customer[] newCustomerArray = new Customer[customerArray.length + 1];//safe to use .length instead of length because we already checked if the array wass null or not
			for(int index = 0; index < customerArray.length; index++)
			{
				newCustomerArray[index] = customerArray[index];
				customerArray[index] = null; //should prevent data leaks in the form of arrays of objects with no references
			}
			
			//add the new customer to the end of the new array
			newCustomerArray[newCustomerArray.length - 1] = customer;
			
			//update the official customer array
			customerArray = newCustomerArray;
			
			return newCustomerArray.length - 1;
		}
	}
	protected int replace(Customer customer, int index) throws CustomerArrayIsNullException //replaces the passed customer with the customer object at the given index. returns the index of the new customer
	{
		if(null == customerArray)
		{
			throw new CustomerArrayIsNullException();
		}
		customerArray[index] = customer;
		return index;
	}
	protected void remove(int index) throws CustomerArrayIsNullException //removes the element at the specified index
	{
		if(length() - 1 >= 0) // do nothing if the array is already empty
		{
			//array isn't empty
			Customer[] newCustomerArray = new Customer[length() - 1];
			for(int loopIndex = 0; loopIndex < length(); loopIndex++)
			{
				if(loopIndex < index)
				{
					//element should be copied into the new array with the same index
					newCustomerArray[loopIndex] = customerArray[loopIndex];
					customerArray[loopIndex] = null; //should help prevent data leaks
				}
				else if(loopIndex == index) 
				{
					//element should be deleted
					customerArray[loopIndex] = null; //should help prevent data leaks
				}
				else
				{
					//element should be copied into the new array with the old index minus one
					newCustomerArray[loopIndex - 1] = customerArray[loopIndex];
					customerArray[loopIndex] = null; //should help prevent data leaks
				}
			}
			//update the customer array
			customerArray = newCustomerArray;
		}
	}
	
	//accessors
	protected int findCustomer(String guestID) //returns the index of the customer, or -1 if not found
	{
		try 
		{
		for(int index = 0; index < length(); index++)
		{
			if(customerArray[index].getGuestID().equals(guestID))
			{
				//the customer exists
				return index;
			}
		}
		return -1;
		}
		catch (CustomerArrayIsNullException e) 
		{
			//the array didn't exist, so the customer wasn't there
			return -1;
		}
	}
	protected Customer get(int index) throws CustomerArrayIsNullException
	{
		return customerArray[index];
	}
	protected int length() throws CustomerArrayIsNullException
	{
		if(null == customerArray)
		{
			throw new CustomerArrayIsNullException();
		}
		return customerArray.length;
	}
	protected boolean customerExists(String guestID) //returns true if there is a customer in the array that has a matching guest ID, otherwise returns false
	{
		try 
		{
		for(int index = 0; index < length(); index++)
		{
			if(customerArray[index].getGuestID().equals(guestID))
			{
				//the customer exists
				return true;
			}
		}
		return false;
		}
		catch (CustomerArrayIsNullException e) 
		{
			//the array didn't exist, so the customer wasn't there
			return false;
		}
	}

	//write data to file
	protected void writeToFile() 
	{
		try 
		{
			if(0 == length())
			{
				//there are no customers in the file, so overwrite the existing file with a blank one
				clearFile();
			}
			else
			{
				try 
				{
					for(int index = 0; index < length(); index++)
					{
						//overwrite file on the first time through
						boolean append = true;
						if(0 == index)
						{
							append = false;
						}
						//call the customer's writeToFile() for each customer in the array
						customerArray[index].writeToFile(fileName, append);
					}
				}
				catch(CustomerArrayIsNullException e)
				{
					//if this exception is thrown then the data wont be written to the file in closing so abort the program
					int status = 8;
					System.exit(status);
				}
			}
		}
		catch(IOException e)
		{
			//something went wrong with the FileWriter
			int status = 9;
			System.exit(status);
		}
		catch(CustomerArrayIsNullException e)
		{
			//the customer array doesn't exist anymore so clear the customer file anyway
			try 
			{
				clearFile();
			} 
			catch (IOException e2) 
			{
				//something went wrong with the FileWriter
				int status = 10;
				System.exit(status);
			}
		}
	}
	
	//clear file
	protected void clearFile() throws IOException
	{
		File file = new File(fileName);
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write('\n');
		fileWriter.close();
	}
}

class CustomerArrayIsNullException extends Exception
{
	 private static final long serialVersionUID = 1L;
	 protected CustomerArrayIsNullException()
	 {
		 super();
	 }
}
