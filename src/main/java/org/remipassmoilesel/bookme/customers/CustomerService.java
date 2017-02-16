package org.remipassmoilesel.bookme.customers;

import com.j256.ormlite.stmt.QueryBuilder;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@Service
public class CustomerService extends AbstractDaoService<Customer> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public CustomerService(CustomConfiguration configuration) {
        super(Customer.class, configuration);
    }

    /**
     * Create a customer
     *
     * @param firstname
     * @param lastname
     * @param phonenumber
     * @return
     * @throws IOException
     */
    public Customer create(String firstname, String lastname, String phonenumber) throws IOException {
        return create(new Customer(firstname, lastname, phonenumber));
    }

    public List<Customer> getLasts(long limit, long offset) throws IOException {
        try {
            QueryBuilder<Customer, String> builder = dao.queryBuilder();
            builder.orderBy(Customer.CREATIONDATE_FIELD_NAME, true);
            builder.limit(limit);
            if (offset > 0) {
                builder.offset(offset);
            }

            return builder.query();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }


}
