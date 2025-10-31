package org.upece.granko.olvmat.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.upece.granko.olvmat.entity.Test;
import org.upece.granko.olvmat.repository.TestRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
public class UptimeController {

    private final TestRepository testRepository;

    @GetMapping
    public UptimeStatus getUp(){
        return new UptimeStatus("UP", LocalDate.now());
    }

    @GetMapping("test")
    public List<Test> getTest(){
        return testRepository.findAll();
    }

    public record UptimeStatus(
            String up,
            LocalDate time
    ){}
}
