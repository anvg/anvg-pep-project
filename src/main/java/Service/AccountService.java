package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.*;

public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account getAccount(int accountID){
        Account account = null;
        //account = accountDAO.getAccountById(accountID);
        return account;
    }

    public boolean insertAccount(Account account){
        boolean accountAdded = false;
        //Account account = accountDAO.insertAccount(account);
        return accountAdded;
    }
    
}
