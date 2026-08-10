package com.yash.ems.controller;


import com.yash.ems.dto.request.CreateEmployeeRequest;
import com.yash.ems.dto.request.UpdateEmployeeRequest;
import com.yash.ems.dto.response.EmployeeResponse;
import com.yash.ems.dto.response.PageResponse;
import com.yash.ems.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeResponse createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

//        return employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(request)).getBody();
    }


//    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
//
//        return ResponseEntity.ok(employeeService.getAllEmployees());
//
//    }

@GetMapping
public ResponseEntity<PageResponse<EmployeeResponse>> getAllEmployees(

        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir

) {

    return ResponseEntity.ok(
            employeeService.getAllEmployees(
                    page,
                    size,
                    sortBy,
                    sortDir
            )
    );
}

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(employeeService.getEmployeeById(id));

    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {

        EmployeeResponse response = employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    // new search methods
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(
            @RequestParam String keyword) {

        List<EmployeeResponse> employees =
                employeeService.searchByKeyword(keyword);

        return ResponseEntity.ok(employees);
    }

}
