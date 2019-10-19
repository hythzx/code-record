package com.aiyun.code.record.service.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aiyun.code.record.domain.SubmitRecord} entity.
 */
@ApiModel(description = "提交记录")
public class SubmitRecordDTO implements Serializable {

    private Long id;

    /**
     * 记录时间
     */
    @NotNull
    @ApiModelProperty(value = "记录时间", required = true)
    private LocalDate recordDate;

    /**
     * 代码行数
     */
    @NotNull
    @ApiModelProperty(value = "代码行数", required = true)
    private Integer cnt;


    private Long developerId;

    private String developerName;

    private Long projectId;

    private String projectName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Long getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(Long developerId) {
        this.developerId = developerId;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SubmitRecordDTO submitRecordDTO = (SubmitRecordDTO) o;
        if (submitRecordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), submitRecordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubmitRecordDTO{" +
            "id=" + getId() +
            ", recordDate='" + getRecordDate() + "'" +
            ", cnt=" + getCnt() +
            ", developer=" + getDeveloperId() +
            ", developer='" + getDeveloperName() + "'" +
            ", project=" + getProjectId() +
            ", project='" + getProjectName() + "'" +
            "}";
    }
}
