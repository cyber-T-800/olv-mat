package org.upece.granko.olvmat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventEditForm {
    private Long id;
    private String nazov;
    private String description;
    private String capacity;
}
