/* Name: Mehrob Farhangmehr
 Course: CNT 4714 – Summer 2020
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday May 31, 2020
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.*;
import java.time.format.*;


public class GUI implements ActionListener
{
	private JButton buttonProcess;
	private JButton buttonConfirm;
	private JButton buttonView;
	private JButton buttonFinish;
	private JButton buttonNew;
	private JButton buttonExit;
	
	private JLabel numLabel;
	private JLabel IDLabel;
	private JLabel infoLabel;
	private JLabel quantityLabel;
	private JLabel subtotalLabel;
	
	private JTextField numText;
	private JTextField IDText;
	private JTextField infoText;
	private JTextField quantityText;
	private JTextField subtotalText;
	
	private Project1Functions test = new Project1Functions();
	
	private final int DEFAULT_CURRENT_ITEMS = 1;
	
	private int quantityOfItem = 0;
	private int currentItem = DEFAULT_CURRENT_ITEMS;
	
	private String inventoryString;
	private String itemName;
	private double itemPrice = 0;
	private double totalItemPrice = 0;
	private String itemInfo;
	private double subTotal = 0;
	private String[] order;
	
	public GUI() 
	{
		JFrame frame = new JFrame();
		frame.setSize(850, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Ye Olde Book Shoppe - Summer 2020");
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		frame.add(mainPanel);
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(null);
		textPanel.setBackground(Color.gray);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.red);
		mainPanel.add(textPanel,BorderLayout.CENTER);
		mainPanel.add(buttonPanel,BorderLayout.PAGE_END);
		
		
		numLabel = new JLabel("Enter number of items in this order:");
		numLabel.setBounds(80, 20, 800, 25);
		textPanel.add(numLabel);
		
		numText = new JTextField(20);
		numText.setBounds(300, 20, 465, 25);
		textPanel.add(numText);
		
		IDLabel = new JLabel("Enter Book ID for item #" + currentItem + ":");
		IDLabel.setBounds(80, 60, 800, 25);
		textPanel.add(IDLabel);
		
		IDText = new JTextField(20);
		IDText.setBounds(300, 60, 465, 25);
		textPanel.add(IDText);
		
		quantityLabel = new JLabel("Enter the Quantity for item #" + currentItem + ":");
		quantityLabel.setBounds(80,100,800,25);
		textPanel.add(quantityLabel);
		
		quantityText = new JTextField(20);
		quantityText.setBounds(300, 100, 465, 25);
		textPanel.add(quantityText);
		
		infoLabel = new JLabel("Item #" + currentItem + " Info:");
		infoLabel.setBounds(80, 140, 800, 25);
		textPanel.add(infoLabel);
		
		infoText = new JTextField(20);
		infoText.setBounds(300, 140, 465, 25);
		textPanel.add(infoText);
		
		subtotalLabel = new JLabel("Order subtotal for " + (currentItem-1) + " item(s):");
		subtotalLabel.setBounds(80, 180, 800, 25);
		textPanel.add(subtotalLabel);
		
		subtotalText = new JTextField(20);
		subtotalText.setBounds(300, 180, 465, 25);
		textPanel.add(subtotalText);
		
		
		buttonProcess = new JButton("Process Item #" + currentItem);
		buttonProcess.setBounds(10, 180, 150, 25);
		buttonProcess.addActionListener(this);
		buttonConfirm = new JButton("Confirm Item #" + currentItem);
		buttonConfirm.setBounds(170, 180, 150, 25);
		buttonConfirm.addActionListener(this);
		buttonView = new JButton("View Order");
		buttonView.setBounds(330, 180, 150, 25);
		buttonView.addActionListener(this);
		buttonFinish = new JButton("Finish Order");
		buttonFinish.setBounds(490, 180, 150, 25);
		buttonFinish.addActionListener(this);
		buttonNew = new JButton("New Order");
		buttonNew.setBounds(650, 180, 150, 25);
		buttonNew.addActionListener(this);
		buttonExit = new JButton("Exit");
		buttonExit.setBounds(810, 180, 150, 25);
		buttonExit.addActionListener(this);
		
		buttonPanel.add(buttonProcess);
		buttonPanel.add(buttonConfirm);
		buttonPanel.add(buttonView);
		buttonPanel.add(buttonFinish);
		buttonPanel.add(buttonNew);
		buttonPanel.add(buttonExit);
		
		resetLabelsandButtons();
		
		frame.setVisible(true);
		
	}
	
	public static void main (String [] args)
	{
		new GUI();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		
		if (e.getSource() == buttonNew)
		{
			resetLabelsandButtons();
		}
		
		if (e.getSource() == buttonExit)
			System.exit(0);
		
		if (e.getSource() == buttonProcess)
		{
			processItem();
		}
		
		if (e.getSource() == buttonConfirm)
		{
			confirmItem();
		}
		
		if (e.getSource() == buttonView)
		{
			viewOrder();
		}
		
		if (e.getSource() == buttonFinish)
		{
			finishOrder();
		}
	}
	
	public void clearField(JTextField fieldToClear)
	{
		fieldToClear.setText("");
	}
	
	public void processItem()
	{
		boolean inInventory;
		
		if (blankFields())
		{
			return;
		}
		
		try 
		{
			String bookID = IDText.getText();
			inInventory = test.checkItem(bookID);
			if (!inInventory)
			{
				JOptionPane.showMessageDialog(null, "Book ID " + bookID + " not in file");
				return;
			}
		}
		
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"Inventory.txt is not found");
		}
		
		quantityOfItem = Integer.parseInt(quantityText.getText());
		
		try 
		{
			inventoryString = test.inventoryString(IDText.getText());
			itemPrice = test.getPrice(inventoryString);
			itemName = test.getName(inventoryString);
			subTotal = subTotal + test.calculateItemPrice(itemPrice,quantityOfItem);
			subTotal = test.doubleFormat(subTotal);
			itemInfo = test.createItemInfo(IDText.getText(),itemName,itemPrice,quantityOfItem);
			infoText.setText(itemInfo);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"Error found while trying to process. Could not process.");
			return;
		}
		
		
		buttonProcess.setEnabled(false);
		buttonConfirm.setEnabled(true);
	}
	
	public boolean blankFields()
	{
		if (numText.getText().equals("") || Integer.parseInt(numText.getText()) < 1)
		{
			JOptionPane.showMessageDialog(null,"Number of items must be a positive number");
			return true;
		}
		
		else if (IDText.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null,"Item ID is empty");
			return true;
		}
		
		else if (quantityText.getText().equals("") || Integer.parseInt(quantityText.getText()) < 1)
		{
			JOptionPane.showMessageDialog(null,"Item's quantity must be a positive number");
			return true;
		}
		else
			return false;
	}
	
	public void confirmItem()
	{
		if (blankFields())
		{
			buttonProcess.setEnabled(true);
			buttonConfirm.setEnabled(false);
			return;
		}
		
		numText.setEnabled(false);
		JOptionPane.showMessageDialog(null,"Item #" + (currentItem) + " accepted");
		buttonProcess.setEnabled(true);
		buttonConfirm.setEnabled(false);
		buttonView.setEnabled(true);
		buttonFinish.setEnabled(true);
		nextItem();
	}
	
	public void nextItem()
	{
		if (currentItem == DEFAULT_CURRENT_ITEMS)
		{
			order = new String [Integer.parseInt(numText.getText())];
		}
		order[currentItem-1] = infoText.getText();
		currentItem++;
		subtotalText.setText(String.valueOf(subTotal));
		if (currentItem-1 == Integer.parseInt(numText.getText()) || Integer.parseInt(numText.getText()) == 1)
		{
			buttonProcess.setEnabled(false);
			buttonConfirm.setEnabled(false);
			IDLabel.setVisible(false);
			quantityLabel.setVisible(false);
			return;
		}
		setLabelsandButtons(currentItem);
	}
	
	public void setLabelsandButtons(int newNum)
	{
		IDLabel.setText("Enter Book ID for item #" + newNum + ":");
		quantityLabel.setText("Enter the Quantity for item #" + newNum + ":");
		infoLabel.setText("Item #" + newNum + " Info:");
		buttonProcess.setText("Process Item #" + newNum);
		buttonConfirm.setText("Confirm Item #" + newNum);
		subtotalLabel.setText("Order subtotal for " + (newNum-1) + " item(s):");
		clearField(IDText);
		clearField(quantityText);
	}
	
	public void resetLabelsandButtons()
	{
			clearField(numText);
			clearField(infoText);
			clearField(subtotalText);
			buttonConfirm.setEnabled(false);
			buttonView.setEnabled(false);
			buttonFinish.setEnabled(false);
			buttonProcess.setEnabled(true);
			numText.setEnabled(true);
			IDLabel.setVisible(true);
			quantityLabel.setVisible(true);
			quantityOfItem = 0;
			subTotal = 0;
			order = null;
			currentItem = DEFAULT_CURRENT_ITEMS;
			setLabelsandButtons(currentItem);
	}
	public void viewOrder()
	{
		JOptionPane.showMessageDialog(null,createViewOrder());
	}
	public String createViewOrder()
	{
		String orderList = "";
		
		for (int i = 0; i < order.length; i++)
		{
			if (order[i] != null)
				orderList = orderList + ((i+1) + ". " + order[i] + "\n");
		}
		return orderList;
	}
	
	public void finishOrder()
	{
		String receipt;
		buttonProcess.setEnabled(false);
		buttonConfirm.setEnabled(false);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yy, HH:mm:ss a z");
		ZonedDateTime now = ZonedDateTime.now();
		receipt = dtf.format(now) + "\n\nNumber of line items: " + (currentItem-1) + "\n\nItem# / ID / Title / Price / Qty / Disc % / Subtotal:\n\n" + createViewOrder() + "\n\nOrder subtotal: " + subTotal + "\n\nTax rate: 6%\n\nTax amount: " + test.doubleFormat((.06*subTotal)) + "\n\nOrder total: " + test.doubleFormat(((.06*subTotal)+subTotal)) + "\n\nThanks for shopping at the Ye Olde Book Shoppe!";
		try 
		{
			test.printTransaction(order);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		JOptionPane.showMessageDialog(null,receipt);
		buttonFinish.setEnabled(false);
	}
}