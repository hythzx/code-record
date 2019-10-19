package com.aiyun.code.record.repository.search;
import com.aiyun.code.record.domain.SubmitRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SubmitRecord} entity.
 */
public interface SubmitRecordSearchRepository extends ElasticsearchRepository<SubmitRecord, Long> {
}
