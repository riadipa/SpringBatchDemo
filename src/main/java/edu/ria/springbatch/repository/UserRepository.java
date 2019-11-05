package edu.ria.springbatch.repository;

import edu.ria.springbatch.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {

}
