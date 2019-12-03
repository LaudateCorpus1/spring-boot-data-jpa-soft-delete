package org.yuequan.jpa.soft.delete.app.controller;

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
import org.yuequan.jpa.soft.delete.app.model.Car;
import org.yuequan.jpa.soft.delete.app.model.CarExpandedProjection;
import org.yuequan.jpa.soft.delete.app.repository.CarRepository;

@RestController
@RequestMapping(value="/cars", produces = "application/hal+json")
public class CarController {
	
	private final ProjectionFactory projectionFactory;
	
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	public CarController(ProjectionFactory projectionFactory) {
		this.projectionFactory = projectionFactory;
	}
	
	/*
	 * 
	 */
	@GetMapping
	public ResponseEntity<?> retrieveAll(@RequestParam(value = "projection", required=false) String projection, Pageable pageable) throws Exception {
		if (projection==null||projection.isEmpty()) {
			Page<Car> assets = carRepository.findAll(pageable);
			return ResponseEntity.ok(assets);
		} else if ("carExpanded".equals(projection)) {
			Page<CarExpandedProjection> itemsProjected = carRepository.findAll(pageable).map(item -> projectionFactory.createProjection(CarExpandedProjection.class, item));
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
		if (projection==null||projection.isEmpty()) {
			return new ResponseEntity<Car>(carRepository.findById(id).orElseThrow(() -> new Exception("Car with ID="+id+" does not found.")), HttpStatus.OK);
		} else if ("carExpanded".equals(projection)) {
			CarExpandedProjection itemProjected = projectionFactory.createProjection(CarExpandedProjection.class, carRepository.findById(id).orElseThrow(() -> new Exception("Car with ID="+id+" was not found.")));
			return ResponseEntity.ok(itemProjected);
		} else {
			throw new Exception("Projection "+projection+" does not exist.");
		}
	}
	
	/*
	 * 
	 */
	@PostMapping
	public ResponseEntity<Car> create(@RequestBody Car car) {
		return new ResponseEntity<Car>(carRepository.save(car), HttpStatus.CREATED);
	}
	
	/*
	 * 
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Car> update(@PathVariable(value = "id") int id, @RequestBody Car car) {
		car.setId(id);
	    return ResponseEntity.ok(carRepository.save(car));
	}
	
	/*
	 * 
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<Car> patch(@PathVariable(value = "id") int id, @RequestBody Car car) {
		car.setId(id);
	    return ResponseEntity.ok(carRepository.save(car));
	}
	
	/*
	 * 
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Car> delete(@PathVariable(value = "id") int id) throws Exception {
		Car car = carRepository.findById(id).orElseThrow(() -> new Exception("Car with ID="+id+" was not found."));
		carRepository.delete(car);
	    return new ResponseEntity<Car>(HttpStatus.OK);
	}
	
}
