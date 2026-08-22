package com.yash.ems.repository;

import com.yash.ems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmail(String email);


    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );


    //by using JPQL
    @Query("""
            SELECT e
            FROM Employee e
            WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    List<Employee> searchByKeyword(@Param("keyword") String keyword);

}
