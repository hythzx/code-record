package com.aiyun.code.record.web.rest;

import com.aiyun.code.record.CodeRecordApplicationApp;
import com.aiyun.code.record.domain.SubmitRecord;
import com.aiyun.code.record.repository.SubmitRecordRepository;
import com.aiyun.code.record.repository.search.SubmitRecordSearchRepository;
import com.aiyun.code.record.service.SubmitRecordService;
import com.aiyun.code.record.service.dto.SubmitRecordDTO;
import com.aiyun.code.record.service.mapper.SubmitRecordMapper;
import com.aiyun.code.record.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.aiyun.code.record.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SubmitRecordResource} REST controller.
 */
@SpringBootTest(classes = CodeRecordApplicationApp.class)
public class SubmitRecordResourceIT {

    private static final LocalDate DEFAULT_RECORD_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECORD_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_CNT = 1;
    private static final Integer UPDATED_CNT = 2;

    @Autowired
    private SubmitRecordRepository submitRecordRepository;

    @Autowired
    private SubmitRecordMapper submitRecordMapper;

    @Autowired
    private SubmitRecordService submitRecordService;

    /**
     * This repository is mocked in the com.aiyun.code.record.repository.search test package.
     *
     * @see com.aiyun.code.record.repository.search.SubmitRecordSearchRepositoryMockConfiguration
     */
    @Autowired
    private SubmitRecordSearchRepository mockSubmitRecordSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSubmitRecordMockMvc;

    private SubmitRecord submitRecord;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubmitRecordResource submitRecordResource = new SubmitRecordResource(submitRecordService);
        this.restSubmitRecordMockMvc = MockMvcBuilders.standaloneSetup(submitRecordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubmitRecord createEntity(EntityManager em) {
        SubmitRecord submitRecord = new SubmitRecord()
            .recordDate(DEFAULT_RECORD_DATE)
            .cnt(DEFAULT_CNT);
        return submitRecord;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubmitRecord createUpdatedEntity(EntityManager em) {
        SubmitRecord submitRecord = new SubmitRecord()
            .recordDate(UPDATED_RECORD_DATE)
            .cnt(UPDATED_CNT);
        return submitRecord;
    }

    @BeforeEach
    public void initTest() {
        submitRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubmitRecord() throws Exception {
        int databaseSizeBeforeCreate = submitRecordRepository.findAll().size();

        // Create the SubmitRecord
        SubmitRecordDTO submitRecordDTO = submitRecordMapper.toDto(submitRecord);
        restSubmitRecordMockMvc.perform(post("/api/submit-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submitRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the SubmitRecord in the database
        List<SubmitRecord> submitRecordList = submitRecordRepository.findAll();
        assertThat(submitRecordList).hasSize(databaseSizeBeforeCreate + 1);
        SubmitRecord testSubmitRecord = submitRecordList.get(submitRecordList.size() - 1);
        assertThat(testSubmitRecord.getRecordDate()).isEqualTo(DEFAULT_RECORD_DATE);
        assertThat(testSubmitRecord.getCnt()).isEqualTo(DEFAULT_CNT);

        // Validate the SubmitRecord in Elasticsearch
        verify(mockSubmitRecordSearchRepository, times(1)).save(testSubmitRecord);
    }

    @Test
    @Transactional
    public void createSubmitRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = submitRecordRepository.findAll().size();

        // Create the SubmitRecord with an existing ID
        submitRecord.setId(1L);
        SubmitRecordDTO submitRecordDTO = submitRecordMapper.toDto(submitRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmitRecordMockMvc.perform(post("/api/submit-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submitRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubmitRecord in the database
        List<SubmitRecord> submitRecordList = submitRecordRepository.findAll();
        assertThat(submitRecordList).hasSize(databaseSizeBeforeCreate);

        // Validate the SubmitRecord in Elasticsearch
        verify(mockSubmitRecordSearchRepository, times(0)).save(submitRecord);
    }


    @Test
    @Transactional
    public void checkRecordDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = submitRecordRepository.findAll().size();
        // set the field null
        submitRecord.setRecordDate(null);

        // Create the SubmitRecord, which fails.
        SubmitRecordDTO submitRecordDTO = submitRecordMapper.toDto(submitRecord);

        restSubmitRecordMockMvc.perform(post("/api/submit-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submitRecordDTO)))
            .andExpect(status().isBadRequest());

        List<SubmitRecord> submitRecordList = submitRecordRepository.findAll();
        assertThat(submitRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCntIsRequired() throws Exception {
        int databaseSizeBeforeTest = submitRecordRepository.findAll().size();
        // set the field null
        submitRecord.setCnt(null);

        // Create the SubmitRecord, which fails.
        SubmitRecordDTO submitRecordDTO = submitRecordMapper.toDto(submitRecord);

        restSubmitRecordMockMvc.perform(post("/api/submit-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submitRecordDTO)))
            .andExpect(status().isBadRequest());

        List<SubmitRecord> submitRecordList = submitRecordRepository.findAll();
        assertThat(submitRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubmitRecords() throws Exception {
        // Initialize the database
        submitRecordRepository.saveAndFlush(submitRecord);

        // Get all the submitRecordList
        restSubmitRecordMockMvc.perform(get("/api/submit-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submitRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordDate").value(hasItem(DEFAULT_RECORD_DATE.toString())))
            .andExpect(jsonPath("$.[*].cnt").value(hasItem(DEFAULT_CNT)));
    }
    
    @Test
    @Transactional
    public void getSubmitRecord() throws Exception {
        // Initialize the database
        submitRecordRepository.saveAndFlush(submitRecord);

        // Get the submitRecord
        restSubmitRecordMockMvc.perform(get("/api/submit-records/{id}", submitRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(submitRecord.getId().intValue()))
            .andExpect(jsonPath("$.recordDate").value(DEFAULT_RECORD_DATE.toString()))
            .andExpect(jsonPath("$.cnt").value(DEFAULT_CNT));
    }

    @Test
    @Transactional
    public void getNonExistingSubmitRecord() throws Exception {
        // Get the submitRecord
        restSubmitRecordMockMvc.perform(get("/api/submit-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubmitRecord() throws Exception {
        // Initialize the database
        submitRecordRepository.saveAndFlush(submitRecord);

        int databaseSizeBeforeUpdate = submitRecordRepository.findAll().size();

        // Update the submitRecord
        SubmitRecord updatedSubmitRecord = submitRecordRepository.findById(submitRecord.getId()).get();
        // Disconnect from session so that the updates on updatedSubmitRecord are not directly saved in db
        em.detach(updatedSubmitRecord);
        updatedSubmitRecord
            .recordDate(UPDATED_RECORD_DATE)
            .cnt(UPDATED_CNT);
        SubmitRecordDTO submitRecordDTO = submitRecordMapper.toDto(updatedSubmitRecord);

        restSubmitRecordMockMvc.perform(put("/api/submit-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submitRecordDTO)))
            .andExpect(status().isOk());

        // Validate the SubmitRecord in the database
        List<SubmitRecord> submitRecordList = submitRecordRepository.findAll();
        assertThat(submitRecordList).hasSize(databaseSizeBeforeUpdate);
        SubmitRecord testSubmitRecord = submitRecordList.get(submitRecordList.size() - 1);
        assertThat(testSubmitRecord.getRecordDate()).isEqualTo(UPDATED_RECORD_DATE);
        assertThat(testSubmitRecord.getCnt()).isEqualTo(UPDATED_CNT);

        // Validate the SubmitRecord in Elasticsearch
        verify(mockSubmitRecordSearchRepository, times(1)).save(testSubmitRecord);
    }

    @Test
    @Transactional
    public void updateNonExistingSubmitRecord() throws Exception {
        int databaseSizeBeforeUpdate = submitRecordRepository.findAll().size();

        // Create the SubmitRecord
        SubmitRecordDTO submitRecordDTO = submitRecordMapper.toDto(submitRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmitRecordMockMvc.perform(put("/api/submit-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submitRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubmitRecord in the database
        List<SubmitRecord> submitRecordList = submitRecordRepository.findAll();
        assertThat(submitRecordList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SubmitRecord in Elasticsearch
        verify(mockSubmitRecordSearchRepository, times(0)).save(submitRecord);
    }

    @Test
    @Transactional
    public void deleteSubmitRecord() throws Exception {
        // Initialize the database
        submitRecordRepository.saveAndFlush(submitRecord);

        int databaseSizeBeforeDelete = submitRecordRepository.findAll().size();

        // Delete the submitRecord
        restSubmitRecordMockMvc.perform(delete("/api/submit-records/{id}", submitRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubmitRecord> submitRecordList = submitRecordRepository.findAll();
        assertThat(submitRecordList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SubmitRecord in Elasticsearch
        verify(mockSubmitRecordSearchRepository, times(1)).deleteById(submitRecord.getId());
    }

    @Test
    @Transactional
    public void searchSubmitRecord() throws Exception {
        // Initialize the database
        submitRecordRepository.saveAndFlush(submitRecord);
        when(mockSubmitRecordSearchRepository.search(queryStringQuery("id:" + submitRecord.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(submitRecord), PageRequest.of(0, 1), 1));
        // Search the submitRecord
        restSubmitRecordMockMvc.perform(get("/api/_search/submit-records?query=id:" + submitRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submitRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordDate").value(hasItem(DEFAULT_RECORD_DATE.toString())))
            .andExpect(jsonPath("$.[*].cnt").value(hasItem(DEFAULT_CNT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmitRecord.class);
        SubmitRecord submitRecord1 = new SubmitRecord();
        submitRecord1.setId(1L);
        SubmitRecord submitRecord2 = new SubmitRecord();
        submitRecord2.setId(submitRecord1.getId());
        assertThat(submitRecord1).isEqualTo(submitRecord2);
        submitRecord2.setId(2L);
        assertThat(submitRecord1).isNotEqualTo(submitRecord2);
        submitRecord1.setId(null);
        assertThat(submitRecord1).isNotEqualTo(submitRecord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmitRecordDTO.class);
        SubmitRecordDTO submitRecordDTO1 = new SubmitRecordDTO();
        submitRecordDTO1.setId(1L);
        SubmitRecordDTO submitRecordDTO2 = new SubmitRecordDTO();
        assertThat(submitRecordDTO1).isNotEqualTo(submitRecordDTO2);
        submitRecordDTO2.setId(submitRecordDTO1.getId());
        assertThat(submitRecordDTO1).isEqualTo(submitRecordDTO2);
        submitRecordDTO2.setId(2L);
        assertThat(submitRecordDTO1).isNotEqualTo(submitRecordDTO2);
        submitRecordDTO1.setId(null);
        assertThat(submitRecordDTO1).isNotEqualTo(submitRecordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(submitRecordMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(submitRecordMapper.fromId(null)).isNull();
    }
}
