package org.upece.granko.olvmat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewProductForm {
    private String textId;
    private String type;
    private MultipartFile image;
    private String title;
    private String description;
    private BigDecimal price;
}
