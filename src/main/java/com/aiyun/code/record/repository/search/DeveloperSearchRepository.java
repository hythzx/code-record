package com.aiyun.code.record.repository.search;
import com.aiyun.code.record.domain.Developer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Developer} entity.
 */
public interface DeveloperSearchRepository extends ElasticsearchRepository<Developer, Long> {
}
