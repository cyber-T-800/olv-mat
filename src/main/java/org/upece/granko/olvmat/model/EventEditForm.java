package org.upece.granko.olvmat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class EventEditForm {
    private UUID id;
    private String name;
    private String description;
    private String capacity;
}
