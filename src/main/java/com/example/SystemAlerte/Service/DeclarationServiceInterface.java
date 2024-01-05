package com.example.SystemAlerte.Service;

import java.util.List;

import com.example.SystemAlerte.Model.DeclarationOfIncident;
import com.example.SystemAlerte.Model.User;

public interface DeclarationServiceInterface {
    DeclarationOfIncident SaveDeclaration(DeclarationOfIncident Declaration);

    DeclarationOfIncident getDeclaration(String Name);

    List<DeclarationOfIncident> getDeclarations();
    List<DeclarationOfIncident> getMyDeclarations(User user);
    DeclarationOfIncident UpdateDeclaration(DeclarationOfIncident Declaration);

    void DeleteDeclaration(Long id);


}
