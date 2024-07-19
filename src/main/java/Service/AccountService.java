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

    public boolean registerUser(Account account){
        return accountDAO.registerUser(account);
    }

    public Account getAccount(int accountID){
        Account account = null;
        //account = accountDAO.getAccountById(accountID);
        return account;
    }

    public List<Account> getAllAcounts(){
        List<Account> listAccount = new ArrayList<>();
        listAccount = accountDAO.getAllAccounts();
        return listAccount;
    }

    public Account getAccount(Account account){
        Account target = null;
        target = accountDAO.getAccount(account);

        return target;
    }

    public Account getAccountByUsername(String targetUsername){
        Account account = null;
        account = accountDAO.getAccountByUsername(targetUsername);
        return account;
    }

    public Account getAccountByUsernameAndPassword(String targetUsername,
    String targetPassword){
        Account account = null;
        account = accountDAO.getAccountByUsernameAndPassword(targetUsername, 
        targetPassword);

        return account;
    }

    public boolean insertAccount(Account account){
        boolean accountAdded = false;
        //Account account = accountDAO.insertAccount(account);
        return accountAdded;
    }
    
}
