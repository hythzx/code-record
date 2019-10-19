package com.aiyun.code.record.service.impl;

import com.aiyun.code.record.service.DeveloperService;
import com.aiyun.code.record.domain.Developer;
import com.aiyun.code.record.repository.DeveloperRepository;
import com.aiyun.code.record.repository.search.DeveloperSearchRepository;
import com.aiyun.code.record.service.dto.DeveloperDTO;
import com.aiyun.code.record.service.mapper.DeveloperMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Developer}.
 */
@Service
@Transactional
public class DeveloperServiceImpl implements DeveloperService {

    private final Logger log = LoggerFactory.getLogger(DeveloperServiceImpl.class);

    private final DeveloperRepository developerRepository;

    private final DeveloperMapper developerMapper;

    private final DeveloperSearchRepository developerSearchRepository;

    public DeveloperServiceImpl(DeveloperRepository developerRepository, DeveloperMapper developerMapper, DeveloperSearchRepository developerSearchRepository) {
        this.developerRepository = developerRepository;
        this.developerMapper = developerMapper;
        this.developerSearchRepository = developerSearchRepository;
    }

    /**
     * Save a developer.
     *
     * @param developerDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DeveloperDTO save(DeveloperDTO developerDTO) {
        log.debug("Request to save Developer : {}", developerDTO);
        Developer developer = developerMapper.toEntity(developerDTO);
        developer = developerRepository.save(developer);
        DeveloperDTO result = developerMapper.toDto(developer);
        developerSearchRepository.save(developer);
        return result;
    }

    /**
     * Get all the developers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeveloperDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Developers");
        return developerRepository.findAll(pageable)
            .map(developerMapper::toDto);
    }


    /**
     * Get one developer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DeveloperDTO> findOne(Long id) {
        log.debug("Request to get Developer : {}", id);
        return developerRepository.findById(id)
            .map(developerMapper::toDto);
    }

    /**
     * Delete the developer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Developer : {}", id);
        developerRepository.deleteById(id);
        developerSearchRepository.deleteById(id);
    }

    /**
     * Search for the developer corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeveloperDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Developers for query {}", query);
        return developerSearchRepository.search(queryStringQuery(query), pageable)
            .map(developerMapper::toDto);
    }
}
