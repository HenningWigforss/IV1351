/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package dbconnect;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
/**
 *
 * @author Henning
 */

public class soundgood {
    private PreparedStatement listAllAvailableInstrument;
    private PreparedStatement getInstrumentName;
    private PreparedStatement getSpecificInstrument;
    private PreparedStatement getNumberOfLeases;
    private PreparedStatement createLease;
    private PreparedStatement updateNumberofLeases;
    private PreparedStatement getLeases;
    private PreparedStatement updateLeaseBit;
    private PreparedStatement terminateLease;
    private PreparedStatement getIntrumentID;
    private PreparedStatement refreshlist;
    
    
  public Scanner scanner = new Scanner(System.in); //Create the input scanner for the menu
  public int userid = 0;
 
    private void accessDB(){
        try (Connection connection = createConnection()) {
            System.out.println("Enter User id:  ");   
            userid = scanner.nextInt();
            String command = scanner.nextLine();
                while(true){
                    System.out.println("Enter command or enter help, Current UserID: "+userid);
                    command = scanner.nextLine();
                    //MENU with IF STATEMENTS
                    if(command.equals("help")){
                        System.out.println("list = lists all available instruments for rent");
                        System.out.println("list instrument = list specific type of instrument");
                        System.out.println("rent = rent instrument");
                        System.out.println("terminate = return instrument");                
                        System.out.println("lease = show all your leasings");
                        System.out.println("exit = exit program");
                        System.out.println("login = change studentID");
                    //command list
                    } else if(command.equals("list")){
                        prepareStatements(connection);  
                        listAllRows();
                     }
                    //commad list instrument
                    else if(command.equals("list instrument")){
                        prepareStatements(connection);
                        listInstrumentName();
                        System.out.println("\nchoose instrument");
                        listSpecificInstrument(connection);
                        
                   //command to rent a instrument Must know what instrument to rent before use.     
                    }else if(command.equals("rent")){
                        int i = getNumberLeases(connection);
                    //Check if the student allready has 2 rented intruments
                        if(i==2){
                            System.out.println("Maximum number of leases reach"); 
                    }else{
                        rentInstrument(connection);
                    }
                    }else if(command.equals("lease")){ //calls the function to lease instrument
                        getLeases(connection);
                    }else if(command.equals("terminate")){//calls the function to terminate lease
                        terminateRent(connection);
        
                    }else if (command.equals("exit")){ //exit the program
                        System.exit(0);
                    }else if (command.equals("login")){ //changes student id
                        
                        System.out.println("Enter new student ID"); 
                        int u = scanner.nextInt();
                        scanner.nextLine();
                        userid=u;
                        
                    }else{
                        System.out.println("Invalid command");
                    }
    
                }
                    
        }catch (SQLException | ClassNotFoundException exc) {
      exc.printStackTrace();
    }    
             
    }
    
  private Connection createConnection() throws SQLException, ClassNotFoundException { //creates connection to database
    Class.forName("org.postgresql.Driver");
    Connection connection =  DriverManager.getConnection("jdbc:postgresql://localhost:5432/soundgooddb",
      "postgres", "example");
      return connection;
  }   
   

  private void listAllRows() { //list all instrument available
    try (ResultSet instrument = listAllAvailableInstrument.executeQuery()) {
     while (instrument.next()) {
        System.out.println(
            "ID: " + instrument.getInt(1) + ", Brand: " + instrument.getString(2) + ", Instrument: " + instrument.getString(3) + ", Fee: " + instrument.getInt(4));
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  private void listInstrumentName() { //lists all type/name of instrument
    try (ResultSet instrument = getInstrumentName.executeQuery()) {
     while (instrument.next()) {
        System.out.println(instrument.getString(1));
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }

private void listSpecificInstrument(Connection connection) {      //list all instrument of selected type       
    String command = scanner.nextLine();
    try {
        prepareStatements(connection);
        getSpecificInstrument.setString(1, command);
        ResultSet instrument = getSpecificInstrument.executeQuery();
        while (instrument.next()) {
        System.out.println(
            "ID: " + instrument.getInt(1) + ", Brand: " + instrument.getString(2) + ", Instrument: " + instrument.getString(3) + ", Fee: " + instrument.getInt(4));
      }
                            
                        }catch (SQLException sqle) {
      sqle.printStackTrace();
    }  
} 
  
private int getNumberLeases(Connection connection) { //gets the number of leases a student has and return it as an int
            int number = 0;
    try {
        prepareStatements(connection);
        getNumberOfLeases.setInt(1, userid);
        ResultSet iNumber = getNumberOfLeases.executeQuery();
        while (iNumber.next()) {
            number = iNumber.getInt(1);
      }                         
                        }catch (SQLException sqle) {
      sqle.printStackTrace();
    }

    return number;
}


private void rentInstrument(Connection connection) { //rents a instrument
    try {
        System.out.println("What object would you like to rent?");
        int object = scanner.nextInt(); //choose what instrument to rent by ID
        scanner.nextLine();
        prepareStatements(connection);
        createLease.setInt(1, object);
        createLease.setInt(1, object);
        createLease.executeUpdate(); //creates a new leasing connected to the instrument and student
        System.out.println("Objekt number: "+ object +" was rented");  
        
        int i = getNumberLeases(connection); //updates the number that tells how many leases the student has
        i=i+1;
        updateNumberofLeases.setInt(1,i);
        updateNumberofLeases.executeUpdate();
        
        updateLeaseBit.setInt(1,1);      //update the leased value of the instrument stock
        updateLeaseBit.setInt(2,object);
        updateLeaseBit.executeUpdate();
        
        refreshlist.executeUpdate();
        
                        }catch (SQLException sqle) {
      sqle.printStackTrace();
    }
}

private void getLeases (Connection connection){ //shows all leases connected to studentid
    try{
        System.out.println("Your rentals");
        prepareStatements(connection);
        ResultSet resultLeases = getLeases.executeQuery();
        while (resultLeases.next()) {
            System.out.println("LeaseID: " +resultLeases.getInt(1)+" StudentID: "+resultLeases.getInt(2)+" InstrumentID: "+resultLeases.getInt(3)+"Start date: "+resultLeases.getDate(4)+" Return date: "+resultLeases.getDate(5)+" Status:"+resultLeases.getString(6)+"");
      }
        
    }catch (SQLException sqle) {
      sqle.printStackTrace();
    }
}
//return a leased object
private void terminateRent(Connection connection) { 
    try {
        System.out.println("What object would you like to terminate?");
        getLeases(connection); //shows your leases
        int object = scanner.nextInt();
        scanner.nextLine();
        prepareStatements(connection);
        terminateLease.setInt(1, object);
        terminateLease.executeUpdate(); //updates the leasing
        System.out.println("Objekt number: "+ object +" was returned");  
        
        int i = getNumberLeases(connection);
        i=i-1;
        updateNumberofLeases.setInt(1,i); //deducts one from your number of leased instruments
        updateNumberofLeases.executeUpdate();
        
        int insID = 0;
        
        getIntrumentID.setInt(1,object); //get the instrument id from leasing that was terminated
        ResultSet instrumentID=getIntrumentID.executeQuery();
        while (instrumentID.next()) {
            insID=instrumentID.getInt(1);
      }
        prepareStatements(connection);//set the lease satatus on the instrument stock to 0
        updateLeaseBit.setInt(1,0);      
        updateLeaseBit.setInt(2,insID);
        updateLeaseBit.executeUpdate();
        
        refreshlist.executeUpdate();
        
        
                        }catch (SQLException sqle) {
      sqle.printStackTrace();
    }
}








//Prepared SQL statments

  
private void prepareStatements(Connection connection) throws SQLException {
    //Gets a list of all instrument
    listAllAvailableInstrument = connection.prepareStatement("SELECT instrument_stock_id, brand_name, instrument_name, fee FROM instrument_available ORDER BY instrument_name");
    //get list of types of intruments
    getInstrumentName = connection.prepareStatement("SELECT instrument_name FROM instrument_play");
    //select specifiv instrument type
    getSpecificInstrument = connection.prepareStatement("SELECT instrument_stock_id, brand_name, instrument_name, fee FROM instrument_available WHERE instrument_name= ? ");
    // gets the number of leases a student has
    getNumberOfLeases = connection.prepareStatement("SELECT number_of_leases FROM student WHERE student_id= ?");
    //creates a lease for a sutdent
    createLease = connection.prepareStatement("INSERT INTO leasing (Student_id, instrument_stock_id, lease_date, lease_status) VALUES ( " + userid + ",?, CURRENT_DATE,'Rented')");
    //terminats lease
    terminateLease = connection.prepareStatement("UPDATE leasing SET return_date = CURRENT_DATE, lease_status ='Returned' WHERE leasing_id = ? ");
    //update the lease status of an instrument in the instrument stock.
    updateLeaseBit = connection.prepareStatement("UPDATE instrument_stock SET leased = ? WHERE instrument_stock_id= ? "); 
    //updates the number of leases a student has
    updateNumberofLeases = connection.prepareStatement("UPDATE student SET number_of_leases = ? WHERE student_id = "+ userid+";");
    //gets all leases connected to a student
    getLeases = connection.prepareStatement("SELECT leasing_id, student_id, instrument_stock_id, lease_date, return_date, lease_status FROM leasing WHERE student_id = "+ userid+";");
    //get the instument id for a lease.
    getIntrumentID = connection.prepareStatement("SELECT instrument_stock_id FROM leasing WHERE leasing_id= ? ");
    //Refresh instrument list when leased or returned
    refreshlist = connection.prepareStatement("REFRESH MATERIALIZED VIEW instrument_available");
  }


    
    public static void main(String[] args){
        new soundgood().accessDB();
    }
}
