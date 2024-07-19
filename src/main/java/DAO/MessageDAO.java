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

                if(messageId != -1){
                    message.setMessage_id(messageId+1);
                }
                
                ps.setInt(1, messageId);
                ps.setInt(2, message.getPosted_by());
                ps.setString(3, message.getMessage_text());
                ps.setLong(4, message.getTime_posted_epoch());
                
                ps.executeUpdate();
                
                conn.close();

                return true;
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

            return nextId;
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
                int messageId = rs.getInt("message_id");
                int postBy = rs.getInt("posted_by");
                String messageText = rs.getString("mesasge_text");
                Long timePosted = rs.getLong("time_posted_epoch");
                message.add(new Message(messageId, postBy, messageText, timePosted));
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        return message;
    }

    public Message retrieveMessageByMessageId(int messageId){
        Message message = null;
        
        try(Connection conn = ConnectionUtil.getConnection()){
            String query = "SELECT * FROM Message WHERE message_id = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int localMessageId = rs.getInt("message_id");
                int postBy = rs.getInt("posted_by");
                String messageText = rs.getString("mesasge_text");
                Long timePosted = rs.getLong("time_posted_epoch");
                message = new Message(localMessageId, postBy, messageText, timePosted);

                return message;
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        return message;
    }

    public boolean deleteMessageByMessageId(int messageId){
        boolean messageDeleted = false;
        
        try(Connection conn = ConnectionUtil.getConnection()){
            String query = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            
            ps.setInt(1, messageId);   
            ps.executeUpdate();
            
            conn.close();
            return true;
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        return messageDeleted;
    }

    public boolean updateMessageText(Message message){
        boolean messageUpdated = false;
        try(Connection conn = ConnectionUtil.getConnection()){
            String query = "UPDATE Message SET posted_by = ?, message_text = ?, " +
            "time_posted_epoch = ? ON message_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            
            ps.setInt(1, message.getPosted_by());   
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.setInt(4, message.getMessage_id());

            ps.executeUpdate();
            
            conn.close();
            return true;
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        return messageUpdated;
    }
    
    public List<Message> retrieveAllMessageForUser(int accountId){
        List<Message> message = new ArrayList<>();
        
        try(Connection conn = ConnectionUtil.getConnection()){
            String query = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int messageId = rs.getInt("message_id");
                int postBy = rs.getInt("posted_by");
                String messageText = rs.getString("mesasge_text");
                Long timePosted = rs.getLong("time_posted_epoch");
                message.add(new Message(messageId, postBy, messageText, timePosted));
            }
            conn.close();
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        return message;
    }
}
