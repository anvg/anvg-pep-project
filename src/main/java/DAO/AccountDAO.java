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
            ps.setString(1, "username");
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
}
