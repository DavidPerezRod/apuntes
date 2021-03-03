package com.zerti.utilities.log.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//TODO esta interfaz al ser genérica, también debería ir a utilities

@Repository
public interface LogRepository extends MongoRepository<LogUtilities, String> {
}
