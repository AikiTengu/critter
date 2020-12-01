package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    private PetDTO convertPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getCustomer()!=null) petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer customer;
        Pet pet = new Pet(petDTO.getType(), petDTO.getName(), petDTO.getBirthDate(), petDTO.getNotes());
        try {
            if(petDTO.getOwnerId()!=0) {
                customer = customerService.findById(petDTO.getOwnerId());
                petService.savePetOwner(pet, customer);
                customerService.save(customer);
            }
            else {
                customer = new Customer();
                customer.setId(0L);
                petService.save(pet);
            }
        } catch (Exception e) {
            throw e;
        }
        PetDTO savedPet = convertPetToPetDTO(pet);
        return savedPet;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId){
    Pet pet;
    try
    {
        pet = petService.findById(petId);
    } catch(Exception e)
    {
        throw e;
    }
    return convertPetToPetDTO(pet);
}
    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets;
        try {
            pets = petService.getAlLPets();
        } catch (Exception e) {
            throw e;
        }
        return pets.stream().
                map(this::convertPetToPetDTO).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets;
        try {
            pets = petService.getAllPetsByOwner(ownerId);
        } catch (Exception e) {
            throw e;
        }
        return pets.stream().
                map(this::convertPetToPetDTO).collect(Collectors.toList());
    }
}
