package com.example.SystemAlerte.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.SystemAlerte.Model.Incident;
import com.example.SystemAlerte.Model.User;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import com.example.SystemAlerte.Model.DeclarationOfIncident;
import com.example.SystemAlerte.Repository.DeclarationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeclarationService implements DeclarationServiceInterface {
    private final DeclarationRepository DR;

    @Override
    public DeclarationOfIncident SaveDeclaration(DeclarationOfIncident Declaration) {
        log.info("Saving new Declaration {} to the database", Declaration.getName());
        LocalDateTime localDateTime = LocalDateTime.now();
        Declaration.setDate(localDateTime);
        return DR.save(Declaration);
    }

    @Override
    public DeclarationOfIncident getDeclaration(String Name) {
        log.info("fetching Declarations{}", Name);

        return DR.findByName(Name);
    }

    @Override
    public List<DeclarationOfIncident> getDeclarations() {
        log.info("fetching all Declaration");
        return DR.findAll();
    }

    @Override
    public List<DeclarationOfIncident> getMyDeclarations(User user) {
        return DR.findByUser(user.getId());
    }

    @Override
    public DeclarationOfIncident UpdateDeclaration(DeclarationOfIncident Declaration) {
        log.info("Declaration updated : {}", Declaration.getName());

        Optional<DeclarationOfIncident> Declarationfct = DR.findById(Declaration.getId());
        Declaration.setId(Declarationfct.get().getId());

        return DR.save(Declaration);
    }

    @Override
    public void DeleteDeclaration(Long id) {

        DeclarationOfIncident declaration = new DeclarationOfIncident();
        declaration = DR.findById(id).get();
        log.info("Declaration deleted : {}", declaration.getName());
        DR.deleteById(id);



    }


}
