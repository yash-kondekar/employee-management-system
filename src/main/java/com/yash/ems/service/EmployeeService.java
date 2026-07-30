package com.yash.ems.service;

import com.yash.ems.dto.request.CreateEmployeeRequest;
import com.yash.ems.dto.request.UpdateEmployeeRequest;
import com.yash.ems.dto.response.EmployeeResponse;

import com.yash.ems.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface EmployeeService {

    //insert
    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    //get all employee
//    List<EmployeeResponse> getAllEmployees();
    PageResponse<EmployeeResponse> getAllEmployees(
            int page,
            int size,
            String sortBy,
            String sortDir
    );

    //get employee by id
    EmployeeResponse getEmployeeById(Long id);

    //update employee
    EmployeeResponse updateEmployee(Long id,UpdateEmployeeRequest request);

    void deleteEmployee(Long id);


    // new methods search
    List<EmployeeResponse> searchEmployeesByFirstName(String firstName);

}