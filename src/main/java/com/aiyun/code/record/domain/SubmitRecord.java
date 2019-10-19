package com.aiyun.code.record.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 提交记录
 */
@Entity
@Table(name = "submit_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "submitrecord")
public class SubmitRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    /**
     * 记录时间
     */
    @NotNull
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    /**
     * 代码行数
     */
    @NotNull
    @Column(name = "cnt", nullable = false)
    private Integer cnt;

    @ManyToOne
    @JsonIgnoreProperties("submitRecords")
    private Developer developer;

    @ManyToOne
    @JsonIgnoreProperties("submitRecords")
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public SubmitRecord recordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
        return this;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getCnt() {
        return cnt;
    }

    public SubmitRecord cnt(Integer cnt) {
        this.cnt = cnt;
        return this;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public SubmitRecord developer(Developer developer) {
        this.developer = developer;
        return this;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public Project getProject() {
        return project;
    }

    public SubmitRecord project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubmitRecord)) {
            return false;
        }
        return id != null && id.equals(((SubmitRecord) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubmitRecord{" +
            "id=" + getId() +
            ", recordDate='" + getRecordDate() + "'" +
            ", cnt=" + getCnt() +
            "}";
    }
}
