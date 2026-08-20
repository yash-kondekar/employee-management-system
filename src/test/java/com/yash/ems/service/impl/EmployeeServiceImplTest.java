package com.yash.ems.service.impl;

import com.yash.ems.dto.request.CreateEmployeeRequest;
import com.yash.ems.dto.request.UpdateEmployeeRequest;
import com.yash.ems.dto.response.EmployeeResponse;
import com.yash.ems.entity.Employee;
import com.yash.ems.exception.EmployeeNotFoundException;
import com.yash.ems.mapper.EmployeeMapper;
import com.yash.ems.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private CreateEmployeeRequest request;

    private Employee employee;

    private Employee savedEmployee;

    private EmployeeResponse response;

    @BeforeEach
    void setUp() {

        request = new CreateEmployeeRequest();

        employee = new Employee();

        savedEmployee = new Employee();

        response = new EmployeeResponse();

    }

    // create employee test
    @Test
    void shouldCreateEmployeeSuccessfully() {

        // Arrange
        request.setFirstName("Yash");
        request.setLastName("Kondekar");
        request.setEmail("yash@gmail.com");
        request.setPhoneNumber("9876543210");
        request.setDesignation("Spring Boot Developer");
        request.setSalary(new BigDecimal("50000"));

        employee.setFirstName("Yash");
        employee.setLastName("Kondekar");
        employee.setEmail("yash@gmail.com");
        employee.setPhoneNumber("9876543210");
        employee.setDesignation("Spring Boot Developer");
        employee.setSalary(new BigDecimal("50000"));

        savedEmployee.setId(1L);
        savedEmployee.setFirstName("Yash");
        savedEmployee.setLastName("Kondekar");
        savedEmployee.setEmail("yash@gmail.com");
        savedEmployee.setPhoneNumber("9876543210");
        savedEmployee.setDesignation("Spring Boot Developer");
        savedEmployee.setSalary(new BigDecimal("50000"));

        response.setId(1L);
        response.setFirstName("Yash");
        response.setLastName("Kondekar");
        response.setEmail("yash@gmail.com");
        response.setPhoneNumber("9876543210");
        response.setDesignation("Spring Boot Developer");
        response.setSalary(new BigDecimal("50000"));

        when(employeeMapper.toEntity(request))
                .thenReturn(employee);

        when(employeeRepository.save(employee))
                .thenReturn(savedEmployee);

        when(employeeMapper.toResponse(savedEmployee))
                .thenReturn(response);

        // Act
        EmployeeResponse result = employeeService.createEmployee(request);

        // Assert
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getFirstName(), result.getFirstName());
        assertEquals(response.getLastName(), result.getLastName());
        assertEquals(response.getEmail(), result.getEmail());
        assertEquals(response.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(response.getDesignation(), result.getDesignation());
        assertEquals(response.getSalary(), result.getSalary());

        verify(employeeMapper, times(1)).toEntity(request);
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeMapper, times(1)).toResponse(savedEmployee);
    }

    //Find by id test
    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenEmployeeDoesNotExist() {

        // Arrange
        Long employeeId = 999L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(employeeId)
        );

        // Verify
        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper, never()).toResponse(any());
    }


    // update employee test
    @Test
    void shouldUpdateEmployeeSuccessfully() {

        Long employeeId = 1L;

        UpdateEmployeeRequest updateRequest = new UpdateEmployeeRequest();

        updateRequest.setFirstName("Yash");
        updateRequest.setLastName("Kondekar");
        updateRequest.setEmail("yash@gmail.com");
        updateRequest.setPhoneNumber("9876543210");
        updateRequest.setDesignation("Senior Java Developer");
        updateRequest.setSalary(new BigDecimal("80000"));

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.save(employee))
                .thenReturn(savedEmployee);

        when(employeeMapper.toResponse(savedEmployee))
                .thenReturn(response);

        EmployeeResponse result =
                employeeService.updateEmployee(employeeId, updateRequest);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getFirstName(), result.getFirstName());
        assertEquals(response.getLastName(), result.getLastName());
        assertEquals(response.getEmail(), result.getEmail());
        assertEquals(response.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(response.getDesignation(), result.getDesignation());
        assertEquals(response.getSalary(), result.getSalary());

        verify(employeeRepository).findById(employeeId);

        verify(employeeMapper)
                .updateEmployeeFromRequest(updateRequest, employee);

        verify(employeeRepository).save(employee);

        verify(employeeMapper).toResponse(savedEmployee);
    }

    // update Employee - Exception test
    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenUpdatingEmployeeDoesNotExist() {

        // Arrange
        Long employeeId = 999L;

        UpdateEmployeeRequest updateRequest = new UpdateEmployeeRequest();

        updateRequest.setFirstName("Yash");
        updateRequest.setLastName("Kondekar");
        updateRequest.setEmail("yash@gmail.com");
        updateRequest.setPhoneNumber("9876543210");
        updateRequest.setDesignation("Senior Java Developer");
        updateRequest.setSalary(new BigDecimal("80000"));

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(employeeId, updateRequest)
        );

        // Verify
        verify(employeeRepository).findById(employeeId);

        verify(employeeMapper, never())
                .updateEmployeeFromRequest(any(), any());

        verify(employeeRepository, never())
                .save(any());

        verify(employeeMapper, never())
                .toResponse(any());
    }

    //delete employee test
    @Test
    void shouldDeleteEmployeeSuccessfully() {

        // Arrange
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));

        // Act
        employeeService.deleteEmployee(employeeId);

        // Assert
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).delete(employee);
    }

    //Delete Employee - Exception test
    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenDeletingEmployeeDoesNotExist() {

        // Arrange
        Long employeeId = 999L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(employeeId)
        );

        // Verify
        verify(employeeRepository).findById(employeeId);

        verify(employeeRepository, never())
                .delete(any(Employee.class));
    }

    @Test
    void shouldSearchEmployeesSuccessfully() {

        // Arrange
        String keyword = "Yash";
        String designation = "Spring Boot Developer";

        List<Employee> employees =
                List.of(employee);

        List<EmployeeResponse> responses =
                List.of(response);

        when(employeeRepository.findAll(any(Specification.class)))
                .thenReturn(employees);

        when(employeeMapper.toResponse(employee))
                .thenReturn(response);

        // Act
        List<EmployeeResponse> result =
                employeeService.searchByKeyword(keyword, designation);

        // Assert
        assertEquals(1, result.size());

        assertEquals(response.getFirstName(),
                result.getFirst().getFirstName());

        verify(employeeRepository)
                .findAll(any(Specification.class));

        verify(employeeMapper)
                .toResponse(employee);
    }
}