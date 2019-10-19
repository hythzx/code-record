package com.aiyun.code.record.service;

import com.aiyun.code.record.service.dto.SubmitRecordDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.aiyun.code.record.domain.SubmitRecord}.
 */
public interface SubmitRecordService {

    /**
     * Save a submitRecord.
     *
     * @param submitRecordDTO the entity to save.
     * @return the persisted entity.
     */
    SubmitRecordDTO save(SubmitRecordDTO submitRecordDTO);

    /**
     * Get all the submitRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubmitRecordDTO> findAll(Pageable pageable);


    /**
     * Get the "id" submitRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubmitRecordDTO> findOne(Long id);

    /**
     * Delete the "id" submitRecord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the submitRecord corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubmitRecordDTO> search(String query, Pageable pageable);
}
