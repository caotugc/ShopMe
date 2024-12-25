package com.shopme.admin.setting.country;

import com.shopme.common.entity.Country;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired EntityManager entityManager;

    @Test
    @WithMockUser(username = "caotugc@gmail.com", password = "123456789", roles = "ADMIN")
    public void testListCountry() throws Exception {
        String url = "/countries/list";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();

        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
        assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "caotugc@gmail.com", password = "123456789", roles = "ADMIN")
    public void testCreateCountry() throws Exception {
        String url = "/countries/save";
        String countryName = "Canada";
        String countryCode = "CA";
        Country country = new Country(countryName, countryCode);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(country)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String responseString = result.getResponse().getContentAsString();
        Integer countryId = Integer.parseInt(responseString);
        Country savedCountry = entityManager.find(Country.class, countryId);
        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "caotugc@gmail.com", password = "123456789", roles = "ADMIN")
    public void testUpdateCountry() throws Exception {
        String url = "/countries/save";
        Integer countryId = 4;
        String countryName = "Germany";
        String countryCode = "DE";
        Country country = new Country(countryId, countryName, countryCode);

        mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(countryId)));

        Country savedCountry = entityManager.find(Country.class, countryId);
        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "caotugc@gmail.com", password = "123456789", roles = "ADMIN")
    public void testDeleteCountry() throws Exception {
        Integer countryId = 4;
        String url = "/countries/delete/" + countryId;
        mockMvc.perform(get(url)).andExpect(status().isOk());

        Country country = entityManager.find(Country.class, countryId);
        assertThat(country).isNull();
    }
}
