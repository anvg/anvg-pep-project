package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    public boolean createMessage(Message message){
        boolean messageInserted = true;
        
        final boolean IS_MESSAGE_BELOW_MAX_LIMIT = 
        message.getMessage_text().length() < 255;
        final boolean IS_EMPTY_MESSAGE = 
        message.getMessage_text().length() != 0;
        final boolean IS_REAL_USER = retrieveAccountId(message.getPosted_by());
        
        if(IS_MESSAGE_BELOW_MAX_LIMIT && IS_EMPTY_MESSAGE && IS_REAL_USER){

            try(Connection conn = ConnectionUtil.getConnection()){

                String query = "INSERT INTO Message(message_id, " +
                "posted_by, message_text, time_posted_epoch) " + 
                "VALUES(?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);

                int messageId = generateMessageId(message);

                //used to simulate message_id auto_increment
                if(messageId != -1){
                    message.setMessage_id(messageId+1);
                }
                
                ps.setInt(1, messageId);
                ps.setInt(2, message.getPosted_by());
                ps.setString(3, message.getMessage_text());
                ps.setLong(4, message.getTime_posted_epoch());
                
                ps.executeUpdate();
                
                conn.close();

            }catch(SQLException e){
                System.out.println("Create Message SQL Error: " + e);
            }
            
        }else{
            messageInserted = false;
        }
 
        return messageInserted;
    }

    public boolean retrieveAccountId(int postedBy){
        boolean accountExists = false;
        
        try(Connection conn = ConnectionUtil.getConnection()){

            String query = "SELECT posted_by FROM Message INNER JOIN " + 
            "Account ON Message.posted_by = Account.account_id";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int targetPostedBy = rs.getInt("posted_by");
                if(postedBy == targetPostedBy){
                    accountExists = true;
                }
                
            }
            conn.close();
            
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        
        return accountExists;
    }

    public int generateMessageId(Message message){
        int nextId = -1;
        
        try(Connection conn = ConnectionUtil.getConnection()){

            String query = "SELECT MAX(message_id) FROM Message";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                nextId = rs.getInt("MAX(message_id)");
            }
            
            conn.close();

        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }

        return nextId;
    }

    public List<Message> retrieveAllMessage(){
        List<Message> message = new ArrayList<>();
        
        try(Connection conn = ConnectionUtil.getConnection()){

            String query = "SELECT * FROM Message";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Message record = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                message.add(record);

            }
            conn.close();

        }catch(SQLException e){
            System.out.println("Retrieve All Message SQL Error: " + e);
        }

        return message;
    }

    public Message retrieveMessageByMessageId(int messageId){
        Message target = null;
        
        try(Connection conn = ConnectionUtil.getConnection()){

            String query = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Message messageCursor = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));

                target = messageCursor;

                return target;
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Retrieve Message SQL Error: " + e);
        }
        return target;
    }

    public boolean deleteMessageByMessageId(int messageId){
        boolean messageDeleted = false;
        
        try(Connection conn = ConnectionUtil.getConnection()){

            String query = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            
            ps.setInt(1, messageId);   
            ps.executeUpdate();
            
            conn.close();
            messageDeleted = true;

        }catch(SQLException e){
            System.out.println("Delete Message SQL Error: " + e);
        }

        return messageDeleted;
    }

    public Message updateMessageText(Message message){
        Message target = null;

        final boolean MESSAGE_HAS_CONTENT = 
        message.getMessage_text().length() != 0;
        final boolean MESSAGE_UNDER_CHARACTER_LIMIT = 
        message.getMessage_text().length() <= 255;
        final boolean MESSAGE_EXISTS = 
        retrievableMessageId(message.getMessage_id());

        if(MESSAGE_HAS_CONTENT && MESSAGE_UNDER_CHARACTER_LIMIT && 
        MESSAGE_EXISTS){

            try(Connection conn = ConnectionUtil.getConnection()){

                String query = "UPDATE Message SET posted_by, " + 
                "message_text = ?, time_posted_epoch = ?" +
                "ON message_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                
                ps.setInt(1, message.getPosted_by());
                ps.setString(2, message.getMessage_text());
                ps.setLong(3, message.getTime_posted_epoch());
                ps.setInt(4, message.getMessage_id());
    
                ps.executeUpdate();
                target = message;
                
                conn.close();
    
            }catch(SQLException e){
                System.out.println("Create Message SQL Error: " + e);
            }
        }

        return target;
    }

    public boolean retrievableMessageId(int id){
        boolean messageExist = false;
        
        try(Connection conn = ConnectionUtil.getConnection()){

            String query = "SELECT message_id FROM Message " + 
            "WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int targetPostedBy = rs.getInt("posted_by");
                if(id == targetPostedBy){
                    messageExist = true;
                }
                
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Select Message by ID SQL Error: " + e);
        }
        return messageExist;
    }
    
    public List<Message> retrieveAllMessageForUser(int accountId){
        List<Message> message = new ArrayList<>();
        
        
        try(Connection conn = ConnectionUtil.getConnection()){
            
            String query = "SELECT Message.* FROM Message " + 
            "INNER JOIN Account ON Message.posted_by = " +
            "Account.account_id WHERE account_id = ?";

            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message currentMessage = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"), 
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));

                message.add(currentMessage);
            }
            
            conn.close();
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        
        return message;
    }
}
