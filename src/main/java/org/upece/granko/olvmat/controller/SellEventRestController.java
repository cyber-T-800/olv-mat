package org.upece.granko.olvmat.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.upece.granko.olvmat.entity.EventEntity;
import org.upece.granko.olvmat.entity.ProductEntity;
import org.upece.granko.olvmat.entity.SellEventEntity;
import org.upece.granko.olvmat.model.SellProductForm;
import org.upece.granko.olvmat.repository.ProductRepository;
import org.upece.granko.olvmat.repository.SellEventRepository;
import org.upece.granko.olvmat.service.EventService;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SellEventRestController {

    private EventService eventService;
    private ProductRepository productRepository;
    private SellEventRepository sellEventRepository;


    @PostMapping("/sell")
    public void postJedloProduct(@ModelAttribute SellProductForm sellProductForm) {
        EventEntity currentEvent = eventService.findSelected().orElseThrow();
        ProductEntity productEntity = productRepository.findById(sellProductForm.getId()).orElseThrow();

        sellEventRepository.save(new SellEventEntity(currentEvent, productEntity));
    }

}
