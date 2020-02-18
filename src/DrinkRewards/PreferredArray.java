//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PreferredArray extends CustomerArray 
{
	private static final float PLATINUM_THRESHOLD = 200.00f;
			
	//constructor
	public PreferredArray(String fileName)
	{
		//create parent and initialize filename
		super();
		super.fileName = fileName;
		
		//open the file
		File file = new File(fileName);
		
		//check to see if the file actually existed
		if(file.exists())
		{
			try 
			{
			//make a scanner to check if the file is empty and traverse it if not
			Scanner scanner = new Scanner(file);
			
			//check to see if the file is empty
			if(scanner.hasNextLine())
			{
				//non-empty file exists so loop through the file and create the array
				//loop through the file until end
				boolean endOfFile = !scanner.hasNext();
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
			}
			
			scanner.close();
			}
			catch(FileNotFoundException e)
			{
				//shouldn't throw this exception because we already checked to see if the file exists
				int status = 2;
				System.exit(status);
			}
		}
	}

	//gather and append like in the super class, but this one creates preferred customers
	private void gatherAndAppend(Scanner scanner)
	{
		//gather data
		String guestID = scanner.nextLine();
		String firstName = scanner.nextLine();
		String lastName = scanner.nextLine();
		float amountSpent = Float.parseFloat(scanner.nextLine());
		
		//determine if gold or platinum
		if(amountSpent >= PLATINUM_THRESHOLD)
		{
			//customer is platinum
			float bonusBucks = Float.parseFloat(scanner.nextLine());
			append(new PlatinumCustomer(new Customer(firstName, lastName, guestID, amountSpent), bonusBucks));
		}
		else
		{
			//customer is gold
			float discount = Float.parseFloat(scanner.nextLine());
			append(new GoldCustomer(new Customer(firstName, lastName, guestID, amountSpent), discount));
		}
	}
	
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
						get(index).writeToFile(fileName, append);
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
			//the customer array doesn't exist anymore so just don't create a new one			
		}
	}
}
