package com.example.SystemAlerte.Controller;

import com.example.SystemAlerte.Model.User;
import com.example.SystemAlerte.Service.DeclarationService;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.SystemAlerte.Model.DeclarationOfIncident;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/Controller")
@RequiredArgsConstructor
public class DeclarationController {
    private final DeclarationService declarationService;

    @GetMapping("/Dashboard/Declarations")
    public ResponseEntity<List<DeclarationOfIncident>> getDeclarations() {
        return ResponseEntity.ok().body(declarationService.getDeclarations());

    }
    @GetMapping("/Front/MyDeclarations")
    public ResponseEntity<List<DeclarationOfIncident>> getMyDeclarations(@RequestBody  User user) {
        return ResponseEntity.ok().body(declarationService.getMyDeclarations(user));

    }

    @PostMapping("/Front/Declaration/save")
    public ResponseEntity<DeclarationOfIncident> SaveDeclaration(@RequestBody DeclarationOfIncident Declaration) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/Controller/Front/Declaration/save").toUriString());

        return ResponseEntity.created(uri).body(declarationService.SaveDeclaration(Declaration));

    }

    @PutMapping("/Front/Declaration/update")
    public ResponseEntity<DeclarationOfIncident> UpdateDeclaration(@RequestBody DeclarationOfIncident Declaration) {
        return ResponseEntity.ok().body(declarationService.UpdateDeclaration(Declaration));
    }

    @DeleteMapping("/Front/Declaration/delete/{id}")
    public void DeleteDeclaration(@PathVariable Long id) {
        declarationService.DeleteDeclaration(id);
    }
}
