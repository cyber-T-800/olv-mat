package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upece.granko.olvmat.entity.EventEntity;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.model.ReservationForm;
import org.upece.granko.olvmat.repository.TicketRepository;
import org.upece.granko.olvmat.service.EmailService;
import org.upece.granko.olvmat.service.EventService;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final EventService eventService;

    private final int maxPocetListkov = 400;
    private final int lastListkyPocet = 25;


    @GetMapping("")
    public String rezervacia(ModelMap model) {
        String popis = """
                <h1>Rezervácia lístka na Majáles</h1>
                
                
                    <p>Ohniví muchachos, krásne señoritas,</p>
                    <div style="height:0.75rem;"></div>
                
                    <p>
                        Pozývame vás na horúci latino majáles plný rytmov, vášne a nezabudnuteľnej energie! 
                        Už <strong>30. apríla</strong> sa naše UPeCe rozozvučí v tónoch salsy, bachaty a latino hitov, ktoré ťa 
                        nenechajú sedieť. Čaká ťa večer plný tanca, zábavy a letnej atmosféry, kde sa necháš 
                        unášať hudbou a možno objavíš aj svoj nový obľúbený krok. 
                    </p>
                    <p> Tak si to nenechaj ujsť! </p>
                    <p>
                        Ak chceš byť pri tom, rezervuj si svoj lístok jednoducho <strong>vyplnením formulára</strong>. 
                        Následne prídeš do <strong>Libressa</strong>, ktorýkoľvek <strong>pracovný deň 30 minút po svätej omši</strong>, 
                        kde nám dáš svoj príspevok a my ti lístok aktivujeme.
                    </p>
                    <p> Príď sa naladiť na vlnu latina – bude to noc, ktorá ťa rozprúdi 💃🕺🔥 </p>
                    <div style="height:2.25rem;"></div>
                
                
                    <p><strong>Odporúčaný príspevok:</strong></p>
                
                    <pre>  -   9€  Študent</pre>
                    <pre>  -  18€  Neštudent</pre>
                    <div style="height:1.25rem;"></div>
                """;

        Optional<EventEntity> event = eventService.findSelected();
        if (event.isPresent()) {
            popis = event.get().getPopis();
            int pocetCelkovo = ticketRepository.countUcastnicke(event.get().getId());
            model.put("percentObsadene", 100.0f * pocetCelkovo / maxPocetListkov);

            if (maxPocetListkov - pocetCelkovo <= lastListkyPocet) {
                model.put("lastListky", maxPocetListkov - pocetCelkovo);
            }
        }

        model.put("popisUdalosti", popis);
        return "rezervacia";
    }

    @PostMapping("")
    public String odosliRezervaciu(ReservationForm form, ModelMap model, BindingResult bindingResult) {
        if (form.getName() == null || form.getEmail() == null) {
            return "redirect:/";
        }

        if (ticketRepository.findDuplicity(form.getEmail(), form.getName(), eventService.findSelected().orElseThrow().getId())) {
            model.addAttribute("duplicitaError", true);
            return rezervacia(model);
        }

        TicketEntity entity = ticketRepository.save(new TicketEntity(form.getName(), form.getEmail(), form.getTicketType(), eventService.findSelected().orElseThrow()));

        emailService.odosliListok(entity);

        model.put("ticketId", entity.getId());
        model.put("securityKey", entity.getSecurityKey());


        return "potvrdenie";
    }

    @GetMapping("/ticket/{id}")
    public String skontrolujRezervaciu(@PathVariable("id") String id, @RequestParam(value = "securityKey", required = false) String securityKey, ModelMap model) {
        if (id == null || id.isBlank() || securityKey == null || securityKey.isBlank()) {
            return "redirect:/";
        }
        try {
            TicketEntity entity = ticketRepository.findById(UUID.fromString(id)).orElseThrow();

            if (!entity.getSecurityKey().equals(UUID.fromString(securityKey))) {
                System.out.println(entity.getSecurityKey());
                System.out.println(UUID.fromString(securityKey));
                throw new RuntimeException();
            }

            model.put("meno", entity.getMeno());
            model.put("mail", entity.getEmail());
            model.put("typ", entity.getTypListka());
            model.put("stav", entity.getStav());

            return "stav-listka";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}
