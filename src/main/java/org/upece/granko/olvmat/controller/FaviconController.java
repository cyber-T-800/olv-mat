package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FaviconController {

    @GetMapping("/favicon.ico")
    public String favicon() {
        return "redirect:/favicon.png";
    }
}
