package org.upece.granko.olvmat.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.upece.granko.olvmat.entity.EventEntity;
import org.upece.granko.olvmat.entity.ProductEntity;
import org.upece.granko.olvmat.repository.ProductRepository;
import org.upece.granko.olvmat.service.EventService;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProductSellingController {

    private EventService eventService;
    private ProductRepository productRepository;

    @GetMapping("/bar")
    public String getBarPage(ModelMap model) {
        EventEntity currentEvent = eventService.findSelected().orElseThrow();
        List<ProductEntity> productList = productRepository.findByEventAndType(currentEvent, "BAR");

        model.addAttribute("products", productList);
        model.addAttribute("title", "BAR");

        return "produkt/product";
    }

    @GetMapping("/jedlo")
    public String getJedloPage(ModelMap model) {
        EventEntity currentEvent = eventService.findSelected().orElseThrow();
        List<ProductEntity> productList = productRepository.findByEventAndType(currentEvent, "JEDLO");

        model.addAttribute("products", productList);
        model.addAttribute("title", "Občerstvenie");

        return "produkt/product";
    }

}
