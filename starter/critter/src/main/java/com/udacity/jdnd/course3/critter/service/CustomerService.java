package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;

    public Customer save(Customer customer){
        List<Pet> pets = petRepository.findPetsByCustomerId(customer.getId());
        if (pets!=null) customer.setPets(pets);
        return customerRepository.save(customer);
    }

    public Customer findById(Long id) { return customerRepository.findById(id).get(); }

    public List<Customer> getAlLCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByPet(Long petId) {
      return petRepository.getOne(petId).getCustomer();
    }

}
