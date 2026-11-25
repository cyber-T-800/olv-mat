package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.upece.granko.olvmat.entity.ErrorLogEntity;
import org.upece.granko.olvmat.repository.ErrorLogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {
    private final ErrorLogRepository errorLogRepository;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        logException(ex);

        return renderErrorPage(model, "500", "Nastala neočakávaná chyba.",
                "Skúste to znova neskôr.");
    }

    @ExceptionHandler({NoResourceFoundException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception ex, Model model) {
        logException(ex);
        return renderErrorPage(model, "404", "Stránka sa nenašla.",
                "Zadaná stránka neexistuje alebo už nie je dostupná.");
    }

    private String renderErrorPage(Model model, String errorCode, String title, String message) {
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorTitle", title);
        model.addAttribute("errorMessage", message);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        String homeUrl = "/";
        String homeLabel = "Späť na úvodnú stránku";
        if (authenticated && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN")
                        || authority.getAuthority().equals("SUPERADMIN"))) {
            homeUrl = "/admin";
            homeLabel = "Späť do administrácie";
        } else if (authenticated && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("VSTUP"))) {
            homeUrl = "/vstup";
            homeLabel = "Späť na vstup";
        }

        model.addAttribute("homeUrl", homeUrl);
        model.addAttribute("homeLabel", homeLabel);
        return "error/fallback";
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
