/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


/**
 *
 * @author ACER
 */
public class koneksi {
    Connection con;
    Statement stm;
    
    public static String username;
    public void config(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kasir", "root", "");
            stm = con.createStatement();
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Koneksi Gagal: " + e.getMessage());
        }
    }
}
