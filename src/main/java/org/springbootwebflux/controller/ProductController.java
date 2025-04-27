package org.springbootwebflux.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbootwebflux.models.documents.Product;
import org.springbootwebflux.models.services.ProductServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.function.Consumer;

import static reactor.core.publisher.Flux.just;

@Controller
public class ProductController {
    private ProductServices productServices;

    public ProductController(ProductServices productServices) {
        this.productServices = productServices;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);


    @GetMapping({"/list", "/"})
    public Mono<String> lists(Model model) {
        Flux<Product> productFlux = productServices.findAllByToUpperCase();
        productFlux.subscribe(getProductConsumer());
        model.addAttribute("product", productFlux);
        model.addAttribute("title", "List of the products");
        return Mono.just("list");
    }

    @GetMapping("/form")
    public Mono<String> create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Form of the product");
        return Mono.just("Form");
    }

    @PostMapping("/form")
    public Mono<String> save(@Valid Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            model.addAttribute("title", "Errors in the form ");
            model.addAttribute("button", "guardar");
            return Mono.just("Form");
        } else {
            return productServices.save(product).doOnNext(
                    product1 -> LOGGER.info("Save the product {} ", product1.getId())).thenReturn("redirect:/list");
        }
    }


    @GetMapping("/form/{id}")
    public Mono<String> edith(@PathVariable String id, Model model) {


        Mono<Product> productMono = productServices.findById(id).doOnNext(product -> {

            LOGGER.info("Product : " + product.getName());
        }).defaultIfEmpty(new Product());

        model.addAttribute("title", "Edith product");
        model.addAttribute("product", productMono);
        return Mono.just("form");

    }

    @GetMapping("/formV2/{id}")
    public Mono<String> edithV2(@PathVariable String id, Model model) {

        return productServices.findById(id).doOnNext(product -> {
                    LOGGER.info("Product : " + product.getName());
                    model.addAttribute("title", "Edith product");
                    model.addAttribute("product", product);
                }).defaultIfEmpty(new Product())
                .flatMap(product -> {
                    if (product == null) {
                        return Mono.error(new InterruptedException("The product does not exist"));

                    }
                    return Mono.just(product);
                })
                .then(Mono.just("form"))
                .onErrorResume(ex -> Mono.just("redirect:/list?error=no+exist=product"));


    }


    @GetMapping("delete/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return productServices.findById(id)
                .defaultIfEmpty(new Product())
                .flatMap(product -> {
                    if (product == null) {
                        return Mono.error(new InterruptedException("The product does not exist"));

                    }
                    return Mono.just(product);
                })
                .flatMap(product -> {
                    LOGGER.info("Product To delete {}", product.getId());
                    return productServices.delete(product);
                }).then(Mono.just("redirect:/list?success=product+delete"));
    }

    @GetMapping("/list-data-driver")
    public String listDataDriver(Model model) {

        Flux<Product> productFlux = productServices.findAllByDelay();
        productFlux.subscribe(getProductConsumer());
        model.addAttribute("product", new ReactiveDataDriverContextVariable(productFlux, 2));
        model.addAttribute("title", "List of the products");
        return "list";
    }


    @GetMapping("/list-full")
    public String listFull(Model model) {
        Flux<Product> productFlux = productServices.findAllByRepeat();

        model.addAttribute("product", productFlux);
        model.addAttribute("title", "List of the products");
        return "list";
    }


    private static Consumer<Product> getProductConsumer() {
        return product -> LOGGER.info("Products {}", product.getName());
    }


//    @GetMapping("/list-chunked")
//    public String listChunked(Model model) {
//        Flux<Product> productFlux = productDAO.findAll().map(product -> {
//                    product.setName(product.getName().toUpperCase());
//                    return product;
//                }
//        ).repeat(5000);
//
//        model.addAttribute("product", productFlux);
//        model.addAttribute("title", "List of the products");
//        return "list_chunked";
//    }


}
