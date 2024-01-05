package com.example.SystemAlerte.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.SystemAlerte.Model.User;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.SystemAlerte.Model.Incident;
import com.example.SystemAlerte.Repository.IncidentRepository;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class IncidentService implements IncidentServiceInterface {
    private final IncidentRepository IR;

    @Override
    public Incident SaveIncident(Incident Incident) {
        log.info("Saving new Incident {} to the database", Incident.getName());
        LocalDateTime localDateTime = LocalDateTime.now();
        Incident.setDate(localDateTime);
        return IR.save(Incident);
    }

    @Override
    public Incident getIncident(String name) {
        log.info("fetching Incidents{}", name);

        return IR.findByName(name);
    }

    @Override
    public List<Incident> getIncidents() {
        log.info("fetching all Incidents");
        return IR.findAll();
    }

    @Override
    public Incident UpdateIncident(Incident Incident) {
        log.info("Incident updated : {}", Incident.getName());

        Optional<Incident> Incidentfct = IR.findById(Incident.getId());
        Incident.setId(Incidentfct.get().getId());
        return IR.save(Incident);

    }

    @Override
    public void DeleteIncident(Long id) {
        Incident incident = new Incident();
        incident = IR.findById(id).get();
        log.info("Incident deleted : {}", incident.getName());
        IR.deleteById(id);


    }

}
