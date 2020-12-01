package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.hibernate.engine.jdbc.env.internal.DefaultSchemaNameResolver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    private ScheduleDTO convertScheToScheDTO(Schedule schedule) {
        ScheduleDTO newScheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, newScheduleDTO);

        List<Employee> employeeList= schedule.getEmployeeList();
        if (employeeList != null) {
            List<Long> employeeIds = schedule.getEmployeeList().stream().map(Employee::getId).collect(Collectors.toList());
            newScheduleDTO.setEmployeeIds(employeeIds);
        }

        List<Pet> petList = schedule.getPetList();
        if (petList != null) {
            List<Long> petIds = schedule.getPetList().stream().map(Pet::getId).collect(Collectors.toList());
            newScheduleDTO.setPetIds(petIds);
        }

        return newScheduleDTO;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        ScheduleDTO convertedSchedule;
        Schedule schedule = new Schedule(scheduleDTO.getDate(), scheduleDTO.getActivities());
        try {
            List<Long> empIds = scheduleDTO.getEmployeeIds();
            List<Employee> employeeList = employeeService.getListById(empIds);
            if(empIds!=null) {
                schedule.setEmployeeList(employeeList);
            }
            List<Long> petIds = scheduleDTO.getPetIds();
            List<Pet> petList = petService.getAllPetsById(petIds);
            if (petList!=null) {
                schedule.setPetList(petList);
            }
            scheduleService.save(schedule);
            convertedSchedule = convertScheToScheDTO(schedule);
        } catch(Exception e) {
            throw e;
        }
        return convertedSchedule;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList;
        try {
            scheduleList = scheduleService.getAllSchedules();
        } catch (Exception e) {
            throw e;
        }
        return scheduleList.stream().
                map(this::convertScheToScheDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> scheduleList;
        try {
            scheduleList = scheduleService.getAllSchedulesForPet(petId);

        } catch (Exception e) {
            throw e;
        }
        return scheduleList.stream().
                map(this::convertScheToScheDTO).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> scheduleList;
        try {
            scheduleList = scheduleService.getAllSchedulesForEmployee(employeeId);

        } catch (Exception e) {
            throw e;
        }
        return scheduleList.stream().
                map(this::convertScheToScheDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> scheduleList;
        try {
            scheduleList = scheduleService.getAllSchedulesForCustomer(customerId);

        } catch (Exception e) {
            throw e;
        }
        return scheduleList.stream().
                map(this::convertScheToScheDTO).collect(Collectors.toList());
    }
}
