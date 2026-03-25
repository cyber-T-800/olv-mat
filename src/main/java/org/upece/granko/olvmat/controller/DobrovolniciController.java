package org.upece.granko.olvmat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DobrovolniciController {

    @GetMapping("dobrovolnici")
    public String getDobrovolnici(){
        return "dobrovolnici";
    }
}
