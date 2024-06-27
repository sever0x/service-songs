package com.sever0x.block2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sever0x.block2.model.dto.request.ArtistRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;
import com.sever0x.block2.service.ArtistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtistController.class)
class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArtistService artistService;

    @Test
    void shouldCreateArtist() throws Exception {
        ArtistRequest request = new ArtistRequest("John Doe", "USA");
        ArtistResponse response = new ArtistResponse(1L, "John Doe", "USA");

        when(artistService.createArtist(any(ArtistRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.country").value("USA"));

        verify(artistService).createArtist(request);
    }

    @Test
    void shouldFailToCreateArtistWithBlankName() throws Exception {
        ArtistRequest request = new ArtistRequest("", "USA");

        mockMvc.perform(post("/api/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToCreateArtistWithBlankCountry() throws Exception {
        ArtistRequest request = new ArtistRequest("John Doe", "");

        mockMvc.perform(post("/api/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetArtists() throws Exception {
        ArtistResponse response1 = new ArtistResponse(1L, "John Doe", "USA");
        ArtistResponse response2 = new ArtistResponse(2L, "Jane Smith", "Canada");

        when(artistService.getArtists()).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/artist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].country").value("USA"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].country").value("Canada"));

        verify(artistService).getArtists();
    }

    @Test
    void shouldUpdateArtist() throws Exception {
        ArtistRequest request = new ArtistRequest("John Doe", "Canada");

        mockMvc.perform(put("/api/artist/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(artistService).updateArtistById(1L, request);
    }

    @Test
    void shouldFailToUpdateArtistWithBlankName() throws Exception {
        ArtistRequest request = new ArtistRequest("", "Canada");

        mockMvc.perform(put("/api/artist/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToUpdateArtistWithBlankCountry() throws Exception {
        ArtistRequest request = new ArtistRequest("John Doe", "");

        mockMvc.perform(put("/api/artist/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToUpdateNonExistingArtist() throws Exception {
        ArtistRequest request = new ArtistRequest("John Doe", "Canada");
        long nonExistingArtistId = 1L;

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(artistService)
                .updateArtistById(nonExistingArtistId, request);

        mockMvc.perform(put("/api/artist/{id}", nonExistingArtistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteArtist() throws Exception {
        when(artistService.deleteArtistById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/artist/1"))
                .andExpect(status().isOk());

        verify(artistService).deleteArtistById(1L);
    }

    @Test
    void shouldFailToDeleteNonExistingArtist() throws Exception {
        when(artistService.deleteArtistById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/api/artist/1"))
                .andExpect(status().isNotFound());
    }
}