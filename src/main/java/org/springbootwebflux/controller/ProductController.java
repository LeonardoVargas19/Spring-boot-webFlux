package org.springbootwebflux.controller;


import org.springbootwebflux.models.dao.ProductDAO;
import org.springbootwebflux.models.documents.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@Controller
public class ProductController {
    private ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @GetMapping({"/list", "/"})
    public String lists(Model model) {
        Flux<Product> productFlux = productDAO.findAll();
        model.addAttribute("product", productFlux);
        model.addAttribute("title", "List of the products");
        return "list";
    }
}
