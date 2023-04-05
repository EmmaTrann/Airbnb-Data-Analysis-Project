import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Executable;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.math.BigDecimal;


import com.microsoft.sqlserver.jdbc.SQLServerStatement;

public class AirbnbInterface {
    
    public static Scanner myObj;
    public static String option;
    public static void main(String[] args) throws InputMismatchException{

        String connectionUrl = "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
        + "database=cs3380;"
        + "user=tranmb;"
        + "password=7876069;"
        + "encrypt=true;"
        + "trustServerCertificate=true;";

        printMenu();

        myObj = new Scanner(System.in);
        // Numerical input
        option = myObj.next();

        while (!option.equals("0")){

            try {
                if (option.equals("1")){
                    doOption1(connectionUrl);
                }
                else if (option.equals("2")){
                    doOption2(connectionUrl);
                }
                else if (option.equals("3")){
                    doOption3(connectionUrl);
                }
                else if (option.equals("4")){
                    doOption4(connectionUrl);
                }
                else if (option.equals("5")){
                    doOption5(connectionUrl);
                }
                else if (option.equals("6")) {
                    doOption6(connectionUrl);
                }
                else if (option.equals("7")) {
                    doOption7(connectionUrl);
                }
                else if (option.equals("8")) {
                    doOption8(connectionUrl);
                }
                else if (option.equals("9")) {
                    doOption9(connectionUrl);
                }
                else if (option.equals("10")) {
                    doOption10(connectionUrl);
                }
                else if (option.equals("11")) {
                    doOption11(connectionUrl);
                }
                else if (option.equals("12")) {
                    doOption12(connectionUrl);
                }
                else if (option.equals("13")) {
                    doOption13(connectionUrl);
                }
                else if (option.equals("14")) {
                    doOption14(connectionUrl);
                }
                else if (option.equals("15")) {
                    doOption15(connectionUrl);
                }
                else{
                    // reprint menu if user puts wrong input to show possible numbers
                    System.out.println("Invalid input! Please enter 0-5 to choose from one of the above options!");
                    printMenu();
                } // end if-else

                option = myObj.next();
            } // end try
            catch(Exception e){
                // System.out.println("Invalid input");
                e.printStackTrace();
            } // end catch
        } // end while

        System.out.println("Program has ended successfully!");
    } // end main

    // option methods
   
    public static void doOption1(String connection){
        try (Connection c = DriverManager.getConnection(connection)){
            PreparedStatement st = c.prepareStatement("Select rating, (base_price + service_fee) as total_price "
            + "from review join price p on review.pID = p.pID where rating is not NULL order by rating desc");

            ResultSet rs = st.executeQuery();
            String result ="";
            System.out.println("Program is processing slowly... please be patient! :)");
            while(rs.next()){
                result+= option1Result(rs);
            } // end while()
            System.out.println("rating  total_price");
            System.out.println(result);

            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        } // end try
        catch (final SQLException e){
            throw new PersistenceException(e);
        } // end catch
    } // end doOption1()
    
    public static void doOption2(String connection){
        
    	final int MIN = 60;
    	final int MAX = 1440;
    	try (Connection c = DriverManager.getConnection(connection);){
            
            String input1 = "";
            String input2 = "";
            float price_min = 0;
            float price_max = 0;
            boolean inputAcquired = false;
            
            while(!inputAcquired ) {
            
	            System.out.println("Specify the min price(lowest value: 60): ");
	            input1 =myObj.next();
	
	            System.out.println("Specify the max price(highest: 1440): ");
	            input2 = myObj.next();
	            
	            try { 
	            	
	            	/*Try converting string to float.*/
		            price_min = Float.parseFloat(input1);
		            price_max = Float.parseFloat(input2);
		            
		            /*Determine if input is valid.*/
		            if(price_min <= price_max) {
		            	
		            	if (price_min >= MIN && price_max <= MAX ) {
			            	/*Acquired valid input.*/
			            	inputAcquired = true;
		            	} else {
		            		/*Invalid number.*/
		            		System.out.println("Please enter numbers within the valid range: " + MIN + " - " + MAX);
		            	}
		            	
		            } else {
		            	/*Error: min can't be > the max.*/
		            	System.out.println("Please make sure min price <= max price");
		            	
		            }
		            
	            } catch(NumberFormatException e) {
	            	/*Catch if the failed to convert string to float.*/
	            	System.out.println("Invalid Input: Expected a number");
	            }
            }

            if(price_min > price_max) {

                Integer.parseInt("i");
            }

            PreparedStatement st = c.prepareStatement("select distinct property.pID as property_ID, property_name, (base_price + service_fee) as total_price\n" +
            "    from property\n     join price p on property.pID = p.pID    where base_price + service_fee between ? and ?");
            st.setFloat(1, price_min);
            st.setFloat(2, price_max);

            System.out.println("The property listing within the price range of " + price_min + " and " + price_max + ":");
            ResultSet rs = st.executeQuery();
            String result ="";
            System.out.println("Program is processing slowly... please be patient! :)");
            while(rs.next()){
                result+=option2Result(rs);
            } // end while

            System.out.println("property_ID property_name   total_price");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        } // end try
        catch (final SQLException e){
            throw new PersistenceException(e);
        } // end catch
    } // end doOption2()

    public static void doOption3(String connection){
    	
    	final int MAX = 5;
    	final int MIN = 1;
    	
        try (Connection c = DriverManager.getConnection(connection);){
            String input = "";
            int rating = 0;
            boolean inputAcquired = false;


            /*Loop until input is acquired.*/
            while (!inputAcquired) {
                
                System.out.println("Please enter rating from 1 to 5:");
                input = myObj.next();
                
                /*Try to convert the string to a integer.*/
                try {
                	rating = Integer.parseInt(input);
                	
                	/*Determine if input is within the valid range.*/
                	if(rating >= MIN && rating <= MAX) {
                		/*Acquired input.*/
                		inputAcquired = true;
                	} else {
                		System.out.println("Please enter a number with in the valid range: " + MIN + " - " + MAX );
                	}
                	
                } catch(NumberFormatException e) {
                	/*Invalid input.*/
                	System.out.println("Invalid Input: Expected a number");
                	
                }
            }  

            PreparedStatement st = c.prepareStatement("select distinct rating, property.pID as property_ID, property_name, room_type, "
            + "l.neighbourhood as neighbourhood, l.country as country, (base_price + service_fee) as total_price "
            + "from property "
            + "join review r on property.pid = r.pid "
            + "join price p on property.pID = p.pID "
            + "join location l on property.pID = l.pID "
            + "where r.rating in (?);");
            st.setInt(1, rating);
            
            ResultSet rs = st.executeQuery();
            String result ="";
            System.out.println("Program is processing slowly... please be patient! :)");
            while(rs.next()){
                result+= option3Result(rs);
            } // end while()

            System.out.println("rating  property_ID property_name   room_type   neighbourhood   country total_price");
            System.out.println(result);

            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        } // end try
        catch (final SQLException e){
            throw new PersistenceException(e);
        } // end catch
    } // end doOption3()

    public static void doOption4(String connection){
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("Select neighbourhood, Count(*) as number_of_listing " 
            + "from location "
            + "where neighbourhood is not null "
            + "GROUP BY neighbourhood "
            + "order by number_of_listing asc;");

            ResultSet rs =st.executeQuery();
            String result ="";
            System.out.println("Program is processing slowly... please be patient! :)");
            while(rs.next()){
                result+= option4Result(rs);
            } // end while()

            System.out.println("neighbourhood   number_of_listing");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        } // end try
        catch (final SQLException e){
            throw new PersistenceException(e);
        }// end catch
    } // end doOption4()

    public static void doOption5(String connection){
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("Select guest_name, email, count(p.pID) as number_of_listing "
            + "from guest "
            + "join booked_by bb on guest.gID = bb.gid "
            + "join property p on p.pID = bb.pID "
            + "group by guest_name, email");

            ResultSet rs = st.executeQuery();
            String result ="";
            System.out.println("Program is processing slowly... please be patient! :)");
            while(rs.next()){
                result += option5Result(rs);
            } // end while()
            System.out.println("guest_name  email   number_of_listing");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        } // end try
        catch (final SQLException e){
            throw new PersistenceException(e);
        } // end catch
    } // end doOption5()

    public static void doOption6(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from property");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result +=option6Result(rs);
            }

            System.out.println("pID property_name   room_type   construction_year   rules");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public static void doOption7(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from availability");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result +=option7Result(rs);
            }

            System.out.println("pID min_nights  days_per_year");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public static void doOption8(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from booked_by");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result+=option8Result(rs);
            }

            System.out.println("pID gid");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public static void doOption9(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from guest");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result+=option9Result(rs);
            }

            System.out.println("gID guest_name  email");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public static void doOption10(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from host");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result+=option10Result(rs);
            }

            System.out.println("hID host_name   verification_status");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public static void doOption11(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from hosted_by");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result+=option11Result(rs);
            }

            System.out.println("pID hID");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public static void doOption12(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from location");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result+=option12Result(rs);
            }

            System.out.println("pID neighbourhood_group longitude   latitude    country neighbourhood");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public static void doOption13(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from policies");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result+=option13Result(rs);
            }

            System.out.println("pID instant_booking cancellation");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public static void doOption14(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from price");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result+=option14Result(rs);
            }

            System.out.println("pID base_price  service_fee");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public static void doOption15(String connection) {
        try (Connection c = DriverManager.getConnection(connection);){
            PreparedStatement st = c.prepareStatement("SELECT * from review");

            ResultSet rs = st.executeQuery();
            String result = "";
            System.out.println("Program is processing slowly... please be patient! :)");
            while (rs.next()) {
                result+=option15Result(rs);
            }

            System.out.println("pID reviews_per_month   last_review_date    rating  number_of_reviews");
            System.out.println(result);
            rs.close();
            st.close();

            System.out.println("Please enter next input (Enter 0 to exit!):");
        }
        catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    // methods to read from db to print as a result
    public static String option1Result(ResultSet rs) throws SQLException{
        String rating = rs.getString("rating");
        int total_price = rs.getInt("total_price");
        return rating + "   " + total_price + "\n";
    } // end option1Result()

    public static String option2Result(ResultSet rs) throws SQLException{
        int property_ID = rs.getInt("property_ID");
        String property_name = rs.getString("property_name");
        int total_price = rs.getInt("total_price");
        return property_ID + "  "+ property_name + "    " + total_price +"\n";
    } // end option2Result()

    public static String option3Result(ResultSet rs) throws SQLException{
        int rating = rs.getInt("rating");
        int property_ID = rs.getInt("property_ID");
        String property_name = rs.getString("property_name");
        String room_type = rs.getString("room_type");
        String neighbourhood = rs.getString("neighbourhood");
        String country = rs.getString("country");
        int total_price = rs.getInt("total_price");

        return rating + "   " + property_ID + " " + property_name + "   " + room_type + "   " + neighbourhood + "   " + country + " " + total_price + "\n";
    } // end option3Result()

    public static String option4Result(ResultSet rs) throws SQLException{
        String neighbourhood = rs.getString("neighbourhood");
        int number_of_listing = rs.getInt("number_of_listing");

        return neighbourhood + "    " + number_of_listing + "\n";
    } // end option4Result()

    public static String option5Result(ResultSet rs) throws SQLException{
        String guest_name = rs.getString("guest_name");
        String email = rs.getString("email");
        String number_of_listing = rs.getString("number_of_listing");
        
        return guest_name + "   " + email + "   " + number_of_listing +"\n";
    } // end option5Result()

    // property table
    public static String option6Result(ResultSet rs) throws SQLException{
        int pID = rs.getInt("pID");
        String property_name = rs.getString("property_name");
        String room_type = rs.getString("room_type");
        int construction_year = rs.getInt("construction_year");
        String rules = rs.getString("rules");

        return pID + "  " + property_name + "   " + room_type + "   " + construction_year + "   " + rules +"\n";
    }

    //availability table
    public static String option7Result(ResultSet rs) throws SQLException{
        String pID = rs.getString("pID");
        int min_nights = rs.getInt("min_nights");
        int days_per_year = rs.getInt("days_per_year");

        return  pID + "  " + min_nights + " " + days_per_year +"\n";
    }

    // booked_by table
    public static String option8Result(ResultSet rs) throws SQLException{
        int pID = rs.getInt("pID");
        int gid = rs.getInt("gid");

        return pID + "  " + gid +"\n";
    }

    // guest table
    public static String option9Result(ResultSet rs) throws SQLException{
        int gID = rs.getInt("gID");
        String guest_name = rs.getString("guest_name");
        String email = rs.getString("email");

        return gID + "  " + guest_name + "  " + email +"\n";
    }

    // host table
    public static String option10Result(ResultSet rs) throws SQLException{
       int hID = rs.getInt("hID");
       String host_name = rs.getString("host_name");
       String verification_status = rs.getString("verification_status");
       
       return hID + "  " + host_name + "   " + verification_status +"\n";
    }

    // hosted_by table
    public static String option11Result(ResultSet rs) throws SQLException{
        int pID = rs.getInt("pID");
        int hID = rs.getInt("hID");
        return pID + "  " + hID + "\n";
    }

    //location table
    public static String option12Result(ResultSet rs) throws SQLException{
        int pID = rs.getInt("pID");
        String neighbourhood_group = rs.getString("neighbourhood_group");
        BigDecimal longitude = rs.getBigDecimal("longitude");
        BigDecimal latitude = rs.getBigDecimal("latitude");
        String country = rs.getString("country");
        String neighbourhood = rs.getString("neighbourhood");

        return pID + "  " + neighbourhood_group + " " + longitude + "   " + latitude + "    " + country + " " +neighbourhood +"\n";
    }

    // policies table
    public static String option13Result(ResultSet rs) throws SQLException{
        int pID = rs.getInt("pID");
        String instant_booking = rs.getString("instant_booking");
        String cancellation = rs.getString("cancellation");

        return pID + "   " + instant_booking + "    " + cancellation +"\n";
    }

    // price table
    public static String option14Result(ResultSet rs) throws SQLException{
        int pID = rs.getInt("pID");
        float base_price = rs.getFloat("base_price");
        float service_fee = rs.getFloat("service_fee");

        return pID + "  " + base_price + "   " + service_fee +"\n";
    }

    // review table
    public static String option15Result(ResultSet rs) throws SQLException{
        int pID = rs.getInt("pID");
        BigDecimal reviews_per_month = rs.getBigDecimal("reviews_per_month");
        Date last_review_date = rs.getDate("last_review_date");
        int rating = rs.getInt("rating");
        int number_of_reviews = rs.getInt("number_of_reviews");

        return pID + "  " + reviews_per_month + "   " + last_review_date + " " + rating + " " +number_of_reviews + "\n";
    }

    // prints the menu as a loop
    public static void printMenu(){
        System.out.println("This program will allow you to analysis the data about listings on Airbnb");
        System.out.println("1.  Display the relationship between price and rating");
        System.out.println("2.  Display all the property listings in a given price range");
        System.out.println("3.  Display all the property listings in a given rating range with its location and price");
        System.out.println("4.  Count and display ascending the nummber of property listings in each neighbourhood");
        System.out.println("5.  Count the number of listing a guest booked under their name and email");
        System.out.println("6.  Display property table");
        System.out.println("7.  Display availability table");
        System.out.println("8.  Display booked_by table");
        System.out.println("9.  Display guest table");
        System.out.println("10. Display host table");
        System.out.println("11. Display hosted_by table");
        System.out.println("12. Display location table:");
        System.out.println("13. Display policies");
        System.out.println("14. Display price table");
        System.out.println("15. Display review table");
        System.out.println("0. Exit the program");
        System.out.println("Please choose a number from one of the following options:");
    } // end printMenu()
} // end AirbnbInterface.java


class PersistenceException extends RuntimeException {

    public PersistenceException(final Exception cause) {
        super(cause);
    } // end PersistenceException()
} // end PersistenceException
