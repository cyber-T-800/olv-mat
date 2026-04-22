package org.upece.granko.olvmat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.upece.granko.olvmat.entity.AdminEntity;
import org.upece.granko.olvmat.entity.AdminRegistraciaZiadostEntity;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.entity.VolunteerEntity;
import org.upece.granko.olvmat.entity.enums.*;
import org.upece.granko.olvmat.model.AdminDetails;
import org.upece.granko.olvmat.model.EventEditForm;
import org.upece.granko.olvmat.repository.AdminRegistraciaZiadostRepository;
import org.upece.granko.olvmat.repository.AdminRepository;
import org.upece.granko.olvmat.repository.TicketRepository;
import org.upece.granko.olvmat.repository.VolunteerRepository;
import org.upece.granko.olvmat.service.AdminDetailService;
import org.upece.granko.olvmat.service.EmailService;
import org.upece.granko.olvmat.service.EventService;
import org.upece.granko.olvmat.service.VytvorenieHeslaSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final EmailService emailService;
    private final AdminRegistraciaZiadostRepository adminRegistraciaZiadostRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final VytvorenieHeslaSession vytvorenieHeslaSession;
    private final TicketRepository ticketRepository;
    private final EventService eventService;
    private final AdminDetailService adminDetailService;
    private final VolunteerRepository volunteerRepository;

    @Value("${super.admin.email}")
    private String superAdminEmail;
    @Value("${hostport}")
    private String hostport;

    private final int maxPocetListkov = 400;

    @GetMapping("/admin/login")
    public String getLogin() {
        return "admin/login";
    }


    @GetMapping("/admin/claim-super")
    public String claimSuper() {
        if (adminRepository.vacantSuperadminPosition()) {
            AdminEntity entity = adminRepository.findById(((AdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAdminEntity().getId()).orElseThrow();

            entity.setRola(AdminRoleEnum.SUPERADMIN);

            adminRepository.save(entity);

            return "redirect:/superadmin";
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String getAdminPage(ModelMap model) {
        if (adminRepository.vacantSuperadminPosition()) {
            model.addAttribute("vacantSuperadminPosition", true);
        }
        return renderPage("admin/admin", model);
    }

    @PostMapping("/admin")
    public String nahrajListky(@Param("subor") MultipartFile subor) throws IOException {
        TypListkaEnum typListka = TypListkaEnum.TEAM;

        String inputString = new String(subor.getBytes(), StandardCharsets.UTF_8);
        String[] lines = inputString.split("\n");
        for (String line : lines) {
            String[] values = line.split(";");
            String email = values[2].trim();
            String meno = values[0].trim();
            String priezvisko = values[1].trim();

            TicketEntity entity = ticketRepository.save(new TicketEntity(meno + " " + priezvisko, email, typListka, eventService.findSelected().orElseThrow()));

            Map<String, Object> mailModel = new HashMap<>();
            mailModel.put("krstneMeno", meno);
            mailModel.put("ticketId", entity.getId());
            mailModel.put("securityKey", entity.getSecurityKey());
            if (typListka == TypListkaEnum.DOBROVOLNIK) {
                mailModel.put("dobrovolnik", true);
            }
            emailService.sendMail(email, "Potvrdenie rezervácie listka", "potvrdenie-rezervacie", mailModel);

        }
        return "redirect:/admin";
    }

    @GetMapping("/admin-ziadost/{id}/vytvorenie-hesla")
    public String vytvorenieHesla(
            @PathVariable("id") UUID id,
            @RequestParam(value = "secret") UUID secret,
            @RequestParam(value = "em") String email
    ) {
        AdminRegistraciaZiadostEntity entity = adminRegistraciaZiadostRepository.findById(id).orElseThrow();
        if (entity.getSecret().equals(secret) && entity.getEmail().equals(email)) {
            if (entity.getStav() == AdminRegistraciaZiadostStavEnum.POTVRDENA) {
                vytvorenieHeslaSession.setSecret(secret);
                vytvorenieHeslaSession.setEmail(email);

                return "admin/vytvorenie-hesla";
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
    }

    @PostMapping("/admin-ziadost/{id}/vytvorenie-hesla")
    public String vytvorenieUlozit(@PathVariable("id") UUID id, @Param("password") String password) {
        AdminRegistraciaZiadostEntity entity = adminRegistraciaZiadostRepository.findById(id).orElseThrow();
        if (entity.getSecret().equals(vytvorenieHeslaSession.getSecret()) && entity.getEmail().equals(vytvorenieHeslaSession.getEmail())) {
            if (entity.getStav() == AdminRegistraciaZiadostStavEnum.POTVRDENA) {
                AdminEntity admin = new AdminEntity(vytvorenieHeslaSession.getEmail(), passwordEncoder.encode(password), entity.getRola());
                adminRepository.save(admin);
                entity.setStav(AdminRegistraciaZiadostStavEnum.VYBAVENA);
                adminRegistraciaZiadostRepository.save(entity);
                return "admin/vytvorenie-uctu-potvrdenie";
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
    }

    @GetMapping("/admin/events")
    public String getEventManagement(ModelMap model) {
        model.addAttribute("events", eventService.findAll());
        return "admin/event-management";
    }

    @GetMapping("/admin/event")
    public String getEvent() {
        return "admin/event";
    }

    @PostMapping("/admin/event")
    public String saveEvent(EventEditForm eventEditForm) {
        eventService.save(eventEditForm);

        System.out.println(eventEditForm);
        return "redirect:/admin/events";
    }

    @PostMapping("/admin/zrus-rezervaciu")
    public String zrusRezervaciu(@Param("id") UUID id) {
        TicketEntity entity = ticketRepository.findById(id).orElseThrow();
        entity.setStav(StavRezervacieEnum.ZRUSENY);
        ticketRepository.save(entity);
        return "redirect:/admin";
    }

    @PostMapping("/admin/zmen-typ-listka")
    public String zmenTypListkaRezervaciu(@Param("id") UUID id, @Param("typ") TypListkaEnum typ) {
        TicketEntity entity = ticketRepository.findById(id).orElseThrow();
        entity.setTypListka(typ);
        ticketRepository.save(entity);

        return "redirect:/admin";
    }

    @GetMapping("/admin/volunteers")
    public String volunteers(ModelMap model,
                             @RequestHeader(required = false, defaultValue = "false", name = "HX-request") boolean hxRequest,
                             @RequestParam(required = false, defaultValue = "false") boolean zrusene,
                             @RequestParam(required = false, defaultValue = "false") boolean zaradene,
                             @RequestParam(required = false) String search) {

        if (hxRequest) {
            List<VolunteerStavEnum> stavy = new ArrayList<>(List.of(VolunteerStavEnum.AKTIVNY));
            if (zrusene)
                stavy.add(VolunteerStavEnum.ZRUSENY);
            if (zaradene)
                stavy.add(VolunteerStavEnum.ZARADENY);
            List<VolunteerEntity> entities = volunteerRepository.findAllByNameAndStav(search, stavy);


            entities.forEach(it -> {
                it.setServices(it.getServices().replace(",", "<br>"));
                it.setAvailability(Arrays.stream(it.getAvailability()
                                .split(","))
                        .map(st -> {
                            int time = Integer.parseInt(st);
                            return String.format("%d - %d", time, time + 2);
                        }).collect(Collectors.joining("<br>"))
                );
            });

            model.addAttribute("volunteers", entities);
        }
        if (hxRequest) {
            return renderPage("admin/volunteers :: volunteers", model);
        } else {
            return renderPage("admin/volunteers", model);
        }
    }

    @GetMapping("/admin/volunteers/cancel/{id}")
    public String zrusRezervaciuDobrovolnika(@PathVariable UUID id, ModelMap model, boolean zrusene, boolean zaradene, String search) {
        VolunteerEntity entity = volunteerRepository.findById(id).orElseThrow();
        entity.setStav(VolunteerStavEnum.ZRUSENY);
        volunteerRepository.save(entity);
        return volunteers(model, true, zrusene, zaradene, search);
    }

    @GetMapping("/admin/volunteers/zarad/{id}")
    public String zaradDobrovolnika(@PathVariable UUID id, ModelMap model, boolean zrusene, boolean zaradene, String search) {
        VolunteerEntity entity = volunteerRepository.findById(id).orElseThrow();
        entity.setStav(VolunteerStavEnum.ZARADENY);
        volunteerRepository.save(entity);
        return volunteers(model, true, zrusene, zaradene, search);
    }

    @GetMapping("/admin/volunteers/revire/{id}")
    public String obnovListok(@PathVariable UUID id, ModelMap model, boolean zrusene, boolean zaradene, String search) {
        VolunteerEntity entity = volunteerRepository.findById(id).orElseThrow();
        entity.setStav(VolunteerStavEnum.AKTIVNY);
        volunteerRepository.save(entity);
        return volunteers(model, true, zrusene, zaradene, search);
    }


    @GetMapping("/admin/volunteers/send-neodoslane")
    public String sendNeodoslaneDobrovolnickeListky() {
        List<VolunteerEntity> entities = volunteerRepository.findEmailNotSend();
        List<TicketEntity> tickets = entities.stream().map(it -> new TicketEntity(it.getName(), it.getEmail(), TypListkaEnum.DOBROVOLNIK, eventService.findSelected().orElseThrow())).toList();
        tickets.forEach(emailService::odosliListok);
        ticketRepository.saveAll(tickets);

        entities.forEach(it -> {
            it.setEmailSend(true);
        });
        volunteerRepository.saveAll(entities);
        return "redirect:/admin";
    }

    public String renderPage(String pageId, ModelMap model) {
        model.put("pocetObsadenych", ticketRepository.countUcastnicke(eventService.findSelected().orElseThrow().getId()));
        model.put("maxPocet", maxPocetListkov);

        model.put("pocetDobrovolnikov", ticketRepository.countDobrovolnicke(eventService.findSelected().orElseThrow().getId()));
        model.put("pocetTeamakov", ticketRepository.countTeamacke(eventService.findSelected().orElseThrow().getId()));
        model.put("pocetPouzite", ticketRepository.countPouzite(eventService.findSelected().orElseThrow().getId()));
        model.put("pocetZaplatene", ticketRepository.countZaplatene(eventService.findSelected().orElseThrow().getId()));
        model.put("pocetCelkovo", ticketRepository.countAll(eventService.findSelected().orElseThrow().getId()));
        model.put("user", ((AdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        model.put("adminLevel", adminDetailService.hasAuthority(AdminRoleEnum.SUPERADMIN) ? "SUPER" : "ADMIN");
        model.put("dobrovolniciNeodoslaneListky", !volunteerRepository.findEmailNotSend().isEmpty());
        return pageId;
    }
}
