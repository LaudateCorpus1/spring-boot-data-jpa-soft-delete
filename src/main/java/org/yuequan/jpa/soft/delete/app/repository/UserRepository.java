package org.yuequan.jpa.soft.delete.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yuequan.jpa.soft.delete.app.model.User;
import org.yuequan.jpa.soft.delete.repository.SoftDelete;

@SoftDelete
public interface UserRepository extends JpaRepository<User, Integer> {

}
