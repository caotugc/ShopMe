package com.shopme.customer;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
    @Autowired private CustomerRepository repo;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer1() {
        Integer customerId = 2;
        Country country = entityManager.find(Country.class, customerId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("James");
        customer.setLastName("Hoang");
        customer.setPassword("123456789");
        customer.setEmail("caotugc@gmail.com");
        customer.setPhoneNumber("+84961050948");
        customer.setAddressLine1("22 CauGiay");
        customer.setCity("Ha Noi");
        customer.setState("Mien Bac");
        customer.setPostalCode("10000");
        customer.setCreateTime(new Date());

        Customer savedCustomer = repo.save(customer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCountry()).isEqualTo(country);
    }

    @Test
    public void testCreateCustomer2() {
        Integer customerId = 5;
        Country country = entityManager.find(Country.class, customerId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Tiothe");
        customer.setLastName("Mohamed");
        customer.setPassword("123456789");
        customer.setEmail("tiothe@gmail.com");
        customer.setPhoneNumber("+33961050948");
        customer.setAddressLine1("22 Avenue des Buttes des Coemes");
        customer.setCity("Rennes");
        customer.setState("Bretagne");
        customer.setPostalCode("31000");
        customer.setCreateTime(new Date());

        Customer savedCustomer = repo.save(customer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCountry()).isEqualTo(country);
    }

    @Test
    public void testUpdateCustomers() {
        Integer customerId = 2;
        String firstName = "Frank";

        Customer customer = repo.findById(customerId).get();
        customer.setFirstName(firstName);
        customer.setEnabled(true);

        Customer updatedCustomer = repo.save(customer);
        assertThat(updatedCustomer.getFirstName()).isEqualTo(firstName);
    }

    @Test
    public void testListCustomers() {
        Iterable<Customer> customers = repo.findAll();
        customers.forEach(customer -> System.out.println(customer));
        assertThat(customers).hasSize(2);
    }

    @Test
    public void testGetCustomerById() {
        Integer customerId = 2;
        Optional<Customer> customer = repo.findById(customerId);
        assertThat(customer).isPresent();

        Customer customFound = customer.get();
        System.out.println(customFound);
    }

    @Test
    public void testDeleteCustomerById() {
        Integer customerId = 2;
        repo.deleteById(customerId);
        Optional<Customer> customer = repo.findById(customerId);
        assertThat(customer).isNotPresent();
    }

    @Test
    public void testFindByEmail() {
        String email = "tiothe@gmail.com";
        Customer customer = repo.findByEmail(email);
        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode() {
        String verificationCode = "001";
        Customer customer = repo.findByVerificationCode(verificationCode);
        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testEnabledCustomer() {
        Integer customerId = 1;
        repo.enable(customerId);

        Customer customer = repo.findById(customerId).get();
        assertThat(customer).isNotNull();
    }

}

