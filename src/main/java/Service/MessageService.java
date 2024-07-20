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

    public boolean createMessage(Message message){
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessage(){
        List<Message> messageList = messageDAO.retrieveAllMessage();
        return messageList;
    }

    public Message getMessageById(int messageId){
        return messageDAO.retrieveMessageByMessageId(messageId);
    }

    public Message deleteMessageById(int id){
        return messageDAO.deleteMessageByMessageId(id);
    }

    public Message updateMessageById(Message message){
        return messageDAO.updateMessageText(message);
    }

    public List<Message> retrieveAllMessageByUser(int id){
        List<Message> messageList = messageDAO.retrieveAllMessageForUser(id);
        return messageList;
    }

}
