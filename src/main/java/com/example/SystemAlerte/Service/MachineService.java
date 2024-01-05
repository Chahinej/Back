package com.example.SystemAlerte.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.SystemAlerte.Model.Machine;
import com.example.SystemAlerte.Repository.MachineRepository;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MachineService implements MachineServiceInterface {
    private final MachineRepository MR;

    @Override
    public Machine SaveMachine(Machine Machine) {
        log.info("Saving new User {} to the database", Machine.getName());

        return MR.save(Machine);
    }

    @Override
    public Machine getMachine(String Machinename) {
        log.info("fetching Machines{}", Machinename);

        return MR.findByName(Machinename);
    }

    @Override
    public List<Machine> getMachines() {
        log.info("fetching all Machines");
        return MR.findAll();
    }

    @Override
    public Machine UpdateMachine(Machine Machine) {
        log.info("Machine updated : {}", Machine.getName());
        Optional<Machine> machinefct = MR.findById(Machine.getId());
        Machine.setId(machinefct.get().getId());
        return MR.save(Machine);
    }

    @Override
    public void DeleteMachine(Long id) {

         Machine machine = new Machine();
        machine = MR.findById(id).get();
        log.info("Machine deleted : {}", machine.getName());
        MR.deleteById(id);
    }

}
