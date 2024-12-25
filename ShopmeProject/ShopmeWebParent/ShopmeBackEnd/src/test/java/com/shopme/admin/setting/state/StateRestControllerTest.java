package com.shopme.admin.setting.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired StateRepository stateRepo;
    @Autowired CountryRepository countryRepo;

    @Test
    @WithMockUser(username = "caotugc@gmail.com", password = "123456789", roles = "ADMIN")
    public void testListByContries() throws Exception {
        Integer countryId = 1;
        String url = "/states/list_by_country/" + countryId;
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        State[] states = objectMapper.readValue(jsonResponse, State[].class);
        assertThat(states).hasSize(2);
    }

    @Test
    @WithMockUser(username = "caotugc@gmail.com", password = "123456789", roles = "ADMIN")
    public void testCreateState() throws Exception {
        String url = "/states/save";
        Integer countryId = 1;
        Country country = countryRepo.findById(countryId).get();
        State state = new State("Nam Ninh", country);
        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(state))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer stateId = Integer.parseInt(response);
        Optional<State> findById = stateRepo.findById(stateId);
        assertThat(findById.isPresent());
    }

    @Test
    @WithMockUser(username = "caotugc@gmail.com", password = "123456789", roles = "ADMIN")
    public void testUpdateState() throws Exception {
        String url = "/states/save";
        Integer stateId = 2;
        String stateName = "Nghệ An";

        State state = stateRepo.findById(stateId).get();
        state.setName(stateName);

        mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(state))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(String.valueOf(stateId)));

        Optional<State> findById = stateRepo.findById(stateId);
        assertThat(findById.isPresent());

        State updatedState = findById.get();
        assertThat(updatedState.getName()).isEqualTo(stateName);
    }

    @Test
    @WithMockUser(username = "caotugc@gmail.com", password = "123456789", roles = "ADMIN")
    public void testDeleteState() throws Exception {
        Integer stateId = 6;
        String url = "/states/delete/" + stateId;
        mockMvc.perform(get(url)).andExpect(status().isOk());
        Optional<State> findById = stateRepo.findById(stateId);
        assertThat(findById.isEmpty());
    }

}
