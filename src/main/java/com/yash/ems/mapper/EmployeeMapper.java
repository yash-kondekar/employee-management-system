package com.yash.ems.mapper;

import com.yash.ems.dto.request.CreateEmployeeRequest;
import com.yash.ems.dto.request.UpdateEmployeeRequest;
import com.yash.ems.dto.response.EmployeeResponse;
import com.yash.ems.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Employee toEntity(CreateEmployeeRequest request);

    EmployeeResponse toResponse(Employee employee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeFromRequest(
            UpdateEmployeeRequest request,
            @MappingTarget Employee employee
    );

}




