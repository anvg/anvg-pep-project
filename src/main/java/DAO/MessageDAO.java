package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    public boolean createMessage(Message message){
        boolean messageInserted = false;
        
        try(Connection conn = ConnectionUtil.getConnection()){
            String query = "INSERT INTO Message(posted_by, " + 
            "message_text, time_posted) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            
            ps.executeUpdate();
            
            conn.close();
            return true;
        }catch(SQLException e){
            System.out.println("Create Message SQL Error: " + e);
        }
        return messageInserted;
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
                int messageId = rs.getInt("message_id");
                int postBy = rs.getInt("posted_by");
                String messageText = rs.getString("mesasge_text");
                Long timePosted = rs.getLong("time_posted_epoch");
                message = new Message(messageId, postBy, messageText, timePosted);

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
}
