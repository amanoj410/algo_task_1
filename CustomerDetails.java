package Atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerDetails{
	static String accno;
    static String accountHolder;
    static int totalAtmBalance;
    static String pinNumber;
    
	 static int accountBalance;
	 static String choice;
	 static Connection conn;
	 static PreparedStatement insert;
	 public void customerDetails() throws SQLException {
		 Scanner sc=new Scanner(System.in);
			
			int count=sc.nextInt();
			for(int i=0;i<count;i++)
			{
			System.out.println("enter the customer details");
		     insert=conn.prepareStatement("insert into customer values(?,?,?,?)");
		   
		     accno=sc.next();
		     accountHolder=sc.next();
		     pinNumber=sc.next();
		     accountBalance=sc.nextInt();
		    insert.setString(1,accno);
		    insert.setString(2,accountHolder);
		    insert.setString(3,pinNumber);
		    insert.setInt(4,accountBalance);
		   
		    insert.execute();
		    System.out.println("insert successful");
			}
	 }
	 public static void options(String validAccount,String validPin) throws SQLException{
	 Scanner s=new Scanner(System.in);
	 String accno=validAccount,pinno=validPin;
	 System.out.println("Enter your choice in string 1.CheckBalance 2.WithDraw 3.TransferMoney");
	 choice=s.nextLine();
	 
	 switch(choice){
	 case "checkbalance":{
		 String query="select * from customer where AccNo=? and PinNumber=?";
		 insert=conn.prepareStatement(query);
		 insert.setString(1,accno );
		 insert.setString(2, pinno);
		 ResultSet rs=insert.executeQuery();
		 while(rs.next()) {
			System.out.print("Your account Balance is:"+rs.getInt("AccountBalance")); 
		 }
		 }
	 break;
	 case "withdraw":{
		 System.out.print("enter the amount to withdraw");
	 int withDraw=s.nextInt(),accountbal;
	 System.out.println("you have withdrawn Rs."+withDraw);
	 String query="select AccountBalance from customer where Accno=? and PinNumber=?";
	 insert=conn.prepareStatement(query);
	 insert.setString(1, validAccount);
	 insert.setString(2, validPin);
	 insert.execute();
	 ResultSet rs=insert.executeQuery();
	 while(rs.next()) {
		accountbal=rs.getInt("AccountBalance"); 
	  
	 
	 int UpdateAccountBalance=accountbal-withDraw;
	 String query1="Update customer set AccountBalance=AccountBalance-?";
	 System.out.print("updatesuccessful");
	 insert=conn.prepareStatement(query1);
	 insert.setInt(1,withDraw );
	 insert.execute();
	System.out.println("Your updated account balance is:"+rs.getInt("AccountBalance"));
	 
	 }
	 }
	 break;
	 case "transfermoney":{
		 Scanner sc=new Scanner(System.in);
	 
	 System.out.print("Enter the accountnumber to transfer the money");
	 String newAccountNumber=s.next();
	 System.out.println("enter the amount");
	 int money=sc.nextInt(),newBalance=0,oldBalance=0;
	 String query="select AccountBalance from customer where AccNo=?";
	 insert=conn.prepareStatement(query);
	 insert.setString(1, newAccountNumber);
     insert.execute();
	 ResultSet rs=insert.executeQuery();
	 while(rs.next()) {
		 if(rs.getString("AccNo").equals(newAccountNumber))
		 {	    
			 newBalance=newBalance+rs.getInt("AccountBalance")+money;
			 String query1="Update customer set AccountBalance=? where AccNo=?";
			 insert=conn.prepareStatement(query1);
			 insert.setInt(1,newBalance );
			 insert.setString(2,newAccountNumber);
			 insert.execute();
		 }
		 
		 else if(rs.getString("AccNo").equals(validAccount)) {
		 oldBalance=rs.getInt("AccountBalance")-money;
		 
		
		 String query2="Update customer set AccountBalance=? where accno=?";
		 insert=conn.prepareStatement(query2);
		 insert.setInt(1,oldBalance );
		 insert.setString(2, validAccount);
		 insert.execute();
		 }
	 System.out.println("money transfered successfully to the account:"+newAccountNumber+"\n NewAccount Balance is:"+newBalance);
	 }
	 }
	 
	 }}
	 
	
public static void main(String[] args) throws SQLException, ClassNotFoundException {
	
	Class.forName("com.mysql.cj.jdbc.Driver");
	 conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/atmdatabase","root","Manoj@2002");
	System.out.println("enter data count to insert");
	CustomerDetails customer=new CustomerDetails();
	      Scanner sc=new Scanner(System.in);
	      System.out.println("enter the task to be completed 1. to load money\n2. to show customer details\n3. To handle atm process");
          int task=sc.nextInt();
          switch(task) {
          case 1:customer.loadMoney();
          case 2:customer.customerDetails();
          case 3:customer.customerValidation();
          
          }
	   
		 
		
		 
		
		 
	
}
private void customerValidation() throws SQLException {
	// TODO Auto-generated method stub
	Scanner sc=new Scanner(System.in);
	System.out.println("enter your AccountNumber and PinNumber for validation");
	  String accountNumber=sc.next();
	     String pin=sc.next();
		 String queryCheck="select * from customer where accno=? and pinNumber=?";
		 insert=conn.prepareStatement(queryCheck);
		 insert.setString(1, accountNumber);
		 insert.setString(2, pin);
		 ResultSet rs=insert.executeQuery();
		 while(rs.next()) {
			if(rs.getString("AccNo").equals(accountNumber) && rs.getString("PinNumber").equals(pin)) {
				System.out.println("Account Exist");
				options(accountNumber,pin);
			}
		 }
	
}
private void loadMoney() throws SQLException {
	// TODO Auto-generated method stub
	System.out.println("how much money your need to load the machine");
	System.out.println("count of 2000 and 500 and 100");
	Scanner sc=new Scanner(System.in);
	int TwoThoNote=sc.nextInt(),fiveHunnote=sc.nextInt(),HunNote=sc.nextInt();
	totalAtmBalance=(fiveHunnote*500)+(HunNote*100)+(TwoThoNote*2000);
	insert=conn.prepareStatement("insert into atmbalance values(?,?,?)");	       
   insert.setInt(1,2000);
   insert.setInt(2,TwoThoNote);
   insert.setInt(3,TwoThoNote*2000);
   insert.execute();
   insert=conn.prepareStatement("insert into atmbalance values(?,?,?)");
   insert.setInt(1,500);
   insert.setInt(2,fiveHunnote);
   insert.setInt(3,fiveHunnote*500);
   insert.execute();
   insert=conn.prepareStatement("insert into atmbalance values(?,?,?)");
   insert.setInt(1,100);
   insert.setInt(2,HunNote);
   insert.setInt(3,HunNote*100);
   insert.execute();
   System.out.println(" Atm money insert successful");
}
    
}


