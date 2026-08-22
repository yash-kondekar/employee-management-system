package com.yash.ems;

import com.yash.ems.dto.request.UpdateEmployeeRequest;
import com.yash.ems.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.ems.dto.request.CreateEmployeeRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yash.ems.repository.EmployeeRepository;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(
        statements = "TRUNCATE TABLE employee RESTART IDENTITY CASCADE",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateEmployeeSuccessfully() throws Exception {

        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();

        request.setFirstName("Yash");
        request.setLastName("Kondekar");
        request.setEmail("integration@yash.com");
        request.setPhoneNumber("9876543210");
        request.setDesignation("Spring Boot Developer");
        request.setSalary(new BigDecimal("50000"));

        // Act + Assert
        mockMvc.perform(
                        post("/api/v1/employees")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Yash"))
                .andExpect(jsonPath("$.lastName").value("Kondekar"))
                .andExpect(jsonPath("$.email")
                        .value("integration@yash.com"))
                .andExpect(jsonPath("$.phoneNumber")
                        .value("9876543210"))
                .andExpect(jsonPath("$.designation")
                        .value("Spring Boot Developer"))
                .andExpect(jsonPath("$.salary").value(50000));

        Employee savedEmployee =
                employeeRepository.findByEmail("integration@yash.com")
                        .orElseThrow();

        assertEquals("Yash", savedEmployee.getFirstName());
        assertEquals("Kondekar", savedEmployee.getLastName());
        assertEquals("integration@yash.com", savedEmployee.getEmail());
        assertEquals("9876543210", savedEmployee.getPhoneNumber());
        assertEquals("Spring Boot Developer", savedEmployee.getDesignation());
        assertEquals(
                0,
                new BigDecimal("50000").compareTo(savedEmployee.getSalary())
        );
    }

    @Test
    void shouldGetEmployeeByIdSuccessfully() throws Exception {

        // Arrange
        Employee employee = new Employee();

        employee.setFirstName("Rahul");
        employee.setLastName("Sharma");
        employee.setEmail("getbyid@yash.com");
        employee.setPhoneNumber("9999999999");
        employee.setDesignation("Java Developer");
        employee.setSalary(new BigDecimal("60000"));

        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());


        Employee savedEmployee = employeeRepository.save(employee);

        Long employeeId = savedEmployee.getId();

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees/{id}", employeeId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId))
                .andExpect(jsonPath("$.firstName").value("Rahul"))
                .andExpect(jsonPath("$.lastName").value("Sharma"))
                .andExpect(jsonPath("$.email").value("getbyid@yash.com"))
                .andExpect(jsonPath("$.phoneNumber").value("9999999999"))
                .andExpect(jsonPath("$.designation").value("Java Developer"))
                .andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
    void shouldGetAllEmployeesSuccessfully() throws Exception {

        // Arrange
        Employee employee1 = new Employee();

        employee1.setFirstName("Yash");
        employee1.setLastName("Kondekar");
        employee1.setEmail("getall1@yash.com");
        employee1.setPhoneNumber("9876543210");
        employee1.setDesignation("Spring Boot Developer");
        employee1.setSalary(new BigDecimal("50000"));
        employee1.setCreatedAt(LocalDateTime.now());
        employee1.setUpdatedAt(LocalDateTime.now());

        Employee employee2 = new Employee();

        employee2.setFirstName("Rahul");
        employee2.setLastName("Sharma");
        employee2.setEmail("getall2@yash.com");
        employee2.setPhoneNumber("9999999999");
        employee2.setDesignation("Java Developer");
        employee2.setSalary(new BigDecimal("60000"));
        employee2.setCreatedAt(LocalDateTime.now());
        employee2.setUpdatedAt(LocalDateTime.now());

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sortBy", "id")
                                .param("sortDir", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content[0].firstName").value("Yash"))
                .andExpect(jsonPath("$.content[0].lastName").value("Kondekar"))
                .andExpect(jsonPath("$.content[1].firstName").value("Rahul"))
                .andExpect(jsonPath("$.content[1].lastName").value("Sharma"));
    }

    @Test
    void shouldUpdateEmployeeSuccessfully() throws Exception {

        // Arrange - create existing employee
        Employee employee = new Employee();

        employee.setFirstName("Rahul");
        employee.setLastName("Sharma");
        employee.setEmail("update@yash.com");
        employee.setPhoneNumber("9999999999");
        employee.setDesignation("Java Developer");
        employee.setSalary(new BigDecimal("60000"));
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        Employee savedEmployee = employeeRepository.save(employee);

        Long employeeId = savedEmployee.getId();

        // Update request
        UpdateEmployeeRequest updateRequest = new UpdateEmployeeRequest();

        updateRequest.setFirstName("Yash");
        updateRequest.setLastName("Kondekar");
        updateRequest.setEmail("update@yash.com");
        updateRequest.setPhoneNumber("9876543210");
        updateRequest.setDesignation("Senior Spring Boot Developer");
        updateRequest.setSalary(new BigDecimal("80000"));

        // Act + Assert
        mockMvc.perform(
                        put("/api/v1/employees/{id}", employeeId)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId))
                .andExpect(jsonPath("$.firstName").value("Yash"))
                .andExpect(jsonPath("$.lastName").value("Kondekar"))
                .andExpect(jsonPath("$.email").value("update@yash.com"))
                .andExpect(jsonPath("$.phoneNumber").value("9876543210"))
                .andExpect(jsonPath("$.designation")
                        .value("Senior Spring Boot Developer"))
                .andExpect(jsonPath("$.salary").value(80000));

        // Verify actual database
        Employee updatedEmployee =
                employeeRepository.findById(employeeId)
                        .orElseThrow();

        assertEquals("Yash", updatedEmployee.getFirstName());
        assertEquals("Kondekar", updatedEmployee.getLastName());
        assertEquals("update@yash.com", updatedEmployee.getEmail());
        assertEquals("9876543210", updatedEmployee.getPhoneNumber());
        assertEquals(
                "Senior Spring Boot Developer",
                updatedEmployee.getDesignation()
        );

        assertEquals(
                0,
                new BigDecimal("80000")
                        .compareTo(updatedEmployee.getSalary())
        );
    }

    @Test
    void shouldDeleteEmployeeSuccessfully() throws Exception {
        // Arrange
        Employee employee = new Employee();

        employee.setFirstName("Delete");
        employee.setLastName("Test");
        employee.setEmail("delete@yash.com");
        employee.setPhoneNumber("9876543210");
        employee.setDesignation("Java Developer");
        employee.setSalary(new BigDecimal("50000"));
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        Employee savedEmployee = employeeRepository.save(employee);

        Long employeeId = savedEmployee.getId();

        // Act + Assert - DELETE API
        mockMvc.perform(
                        delete("/api/v1/employees/{id}", employeeId)
                )
                .andExpect(status().isNoContent());

        // Verify actual database
        assertTrue(
                employeeRepository.findById(employeeId).isEmpty()
        );
    }

    @Test
    void shouldReturnNotFoundWhenGettingNonExistingEmployee() throws Exception {

        // Arrange
        Long employeeId = 999999L;

        // Act + Assert
        mockMvc.perform(
                        get("/api/v1/employees/{id}", employeeId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Employee not found with id: " + employeeId))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/employees/" + employeeId))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingEmployee() throws Exception {

        // Arrange
        Long employeeId = 999999L;

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();

        request.setFirstName("Yash");
        request.setLastName("Kondekar");
        request.setEmail("notfound@yash.com");
        request.setPhoneNumber("9876543210");
        request.setDesignation("Senior Java Developer");
        request.setSalary(new BigDecimal("80000"));

        // Act + Assert
        mockMvc.perform(
                        put("/api/v1/employees/{id}", employeeId)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingEmployee() throws Exception {

        // Arrange
        Long employeeId = 999999L;

        // Act + Assert
        mockMvc.perform(
                        delete("/api/v1/employees/{id}", employeeId)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingEmployeeWithInvalidData() throws Exception {

        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();

        request.setFirstName("A");
        request.setLastName("B");
        request.setEmail("invalid-email");
        request.setPhoneNumber("123");
        request.setDesignation("");
        request.setSalary(new BigDecimal("-5000"));

        // Act + Assert
        mockMvc.perform(
                        post("/api/v1/employees")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.path").value("/api/v1/employees"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.validationErrors").exists())
                .andExpect(jsonPath("$.validationErrors.firstName")
                        .value("First name must be between 2 and 50 characters"))
                .andExpect(jsonPath("$.validationErrors.lastName")
                        .value("Last name must be between 2 and 50 characters"))
                .andExpect(jsonPath("$.validationErrors.email")
                        .value("Invalid email format"))
                .andExpect(jsonPath("$.validationErrors.phoneNumber")
                        .value("Phone number must be a valid 10-digit Indian mobile number"))
                .andExpect(jsonPath("$.validationErrors.designation")
                        .value("Designation is required"))
                .andExpect(jsonPath("$.validationErrors.salary")
                        .value("Salary must be greater than zero"));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingEmployeeWithInvalidData() throws Exception {

        // Arrange
        Long employeeId = 999999L;

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();

        request.setFirstName("");
        request.setLastName("");
        request.setEmail("invalid-email");
        request.setPhoneNumber("123");
        request.setDesignation("");
        request.setSalary(new BigDecimal("-5000"));

        // Act + Assert
        mockMvc.perform(
                        put("/api/v1/employees/{id}", employeeId)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

}