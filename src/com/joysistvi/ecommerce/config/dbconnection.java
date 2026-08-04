/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.joysistvi.ecommerce.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnection {
    private final String url = "jdbc:mysql://localhost:3306/e_commerce_shopping_cart_system_app?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
    private final String user = "root";
    private final String password = "";
    
    public Connection connect(){
        Connection conn = null;
        
        try{
            conn = DriverManager.getConnection(url,user,password);
            
        }catch(SQLException e){
            System.err.println("Database Connection Failed: " + e.getMessage());
        }
        return conn;
    }
}
