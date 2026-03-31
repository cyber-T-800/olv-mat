package org.upece.granko.olvmat.model;

import java.util.List;

public class DobrovolnikForm {

    private String name;
    private String email;
    private String availability;
    private String services;

    // Gettery a settery
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getServices() { return services; }
    public void setServices(String services) { this.services = services; }

    // Pomocné metódy pre rozdelenie
    public List<String> getAvailabilityList() {
        if (availability == null || availability.isBlank()) return List.of();
        return List.of(availability.split(","));
    }

    public List<String> getServicesList() {
        if (services == null || services.isBlank()) return List.of();
        return List.of(services.split(","));
    }
}