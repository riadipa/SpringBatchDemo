package edu.ria.springbatchmultipledatabases.mongo.repository;

import edu.ria.springbatchmultipledatabases.mongo.model.MongoUsers;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUsersRepository extends MongoRepository<MongoUsers, Integer> {
}
