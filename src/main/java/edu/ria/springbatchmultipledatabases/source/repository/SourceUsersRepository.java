package edu.ria.springbatchmultipledatabases.source.repository;

import edu.ria.springbatchmultipledatabases.source.model.SourceUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceUsersRepository extends JpaRepository<SourceUsers, Integer> {

}
