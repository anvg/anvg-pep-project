package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.*;
import java.util.*;

public class AccountDAO {
    public boolean registerUser(Account account){
        boolean isRegister = false;
        final boolean USERNAME_NOT_BLANK = account.getUsername().length() != 0;
        final boolean PASSWORD_FOUR_CHARACTER_MINIMUN = account.getPassword().length() >= 4;
        final boolean ACCOUNT_EXISTS = getAccountByUsername(account.getUsername()) == null;
        
        if(USERNAME_NOT_BLANK && PASSWORD_FOUR_CHARACTER_MINIMUN &&
        ACCOUNT_EXISTS){

            try(Connection conn = ConnectionUtil.getConnection()){
                String query = "INSERT INTO Account(account_id, username, " +
                "password) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
    
                int idNumber = generateAccountId(account);

                // simulate account_id auto_increment
                if(idNumber != -1){
                    account.setAccount_id(idNumber+1);;
                }
                
                ps.setInt(1, account.getAccount_id());
                ps.setString(2, account.getUsername());
                ps.setString(3, account.getPassword());
                ps.executeUpdate();
                
                isRegister = true;
                conn.close();

            }catch(SQLException e){
                System.out.println("Create Message SQL Error: " + e);
            }
        }
        
        return isRegister;
    }

    public int generateAccountId(Account account){
        int nextId = -1;
        
        try(Connection conn = ConnectionUtil.getConnection()){

            String query = "SELECT MAX(account_id) FROM Account";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                nextId = rs.getInt("MAX(account_id)");
            }
            

            conn.close();

        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        
        return nextId;
    }

    public Account getAccount(Account account){
        Account target = null;
        
        try(Connection conn = ConnectionUtil.getConnection()){
            String query = "SELECT * FROM Account WHERE account_id = ? &&" +
            "username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, account.getAccount_id());
            ps.setString(2, account.getUsername());
            ps.setString(3, account.getPassword());
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int accountId = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                account = new Account(accountId, username, password);
            }
            conn.close();

        }catch(SQLException e){
            System.out.println("Select Account SQL Error: " + e);
        }
        return target;
    }

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
            String query = "SELECT account_id, username, password FROM Account "
            + "WHERE username = ? AND password = ?";
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
