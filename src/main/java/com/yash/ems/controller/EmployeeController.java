package com.yash.ems.controller;


import com.yash.ems.dto.request.CreateEmployeeRequest;
import com.yash.ems.dto.request.UpdateEmployeeRequest;
import com.yash.ems.dto.response.EmployeeResponse;
import com.yash.ems.dto.response.PageResponse;
import com.yash.ems.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(
        name = "Employee Management",
        description = "REST APIs for managing employees"
)
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(
            summary = "Create Employee",
            description = "Creates a new employee in the system"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "201",
                    description = "Employee created successfully"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            )

    })
    @PostMapping
    public EmployeeResponse createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

//        return employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(request)).getBody();
    }




    @Operation(
            summary = "Get All Employee",
            description = "Get all employee list from the system"
    )
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

    @Operation(
            summary = "Get single Employee",
            description = "Get single employee by id from the system"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found successfully"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )

    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(employeeService.getEmployeeById(id));

    }

    @Operation(
            summary = "Update Employee",
            description = "Update employee by id from the system"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Employee updated successfully"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )

    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {

        EmployeeResponse response = employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete single Employee",
            description = "Delete single employee from the system"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "204",
                    description = "Employee deleted successfully"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )

    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search Employee - Filter",
            description = "Search employees in the system"
    )
    // new search methods
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String designation
    ) {

        List<EmployeeResponse> employees =
                employeeService.searchByKeyword(keyword, designation);

        return ResponseEntity.ok(employees);
    }

}
