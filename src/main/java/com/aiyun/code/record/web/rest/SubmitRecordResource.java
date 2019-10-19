package com.aiyun.code.record.web.rest;

import com.aiyun.code.record.service.SubmitRecordService;
import com.aiyun.code.record.web.rest.errors.BadRequestAlertException;
import com.aiyun.code.record.service.dto.SubmitRecordDTO;

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
 * REST controller for managing {@link com.aiyun.code.record.domain.SubmitRecord}.
 */
@RestController
@RequestMapping("/api")
public class SubmitRecordResource {

    private final Logger log = LoggerFactory.getLogger(SubmitRecordResource.class);

    private static final String ENTITY_NAME = "submitRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubmitRecordService submitRecordService;

    public SubmitRecordResource(SubmitRecordService submitRecordService) {
        this.submitRecordService = submitRecordService;
    }

    /**
     * {@code POST  /submit-records} : Create a new submitRecord.
     *
     * @param submitRecordDTO the submitRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new submitRecordDTO, or with status {@code 400 (Bad Request)} if the submitRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/submit-records")
    public ResponseEntity<SubmitRecordDTO> createSubmitRecord(@Valid @RequestBody SubmitRecordDTO submitRecordDTO) throws URISyntaxException {
        log.debug("REST request to save SubmitRecord : {}", submitRecordDTO);
        if (submitRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new submitRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubmitRecordDTO result = submitRecordService.save(submitRecordDTO);
        return ResponseEntity.created(new URI("/api/submit-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /submit-records} : Updates an existing submitRecord.
     *
     * @param submitRecordDTO the submitRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submitRecordDTO,
     * or with status {@code 400 (Bad Request)} if the submitRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the submitRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/submit-records")
    public ResponseEntity<SubmitRecordDTO> updateSubmitRecord(@Valid @RequestBody SubmitRecordDTO submitRecordDTO) throws URISyntaxException {
        log.debug("REST request to update SubmitRecord : {}", submitRecordDTO);
        if (submitRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubmitRecordDTO result = submitRecordService.save(submitRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submitRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /submit-records} : get all the submitRecords.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submitRecords in body.
     */
    @GetMapping("/submit-records")
    public ResponseEntity<List<SubmitRecordDTO>> getAllSubmitRecords(Pageable pageable) {
        log.debug("REST request to get a page of SubmitRecords");
        Page<SubmitRecordDTO> page = submitRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /submit-records/:id} : get the "id" submitRecord.
     *
     * @param id the id of the submitRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the submitRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/submit-records/{id}")
    public ResponseEntity<SubmitRecordDTO> getSubmitRecord(@PathVariable Long id) {
        log.debug("REST request to get SubmitRecord : {}", id);
        Optional<SubmitRecordDTO> submitRecordDTO = submitRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(submitRecordDTO);
    }

    /**
     * {@code DELETE  /submit-records/:id} : delete the "id" submitRecord.
     *
     * @param id the id of the submitRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/submit-records/{id}")
    public ResponseEntity<Void> deleteSubmitRecord(@PathVariable Long id) {
        log.debug("REST request to delete SubmitRecord : {}", id);
        submitRecordService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/submit-records?query=:query} : search for the submitRecord corresponding
     * to the query.
     *
     * @param query the query of the submitRecord search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/submit-records")
    public ResponseEntity<List<SubmitRecordDTO>> searchSubmitRecords(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SubmitRecords for query {}", query);
        Page<SubmitRecordDTO> page = submitRecordService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
