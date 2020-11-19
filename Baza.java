/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mycompany.docker;
//package com.mycompany.docker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Kasia
 */
public class Baza {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
 
        Connection conn;
        conn=null;
        Statement statement=null;
        ResultSet resultSet=null;
         int liczba = 0;
        System.out.println("application works");
  	wyswietlanie();
            String wybor="";

            statement=connectToDatabase(conn);

	
         do{
           
           // wyswietlanie();
           
            Scanner scanner = new Scanner(System.in);
       
            liczba=parsowanie(scanner);
               
            
            switch (liczba) {
                case 1:
                    statement.executeUpdate(AddRow());
                    System.out.println("Dodano pomyślnie");
                    break;
                case 2:
                    statement.executeUpdate(DeleteRow());
                    System.out.println("Usunieto pomyslnie");
                    break;
                case 3:
                    resultSet = statement.executeQuery(Select());
                    while(resultSet.next())
                    {
                        System.out.println(resultSet.getString("Id")+" "+resultSet.getString("Tytul")+" "+resultSet.getString("Autor"));
                    }
                    break;
                case 4:
                    UpdateRow(statement, resultSet);
                    System.out.println("Zaktualizowano pomyślnie");
                    break;
                default:
                   break;
                //instrukcje, gdy nie znaleziono żadnego pasującego przypadku
            }
           }while(liczba!=0);
         if(liczba==0)
         {
              if (conn != null) {
                try {
                    conn.close();
                    
                } catch (Exception e) {
                    System.out.println(e); }
            }
             
         }
            
        }  
        
    

    public static String AddRow() {
        String[] nazwy = {"Id", "Tytul", "Autor"};
        Scanner scanner = new Scanner(System.in);
        String values = "";
        for (int i = 0; i < 3; i++) {
            System.out.println("Podaj " + nazwy[i]);
            String value = scanner.nextLine();
            if(i==0)
                values=values+value;
            else
                values = values + "'" + value + "'";
            
            if (i <= 1) {
                values += ",";
            }
        }
        String query = "INSERT INTO Ksiazki (Id, Tytul, Autor)"
                + "VALUES (" + values + ");";
        System.out.println(query);
        return query;
    }

    public static String DeleteRow() {
        System.out.println("Podaj id ksiazki do usuniecia");
        Scanner scanner = new Scanner(System.in);
        String wybor = "";
       // wybor = scanner.nextLine();
        int id = 0;
        id=parsowanie(scanner);
        String query = "DELETE FROM Ksiazki where Id=" + id;
        System.out.println(query);
        return query;
    }

    public static void UpdateRow(Statement st, ResultSet resultSet) throws SQLException {
        System.out.println("Podaj id do zmiany");
        Scanner scanner = new Scanner(System.in);
        String wybor = "";
         
        int id1 = 0;

        id1=parsowanie(scanner);
        System.out.println("select=");
       
        String query = "Select * from Ksiazki where Id=" + id1;
        System.out.println(query);
        resultSet = st.executeQuery(query);
        
        
        while(resultSet.next())
        {
        System.out.println(resultSet.getString("Id")+" "+resultSet.getString("Tytul")+" "+resultSet.getString("Autor"));
        }
        
        String[] nazwy = {"Id", "Tytul", "Autor"};
        scanner = new Scanner(System.in);
        String values = "";
        int id = 0;
        for (int i = 0; i < 3; i++) {
            System.out.println("Podaj " + nazwy[i]);
            int value=0;
            String value1="";

            if (i == 0) {
                    value=parsowanie(scanner);
                    value1=Integer.toString(value);
                    values += nazwy[i] + "=" + value1;
            }
               
            else{
                value1=scanner.next();
             
                values += nazwy[i] + "=" + "'"+value1+"'";
            }
            
            if (i < 2) {
                values += ",";
            }
            
        }
        System.out.println(values);
        query = "Update Ksiazki SET " + values + " WHERE Id=" + id1;
        System.out.println(query);
        st.executeUpdate(query);
        
       
    }

    public static String Select() {
        String query = "Select * from Ksiazki";
        return query;
    }
    public static Statement connectToDatabase(Connection conn) throws SQLException
    {
        Statement st=null;
        do{
	try {
	     
	    Class.forName("com.mysql.jdbc.Driver"); //pobranie sterownika do MySQL 
            System.out.println("laczenie...");
            conn = DriverManager.getConnection("jdbc:mysql://10.0.10.3:3306/docker?serverTimezone=UTC", "kmazurek", "123456");
	    st= conn.createStatement();
            System.out.println("polaczono");
		if(conn!=null)
		break;		
	
	      
                
            //String query="SELECT Code,Name,Population FROM country WHERE upper(Continent) = 'Europe'"; 
           
            System.out.println("Polaczenie powiodlo sie");

		          }
       
		catch(Exception e)
		{
			System.out.println(e);
		}
         }while(conn==null);
         String startQuery = "CREATE TABLE Ksiazki (Id int, Tytul varchar(255), Autor varchar(255))";
            String insertQuery = "INSERT INTO Ksiazki VALUES (1, 'Pan Tadeusz', 'Adam Mickiewicz'),(2, 'Gra o tron', 'George Martin'),(3, 'Hobbit', 'John Tolkien')";
            if (conn != null) {

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "Ksiazki", null);
            if (tables.next()) {
                String dropQuery = "DROP TABLE Ksiazki";
                st.executeUpdate(dropQuery);
            } 
                         
            st.executeUpdate(startQuery);
            st.executeUpdate(insertQuery);
                    
            }
        
		
        return st;
    }
    public static int parsowanie(Scanner scanner)
    {
        int Value=0;
    
        boolean CzyLiczba=false;
        while (scanner.hasNext()) {
    if (scanner.hasNextInt()) {
         Value = scanner.nextInt();
        break;
    } else {
        System.out.println("Podaj dobrą liczbe");
        String stringValue = scanner.next();
    }
           
        }
        return Value;
    }
    public static void wyswietlanie()
    {
            System.out.println("Wybierz jedna z opcji");
            System.out.println("1. Dodaj wiersz ");
            System.out.println("2. Usun wiersz");
            System.out.println("3. Wyswietl zawartosc bazy");
            System.out.println("4. Zmodyfikuj");
            System.out.println("0. Wyjście");
            
    }

}
