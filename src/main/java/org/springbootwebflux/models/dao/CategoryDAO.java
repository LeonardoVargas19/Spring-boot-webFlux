package org.springbootwebflux.models.dao;

import org.springbootwebflux.models.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryDAO extends ReactiveMongoRepository<Category, String> {
}
