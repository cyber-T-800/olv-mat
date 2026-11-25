package org.upece.granko.olvmat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.upece.granko.olvmat.entity.ErrorLogEntity;
import org.upece.granko.olvmat.entity.enums.AdminRoleEnum;
import org.upece.granko.olvmat.repository.ErrorLogRepository;
import org.upece.granko.olvmat.service.AdminDetailService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {
    private final AdminDetailService adminDetailService;
    private final ErrorLogRepository errorLogRepository;

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request) {
        logException(ex);

        return "redirect:/";
    }

    @ExceptionHandler({NoResourceFoundException.class, MethodArgumentTypeMismatchException.class})
    public String handleNotFound(Exception ex, HttpServletRequest request) {
        logException(ex);
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

    private void logException(Exception ex) {
        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));

        String message = ex.getMessage();
        if (message != null && message.length() > 200) {
            message = message.substring(0, 200);
        }

        errorLogRepository.save(new ErrorLogEntity(message, stackTrace.toString()));
    }
}
