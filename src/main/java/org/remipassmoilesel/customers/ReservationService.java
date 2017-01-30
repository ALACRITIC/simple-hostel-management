package org.remipassmoilesel.customers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by remipassmoilesel on 30/01/17.
 */

@Service
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService() {
    }

    public void getAll() {

        System.out.println("Get all !");

        /*
        // instantiate the dao
        Dao<Account, String> accountDao =
                DaoManager.createDao(connectionSource, Account.class);

        // if you need to create the 'accounts' table make this call
        TableUtils.createTableIfNotExists(connectionSource, Account.class);

        // create an instance of Account
        Account account = new Account();
        account.setName("Jim Coakley");

        // persist the account object to the database
        accountDao.create(account);

        account.setPassword("azerty");
        accountDao.update(account);

        Account account3 = new Account("heyhey", "hoho");
        accountDao.create(account3);
        accountDao.delete(account);


        // dao are iterators
        for (Account a : accountDao) {
            if (account.getName().equals("Bob Smith")) {
                // you can't return, break, or throw from here
//                return a;
            }
        }


        // retrieve the account from the database by its id field (name)
        List<Account> account2 = accountDao.queryForAll();
        System.out.println("Accounts: " + account2);

        // close the connection source
        connectionSource.close();

        */

    }

}
