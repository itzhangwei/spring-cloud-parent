package com.learn.server.service.impl;

import com.learn.common.db.dao.learn.UserDao;
import com.learn.common.db.dao.storage.ProductDao;
import com.learn.common.entity.db.learn.User;
import com.learn.common.entity.db.storage.Product;
import com.learn.common.util.StringUtil;
import com.learn.server.client.ProductClient;
import com.learn.server.request.AddUserRequest;
import com.learn.server.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title UserServiceImpl
 * @package com.learn.server.service.impl
 * @description 用户
 * @date 2020/4/28 6:48 下午
 */
@Service
@Slf4j(topic = "用户业务类【UserServiceImpl】")
public class UserServiceImpl implements UserService {
	
	private final UserDao userDao;
	private final ProductDao productDao;
	private ProductClient productClient;
	
	UserServiceImpl(UserDao userDao, ProductDao productDao, ProductClient productClient) {
		this.userDao=userDao;
		this.productDao=productDao;
		this.productClient=productClient;
	}
	
	/**
	 * GlobalTransactional可以在分布式feign调用中起到很好的分布式事务管理作用。
	 * 在异步线程间没有办法控制事务，还需要进一步了解下
	 *
	 * @param addUserRequest 请求参数
	 */
	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
//	@Transactional(rollbackFor = Exception.class)
	public void addUser(AddUserRequest addUserRequest) {
		User user = new User();
		BeanUtils.copyProperties(addUserRequest, user);
		final String userId = StringUtil.getUUID32();
		user.setId(userId);
		final String createBy = StringUtil.getUUID32();
		user.setCreateBy(createBy);
		final Date createTime = new Date();
		user.setCreateTime(createTime);
		user.setLastUpdateBy(createBy);
		user.setLastUpdateTime(createTime);
		
		//保存用户
		this.userDao.insert(user);
		
		//新用户注册 送iPhone plus
		this.doSave(userId, createBy, createTime);
//		try {
////			Thread.sleep(5000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			final CompletableFuture<Integer> saveAsync = this.saveProduct(userId, createBy, createTime);
//			final Integer integer = saveAsync.get(5, TimeUnit.SECONDS);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		log.info("========================方法结束================");
		int i =1/0;
	}
	
	CompletableFuture<Integer> saveProduct(String userId, String createBy, Date createTime) throws InterruptedException, ExecutionException, TimeoutException {
		return CompletableFuture.supplyAsync(() -> {
			return doSave(userId, createBy, createTime);
		});
	}
	
	/**
	 * 事务传播特性设置为 REQUIRES_NEW 开启新的事务    重要！！！！一定要使用REQUIRES_NEW
	 */
//	@GlobalTransactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
	Integer doSave(String userId, String createBy, Date createTime) {
		final Product product = Product.builder().build();
//		final Product product = new Product();
		product.setId(StringUtil.getUUID32());
		product.setProductName("iPhone plus");
		product.setColor("屎黄色");
		product.setUserId(userId);
		product.setLastUpdateBy(createBy);
		product.setCreateTime(createTime);
		product.setCreateBy(createBy);
		product.setLastUpdateTime(createTime);
//		this.productDao.insert(product);
//		log.info("异步线程保存完毕！！！！！！");
		productClient.addUser(product);
		return 1;
	}
}
