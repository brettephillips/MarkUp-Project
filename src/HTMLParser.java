/*
 * @author Brett Phillips
 * Markup Project - Red Ventures
 */ 

import java.util.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;

/* A java class that will read in an HTML file and give it a score based on a certain set of criteria */
public class HTMLParser{
   
   //Variable that will keep track of the total score of each file
   private int totalScore = 0;
   private Scanner scan;
   //Variable that will create the filepath
   private String filename = "../data/";
   private String[] nameList;
   private String uniqueID;
   private String findName;
   //FileReader and BufferedReader objects which will read in the data from the HTML file
   private FileReader fr;
   private BufferedReader br;
   //Variable that will be used to establish a connection to the database
   private Connection conn;
   
   /* Empty default constructor */
   public HTMLParser(){  
   }
   
   /* A method that will ask the user the name of the HTML
      file that they would like to check. It will then concatenate 
      the string to the filepath, so that it will be able to locate
      the file. */
   public void checkFileName(){
      
      scan = new Scanner(System.in);
      System.out.println("Which HTML file would you like to use?");
      //Adds the prefix to the filepath
      filename += scan.nextLine() + ".html";
      findName = filename.substring(8);
      //Acquires the specific name related to the file
      nameList = findName.split("_");
      uniqueID = nameList[0];
   }
   
   /* A method that will parse the HTML file to determine
      the score on the specified criteria. */
   public void parseFile(){
      
      File chosenFile = new File(filename);
      
      try{
         //Inizializes the objects and passes through the filename
         fr = new FileReader(chosenFile);
         br = new BufferedReader(fr);
      }catch(FileNotFoundException fnfe){
         System.err.println("The file was not found.");
         System.exit(0);
      }
      
      try{
         String line = br.readLine();
         //While loop that will keep going until there are no more lines to read
         while(line != null){
            //Converts everything in the line to lowercase, which will solve a case-insensitive problem
            line.toLowerCase();
            //Checks tags based on certain criteria and adds the score to the variable "Total Score"
            if(line.contains("<div") || line.contains("<h1")){
               totalScore += 3;
            }else if(line.contains("<p")){
               totalScore += 1;
            }else if(line.contains("<h2")){
               totalScore += 2;
            }else if(line.contains("<html") || line.contains("<body")){
               totalScore += 5;
            }else if(line.contains("<header") || line.contains("<footer")){
               totalScore += 10;
            }else if(line.contains("<font") || line.contains("<strike")){
               totalScore -= 1;
            }else if(line.contains("<center") || line.contains("<big") || line.contains("<tt")){
               totalScore -= 2;
            }else if(line.contains("<frameset") || line.contains("<frame")){
               totalScore -= 5;
            }else{
            }  
            line = br.readLine();
         }
         System.out.println(uniqueID + " received " + totalScore + " points.");
      }catch(IOException ioe){
         System.err.println("Unable to read the file.");
         System.exit(0);
      }
      
      try{
         //Closes the connection to the FileReader and BufferedReader
         fr.close();
         br.close(); 
      }catch(IOException ioe){
         System.err.println("The file could not be closed.");
         System.exit(0);
      }
   }
   
   /* A method that will connect the java program to a database on the users local computer */
   public void connect(){
      
      //Getting the url, username, and password for the database
      String url = "jdbc:mysql://localhost:3306/Users";
      String username = "root";
      String password = "";
      
      try{
         //Connecting to the database
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(url, username, password);
         System.out.println("Successfully connected to the database!\n");
      }catch(Exception e){
         System.err.println("Unable to connect to the database.");
         System.exit(0);
      }
   }
   
   /* A method that will save the results from the html file that was parsed to the database */
   public void saveResults(){
      
      try{
         //Prepares the statement to save data to the database and executes the table update
         PreparedStatement saveData = conn.prepareStatement("INSERT INTO FINALSCORES (fileName, name, score) VALUES ('"+findName+"', '"+uniqueID+"', '"+totalScore+"')");
         saveData.executeUpdate();
      }catch(SQLException sqle){
         System.err.println("There was a problem updating the table or the entry already exists\n");
      }
   }
   
   /* A method that will retrieve the scores from a uniqueID */
   public void findScores(){
      String option;
      
      try{
         //Allows the user to enter a uniqueID
         System.out.println("Who would you like to retrieve scores for?");
         uniqueID = scan.nextLine();
         //Prepares the statement to retrieve the data from the database and executes the query
         PreparedStatement retrievedScores = conn.prepareStatement("SELECT score AS scores FROM FINALSCORES WHERE name LIKE '"+uniqueID+"'");
         ResultSet rs = retrievedScores.executeQuery();
         System.out.println("Retrieving scores for the unique ID, " + uniqueID + ":\n");
         //Prints out the header
         System.out.println("scores");
         
         //While loop that will print out the entries found
         while(rs.next()){
            int score = rs.getInt("scores");
            System.out.println(score);
         }
      }catch(SQLException sqle){
         System.err.println("There was a problem retrieving data for the table");
      }
      //Allows the user to check other scores if they would like
      System.out.println("\nWould you like to retrieve scores for someone else?");
      option = scan.nextLine();
      if(option.toLowerCase().equals("yes")){
         findScores();
      }
   }
   
   /* A method that will retrieve the scores run in the system for a custom date range */
   public void dateScored(){
      String option;
      String startDate;
      String endDate;
      
      try{
         //Allows the user to enter a start and end date
         System.out.println("Please enter a start date (yyyy-mm-dd):");
         startDate = scan.nextLine();
         System.out.println("Please enter an end date (yyyy-mm-dd):");
         endDate = scan.nextLine();
         //Prepares the statement to retrieve the data from the database and executes the query
         PreparedStatement retrievedScores = conn.prepareStatement("SELECT DATE(date_saved) AS theDate, name, score FROM FINALSCORES HAVING theDate BETWEEN '"+startDate+"' AND '"+endDate+"'");
         ResultSet rs = retrievedScores.executeQuery();
         System.out.println("Retrieving scores in the system for a custom date range:\n");
         //Prints out the header
         System.out.println("theDate\t\tname\t\tscores");
         
         //While loop that will print out the entries found
         while(rs.next()){
            String theDate = rs.getString("theDate");
            String name = rs.getString("name");
            int score = rs.getInt("score");
            System.out.println(theDate + "\t" + name + "\t\t" + score);
         }
      }catch(SQLException sqle){
         System.err.println("There was a problem retrieving data for the table");
      }
      //Allows the user to check other scores if they would like
      System.out.println("\nWould you like to retrieve scores for a different date range?");
      option = scan.nextLine();
      if(option.toLowerCase().equals("yes")){
         dateScored();
      }
   }
   
   /* A method that will retrieve the highest scored unique ID */
   public void highestScore(){
      try{
         //Prepares the statement to retrieve the data from the database and execute the query
         PreparedStatement retrievedScores = conn.prepareStatement("SELECT name, score FROM FINALSCORES WHERE score = (SELECT MAX(score) FROM FINALSCORES)");
         ResultSet rs = retrievedScores.executeQuery();
         System.out.println("\nRetrieving the highest scored unique ID:\n");
         //Prints out the header
         System.out.println("name\tscore");
         
         //While loop that will print out the entry found
         while(rs.next()){
            String name = rs.getString("name");
            int score = rs.getInt("score");
            System.out.println(name + "\t" + score);
         }
      }catch(SQLException sqle){
         System.err.println("There was a problem retrieving data for the table");
         System.exit(0);
      }
   }
   
   /* A method that will retrieve the lowest scored unique ID */
   public void lowestScore(){
      try{
         //Prepares the statement to retrieve the data from the database and execute the query
         PreparedStatement retrievedScores = conn.prepareStatement("SELECT name, score FROM FINALSCORES WHERE score = (SELECT MIN(score) FROM FINALSCORES)");
         ResultSet rs = retrievedScores.executeQuery();
         System.out.println("\nRetrieving the lowest scored unique ID:\n");
         //Prints out the header
         System.out.println("name\tscore");
         
         //While loop that will print out the entry found
         while(rs.next()){
            String name = rs.getString("name");
            int score = rs.getInt("score");
            System.out.println(name + "\t" + score);
         }
      }catch(SQLException sqle){
         System.err.println("There was a problem retrieving data for the table");
         System.exit(0);
      }
   }
   
   //Main method
   public static void main(String [] args){
      //Creates the object and calls it's methods
      HTMLParser parser = new HTMLParser();
      parser.checkFileName();
      parser.parseFile();
      parser.connect();
      parser.saveResults();
      parser.findScores();
      parser.dateScored();
      parser.highestScore();
      parser.lowestScore();
   }
}