package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.*;
import java.util.*;

public class AccountDAO {
    public Account getAccountByUsername(String targetUsername){
        Account account = null;
        
        try(Connection conn = ConnectionUtil.getConnection()){
            String query = "SELECT username FROM Account WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, targetUsername);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int accountId = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                account = new Account(accountId, username, password);
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        return account;
    }

    public Account getAccountByUsernameAndPassword(String targetUsername, String targetPassword){
        Account account = null;
        
        try(Connection conn = ConnectionUtil.getConnection()){
            String query = "SELECT password FROM Account WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, targetUsername);
            ps.setString(2, targetPassword);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int accountId = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                account = new Account(accountId, username, password);
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Get Account Username and Password SQL Error: " + e);
        }
        return account;
    }


    public List<Account> getAllAccounts(){
        List<Account> account = new ArrayList<>();
        
        try(Connection conn = ConnectionUtil.getConnection()){
            String query = "SELECT * FROM Account";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int accountId = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                account.add(new Account(accountId, username, password));
            }
            conn.close();
            
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        return account;
    }
}
