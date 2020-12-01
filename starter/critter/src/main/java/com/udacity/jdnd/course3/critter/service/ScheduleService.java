package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Schedule save (Schedule schedule) {

        //saving schedule data to employees and pets
        List<Employee> employeeList = schedule.getEmployeeList();
        List<Pet> petList = schedule.getPetList();

        Iterator<Employee> employeeIterator = employeeList.iterator();

        while (employeeIterator.hasNext()) {
            Employee employee = employeeIterator.next();
            List<Schedule> scheduleList = employee.getScheduleList();
            if (scheduleList!=null) scheduleList.add(schedule);
            else {
                scheduleList = new ArrayList<>();
                scheduleList.add(schedule);
            }
            employee.setScheduleList(scheduleList);
            employeeRepository.save(employee);
        }

        Iterator<Pet> petIterator = petList.iterator();
        while (petIterator.hasNext()) {
            Pet pet = petIterator.next();
            List<Schedule> scheduleList = pet.getScheduleList();
            if (scheduleList!=null) scheduleList.add(schedule);
            else {
                scheduleList = new ArrayList<>();
                scheduleList.add(schedule);
            }
            pet.setScheduleList(scheduleList);
            petRepository.save(pet);
        }
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() { return scheduleRepository.findAll();}

    public List<Schedule> getAllSchedulesForPet(Long petId) {
        Pet pet = petRepository.findById(petId).get();
        return pet.getScheduleList();
    }

    public List<Schedule> getAllSchedulesForEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        return employee.getScheduleList();
    }

    public List<Schedule> getAllSchedulesForCustomer(Long customerId) {
        List<Pet> petList= petRepository.findPetsByCustomerId(customerId);
        List<Schedule> scheduleList = new ArrayList<>();
        for (Pet pet : petList) {
            scheduleList.addAll(pet.getScheduleList());
        }
        return scheduleList;
    }
}
