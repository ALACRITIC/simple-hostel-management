package org.remipassmoilesel.bookme;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.sharedresources.SharedResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Created by remipassmoilesel on 13/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SharedResourceServiceTest{

    private final Logger logger = LoggerFactory.getLogger(SharedResourceServiceTest.class);

    @Autowired
    private SharedResourceService sharedResourceService;

    @Test
    public void test() {



    }

}
