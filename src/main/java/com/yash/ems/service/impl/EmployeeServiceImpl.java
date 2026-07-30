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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {

        Employee employee = employeeMapper.toEntity(request);

        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        Employee savedEmployee = employeeRepository.save(employee);

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

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return employeeMapper.toResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employeeMapper.updateEmployeeFromRequest(request, employee);

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(updatedEmployee);
    }


    @Override
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employeeRepository.delete(employee);
    }

}