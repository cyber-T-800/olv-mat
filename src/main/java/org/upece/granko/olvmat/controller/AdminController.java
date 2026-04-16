package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.upece.granko.olvmat.entity.AdminEntity;
import org.upece.granko.olvmat.entity.AdminRegistraciaZiadostEntity;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.entity.VolunteerEntity;
import org.upece.granko.olvmat.entity.enums.AdminRegistraciaZiadostStavEnum;
import org.upece.granko.olvmat.entity.enums.AdminRoleEnum;
import org.upece.granko.olvmat.entity.enums.StavRezervacieEnum;
import org.upece.granko.olvmat.entity.enums.TypListkaEnum;
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

    private final int maxPocetListkov = 600;

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
    public String nahrajListky(@Param("typ") String typ, @Param("subor") MultipartFile subor, ModelMap modelMap) throws IOException {
        TypListkaEnum typListka = TypListkaEnum.valueOf(typ);

        String inputString = new String(subor.getBytes(), StandardCharsets.UTF_8);
        String[] lines = inputString.split("\n");
        for (String line : lines) {
            String[] values = line.split(",");
            String email = values[0].trim();
            String meno = values[1].trim();
            String priezvisko = values[2].trim();

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
    public String volunteers(ModelMap model) {
        List<VolunteerEntity> entities = volunteerRepository.findAll();
        entities.forEach(it->{
            it.setServices(it.getServices().replace(",", "\n"));
            it.setAvailability(Arrays.stream(it.getAvailability()
                    .split(","))
                    .map(st -> {
                        int time = Integer.parseInt(st);
                        return String.format("%d - %d", time, time + 2);
                    }).collect(Collectors.joining("\n"))
            );
        });
        model.addAttribute("volunteers", entities);
        return renderPage("admin/volunteers", model);
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
        return pageId;
    }
}
