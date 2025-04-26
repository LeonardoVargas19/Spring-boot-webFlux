package org.springbootwebflux.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootwebflux.models.dao.ProductDAO;
import org.springbootwebflux.models.documents.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class ProductController {
    private ProductDAO productDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);


    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @GetMapping({"/list", "/"})
    public String lists(Model model) {
        Flux<Product> productFlux = productDAO.findAll().map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }
        );
        productFlux.subscribe(product -> LOGGER.info("Products {}", product.getName()));
        model.addAttribute("product", productFlux);
        model.addAttribute("title", "List of the products");
        return "list";
    }

    @GetMapping("/list-data-driver")
    public String listDataDriver(Model model) {

        Flux<Product> productFlux = productDAO.findAll().map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }
        ).delayElements(Duration.ofSeconds(1));
        productFlux.subscribe(product -> LOGGER.info("Products {}", product.getName()));
        model.addAttribute("product", new ReactiveDataDriverContextVariable(productFlux, 2));
        model.addAttribute("title", "List of the products");
        return "list";
    }


    @GetMapping("/list-full")
    public String listFull(Model model) {
        Flux<Product> productFlux = productDAO.findAll().map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }
        ).repeat(5000);

        model.addAttribute("product", productFlux);
        model.addAttribute("title", "List of the products");
        return "list";
    }

    @GetMapping("/list-chunked")
    public String listChunked(Model model) {
        Flux<Product> productFlux = productDAO.findAll().map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                }
        ).repeat(5000);

        model.addAttribute("product", productFlux);
        model.addAttribute("title", "List of the products");
        return "list_chunked";
    }


}
