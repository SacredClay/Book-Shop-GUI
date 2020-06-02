/* Name: Mehrob Farhangmehr
 Course: CNT 4714 – Summer 2020
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday May 31, 2020
*/
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.time.*;
import java.time.format.*;

public class Project1Functions
{
	public String inventoryString(String itemToCheck) throws Exception
	{
		Scanner readInv = new Scanner(new File("inventory.txt"));
		String inventoryItem;
		String [] itemID;
		while (readInv.hasNextLine())
		{
			inventoryItem = readInv.nextLine();
			itemID = inventoryItem.split(",",5);
			if (itemToCheck.equals(itemID[0]))
				return inventoryItem;
		}
			return null;
	}
	
	public Boolean checkItem(String itemToCheck) throws Exception
	{
		String inventoryLine = inventoryString(itemToCheck);
		if (inventoryLine == null)
			return false;
		String[] itemID = inventoryLine.split(",",5);
		if (itemToCheck.equals(itemID[0]))
			return true;
		else return false;
	}
	
	public double getPrice(String inventoryString)
	{
		double price = 0;
		String[] itemPrice = inventoryString.split(",",5);
		price = Double.parseDouble(itemPrice[2]);
		return price;
	}
	
	public String getName(String inventoryString)
	{
		String[] itemName = inventoryString.split(",",5);
		return itemName[1];
	}
	
	public double calculateDiscount(int quantityOfItem)
	{
		double discount = 0;
		if (quantityOfItem > 5)
			discount = discount + 10;
		if (quantityOfItem > 10)
			discount = discount + 5;
		if (quantityOfItem > 15)
			discount = discount + 5;
		return discount/100;
	}
	
	public double calculateItemPrice(double originalPrice, int quantityOfItem)
	{
		
		if (quantityOfItem <= 5)
			return originalPrice * quantityOfItem;
		
		double finalPrice = (originalPrice * quantityOfItem) - (originalPrice * quantityOfItem * calculateDiscount(quantityOfItem));
		BigDecimal bd = new BigDecimal(finalPrice).setScale(2, RoundingMode.HALF_UP);
		double newInput = bd.doubleValue();
		return newInput;
	}
	
	public String createItemInfo(String itemID, String itemName, double itemPrice, int quantityOfItem)
	{
		return itemID + itemName + " $" + itemPrice + " " + quantityOfItem + " " + (int) (calculateDiscount(quantityOfItem) * 100) + "% $" + doubleFormat(calculateItemPrice(itemPrice,quantityOfItem));
	}
	
	public Double doubleFormat(double doubleToFormat)
	{
		BigDecimal bd = new BigDecimal(doubleToFormat).setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	public void printTransaction(String[] order) throws Exception
	{
		String[] parts = null;
		String receipt;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yy, HH:mm:ss a z");
		DateTimeFormatter permutation = DateTimeFormatter.ofPattern("ddMMyyyykkmm");
		ZonedDateTime now = ZonedDateTime.now();
		BufferedWriter writer = new BufferedWriter(new FileWriter("transaction.txt", true)); 
		int quantity = 0;
		
		for (int i=0; i < order.length; i ++)
		{
			if (order[i] != null)
			{
				parts = order[i].split(" ");
			
			quantity = Integer.valueOf(parts[parts.length-3]);
			receipt = permutation.format(now) + ", " + inventoryString(parts[0]) + ", " + quantity + ", " + calculateDiscount(quantity) + ", " + parts[parts.length-1] + ", " + dtf.format(now);
			writer.newLine();
			writer.write(receipt);
			}
		}
		writer.close();
	}
}