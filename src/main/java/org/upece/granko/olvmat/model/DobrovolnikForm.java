package org.upece.granko.olvmat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.upece.granko.olvmat.entity.enums.TypListkaEnum;
import java.util.List;

@Data
@NoArgsConstructor
public class DobrovolnikForm {
    private String name;
    private String email;
    private String text;
    private List availability;
    private List services;
}
