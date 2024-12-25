package com.shopme.admin.setting.country;

import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {
    @Autowired private CountryRepository repo;

    @Test
    public void testCreateCountry() {
        Country country = repo.save(new Country("Vietnam", "VN"));
        assertThat(country).isNotNull();
        assertThat(country.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCountries() {
        List<Country> listCountries = repo.findAllByOrderByNameAsc();
        listCountries.forEach(System.out::println);
        assertThat(listCountries).isNotEmpty();
    }

    @Test
    public void testUpdateCountry() {
        Integer id = 1;
        String name = "RPC";
        Country country = repo.findById(id).get();
        country.setName(name);
        assertThat(country.getName()).isEqualTo(name);
    }

    @Test
    public void testGetCountry() {
        Country country = repo.findById(1).get();
        assertThat(country).isNotNull();
    }

    @Test
    public void testDeleteCountry() {
        Integer id = 2;
        repo.deleteById(id);
        assertThat(repo.findById(id)).isNull();
    }
}
