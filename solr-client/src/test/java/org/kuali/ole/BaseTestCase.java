package org.kuali.ole;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.repo.BibRecordRepository;
import org.kuali.ole.repo.jpa.ItemRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OleSolrClientApplication.class)
@WebAppConfiguration
@Transactional
@Rollback(false)
public class BaseTestCase {

	@Autowired
	protected BibRecordRepository bibRecordRepository;

	@Autowired
	protected ItemRecordRepository itemRecordRepository;

	@Test
	public void contextLoads() {
	}

}
