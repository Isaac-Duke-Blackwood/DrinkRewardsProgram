//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

import java.util.Scanner;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.NumberFormatException;

//Has/calculates all the characteristics of an order including price, but comments out things that have to do with the customization options at the directions of Dr. Oladimeji, because they were poorly explained
public class OrderList 
{
	private static final String SODA = "soda", TEA = "tea", PUNCH = "punch";
	private static final int CORRECT_NUMBER_OF_LINES = 5; //this is maybe supposed to be 4 without the customization features
	private static final int GUEST_ID_LINE_INDEX = 0, SIZE_LINE_INDEX = 1, DRINK_TYPE_LINE_INDEX = 2, /*SQUARE_INCH_PRICE_LINE_INDEX = 3,*/ QUANTITY_LINE_INDEX = 4; // The quality index might be 3 depending on whether or not each order still has a square inch price line 
	private LinkedList<Order> orderList = new LinkedList<Order>();
	private CustomerArrayHolder customerArrayHolder = null;
	
	//should be passed the file "Orders.dat" to read in and create a list of valid orders
	public OrderList(String fileName, CustomerArrayHolder customerArrayHolder) throws FileNotFoundException
	{
		//decides the arrays to be used for customer data for these orders NOTE: the preferredArray may contain null if it hasn't been created yet
		this.customerArrayHolder = customerArrayHolder;
		
		//creates a scanner to traverse the file
		Scanner scanner = new Scanner(new File(fileName));
		
		//Scans through entire file, appending good orders to the order list.
		boolean endOfFile = !scanner.hasNextLine();
		
		while(!endOfFile) //scans until the file ends
		{
			//creates an list of strings to hold each line of the order
			LinkedList<String> orderLines = new LinkedList<String>();
			
			//We are at the start of an order
			boolean endOfOrder = false;
			
			while(!endOfOrder && !endOfFile) //scans until the order ends or file ends
			{
				//file hasn't ended so grab the next line
				String currentLine = scanner.nextLine();
				
				//if the line is blank then the order is over
				if(0 == currentLine.length())
				{
					endOfOrder = true; //inner loop will end
				}
				else 
				{
					//order isn't over, so add the current line to orderLines
					orderLines.add(currentLine);
				}
				
				//checks to see if the file has ended or the order is over, in which case both loops should end
				endOfFile = !scanner.hasNextLine();
			}
			
			//validates the current order (orderLines) to see if each line is as expected.
			boolean isValid = validatePotentialOrder(orderLines);
			
			if(isValid) 
			{
				//converts each of the fields stored as a string into the proper type to pass as parameters
				String guestID = orderLines.get(GUEST_ID_LINE_INDEX);
				char size = Character.toUpperCase(orderLines.get(SIZE_LINE_INDEX).charAt(0)); // makes sure the char is passed as the uppercase version, if the program is not supposed to accept lowercase letters for the size, then remove the .toUpperCase conversion and edit isSizeValid()
				String drinkType = orderLines.get(DRINK_TYPE_LINE_INDEX);
//				float squareInchPrice = Float.parseFloat(orderLines.get(SQUARE_INCH_PRICE_LINE_INDEX));
				int quantity = Integer.parseInt(orderLines.get(QUANTITY_LINE_INDEX));
				
				//creates an order and appends it to the order list
				orderList.add(new Order(guestID, size, drinkType, /*squareInchPrice,*/ quantity));
			}
		}
		//file should have been processed
		scanner.close();
	}
	
	//Input Validation
	private boolean validatePotentialOrder(LinkedList<String> orderLines) 
	{
		if(!correctNumberOfLines(orderLines))
		{
			return false;
		}
		
		//the order has the correct number of fields, so check each to make sure it is a valid order
		//check type of each field (String fields cannot be checked)
		if(!isChar(orderLines.get(SIZE_LINE_INDEX)))
		{
			//the size is not denoted by 1 character, ergo the order is bad
			return false;
		}
		/* if(!isFloat(orderLines.get(SQUARE_INCH_PRICE_LINE_INDEX))
		 * {
		 * 		//the price per inch^2 isn't a float, ergo the order is bad
		 * 		return false;
		 * }
		 */
		if(!isInt(orderLines.get(QUANTITY_LINE_INDEX)))
		{
			//the quantity isn't an integer, ergo the order is bad
			return false;
		}
		
		//the fields have the correct data types, validate the data
		if(!isQuantityValid(Integer.parseInt(orderLines.get(QUANTITY_LINE_INDEX))))
		{
			//quantity isn't >=0
			return false;
		}
		if(!isSizeValid(orderLines.get(SIZE_LINE_INDEX).charAt(0)))
		{
			//size isn't one of the choices
			return false;
		}
		/* if(!isSquareInchPriceValid(Float.parseFloat(orderLines.get(SIZE_LINE_INDEX))))
		 * {
		 * 		//price isn't >=0
		 * 		return false;
		 * } 
		 */
		if(!isDrinkTypeValid(orderLines.get(DRINK_TYPE_LINE_INDEX)))
		{
			//drink type invalid
			return false;
		}
		if(!isGuestIDValid(orderLines.get(GUEST_ID_LINE_INDEX)))
		{
			//guest id isn't one of the guests in the customer or preferred customer arrays.
			return false;
		}
		
		//The potential order is totally good
		return true;
	}
	private boolean correctNumberOfLines(LinkedList<String> orderLines) //tests if the order has the correct number of fields
	{
		if(orderLines.size() != CORRECT_NUMBER_OF_LINES)
		{
			//there were more or less fields than required in the order
			return false;
		}
		else
		{
			return true;
		}
	}
	private boolean isChar(String string) //tests if the passed string is a char formatted as a string
	{
		if(string.length() != 1)
		{
			//the size is not denoted by 1 character, ergo the order is bad
			return false;
		}
		return true;
	}
/*	private boolean isFloat(String string) // tests if the passed string is a float formatted as a string
 *	{
 *		try 
 *		{
 *		  	Float.parseFloat(string);
 *		} 
 *		catch (NumberFormatException e)
 *		{
 *	 		//the price per square inch is not a float
 *	 		return false;
 *		}
 *		return true; 
 *	}
 */
	private boolean isInt(String string) //tests if the passed string is an int formatted as a string 
	{
		try 
		{
			Integer.parseInt(string);
		} 
		catch (NumberFormatException e)
		{
			//the quantity is not an integer
			return false;
		}
		return true;
	}
	private boolean isQuantityValid(int quantity) //tests if the quantity is above 0
	{
		if(quantity < 0)
		{
			return false;
		}
		return true;
	}
	private boolean isSizeValid(char size) //checks if the size is equal to lowercase 'S', 'M', or 'L'.
	{
		char testChar = size; //Character.toUpperCase(size); // replace "size;" with this statement if lowercase size letters should be accepted
		if(testChar != 'S' && testChar != 'M' && testChar != 'L')
		{
			//bad size character
			return false;
		}
		return true;
	}
	private boolean isDrinkTypeValid(String drinkType) //checks if the drink type is equal to lowercase 'soda', 'tea', or 'punch'.
	{
		if(drinkType.equals(SODA) || drinkType.equals(TEA) || drinkType.equals(PUNCH)) //replace each .equals with .equalsIgnoreCase if the uppercase variants should be accepted
		{
			//valid drink type
			return true;
		}
		return false;
	}
/*  private boolean isSquareInchPriceValid(float squareInchPrice) //tests if the squareInchPrice is above 0
 * {
 * 		if(squareInchPrice < 0)
 * 		{
 * 			//not a valid price
 * 			return false;
 * 		}
 * 		return true;
 * }
 */
	private boolean isGuestIDValid(String guestID) //checks to see if the guest is in one of the customer arrays
	{
		return customerArrayHolder.customerExists(guestID);
	}

	
	//step through the order list and process each order
	public void processOrders() 
	{
		for(int index = 0; index < orderList.size(); index++)
		{	
			//process the order and possibly promote the customer object
			customerArrayHolder.processOrder(orderList.get(index));
		}
		orderList.clear(); //should prevent memory leaks, not sure if necessary
	}
}
