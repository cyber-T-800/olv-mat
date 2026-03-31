package org.upece.granko.olvmat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.upece.granko.olvmat.model.DobrovolnikForm;

@Controller
public class DobrovolniciController {

    @GetMapping("dobrovolnici")
    public String getDobrovolnici() {
        return "dobrovolnici";
    }

    @PostMapping("dobrovolnici")
    public String odosliDobrovolnika(DobrovolnikForm form, ModelMap model) {

        if (form.getName() == null || form.getName().isBlank() ||
            form.getEmail() == null || form.getEmail().isBlank()) {
            model.put("error", "Meno a email sú povinné.");
            return "dobrovolnici";
        }

        // --- Zatiaľ len výpis do konzoly ---
        System.out.println("=== Nový dobrovoľník ===");
        System.out.println("Meno:       " + form.getName());
        System.out.println("Email:      " + form.getEmail());
        System.out.println("Dostupnosť: " + form.getAvailabilityList());
        System.out.println("Služby:     " + form.getServicesList());
        System.out.println("=======================");

        model.put("success", true);
        return "dobrovolnici";
    }
}