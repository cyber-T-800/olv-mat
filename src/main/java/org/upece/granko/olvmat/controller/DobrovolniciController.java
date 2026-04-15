package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.upece.granko.olvmat.entity.VolunteerEntity;
import org.upece.granko.olvmat.model.DobrovolnikForm;
import org.upece.granko.olvmat.repository.TicketRepository;
import org.upece.granko.olvmat.repository.VolunteerRepository;
import org.upece.granko.olvmat.service.EmailService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class DobrovolniciController {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final VolunteerRepository volunteerRepository;

    @GetMapping("dobrovolnici")
    public String getDobrovolnici(ModelMap model) {
        model.put("popisUdalosti", """
                    <h1>Dobrovoľníci</h1>
                
                        <p>
                            Ak sa chceš zapojiť do organizácie Majálesu,
                            vyplň formulár nižšie. Uveď svoje meno, e-mail, preferované služby
                            a v harmonograme vyklikaj časy, kedy si dostupný.
                        </p>
                        <div style="height:1.5rem;"></div>
                """);

        // rowspan 1 = 5 minút
        List<Map<String, Object>> program = List.of(
            Map.of("rowspan", 12, "color", "#6e8c8c", "text", "Registrácia"),
            Map.of("rowspan", 12, "color", "#5082d7", "text", "Sv. omša"),
            Map.of("rowspan",  2, "color", "#87bed2", "text", "Otvorenie"),
            Map.of("rowspan",  6, "color", "#32affa", "text", "Tanečné kolo"),
            Map.of("rowspan",  2, "color", "#fafafa", "text", ""),
            Map.of("rowspan",  6, "color", "#64affa", "text", "Škola tanca"),
            Map.of("rowspan",  6, "color", "#32affa", "text", "Tanečné kolo"),
            Map.of("rowspan",  2, "color", "#fafafa", "text", ""),
            Map.of("rowspan",  4, "color", "#a5bed2", "text", "Súťaže"),
            Map.of("rowspan",  6, "color", "#32affa", "text", "Tanečné kolo"),
            Map.of("rowspan",  3, "color", "#87bed2", "text", "Fotka"),
            Map.of("rowspan",  4, "color", "#a5bed2", "text", "Limbo"),
            Map.of("rowspan",  7, "color", "#32affa", "text", "Tanečné kolo"),
            Map.of("rowspan",  6, "color", "#87bed2", "text", "Tombola"),
            Map.of("rowspan", 42, "color", "#64affa", "text", "Voľná zábava")
            );

        model.put("program", program);
        model.put("startHour", 18);
        model.put("endHour", 4);
        model.put("blockRowspan", 24); // 12 = 1 hodina
        model.put("services", List.of("Bar", "Šatňa", "SBS", "Upratovanie", "Vstup", "Výdaj jedla", "Zmenáreň"));

        return "dobrovolnici";
    }

    @PostMapping("dobrovolnici")
    public String odosliDobrovolnika(DobrovolnikForm form, ModelMap model) {
        if (form.getName() == null || form.getEmail() == null || form.getAvailability() == null) {
            return "dobrovolnici";
        }


        StringBuilder avaibilityBuilder = new StringBuilder("");
        StringBuilder servicesBuilder = new StringBuilder("");

        form.getAvailability().forEach(it -> avaibilityBuilder.append(it).append(","));
        form.getServices().forEach(it -> servicesBuilder.append(it).append(","));

        VolunteerEntity entity = new VolunteerEntity(
                UUID.randomUUID(),
                form.getName(),
                form.getEmail(),
                form.getText(),
                avaibilityBuilder.toString(),
                servicesBuilder.toString()
        );

        volunteerRepository.save(entity);

        return "dobrovolnici-potvrdenie";
    }
}