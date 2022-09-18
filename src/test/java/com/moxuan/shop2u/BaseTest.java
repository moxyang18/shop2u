package com.moxuan.shop2u;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-redis.xml","classpath:spring/spring-dao.xml",
		"classpath:spring/spring-service.xml" })
public class BaseTest {

}