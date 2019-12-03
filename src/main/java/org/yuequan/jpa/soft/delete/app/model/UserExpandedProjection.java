package org.yuequan.jpa.soft.delete.app.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Projection(name = "userExpanded", types = User.class)
@JsonInclude(value=Include.NON_EMPTY, content=Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface UserExpandedProjection {
	
	public Integer getId();

    public String getName();

    public String getUsername();

    public String getPassword();

	public Collection<Car> getCars();
	
	public Date getRemovedAt();

}
