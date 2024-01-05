package com.example.SystemAlerte.Service;

import java.util.List;

import com.example.SystemAlerte.Model.Machine;

public interface MachineServiceInterface {
    Machine SaveMachine(Machine Machine);

    Machine getMachine(String Machinename);

    List<Machine> getMachines();

    Machine UpdateMachine(Machine Machine);

    void DeleteMachine(Long id);
}
