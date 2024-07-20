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

    public List<Account> getAllAcounts(){
        List<Account> listAccount = new ArrayList<>();
        listAccount = accountDAO.getAllAccounts();

        return listAccount;
    }

    public Account getAccountByUsernameAndPassword(String targetUsername,
    String targetPassword){
        Account account = null;
        account = accountDAO.getAccountByUsernameAndPassword(targetUsername, 
        targetPassword);

        return account;
    }
    
}
