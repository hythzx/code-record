package com.aiyun.code.record.repository;
import com.aiyun.code.record.domain.Developer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Developer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

}
