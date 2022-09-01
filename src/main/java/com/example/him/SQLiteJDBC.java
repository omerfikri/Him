package com.example.him;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class SQLiteJDBC {
    int sayi;
    public  void create(){
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Opened JDBC connection");

            statement = connection.createStatement();
            String sql = "Create Table Him "+
                    "(ID                  INTEGER PRIMARY KEY   NOT NULL,"+
                    "FilterName           CHAR(50)           NOT NULL,"+
                    "Aktif                CHAR(50)           NOT NULL,"+
                    "Companies            BLOB,"+
                    "Fons                 BLOB,"+
                    "CompanyNotifications BLOB,"+
                    "FonNotification      BLOB)";
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
            System.out.println("Table Created");
        } catch (Exception e) {
            System.out.println("Already created Table Him");
        }
    }
    public  void insert(String name, String check, List<String> liste1, List<String> liste2, List<String> liste3, List<String> liste4, int id){
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
            System.out.println("Opened database succesfully ");

            statement = connection.createStatement();

            String sql = "insert into Him (ID, FilterName, Aktif, Companies, Fons, CompanyNotifications,FonNotification)" +
                    " VALUES ('"+(id+1)+"' ,'"+name+"' , '"+check+"' , '"+liste1+"' , '"+liste2+"','"+liste3+"','"+liste4+"');";

            statement.executeUpdate(sql);
            statement.close();
            connection.commit();
            connection.close();
            System.out.println("added");
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }
    public void delete(String name){
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
            System.out.println("Opened database succesfully ");

            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM Him");

            while (rs.next()){
                int id = rs.getInt("id");
                String filterName = rs.getString("FilterName");
                String aktif = rs.getString("Aktif");
                String companies = rs.getString("Companies");

                if(name.equals(filterName)){
                    sayi=id;
                }
            }
            String sql = "DELETE from Him WHERE ID="+sayi+";";
            statement.executeUpdate(sql);
            connection.commit();
            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("delete");
    }

    public void update(int id,String name, String check,
                       List<String> liste1, List<String> liste2,
                       List<String> liste3, List<String> liste4){
        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
            System.out.println("Opened database succesfully ");

            statement = connection.createStatement();
            String sql = "UPDATE Him set FilterName ='"+name+
                    "',Aktif ='"+check+"',Companies ='"+liste1+
                    "',Fons ='"+liste2+"',CompanyNotifications ='"+liste3+
                    "',FonNotification ='"+liste4+"' WHERE ID='"+id+"';";
            statement.executeUpdate(sql);
            statement.close();
            connection.commit();
            connection.close();

        }catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
