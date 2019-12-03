package org.yuequan.jpa.soft.delete.app.controller;

import java.util.Date;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yuequan.jpa.soft.delete.app.model.User;
import org.yuequan.jpa.soft.delete.app.model.UserExpandedProjection;
import org.yuequan.jpa.soft.delete.app.repository.UserRepository;

@RestController
@RequestMapping(value="/users", produces = "application/hal+json")
public class UserController {
	
	@Autowired
    private EntityManager em;
	
	@Autowired
	private UserRepository userRepository;
	
	private final ProjectionFactory projectionFactory;
	
	@Autowired
	public UserController(ProjectionFactory projectionFactory) {
		this.projectionFactory = projectionFactory;
	}
	
	/*
	 * 
	 */
	@GetMapping
	public ResponseEntity<?> retrieveAll(@RequestParam(value = "projection", required=false) String projection, Pageable pageable) throws Exception {
		
		em.unwrap(Session.class)
	    .enableFilter("noRemovedCar");
	    
		if (projection==null||projection.isEmpty()) {
			Page<User> assets = userRepository.findAll(pageable);
			return ResponseEntity.ok(assets);
		} else if ("userExpanded".equals(projection)) {
			Page<UserExpandedProjection> itemsProjected = userRepository.findAll(pageable).map(item -> projectionFactory.createProjection(UserExpandedProjection.class, item));
			return ResponseEntity.ok(itemsProjected);
		} else {
			throw new Exception("Projection "+projection+" does not exist.");
		}
	}
	
	/*
	 * 
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> retrieve(@PathVariable(value = "id") int id, @RequestParam(value = "projection", required=false) String projection) throws Exception {
		em.unwrap(Session.class)
	    .enableFilter("noRemovedCar")
	    //.setParameter("removed", null)
	    ;
		
//		User user = em.find(User.class, id);
//		if (user!=null)
//			return new ResponseEntity<User>(user, HttpStatus.OK);//.orElseThrow(() -> new Exception("User with ID="+id+" does not found.")), HttpStatus.OK);
//		else
//			throw new Exception("User with ID="+id+" does not found.");
		
		if (projection==null||projection.isEmpty()) {
			return new ResponseEntity<User>(userRepository.findById(id).orElseThrow(() -> new Exception("User with ID="+id+" does not found.")), HttpStatus.OK);
		} else if ("userExpanded".equals(projection)) {
			UserExpandedProjection itemProjected = projectionFactory.createProjection(UserExpandedProjection.class, userRepository.findById(id).orElseThrow(() -> new Exception("User with ID="+id+" was not found.")));
			return ResponseEntity.ok(itemProjected);
		} else {
			throw new Exception("Projection "+projection+" does not exist.");
		}
		
	}
	
	/*
	 * 
	 */
	@PostMapping
	public ResponseEntity<User> create(@RequestBody User user) {
		return new ResponseEntity<User>(userRepository.save(user), HttpStatus.CREATED);
	}
	
	/*
	 * 
	 */
	@PutMapping("/{id}")
	public ResponseEntity<User> update(@PathVariable(value = "id") int id, @RequestBody User user) {
		user.setId(id);
	    return ResponseEntity.ok(userRepository.save(user));
	}
	
	/*
	 * 
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<User> patch(@PathVariable(value = "id") int id, @RequestBody User user) {
		user.setId(id);
	    return ResponseEntity.ok(userRepository.save(user));
	}
	
	/*
	 * 
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<User> delete(@PathVariable(value = "id") int id) throws Exception {
		User user = userRepository.findById(id).orElseThrow(() -> new Exception("User with ID="+id+" was not found."));
		userRepository.delete(user);
	    return new ResponseEntity<User>(HttpStatus.OK);
	}
	
}
