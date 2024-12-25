package com.shopme.admin.setting.state;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {
    @Autowired private StateRepository repo;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateStateInVietnam() {
        Integer countryId = 2;
        Country country = entityManager.find(Country.class, countryId);
        State state1 = repo.save(new State("HCM", country));
        assertThat(state1.getName()).isEqualTo("HCM");
        assertThat(state1.getCountry()).isEqualTo(country);
    }

    @Test
    public void testCreateStateInChina() {
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);
        State state2 = repo.save(new State("ShangHai", country));
        assertThat(state2.getName()).isEqualTo("ShangHai");
        assertThat(state2.getCountry()).isEqualTo(country);
    }

    @Test
    public void testListStatesByCountry() {
        Integer countryId = 2;
        Country country = entityManager.find(Country.class, countryId);
        List<State> states = repo.findByCountryOrderByNameAsc(country);
        assertThat(states.size()).isEqualTo(3);
        states.forEach(System.out::println);
    }

    @Test
    public void testUpdateStateInVietnam() {
        Integer stateId = 1;
        String name = "Hà Nội";
        State state = repo.findById(stateId).get();
        state.setName(name);
        State state_save = repo.save(state);
        assertThat(state_save.getName()).isEqualTo(name);
    }

    @Test
    public void testGetState() {
        Integer stateId = 1;
        Optional<State> state = repo.findById(stateId);
        assertThat(state.isPresent()).isTrue();
    }

    @Test
    public void testDeleteStateInVietnam() {
        Integer stateId = 1;
        repo.deleteById(stateId);
        Optional<State> state = repo.findById(stateId);
        assertThat(state.isPresent()).isFalse();
    }
}
