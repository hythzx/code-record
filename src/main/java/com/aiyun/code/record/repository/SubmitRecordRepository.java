package com.aiyun.code.record.repository;
import com.aiyun.code.record.domain.SubmitRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SubmitRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubmitRecordRepository extends JpaRepository<SubmitRecord, Long> {

}
