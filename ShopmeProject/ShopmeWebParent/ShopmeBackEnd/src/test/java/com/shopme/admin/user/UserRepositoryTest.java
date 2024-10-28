package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserwith1Role() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userCaoTu = new User("caotugc@gmail.com", "123456", "Cao Tu", "Hoang");
		userCaoTu.addRole(roleAdmin);
		User savedUser = repo.save(userCaoTu);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void test2CreateUserwith1Role() {
		Role roleEditor = new Role(3);
		User user1 = new User("thukhoa@gmail.com", "123456", "Danh Ngoc", "Hoang");
		user1.addRole(roleEditor);
		User savedUser = repo.save(user1);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserwith2Role() {
		User user2 = new User("james.hoang@gmail.com", "123456", "James", "Hoang");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		user2.addRole(roleEditor);
		user2.addRole(roleAssistant);
		User savedUser2 = repo.save(user2);
		assertThat(savedUser2.getId()).isGreaterThan(0);	
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User foundUser = repo.findById(1).get();
		System.out.println(foundUser);
		assertThat(foundUser).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetail() {
		User foundUser = repo.findById(1).get();
		foundUser.setEnable(true);
		foundUser.setEmail("caotuit@gmail.com");
		repo.save(foundUser);
	}
	
	@Test
	public void testUpdateRole() {
		User foundUser = repo.findById(2).get();
		Role roleAssistant = new Role(5);
		Role roleSales = new Role(2);
		foundUser.getRoles().remove(roleSales);
		foundUser.addRole(roleAssistant);
		repo.save(foundUser);
	}
	
	@Test
	public void deleteUserId() {
		Integer userID = 2;
		repo.deleteById(userID);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "thukhoa@gmail.com";
		User user1 = repo.getUserByEmail(email);
		System.out.println(user1);
		assertThat(user1).isNotNull();
	}
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countId = repo.countById(id);
		assertThat(countId).isNotNull().isGreaterThan(0);
	}
	@Test
	public void testUnabledUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}
	@Test
	public void testEnabledUser() {
		Integer id = 7;
		repo.updateEnabledStatus(id, true);
	}
	@Test
	public void testList1stPage() {
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize) ;
		Page<User> page = repo.findAll(pageable);
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchFirstName() {
		String keyword = "bruce";
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize) ;
		Page<User> page = repo.findAll(keyword, pageable);
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isGreaterThan(0);
	}
}
