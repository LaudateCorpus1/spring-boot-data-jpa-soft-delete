package org.yuequan.jpa.soft.delete;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.yuequan.jpa.soft.delete.app.model.Car;
import org.yuequan.jpa.soft.delete.app.model.User;
import org.yuequan.jpa.soft.delete.app.repository.CarRepository;
import org.yuequan.jpa.soft.delete.app.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@Profile("test")
@SpringBootTest
//@TestMethodOrder(OrderAnnotation.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SoftDeleteTest {
	
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarRepository carRepository;
    
    Integer userId = null;
    int createCarCount = 5;
    
    @Test
    //@Order(1)
    @Transactional
    public void aTestSoftDeleteUser() {
        User user = getUser();
        userRepository.save(user);
        userRepository.delete(user);
        Assert.assertEquals(userRepository.findById(user.getId()), Optional.empty());
        
        int createUserCount = 100;
        List<User> users = getUsers(createUserCount);
        userRepository.saveAll(users);
        Assert.assertEquals(createUserCount, userRepository.findAll().size());
        userRepository.deleteAllInBatch();
        Assert.assertEquals(0, userRepository.findAll().size());

        users = getUsers(createUserCount);
        userRepository.saveAll(users);
        users = userRepository.findAll();
        Assert.assertEquals(createUserCount, userRepository.findAll(PageRequest.of(0, createUserCount)).getTotalElements());
        userRepository.deleteInBatch(users);
        Assert.assertEquals(0, userRepository.findAll().size());
    }
    
    @Test
    //@Order(2)
    @Transactional
    public void bTestSoftDeleteCar() {
        User user = getUser();
        userRepository.save(user);
        Assert.assertNotEquals(userRepository.findById(user.getId()), Optional.empty());

        Collection<Car> cars = getCars(createCarCount, user);
        carRepository.saveAll(cars);
        user.setCars(cars);
        user = userRepository.save(user);
        Assert.assertEquals(createCarCount, carRepository.findAll().size());
        //user = userRepository.findById(user.getId()).orElse(null);
        Assert.assertEquals(createCarCount, user.getCars()!=null?user.getCars().size():0);
        
        Collection<Car> cars2Del = getCars(createCarCount, user);
        cars2Del = carRepository.saveAll(cars2Del);
        user.getCars().addAll(cars2Del);
        user = userRepository.save(user);
        cars = carRepository.findAll();
        Assert.assertEquals(createCarCount*2, cars!=null?cars.size():0);
        //user = userRepository.findById(user.getId()).orElse(null);
        Assert.assertEquals(createCarCount*2, user.getCars()!=null?user.getCars().size():0);
        
        Iterator<Car> itCar = cars2Del.stream().iterator();
        carRepository.deleteAll(() -> itCar);
        cars = carRepository.findAll();
        Assert.assertEquals(createCarCount, cars!=null?cars.size():0);
        User user2 = userRepository.findById(user.getId()).orElse(getUser());
        userId = user2.getId();
        cars = user2.getCars();
        Assert.assertEquals(createCarCount, cars!=null?cars.size():0);
        //System.out.println("Cars.size() = "+cars.size());
        
    }
    
//    @Test
//    //@Order(3)
//    @Transactional
//    public void cTestSoftDeleteCar2() {
//    	Collection<User> users = userRepository.findAll();
//    	Assert.assertNotEquals(null, users);
//    	//User user3 = userRepository.findById(userId).orElse(null);
//    	User user3 = users.stream().findFirst().orElse(null); 
//    	Assert.assertNotEquals(null, user3);
//    	
//    	Collection<Car> cars3 = user3.getCars();
//    	Assert.assertEquals(createCarCount, cars3!=null?cars3.size():0);
//    }

    private User getUser() {
        User user = new User();
        user.setUsername("Tester" + new Random().nextInt(1000));
        user.setName("Tester");
        user.setPassword("test");
        return user;
    }

    private List<User> getUsers(int createUserCount) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < createUserCount; i++) {
            users.add(getUser());
        }
        return users;
    }
    
    private Car getCar(User user) {
        Car car = new Car();
        car.setType("Tester" + new Random().nextInt(1000));
        car.setColor("Tester");
        car.setYear(ThreadLocalRandom.current().nextInt(1900, 2021));
        car.setUser(user);
        return car;
    }

    private List<Car> getCars(int createCarCount, User user) {
        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < createCarCount; i++) {
            cars.add(getCar(user));
        }
        return cars;
    }
    
}
