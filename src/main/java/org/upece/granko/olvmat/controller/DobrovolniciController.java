package org.upece.granko.olvmat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class DobrovolniciController {

    @GetMapping("dobrovolnici")
    public String getDobrovolnici(ModelMap model){
        model.addAttribute("bookings", new ArrayList<String>());
        return "dobrovolnici";
    }
}
