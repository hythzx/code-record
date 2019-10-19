package com.aiyun.code.record.service.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aiyun.code.record.domain.Developer} entity.
 */
@ApiModel(description = "开发人员")
public class DeveloperDTO implements Serializable {

    private Long id;

    /**
     * 开发人员姓名
     */
    @NotNull
    @Size(min = 0, max = 12)
    @ApiModelProperty(value = "开发人员姓名", required = true)
    private String name;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeveloperDTO developerDTO = (DeveloperDTO) o;
        if (developerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), developerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeveloperDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
