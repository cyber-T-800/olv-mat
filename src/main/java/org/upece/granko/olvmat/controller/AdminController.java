package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/admin/login")
    public String getLogin(ModelMap modelMap) {
        modelMap.put("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return "admin/login";
    }


    @GetMapping("/admin")
    public String getAdminPage() {
        return "admin/admin";
    }
}
