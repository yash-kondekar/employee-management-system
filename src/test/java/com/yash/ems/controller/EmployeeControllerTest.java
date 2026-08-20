package com.yash.ems.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.ems.dto.request.CreateEmployeeRequest;
import com.yash.ems.dto.request.UpdateEmployeeRequest;
import com.yash.ems.dto.response.EmployeeResponse;
import com.yash.ems.dto.response.PageResponse;
import com.yash.ems.exception.EmployeeNotFoundException;
import com.yash.ems.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void shouldCreateEmployeeSuccessfully() throws Exception {

        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();

        request.setFirstName("Yash");
        request.setLastName("Kondekar");
        request.setEmail("yash@gmail.com");
        request.setPhoneNumber("9876543210");
        request.setDesignation("Spring Boot Developer");
        request.setSalary(new BigDecimal("50000"));

        EmployeeResponse response = new EmployeeResponse();

        response.setId(1L);
        response.setFirstName("Yash");
        response.setLastName("Kondekar");
        response.setEmail("yash@gmail.com");
        response.setPhoneNumber("9876543210");
        response.setDesignation("Spring Boot Developer");
        response.setSalary(new BigDecimal("50000"));

        when(employeeService.createEmployee(any(CreateEmployeeRequest.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(
                        post("/api/v1/employees")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Yash"))
                .andExpect(jsonPath("$.lastName").value("Kondekar"))
                .andExpect(jsonPath("$.email").value("yash@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("9876543210"))
                .andExpect(jsonPath("$.designation").value("Spring Boot Developer"))
                .andExpect(jsonPath("$.salary").value(50000));

        // Verify
        verify(employeeService)
                .createEmployee(any(CreateEmployeeRequest.class));
    }


    @Test
    void shouldGetEmployeeByIdSuccessfully() throws Exception {

        // Arrange
        Long employeeId = 1L;

        EmployeeResponse response = new EmployeeResponse();

        response.setId(1L);
        response.setFirstName("Yash");
        response.setLastName("Kondekar");
        response.setEmail("yash@gmail.com");
        response.setPhoneNumber("9876543210");
        response.setDesignation("Spring Boot Developer");
        response.setSalary(new BigDecimal("50000"));

        when(employeeService.getEmployeeById(employeeId))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees/{id}", employeeId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Yash"))
                .andExpect(jsonPath("$.lastName").value("Kondekar"))
                .andExpect(jsonPath("$.email").value("yash@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("9876543210"))
                .andExpect(jsonPath("$.designation").value("Spring Boot Developer"))
                .andExpect(jsonPath("$.salary").value(50000));

        // Verify
        verify(employeeService)
                .getEmployeeById(employeeId);
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() throws Exception {

        // Arrange
        Long employeeId = 999L;

        when(employeeService.getEmployeeById(employeeId))
                .thenThrow(new EmployeeNotFoundException(employeeId));

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees/{id}", employeeId)
                )
                .andExpect(status().isNotFound());

        // Verify
        verify(employeeService)
                .getEmployeeById(employeeId);
    }

    @Test
    void shouldGetAllEmployeesSuccessfully() throws Exception {

        // Arrange
        EmployeeResponse response = new EmployeeResponse();

        response.setId(1L);
        response.setFirstName("Yash");
        response.setLastName("Kondekar");
        response.setEmail("yash@gmail.com");
        response.setPhoneNumber("9876543210");
        response.setDesignation("Spring Boot Developer");
        response.setSalary(new BigDecimal("50000"));

        PageResponse<EmployeeResponse> pageResponse =
                new PageResponse<>(
                        List.of(response),
                        0,
                        10,
                        1,
                        1,
                        true,
                        true
                );

        when(employeeService.getAllEmployees(
                anyInt(),
                anyInt(),
                anyString(),
                anyString()
        )).thenReturn(pageResponse);

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sortBy", "id")
                                .param("sortDirection", "asc")
                )
                .andExpect(status().isOk())

                // Employee data
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("Yash"))
                .andExpect(jsonPath("$.content[0].lastName").value("Kondekar"))
                .andExpect(jsonPath("$.content[0].email").value("yash@gmail.com"))
                .andExpect(jsonPath("$.content[0].phoneNumber").value("9876543210"))
                .andExpect(jsonPath("$.content[0].designation")
                        .value("Spring Boot Developer"))
                .andExpect(jsonPath("$.content[0].salary").value(50000))

                // Pagination data
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true));

        // Verify
        verify(employeeService)
                .getAllEmployees(
                        anyInt(),
                        anyInt(),
                        anyString(),
                        anyString()
                );
    }

    @Test
    void shouldUpdateEmployeeSuccessfully() throws Exception {

        // Arrange
        Long employeeId = 1L;

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();

        request.setFirstName("Yash");
        request.setLastName("Kondekar");
        request.setEmail("yash@gmail.com");
        request.setPhoneNumber("9876543210");
        request.setDesignation("Senior Java Developer");
        request.setSalary(new BigDecimal("80000"));

        // Act + Assert
        mockMvc.perform(
                        put("/api/v1/employees/{id}", employeeId)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        // Verify
        verify(employeeService)
                .updateEmployee(
                        eq(employeeId),
                        any(UpdateEmployeeRequest.class)
                );
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingEmployee()
            throws Exception {

        // Arrange
        Long employeeId = 999L;

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();

        request.setFirstName("Yash");
        request.setLastName("Kondekar");
        request.setEmail("yash@gmail.com");
        request.setPhoneNumber("9876543210");
        request.setDesignation("Senior Java Developer");
        request.setSalary(new BigDecimal("80000"));

        when(employeeService.updateEmployee(
                eq(employeeId),
                any(UpdateEmployeeRequest.class)))
                .thenThrow(new EmployeeNotFoundException(employeeId));

        // Act + Assert
        mockMvc.perform(
                        put("/api/v1/employees/{id}", employeeId)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());

        // Verify
        verify(employeeService)
                .updateEmployee(eq(employeeId), any(UpdateEmployeeRequest.class));
    }

    @Test
    void shouldDeleteEmployeeSuccessfully() throws Exception {

        // Arrange
        Long employeeId = 1L;

        doNothing()
                .when(employeeService)
                .deleteEmployee(employeeId);

        // Act + Assert
        mockMvc.perform(
                        delete("/api/v1/employees/{id}", employeeId)
                )
                .andExpect(status().isNoContent());

        // Verify
        verify(employeeService)
                .deleteEmployee(employeeId);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingEmployee()
            throws Exception {

        // Arrange
        Long employeeId = 999L;

        doThrow(new EmployeeNotFoundException(employeeId))
                .when(employeeService)
                .deleteEmployee(employeeId);

        // Act + Assert
        mockMvc.perform(
                        delete("/api/v1/employees/{id}", employeeId)
                )
                .andExpect(status().isNotFound());

        // Verify
        verify(employeeService)
                .deleteEmployee(employeeId);
    }

    @Test
    void shouldSearchEmployeesSuccessfully() throws Exception {

        // Arrange
        EmployeeResponse response = new EmployeeResponse();

        response.setId(1L);
        response.setFirstName("Yash");
        response.setLastName("Kondekar");
        response.setEmail("yash@gmail.com");
        response.setDesignation("Spring Boot Developer");
        response.setSalary(new BigDecimal("50000"));

        when(employeeService.searchByKeyword(
                "Yash",
                "Spring Boot Developer"))
                .thenReturn(List.of(response));

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees/search")
                                .param("keyword", "Yash")
                                .param("designation", "Spring Boot Developer")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Yash"))
                .andExpect(jsonPath("$[0].designation")
                        .value("Spring Boot Developer"));

        // Verify
        verify(employeeService)
                .searchByKeyword(
                        "Yash",
                        "Spring Boot Developer");
    }

    @Test
    void shouldReturnBadRequestWhenCreateEmployeeRequestIsInvalid()
            throws Exception {

        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();

        request.setFirstName("");
        request.setLastName("");
        request.setEmail("invalid-email");
        request.setPhoneNumber("123");
        request.setDesignation("");
        request.setSalary(new BigDecimal("-500"));

        // Act + Assert
        mockMvc.perform(
                        post("/api/v1/employees")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        // Service should NOT be called
        verify(employeeService, never())
                .createEmployee(any(CreateEmployeeRequest.class));
    }


}