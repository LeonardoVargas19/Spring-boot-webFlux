package org.springbootwebflux.models.services;

import org.springbootwebflux.models.dao.CategoryDAO;
import org.springbootwebflux.models.dao.ProductDAO;
import org.springbootwebflux.models.documents.Category;
import org.springbootwebflux.models.documents.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ProductServicesImp implements ProductServices {
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public ProductServicesImp(ProductDAO productDAO, CategoryDAO categoryDAO) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
    }

    @Override
    public Flux<Product> findAll() {
        return productDAO.findAll();
    }

    @Override
    public Flux<Product> findAllByToUpperCase() {
        return productDAO.findAll().map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }
        );
    }

    @Override
    public Flux<Product> findAllByDelay() {
        return productDAO.findAll().map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }
        ).delayElements(Duration.ofSeconds(1));
    }

    @Override
    public Flux<Product> findAllByRepeat() {
        return productDAO.findAll().map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }
        ).repeat(5000);
    }


    @Override
    public Mono<Product> findById(String id) {
        return productDAO.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        return productDAO.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return productDAO.delete(product);
    }

    @Override
    public Flux<Category> findAllCategory() {
        return categoryDAO.findAll();
    }

    @Override
    public Mono<Category> findByIdCategory(String id) {
        return categoryDAO.findById(id);
    }

    @Override
    public Mono<Category> saveCategory(Category category) {
        return categoryDAO.save(category);
    }
}
