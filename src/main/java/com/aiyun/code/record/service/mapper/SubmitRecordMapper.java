package com.aiyun.code.record.service.mapper;

import com.aiyun.code.record.domain.*;
import com.aiyun.code.record.service.dto.SubmitRecordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubmitRecord} and its DTO {@link SubmitRecordDTO}.
 */
@Mapper(componentModel = "spring", uses = {DeveloperMapper.class, ProjectMapper.class})
public interface SubmitRecordMapper extends EntityMapper<SubmitRecordDTO, SubmitRecord> {

    @Mapping(source = "developer.id", target = "developerId")
    @Mapping(source = "developer.name", target = "developerName")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    SubmitRecordDTO toDto(SubmitRecord submitRecord);

    @Mapping(source = "developerId", target = "developer")
    @Mapping(source = "projectId", target = "project")
    SubmitRecord toEntity(SubmitRecordDTO submitRecordDTO);

    default SubmitRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubmitRecord submitRecord = new SubmitRecord();
        submitRecord.setId(id);
        return submitRecord;
    }
}
