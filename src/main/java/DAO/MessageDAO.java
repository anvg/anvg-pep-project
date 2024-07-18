package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.*;

public class MessageDAO {
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
}
