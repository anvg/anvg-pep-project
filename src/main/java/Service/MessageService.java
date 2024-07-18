package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.*;


public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessage(){
        List<Message> messageList = messageDAO.retrieveAllMessage();
        return messageList;
    }

    public Message insertMessage(Message message){
        Message target = null;
        //Message target = messageDAO.insertMessage(message);
        return message;
    }
}
