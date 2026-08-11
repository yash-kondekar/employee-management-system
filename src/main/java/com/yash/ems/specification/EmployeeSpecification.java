package com.yash.ems.specification;

import com.yash.ems.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasKeyword(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        "%" + keyword.toLowerCase() + "%"
                );
    }

    public static Specification<Employee> hasDesignation(String designation) {

        if (designation == null || designation.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("designation")),
                        designation.toLowerCase()
                );
    }
}