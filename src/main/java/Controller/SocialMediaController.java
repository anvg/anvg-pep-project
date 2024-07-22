package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.*;
import Service.*;

import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::retrieveAllMessagesHandler);
        app.get("/messages/{message_id}", this::retrieveMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::retrieveAllMessageByUserHandler);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    
    private void registerHandler(Context context){
        AccountService accountService = new AccountService();
        Account account = context.bodyAsClass(Account.class);

        boolean registerationSaved = accountService.registerUser(account);
        
        if(registerationSaved){
            context.status(200);
            context.json(account);
        }else{
            context.status(400);
        }
        
    }

    private void loginHandler(Context context){
        AccountService accountService = new AccountService();
        Account httpRequest = context.bodyAsClass(Account.class);
        final String USERNAME = httpRequest.getUsername();
        final String PASSWORD = httpRequest.getPassword();

        Account databaseRequest = accountService.getAccountByUsernameAndPassword(USERNAME,
         PASSWORD);

        if(databaseRequest != null){
            context.status(200);
            context.json(databaseRequest);
        }else{
            context.status(401);
        }
    }

    private void createMessageHandler(Context context){
        MessageService messageService = new MessageService();
        Message message = context.bodyAsClass(Message.class);

        boolean messageCreated = messageService.createMessage(message);

        if(messageCreated){
            context.status(200);
            context.json(message);
        }else{
            context.status(400);
        }
    }

    private void retrieveAllMessagesHandler(Context context){
        MessageService messageService = new MessageService();
        
        List<Message> messageList = messageService.getAllMessage();

        context.json(messageList);
        context.status(200);
    }

    private void retrieveMessageByIdHandler(Context context){
        MessageService messageService = new MessageService();
        int id = Integer.parseInt(context.pathParam("message_id"));

        Message target = messageService.getMessageById(id);

        context.status(200);
        if(target != null){
            context.json(target);
        }else{
            context.json("");
        }
    }

    private void deleteMessageByIdHandler(Context context){
        MessageService messageService = new MessageService();
        int id = Integer.parseInt(context.pathParam("message_id"));

        Message target = messageService.deleteMessageById(id);

        if(target != null){
            context.json(target);
        }else{
            context.json("");
        }

    }

    private void updateMessageByIdHandler(Context context){
        MessageService messageService = new MessageService();
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = context.bodyAsClass(Message.class);
        //int id = message.getMessage_id();
        //String bodyText = message.getMessage_text();
        //Message message = messageService.getMessageById(id);

        Message result = messageService.updateMessageById(message, id);

       if(result != null){
            context.status(200);
            context.json(result);
        }else{
            context.status(400);
        }
    }

    private void retrieveAllMessageByUserHandler(Context context){
        MessageService messageService = new MessageService();
        int id = Integer.parseInt(context.pathParam("account_id"));

        List<Message> listMessage = messageService.retrieveAllMessageByUser(id);
        
        context.json(listMessage);
        context.status(200);

    }


}