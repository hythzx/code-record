package com.aiyun.code.record.service.mapper;

import com.aiyun.code.record.domain.*;
import com.aiyun.code.record.service.dto.DeveloperDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Developer} and its DTO {@link DeveloperDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeveloperMapper extends EntityMapper<DeveloperDTO, Developer> {



    default Developer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Developer developer = new Developer();
        developer.setId(id);
        return developer;
    }
}
