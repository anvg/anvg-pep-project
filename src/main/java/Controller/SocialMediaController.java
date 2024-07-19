package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import DAO.AccountDAO;

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
        AccountDAO accountDAO = new AccountDAO();
        Account account = context.bodyAsClass(Account.class);

        boolean isRegister = accountDAO.registerUser(account);
        
        if(isRegister){
            context.status(200);
            context.json(account);
        }else{
            context.status(400);
        }
        
    }

    private void loginHandler(Context context){
        AccountDAO accountDAO = new AccountDAO();
        Account httpRequest = context.bodyAsClass(Account.class);
        String USERNAME = httpRequest.getUsername();
        String PASSWORD = httpRequest.getPassword();

        Account databaseRequest = accountDAO.getAccountByUsernameAndPassword(USERNAME,
         PASSWORD);

        if(httpRequest.equals(databaseRequest)){
            context.status(200);
            context.json(databaseRequest);
        }else{
            context.status(401);
        }
    }


}