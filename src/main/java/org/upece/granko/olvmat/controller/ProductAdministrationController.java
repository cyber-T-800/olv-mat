package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.upece.granko.olvmat.entity.EventEntity;
import org.upece.granko.olvmat.entity.ProductEntity;
import org.upece.granko.olvmat.model.NewProductForm;
import org.upece.granko.olvmat.repository.ProductRepository;
import org.upece.granko.olvmat.service.EventService;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ProductAdministrationController {

    private final ProductRepository productRepository;
    private final EventService eventService;

    @GetMapping("/products")
    public String getProducts(ModelMap model) {
        EventEntity currentEvent = currentEvent();
        List<ProductEntity> products = productRepository.findByEvent(currentEvent);

        model.addAttribute("products", products);
        model.addAttribute("productForm", new NewProductForm());
        return "admin/products";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable UUID id, ModelMap model) {
        ProductEntity product = findProductForCurrentEvent(id);
        model.addAttribute("products", productRepository.findByEvent(currentEvent()));
        model.addAttribute("productForm", toForm(product));
        model.addAttribute("editingProductId", product.getId());
        model.addAttribute("editingProduct", product);
        return "admin/products";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute("productForm") NewProductForm form) throws IOException {
        EventEntity currentEvent = currentEvent();
        ProductEntity product = new ProductEntity();
        product.setEvent(currentEvent);
        product.setCreatedAt(LocalDateTime.now());
        updateProduct(product, form, true);
        productRepository.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable UUID id,
                                @ModelAttribute("productForm") NewProductForm form) throws IOException {
        ProductEntity product = findProductForCurrentEvent(id);
        updateProduct(product, form, false);
        productRepository.save(product);
        return "redirect:/admin/products";
    }

    private void updateProduct(ProductEntity product, NewProductForm form, boolean newProduct) throws IOException {
        product.setTextId(form.getTextId());
        product.setType(form.getType());
        product.setTitle(form.getTitle());
        product.setDescription(form.getDescription());
        product.setPrice(validatePrice(form.getPrice()));

        if (form.getImage() != null && !form.getImage().isEmpty()) {
            product.setImage(rescaleImage(form.getImage().getBytes(), form.getImage().getContentType()));
        } else if (newProduct) {
            throw new IllegalArgumentException("An image is required for a new product");
        }
    }

    private BigDecimal validatePrice(BigDecimal price) {
        if (price == null) {
            return null;
        }
        if (price.signum() < 0 || price.compareTo(new BigDecimal("99.5")) > 0
                || price.remainder(new BigDecimal("0.5")).signum() != 0) {
            throw new IllegalArgumentException("Price must be between 0 and 99.5 in 0.5 increments");
        }
        return price;
    }

    private byte[] rescaleImage(byte[] imageBytes, String contentType) throws IOException {
        BufferedImage source = ImageIO.read(new java.io.ByteArrayInputStream(imageBytes));
        if (source == null) {
            throw new IllegalArgumentException("Uploaded file is not a supported image");
        }

        int height = Math.max(1, (int) Math.round(source.getHeight() * 200.0 / source.getWidth()));
        int imageType = source.getColorModel().hasAlpha()
                ? BufferedImage.TYPE_INT_ARGB
                : BufferedImage.TYPE_INT_RGB;
        BufferedImage resized = new BufferedImage(200, height, imageType);

        Graphics2D graphics = resized.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(source, 0, 0, 200, height, null);
        } finally {
            graphics.dispose();
        }

        String format = "image/png".equalsIgnoreCase(contentType) || source.getColorModel().hasAlpha()
                ? "png"
                : "jpg";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        if (!ImageIO.write(resized, format, output)) {
            throw new IOException("Unable to encode resized image");
        }
        return output.toByteArray();
    }

    private ProductEntity findProductForCurrentEvent(UUID id) {
        EventEntity currentEvent = currentEvent();
        return productRepository.findById(id)
                .filter(product -> product.getEvent().getId().equals(currentEvent.getId()))
                .orElseThrow();
    }

    private EventEntity currentEvent() {
        return eventService.findSelected().orElseThrow();
    }

    private NewProductForm toForm(ProductEntity product) {
        return new NewProductForm(
                product.getTextId(),
                product.getType(),
                null,
                product.getTitle(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
