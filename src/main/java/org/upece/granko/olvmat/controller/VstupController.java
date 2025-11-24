package org.upece.granko.olvmat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vstup")
public class VstupController {

    @GetMapping("")
    public String getQrReader() {
        return "admin/qr_reader";
    }
}
