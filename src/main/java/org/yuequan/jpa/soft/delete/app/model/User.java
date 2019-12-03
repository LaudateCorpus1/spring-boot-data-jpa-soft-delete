package org.yuequan.jpa.soft.delete.app.model;

import javax.persistence.*;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users")
//@FilterDef(
//    name="noRemovedCar",
//    parameters = @ParamDef(
//        name="removed",
//        type="Date"
//    )
//)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String username;
    private String password;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "user")//, fetch = FetchType.EAGER)
    @Filter(
	    name="noRemovedCar",
	    condition="(removed_at is null)"
	    //condition="removed_at = :removed"
	)
    private Collection<Car> cars = new ArrayList<>();

    @Column(name = "removed_at")
    private Date removedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Collection<Car> getCars() {
		return cars;
	}

	public void setCars(Collection<Car> cars) {
		this.cars = cars;
	}
	
	public Date getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(Date removedAt) {
        this.removedAt = removedAt;
    }
    
}
