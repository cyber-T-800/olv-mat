package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.upece.granko.olvmat.model.DobrovolnikForm;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DobrovolniciController {

    @GetMapping("dobrovolnici")
    public String getDobrovolnici(ModelMap model) {
        List<Map<String, Object>> program = List.of(
            Map.of("rowspan", 12, "color", "#fa0000", "text", "Registrácia"),
            Map.of("rowspan", 12, "color", "#00fa00", "text", "Sv. omša"),
            Map.of("rowspan",  3, "color", "#00fafa", "text", "Otvorenie"),
            Map.of("rowspan",  7, "color", "#fa00fa", "text", "Tanečné kolo"),
            Map.of("rowspan",  2, "color", "#fafafa", "text", ""),
            Map.of("rowspan",  4, "color", "#00fafa", "text", "Divadlo"),
            Map.of("rowspan",  8, "color", "#fa00fa", "text", "Tanečné kolo"),
            Map.of("rowspan",  2, "color", "#fafafa", "text", ""),
            Map.of("rowspan",  6, "color", "#fa00fa", "text", "Škola tanca"),
            Map.of("rowspan",  8, "color", "#fa00fa", "text", "Tanečné kolo"),
            Map.of("rowspan",  2, "color", "#fafafa", "text", ""),
            Map.of("rowspan",  4, "color", "#00fafa", "text", "Súťaže"),
            Map.of("rowspan",  8, "color", "#fa00fa", "text", "Tanečné kolo"),
            Map.of("rowspan",  3, "color", "#00fafa", "text", "Fotka"),
            Map.of("rowspan",  6, "color", "#00fafa", "text", "Vatra"),
            Map.of("rowspan",  9, "color", "#fa00fa", "text", "Tanečné kolo")
        );

        model.put("program", program);
        model.put("startHour", 18);
        model.put("endHour", 2);
        model.put("blockRowspan", 24); // 12 = 1 hodina
        model.put("services", List.of("Bar", "Šatňa", "SBS", "Upratovanie", "Vstup", "Výdaj jedla", "Zmenáreň"));

        return "dobrovolnici";
    }

    @PostMapping("dobrovolnici")
    public String odosliDobrovolnika(DobrovolnikForm form, ModelMap model) {

        if (form.getName() == null || form.getName().isBlank() ||
            form.getEmail() == null || form.getEmail().isBlank() ||
            form.getAvailabilityList() == null || form.getAvailabilityList().isBlank()) {
            model.put("error", "Meno, email a dostupnosť sú povinné.");
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