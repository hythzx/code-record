package com.aiyun.code.record.service.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.aiyun.code.record.domain.Project} entity.
 */
@ApiModel(description = "项目")
public class ProjectDTO implements Serializable {

    private Long id;

    /**
     * 项目名称
     */
    @NotNull
    @Size(min = 0, max = 12)
    @ApiModelProperty(value = "项目名称", required = true)
    private String name;

    /**
     * 项目编号
     */
    @NotNull
    @Size(min = 0, max = 64)
    @ApiModelProperty(value = "项目编号", required = true)
    private String code;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Lob
    private String desc;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectDTO projectDTO = (ProjectDTO) o;
        if (projectDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
