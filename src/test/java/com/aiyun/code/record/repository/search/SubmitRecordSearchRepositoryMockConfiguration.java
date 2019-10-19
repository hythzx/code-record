package com.aiyun.code.record.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SubmitRecordSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SubmitRecordSearchRepositoryMockConfiguration {

    @MockBean
    private SubmitRecordSearchRepository mockSubmitRecordSearchRepository;

}
