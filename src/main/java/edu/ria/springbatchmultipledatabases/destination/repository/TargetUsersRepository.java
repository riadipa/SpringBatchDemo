package edu.ria.springbatchmultipledatabases.destination.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.ria.springbatchmultipledatabases.destination.model.TargetUsers;

@Repository
public interface TargetUsersRepository extends JpaRepository<TargetUsers, Integer> {

}
