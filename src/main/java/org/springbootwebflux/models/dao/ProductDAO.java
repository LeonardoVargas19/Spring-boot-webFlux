package org.springbootwebflux.models.dao;

import org.springbootwebflux.models.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductDAO extends ReactiveMongoRepository<Product,String> {

}
