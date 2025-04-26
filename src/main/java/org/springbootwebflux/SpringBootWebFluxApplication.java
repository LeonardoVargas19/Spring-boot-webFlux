package org.springbootwebflux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootwebflux.models.dao.ProductDAO;
import org.springbootwebflux.models.documents.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebFluxApplication implements CommandLineRunner {
    private ProductDAO productDAO;
    private ReactiveMongoTemplate mongoTemplate;

    public SpringBootWebFluxApplication(ProductDAO productDAO, ReactiveMongoTemplate mongoTemplate) {
        this.productDAO = productDAO;
        this.mongoTemplate = mongoTemplate;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootWebFluxApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebFluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.dropCollection("products").subscribe();
        Flux.just(
                        new Product("Nintendo Switch OLED", 349.99),
                        new Product("The Legend of Zelda: Breath of the Wild", 59.99),
                        new Product("Super Mario Odyssey", 49.99),
                        new Product("Mario Kart 8 Deluxe", 59.99),
                        new Product("Metroid Dread", 59.99),
                        new Product("Pokémon Scarlet", 59.99),
                        new Product("Splatoon 3", 59.99),
                        new Product("Animal Crossing: New Horizons", 49.99),
                        new Product("Nintendo Switch Pro Controller", 69.99),
                        new Product("Ring Fit Adventure", 79.99)

                )
                .flatMap(product -> {
                    product.setCreation(new Date());
                    return productDAO.save(product);

                })
                .subscribe(product -> LOGGER.info("Insert : {}", product));


    }
}
