package com.aiyun.code.record.web.rest;

import com.aiyun.code.record.service.DeveloperService;
import com.aiyun.code.record.web.rest.errors.BadRequestAlertException;
import com.aiyun.code.record.service.dto.DeveloperDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.aiyun.code.record.domain.Developer}.
 */
@RestController
@RequestMapping("/api")
public class DeveloperResource {

    private final Logger log = LoggerFactory.getLogger(DeveloperResource.class);

    private static final String ENTITY_NAME = "developer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeveloperService developerService;

    public DeveloperResource(DeveloperService developerService) {
        this.developerService = developerService;
    }

    /**
     * {@code POST  /developers} : Create a new developer.
     *
     * @param developerDTO the developerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new developerDTO, or with status {@code 400 (Bad Request)} if the developer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/developers")
    public ResponseEntity<DeveloperDTO> createDeveloper(@Valid @RequestBody DeveloperDTO developerDTO) throws URISyntaxException {
        log.debug("REST request to save Developer : {}", developerDTO);
        if (developerDTO.getId() != null) {
            throw new BadRequestAlertException("A new developer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeveloperDTO result = developerService.save(developerDTO);
        return ResponseEntity.created(new URI("/api/developers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /developers} : Updates an existing developer.
     *
     * @param developerDTO the developerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated developerDTO,
     * or with status {@code 400 (Bad Request)} if the developerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the developerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/developers")
    public ResponseEntity<DeveloperDTO> updateDeveloper(@Valid @RequestBody DeveloperDTO developerDTO) throws URISyntaxException {
        log.debug("REST request to update Developer : {}", developerDTO);
        if (developerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeveloperDTO result = developerService.save(developerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, developerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /developers} : get all the developers.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of developers in body.
     */
    @GetMapping("/developers")
    public ResponseEntity<List<DeveloperDTO>> getAllDevelopers(Pageable pageable) {
        log.debug("REST request to get a page of Developers");
        Page<DeveloperDTO> page = developerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /developers/:id} : get the "id" developer.
     *
     * @param id the id of the developerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the developerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/developers/{id}")
    public ResponseEntity<DeveloperDTO> getDeveloper(@PathVariable Long id) {
        log.debug("REST request to get Developer : {}", id);
        Optional<DeveloperDTO> developerDTO = developerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(developerDTO);
    }

    /**
     * {@code DELETE  /developers/:id} : delete the "id" developer.
     *
     * @param id the id of the developerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/developers/{id}")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        log.debug("REST request to delete Developer : {}", id);
        developerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/developers?query=:query} : search for the developer corresponding
     * to the query.
     *
     * @param query the query of the developer search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/developers")
    public ResponseEntity<List<DeveloperDTO>> searchDevelopers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Developers for query {}", query);
        Page<DeveloperDTO> page = developerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
