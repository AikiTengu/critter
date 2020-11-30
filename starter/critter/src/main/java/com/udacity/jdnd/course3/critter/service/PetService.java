package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository petRepository;

    public Pet save(Pet pet){
        return petRepository.save(pet);
    }

    public Pet savePetOwner(Pet pet, Customer customer) {
        pet.setCustomer(customer);
        return petRepository.save(pet);
    }

    public Pet findById(Long id) {
        return petRepository.findById(id).get();
    }

    public List<Pet> getAlLPets() {
        return petRepository.findAll();
    }
}
