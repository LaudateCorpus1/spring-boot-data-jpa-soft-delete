package org.yuequan.jpa.soft.delete.app.model;

import javax.persistence.*;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Date;

@Entity
@Table(name = "cars")
@FilterDef(
    name="noRemovedCar"
//    ,
//    parameters = @ParamDef(
//        name="removed",
//        type="date"
//    )
)
@Filter(
    name="noRemovedCar",
    condition="(removed_at is null)"
    //condition="removed_at = :removed"
)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String type;
    private String color;
    private Integer year;
    
    @JsonBackReference
    @ManyToOne//(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "removed_at")
    private Date removedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Date getRemovedAt() {
		return removedAt;
	}

	public void setRemovedAt(Date removedAt) {
		this.removedAt = removedAt;
	}
    
}
