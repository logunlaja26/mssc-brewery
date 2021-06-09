package com.lyomann.msscbrewery.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyomann.msscbrewery.services.BeerService;
import com.lyomann.msscbrewery.web.controller.BeerController;
import com.lyomann.msscbrewery.web.model.BeerDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BeerController.class)
public class BeerControllerTest {

    @Autowired
    private BeerController beerController;

    @MockBean
    private BeerService beerService;

//    @Autowired
//    BeerService beerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    BeerDto validBeer;

    @Before
    public void setUp(){
        validBeer = BeerDto.builder().id(UUID.randomUUID())
                .beerName("Beer1")
                .beerStyle("PALE_ALE")
                .upc(123456789012L)
                .build();
    }

    @Test
    public void getBeer() throws Exception {
        when(beerService.getBeerById(any(UUID.class))).thenReturn(validBeer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/" + validBeer.getId().toString()))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(validBeer.getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.beerName", is("Beer1")))
                .andExpect(status().isOk());
    }

    @Test
    public void handlePostTest() throws Exception {

        //https://github.com/logunlaja26/devtalkers/blob/master/src/test/java/com/devtalkers/restapitest/UserControllerTest.java
        //(Post testing example)
        // Mock beer we have to use
        //given
        BeerDto beerDto = validBeer;
        beerDto.setId(null);
        BeerDto savedDto = BeerDto.builder().id(UUID.randomUUID()).beerName("New Beer").build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerService.saveNewBeer(any())).willReturn(savedDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/beer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
