package org.springbootwebflux.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootwebflux.models.dao.ProductDAO;
import org.springbootwebflux.models.documents.Product;
import org.springbootwebflux.models.services.ProductServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductRestController {
    private ProductServices productServices;

    public ProductRestController(ProductServices productServices) {
        this.productServices = productServices;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);


    @GetMapping("/")
    public Flux<Product> index() {
        return productServices.findAll().map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }
        ).doOnNext(product -> LOGGER.info(product.getName()));
    }

    @GetMapping("/{id}")
    public Mono<Product> findById(@PathVariable String id) {
        //Mono<Product> productFlux = productDAO.findById(id);
        Flux<Product> productFlux = productServices.findAll();

        return productFlux.filter(product -> product.getId().equals(id))
                .next()
                .doOnNext(product -> LOGGER.info(product.getName()));
    }

}
