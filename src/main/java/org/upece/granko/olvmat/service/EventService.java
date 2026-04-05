package org.upece.granko.olvmat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.upece.granko.olvmat.entity.EventEntity;
import org.upece.granko.olvmat.model.EventEditForm;
import org.upece.granko.olvmat.repository.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public void save(EventEditForm form) {
        EventEntity entity = null;
        if (form.getId() == null) {
            entity = new EventEntity();
            entity.setStav("UNSELECTED");
            entity.setId(UUID.randomUUID());
        }else{
            entity = eventRepository.findById(form.getId()).orElseThrow();
        }
        entity.setNazov(form.getName());
        entity.setPopis(form.getDescription());

        eventRepository.save(entity);
    }

    public List<EventEntity> findAll() {
        return eventRepository.findAll();
    }

    @Transactional
    public void selectEvent(UUID id) {
        eventRepository.unselectCurrentEvent();
        eventRepository.selectEvent(id);
    }

    public Optional<EventEntity> findSelected() {
        return eventRepository.findSelected();
    }
}
