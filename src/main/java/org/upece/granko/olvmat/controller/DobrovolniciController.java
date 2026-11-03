package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.upece.granko.olvmat.entity.ScheduleItemEntity;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.entity.VolunteerEntity;
import org.upece.granko.olvmat.entity.enums.TypListkaEnum;
import org.upece.granko.olvmat.entity.enums.VolunteerStavEnum;
import org.upece.granko.olvmat.model.DobrovolnikForm;
import org.upece.granko.olvmat.model.ScheduleItem;
import org.upece.granko.olvmat.repository.ScheduleItemRepository;
import org.upece.granko.olvmat.repository.TicketRepository;
import org.upece.granko.olvmat.repository.VolunteerRepository;
import org.upece.granko.olvmat.service.EmailService;
import org.upece.granko.olvmat.service.EventService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class DobrovolniciController {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final EventService eventService;
    private final VolunteerRepository volunteerRepository;
    private final ScheduleItemRepository scheduleItemRepository;

    @GetMapping("dobrovolnici")
    public String getDobrovolnici(ModelMap model) {
        model.put("popisUdalosti", """
                    <h1>Dobrovoľníci</h1>
                
                        <p>
                            Ak sa chceš zapojiť do organizácie OĽV,
                            vyplň formulár nižšie. Uveď svoje meno, e-mail, preferované služby
                            a v harmonograme vyklikaj časy, kedy si dostupný.
                        </p>
                        <div style="height:1.5rem;"></div>
                """);

        List<ScheduleItemEntity> programEntity = scheduleItemRepository.findAllByEventIdOrderByOrder(eventService.findSelected().orElseThrow().getId());

        List<ScheduleItem> program = programEntity.stream().map(it -> new ScheduleItem(it.getLength() / 5, it.getColor(), it.getText())).toList();

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
                servicesBuilder.toString(),
                true,
                eventService.findSelected().orElseThrow().getId(),
                VolunteerStavEnum.AKTIVNY
        );

        TicketEntity ticket = new TicketEntity(form.getName(), form.getEmail(), TypListkaEnum.DOBROVOLNIK, eventService.findSelected().orElseThrow());

        ticketRepository.save(ticket);
        volunteerRepository.save(entity);

        emailService.odosliListok(ticket);

        return "dobrovolnici-potvrdenie";
    }
}