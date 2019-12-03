package org.yuequan.jpa.soft.delete.app.model;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Projection(name = "carExpanded", types = Car.class)
@JsonInclude(value=Include.NON_EMPTY, content=Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface CarExpandedProjection {
	
	public Integer getId();

	public String getType();

	public String getColor();

	public Integer getYear();

	public User getUser();
	
	public Date getRemovedAt();
	
}
