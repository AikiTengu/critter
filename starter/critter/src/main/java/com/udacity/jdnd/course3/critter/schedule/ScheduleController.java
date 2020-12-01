package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

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
        try {
            Schedule schedule = new Schedule(scheduleDTO.getDate(), scheduleDTO.getActivities());
            convertedSchedule = convertScheToScheDTO(schedule);
        } catch(Exception e) {
            throw new UnsupportedOperationException();
        }
        return convertedSchedule;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        throw new UnsupportedOperationException();
    }
}
