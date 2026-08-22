package com.yash.ems.service.impl;

import com.yash.ems.dto.request.CreateEmployeeRequest;
import com.yash.ems.dto.request.UpdateEmployeeRequest;
import com.yash.ems.dto.response.EmployeeResponse;
import com.yash.ems.dto.response.PageResponse;
import com.yash.ems.entity.Employee;
import com.yash.ems.exception.EmployeeNotFoundException;
import com.yash.ems.mapper.EmployeeMapper;
import com.yash.ems.repository.EmployeeRepository;
import com.yash.ems.service.EmployeeService;
import com.yash.ems.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {

        Employee employee = employeeMapper.toEntity(request);

        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        logger.info("Creating employee with email: {}", request.getEmail());

        Employee savedEmployee = employeeRepository.save(employee);

        logger.info("Employee created successfully with ID: {}", savedEmployee.getId());

        return employeeMapper.toResponse(savedEmployee);
    }

//    @Override
//    public List<EmployeeResponse> getAllEmployees() {
//
//        List<Employee> employees = employeeRepository.findAll();
//
//        return employees.stream()
//                .map(employeeMapper::toResponse)
//                .toList();
//    }

//    @Override
//    public Page<EmployeeResponse> getAllEmployees(
//            int page,
//            int size,
//            String sortBy,
//            String sortDir) {
//
//        Sort sort = sortDir.equalsIgnoreCase("asc")
//                ? Sort.by(sortBy).ascending()
//                : Sort.by(sortBy).descending();
//
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<Employee> employeePage = employeeRepository.findAll(pageable);
//
//        return employeePage.map(employeeMapper::toResponse);
//    }
@Override
public PageResponse<EmployeeResponse> getAllEmployees(
        int page,
        int size,
        String sortBy,
        String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Employee> employeePage = employeeRepository.findAll(pageable);

    List<EmployeeResponse> employeeResponses = employeePage
            .getContent()
            .stream()
            .map(employeeMapper::toResponse)
            .toList();

    return new PageResponse<>(
            employeeResponses,
            employeePage.getNumber(),
            employeePage.getSize(),
            employeePage.getTotalElements(),
            employeePage.getTotalPages(),
            employeePage.isFirst(),
            employeePage.isLast()
    );
}

    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        logger.info("Fetching employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {

                    logger.warn("Employee not found with ID: {}", id);

                    return new EmployeeNotFoundException(id);
                });

        logger.info(
                "Successfully fetched employee with ID: {}",
                employee.getId()
        );

        return employeeMapper.toResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->{

                    logger.warn("Employee not found with ID: {}", id);

                    return new EmployeeNotFoundException(id);
                });

        logger.info("Updating employee with ID: {}", id);

        employeeMapper.updateEmployeeFromRequest(request, employee);

        Employee updatedEmployee = employeeRepository.save(employee);

        logger.info("Employee updated successfully with ID: {}", updatedEmployee.getId());

        return employeeMapper.toResponse(updatedEmployee);
    }


    @Override
    public void deleteEmployee(Long id) {

        logger.info("Deleting employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->{

            logger.warn("Employee not found with ID: {}", id);

            return new EmployeeNotFoundException(id);
        });

        employeeRepository.delete(employee);

        logger.info(
                "Employee deleted successfully with ID: {}",
                employee.getId()
        );
    }

    @Override
    public List<EmployeeResponse> searchByKeyword(
            String keyword,
            String designation) {

        logger.info(
                "Searching employees with keyword: {} and designation: {}",
                keyword,
                designation
        );

        Specification<Employee> specification =
                EmployeeSpecification.hasKeyword(keyword)
                        .and(EmployeeSpecification.hasDesignation(designation));

        List<Employee> employees =
                employeeRepository.findAll(specification);

        logger.info(
                "Found {} employees matching search criteria",
                employees.size()
        );

        return employees.stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

}