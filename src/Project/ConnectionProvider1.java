/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author 322am
 */
public class ConnectionProvider1 {
    
     public static void main(String[] args)
    {
        getCon();
    }
   
    public static Connection getCon()
    {
        Connection con = null;
       // Statement st;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
     con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/data?zeroDateTimeBehavior=CONVERT_TO_NULL ","root","");
        }
          
        catch(Exception e)
        {
            System.out.println(e);
        }
        return con;
    
}
}