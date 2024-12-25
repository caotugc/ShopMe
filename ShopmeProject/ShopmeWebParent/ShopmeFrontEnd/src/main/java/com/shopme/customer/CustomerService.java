package com.shopme.customer;

import com.shopme.common.entity.Country;
import com.shopme.setting.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CountryRepository countryRepo;
    @Autowired
    private CustomerRepository customerRepo;

    public List<Country> listAllCountry() {
        return countryRepo.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email) {
        return customerRepo.findByEmail(email) != null;
    }
}
