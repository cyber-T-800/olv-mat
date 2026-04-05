package org.upece.granko.olvmat.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.upece.granko.olvmat.entity.AdminRegistraciaZiadostEntity;
import org.upece.granko.olvmat.entity.enums.AdminRegistraciaZiadostStavEnum;
import org.upece.granko.olvmat.entity.enums.AdminRoleEnum;
import org.upece.granko.olvmat.model.AdminDetails;
import org.upece.granko.olvmat.repository.AdminRegistraciaZiadostRepository;
import org.upece.granko.olvmat.repository.AdminRepository;
import org.upece.granko.olvmat.service.EmailService;
import org.upece.granko.olvmat.service.EventService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/superadmin")
public class SuperAdminController {


    private final AdminRegistraciaZiadostRepository adminRegistraciaZiadostRepository;
    private final AdminRepository adminRepository;
    private final EventService eventService;

    @Value("${hostport}")
    private String hostport;

    private final EmailService emailService;


    @GetMapping("")
    public String getSuperadminHome(ModelMap model) {

        model.put("user", ((AdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        model.put("admins", adminRepository.findAll());
        return "admin/superadmin";
    }

    @PostMapping("/vytvor-admina")
    public String vytvorAdmina(@Param("email") String email, @Param("typ") String typ) {
        AdminRegistraciaZiadostEntity entity = new AdminRegistraciaZiadostEntity();

        entity.setStav(AdminRegistraciaZiadostStavEnum.POTVRDENA);
        entity.setId(UUID.randomUUID());
        entity.setSecret(UUID.randomUUID());
        entity.setEmail(email);
        entity.setRola(AdminRoleEnum.valueOf(typ));

        adminRegistraciaZiadostRepository.save(entity);
        String url = hostport + "/admin-ziadost/" + entity.getId() + "/vytvorenie-hesla" + "?em=" + email + "&secret=" + entity.getSecret();

        Map<String, Object> model = new HashMap<>();
        model.put("email", email);
        model.put("url", url);

        emailService.sendMail(email, "Vytvorenie admin účtu v správe lístkov na granko akcie", "admin-registracia-mail-heslo", model);


        return "admin/registracia-schvalenie";
    }

    @GetMapping("/events/select/{id}")
    public String selectEvent(@PathVariable UUID id) {
        eventService.selectEvent(id);
        return "redirect:/superadmin";
    }
}
