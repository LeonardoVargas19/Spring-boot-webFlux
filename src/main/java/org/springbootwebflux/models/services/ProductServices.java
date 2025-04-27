package org.springbootwebflux.models.services;

import org.springbootwebflux.models.documents.Category;
import org.springbootwebflux.models.documents.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductServices {

    Flux<Product> findAll();

    Flux<Product> findAllByToUpperCase();

    Flux<Product> findAllByDelay();

    Flux<Product> findAllByRepeat();


    Mono<Product> findById(String id);

    Mono<Product> save(Product product);

    Mono<Void> delete(Product product);

    Flux<Category> findAllCategory();

    Mono<Category> findByIdCategory(String id);

    Mono<Category> saveCategory(Category category);


}
