package org.springbootwebflux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootwebflux.models.documents.Category;
import org.springbootwebflux.models.documents.Product;
import org.springbootwebflux.models.services.ProductServices;
import org.springbootwebflux.models.services.ProductServicesImp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebFluxApplication implements CommandLineRunner {
    private ProductServices productServicesImp;
    private ReactiveMongoTemplate mongoTemplate;

    public SpringBootWebFluxApplication(ProductServicesImp productServicesImp, ReactiveMongoTemplate mongoTemplate) {
        this.productServicesImp = productServicesImp;
        this.mongoTemplate = mongoTemplate;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootWebFluxApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebFluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.dropCollection("products").subscribe();
        mongoTemplate.dropCollection("category").subscribe();
        Category electronic = new Category("Electronic");
        Category toys = new Category("Toys");
        Category furniture = new Category("Furniture");
        Flux.just(electronic, toys, furniture)
                .flatMap(category -> productServicesImp.saveCategory(category)).doOnNext(category ->
                        LOGGER.info("Create Category {}", category.getName())

                ).thenMany(Flux.just(
                                // Categoría Electronics
                                new Product("Nintendo Switch OLED", 349.99, electronic),
                                new Product("PlayStation 5", 499.99, electronic),
                                new Product("Xbox Series X", 479.99, electronic),
                                new Product("Steam Deck", 399.99, electronic),
                                new Product("Gaming PC", 1199.99, electronic),

// Categoría Toys
                                new Product("LEGO Star Wars Set", 89.99, toys),
                                new Product("Hot Wheels Mega Track", 59.99, toys),
                                new Product("NERF Blaster Elite", 49.99, toys),
                                new Product("Barbie Dreamhouse", 179.99, toys),
                                new Product("Play-Doh Kitchen Creations", 29.99, toys),

// Categoría Furniture
                                new Product("Gaming Chair", 229.99, furniture),
                                new Product("Office Desk", 199.99, furniture),
                                new Product("Bookshelf Modern", 129.99, furniture),
                                new Product("TV Stand", 89.99, furniture),
                                new Product("Bed Frame Queen Size", 499.99, furniture)
                        )
                        .flatMap(product -> {
                            product.setCreation(new Date());
                            return productServicesImp.save(product);

                        })).subscribe(product -> LOGGER.info("Insert : {}", product));


    }
}
