package org.upece.granko.olvmat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.upece.granko.olvmat.entity.enums.AdminRoleEnum;
import org.upece.granko.olvmat.service.AdminDetailService;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {
    private final AdminDetailService adminDetailService;

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return "redirect:/";
    }

    @ExceptionHandler({NoResourceFoundException.class, MethodArgumentTypeMismatchException.class})
    public String handleNotFound(HttpServletRequest request) {
        String url = request.getRequestURI();
        if (url.contains("vstup")
                && (adminDetailService.hasAuthority(AdminRoleEnum.VSTUP)
                || adminDetailService.hasAuthority(AdminRoleEnum.ADMIN))) {
            return "redirect:/vstup";
        }
        if (url.contains("admin")){
             if (adminDetailService.hasAuthority(AdminRoleEnum.ADMIN)) {
                 return "redirect:/admin";
             }else if (adminDetailService.hasAuthority(AdminRoleEnum.VSTUP)){
                 return "redirect:/vstup";
             }
        }
        return "redirect:/";
    }
}
