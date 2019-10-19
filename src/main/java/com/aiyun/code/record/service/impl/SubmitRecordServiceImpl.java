package com.aiyun.code.record.service.impl;

import com.aiyun.code.record.service.SubmitRecordService;
import com.aiyun.code.record.domain.SubmitRecord;
import com.aiyun.code.record.repository.SubmitRecordRepository;
import com.aiyun.code.record.repository.search.SubmitRecordSearchRepository;
import com.aiyun.code.record.service.dto.SubmitRecordDTO;
import com.aiyun.code.record.service.mapper.SubmitRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link SubmitRecord}.
 */
@Service
@Transactional
public class SubmitRecordServiceImpl implements SubmitRecordService {

    private final Logger log = LoggerFactory.getLogger(SubmitRecordServiceImpl.class);

    private final SubmitRecordRepository submitRecordRepository;

    private final SubmitRecordMapper submitRecordMapper;

    private final SubmitRecordSearchRepository submitRecordSearchRepository;

    public SubmitRecordServiceImpl(SubmitRecordRepository submitRecordRepository, SubmitRecordMapper submitRecordMapper, SubmitRecordSearchRepository submitRecordSearchRepository) {
        this.submitRecordRepository = submitRecordRepository;
        this.submitRecordMapper = submitRecordMapper;
        this.submitRecordSearchRepository = submitRecordSearchRepository;
    }

    /**
     * Save a submitRecord.
     *
     * @param submitRecordDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SubmitRecordDTO save(SubmitRecordDTO submitRecordDTO) {
        log.debug("Request to save SubmitRecord : {}", submitRecordDTO);
        SubmitRecord submitRecord = submitRecordMapper.toEntity(submitRecordDTO);
        submitRecord = submitRecordRepository.save(submitRecord);
        SubmitRecordDTO result = submitRecordMapper.toDto(submitRecord);
        submitRecordSearchRepository.save(submitRecord);
        return result;
    }

    /**
     * Get all the submitRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SubmitRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubmitRecords");
        return submitRecordRepository.findAll(pageable)
            .map(submitRecordMapper::toDto);
    }


    /**
     * Get one submitRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SubmitRecordDTO> findOne(Long id) {
        log.debug("Request to get SubmitRecord : {}", id);
        return submitRecordRepository.findById(id)
            .map(submitRecordMapper::toDto);
    }

    /**
     * Delete the submitRecord by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubmitRecord : {}", id);
        submitRecordRepository.deleteById(id);
        submitRecordSearchRepository.deleteById(id);
    }

    /**
     * Search for the submitRecord corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SubmitRecordDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SubmitRecords for query {}", query);
        return submitRecordSearchRepository.search(queryStringQuery(query), pageable)
            .map(submitRecordMapper::toDto);
    }
}
