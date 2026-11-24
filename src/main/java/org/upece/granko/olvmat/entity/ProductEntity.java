package org.upece.granko.olvmat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "text_id", nullable = false, length = 50)
    private String textId;

    @Column(nullable = false, length = 10)
    private String type;

    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] image;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 150)
    private String description;

    @Column(precision = 3, scale = 1)
    private BigDecimal price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public String getImageBase64() {
        if (image == null) return null;

        return Base64.getEncoder().encodeToString(image);
    }

    public String getImageContentType() {
        if (image != null && image.length >= 8
                && (image[0] & 0xFF) == 0x89
                && image[1] == 0x50
                && image[2] == 0x4E
                && image[3] == 0x47
                && image[4] == 0x0D
                && image[5] == 0x0A
                && image[6] == 0x1A
                && image[7] == 0x0A) {
            return "image/png";
        }
        return "image/jpeg";
    }
}
