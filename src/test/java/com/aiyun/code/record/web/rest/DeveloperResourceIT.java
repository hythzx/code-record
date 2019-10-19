package com.aiyun.code.record.web.rest;

import com.aiyun.code.record.CodeRecordApplicationApp;
import com.aiyun.code.record.domain.Developer;
import com.aiyun.code.record.repository.DeveloperRepository;
import com.aiyun.code.record.repository.search.DeveloperSearchRepository;
import com.aiyun.code.record.service.DeveloperService;
import com.aiyun.code.record.service.dto.DeveloperDTO;
import com.aiyun.code.record.service.mapper.DeveloperMapper;
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
 * Integration tests for the {@link DeveloperResource} REST controller.
 */
@SpringBootTest(classes = CodeRecordApplicationApp.class)
public class DeveloperResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DeveloperMapper developerMapper;

    @Autowired
    private DeveloperService developerService;

    /**
     * This repository is mocked in the com.aiyun.code.record.repository.search test package.
     *
     * @see com.aiyun.code.record.repository.search.DeveloperSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeveloperSearchRepository mockDeveloperSearchRepository;

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

    private MockMvc restDeveloperMockMvc;

    private Developer developer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeveloperResource developerResource = new DeveloperResource(developerService);
        this.restDeveloperMockMvc = MockMvcBuilders.standaloneSetup(developerResource)
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
    public static Developer createEntity(EntityManager em) {
        Developer developer = new Developer()
            .name(DEFAULT_NAME);
        return developer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Developer createUpdatedEntity(EntityManager em) {
        Developer developer = new Developer()
            .name(UPDATED_NAME);
        return developer;
    }

    @BeforeEach
    public void initTest() {
        developer = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeveloper() throws Exception {
        int databaseSizeBeforeCreate = developerRepository.findAll().size();

        // Create the Developer
        DeveloperDTO developerDTO = developerMapper.toDto(developer);
        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isCreated());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeCreate + 1);
        Developer testDeveloper = developerList.get(developerList.size() - 1);
        assertThat(testDeveloper.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(1)).save(testDeveloper);
    }

    @Test
    @Transactional
    public void createDeveloperWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = developerRepository.findAll().size();

        // Create the Developer with an existing ID
        developer.setId(1L);
        DeveloperDTO developerDTO = developerMapper.toDto(developer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(0)).save(developer);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = developerRepository.findAll().size();
        // set the field null
        developer.setName(null);

        // Create the Developer, which fails.
        DeveloperDTO developerDTO = developerMapper.toDto(developer);

        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isBadRequest());

        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDevelopers() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        // Get all the developerList
        restDeveloperMockMvc.perform(get("/api/developers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(developer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getDeveloper() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        // Get the developer
        restDeveloperMockMvc.perform(get("/api/developers/{id}", developer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(developer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingDeveloper() throws Exception {
        // Get the developer
        restDeveloperMockMvc.perform(get("/api/developers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeveloper() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        int databaseSizeBeforeUpdate = developerRepository.findAll().size();

        // Update the developer
        Developer updatedDeveloper = developerRepository.findById(developer.getId()).get();
        // Disconnect from session so that the updates on updatedDeveloper are not directly saved in db
        em.detach(updatedDeveloper);
        updatedDeveloper
            .name(UPDATED_NAME);
        DeveloperDTO developerDTO = developerMapper.toDto(updatedDeveloper);

        restDeveloperMockMvc.perform(put("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isOk());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeUpdate);
        Developer testDeveloper = developerList.get(developerList.size() - 1);
        assertThat(testDeveloper.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(1)).save(testDeveloper);
    }

    @Test
    @Transactional
    public void updateNonExistingDeveloper() throws Exception {
        int databaseSizeBeforeUpdate = developerRepository.findAll().size();

        // Create the Developer
        DeveloperDTO developerDTO = developerMapper.toDto(developer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeveloperMockMvc.perform(put("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(0)).save(developer);
    }

    @Test
    @Transactional
    public void deleteDeveloper() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        int databaseSizeBeforeDelete = developerRepository.findAll().size();

        // Delete the developer
        restDeveloperMockMvc.perform(delete("/api/developers/{id}", developer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Developer in Elasticsearch
        verify(mockDeveloperSearchRepository, times(1)).deleteById(developer.getId());
    }

    @Test
    @Transactional
    public void searchDeveloper() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);
        when(mockDeveloperSearchRepository.search(queryStringQuery("id:" + developer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(developer), PageRequest.of(0, 1), 1));
        // Search the developer
        restDeveloperMockMvc.perform(get("/api/_search/developers?query=id:" + developer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(developer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Developer.class);
        Developer developer1 = new Developer();
        developer1.setId(1L);
        Developer developer2 = new Developer();
        developer2.setId(developer1.getId());
        assertThat(developer1).isEqualTo(developer2);
        developer2.setId(2L);
        assertThat(developer1).isNotEqualTo(developer2);
        developer1.setId(null);
        assertThat(developer1).isNotEqualTo(developer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeveloperDTO.class);
        DeveloperDTO developerDTO1 = new DeveloperDTO();
        developerDTO1.setId(1L);
        DeveloperDTO developerDTO2 = new DeveloperDTO();
        assertThat(developerDTO1).isNotEqualTo(developerDTO2);
        developerDTO2.setId(developerDTO1.getId());
        assertThat(developerDTO1).isEqualTo(developerDTO2);
        developerDTO2.setId(2L);
        assertThat(developerDTO1).isNotEqualTo(developerDTO2);
        developerDTO1.setId(null);
        assertThat(developerDTO1).isNotEqualTo(developerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(developerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(developerMapper.fromId(null)).isNull();
    }
}
