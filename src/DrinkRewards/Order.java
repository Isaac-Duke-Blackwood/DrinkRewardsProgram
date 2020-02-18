//Name: Isaac Blackwood
//NetID: idb170030
package DrinkRewards;

//Has/calculates all the characteristics of an order including price, but comments out things that have to do with the customization options at the directions of Dr. Oladimeji, because they were poorly explained
class Order
{
	//independent members
	private String guestID;
	private char size;
	private String drinkType;
//	private float squareInchPrice;
	private int quantity;
	
	//dependent members (be careful not to let these cause bugs!)
	private int numberOfOz = 0; 		//if a setter is implemented for size, make sure it updates this value
	private float pricePerOz = 0.0f;	//if a setter is implemented for drink type, make sure it updates this value
//	private float surfaceArea = 0.0f;	//if a setter is implemented for drink size, make sure it updates this value
	
	//constants
//	private static final float SMALL_SURFACE_AREA = 4.0f * 4.5f * 3.14f, MEDIUM_SURFACE_AREA = 4.5f * 5.75f * 3.14f, LARGE_SURFACE_AREA = 5.5f * 7.0f * 3.14f;
	private static final float SODA_PRICE_PER_OZ = 0.20f, TEA_PRICE_PER_OZ = 0.12f, PUNCH_PRICE_PER_OZ = 0.15f;
	private static final String SODA = "soda", TEA = "tea", PUNCH = "punch";
	private static final char SMALL = 'S', MEDIUM = 'M', LARGE = 'L';
	private static final int SMALL_OZ = 12, MEDIUM_OZ = 20, LARGE_OZ = 32;
	
	public Order(String guestID, char size, String drinkType,/*float squareInchPrice,*/ int quantity) //only pass good value to this - there is no input validation at this level.
	{
		//set the provided values
		this.guestID = guestID;
		this.size = size;
		this.drinkType = drinkType;
//		this.squareInchPrice = squareInchPrice;
		this.quantity = quantity;
		
		//determine the values that should be used to calculate price
		setNumberOfOz();
		setPricePerOz();
//		setSurfaceArea();
		
	}
	
	//setters
	private void setNumberOfOz()
	{
		switch(size) 
		{
		case SMALL:	numberOfOz = SMALL_OZ;
					break;
		case MEDIUM:numberOfOz = MEDIUM_OZ;
					break;
		case LARGE: numberOfOz = LARGE_OZ;
					break;				
		}
	}
	private void setPricePerOz()
	{
		switch(drinkType)
		{
		case SODA: 	pricePerOz = SODA_PRICE_PER_OZ;
					break;
		case TEA:	pricePerOz = TEA_PRICE_PER_OZ;
					break;
		case PUNCH: pricePerOz = PUNCH_PRICE_PER_OZ;
					break;
		}
	}
	/* private void setSurfaceArea() 
	 * {
	 * 		switch(size) 
	 * 		{
	 * 		case SMALL:	surfaceArea = SMALL_SURFACE_AREA;
	 * 					break;
	 * 		case MEDIUM:surfaceArea = MEDIUM_SURFACE_AREA;
	 * 					break;
	 * 		case LARGE: surfaceArea = LARGE_SURFACE_AREA;
	 * 					break;				
	 * 		}
	 * }
	*/
	
	//accessors
	public String getGuestID()
	{
		return guestID;
	}
	
	
	//determines the price of the current order
	public float price()
	{
		return numberOfOz * pricePerOz * quantity; // + surfaceArea * squareInchPrice; would calculate the customization price as well
	}
	
}
