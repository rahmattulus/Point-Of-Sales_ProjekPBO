/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.sql.*;

public class config {
    private static Connection mysqlconfig;
    
    public static Connection configDB() throws SQLException{
        try{
            String url = "jdbc:mysql://http://localhost/phpmyadmin/kasir/useSSL=false";
            String user = "root";
            String pass = "";
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch(Exception e){
            System.out.println (e);
        }
        return mysqlconfig;
    }
}
