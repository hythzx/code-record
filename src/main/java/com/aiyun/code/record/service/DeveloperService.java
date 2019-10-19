package com.aiyun.code.record.service;

import com.aiyun.code.record.service.dto.DeveloperDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.aiyun.code.record.domain.Developer}.
 */
public interface DeveloperService {

    /**
     * Save a developer.
     *
     * @param developerDTO the entity to save.
     * @return the persisted entity.
     */
    DeveloperDTO save(DeveloperDTO developerDTO);

    /**
     * Get all the developers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeveloperDTO> findAll(Pageable pageable);


    /**
     * Get the "id" developer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeveloperDTO> findOne(Long id);

    /**
     * Delete the "id" developer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the developer corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeveloperDTO> search(String query, Pageable pageable);
}
