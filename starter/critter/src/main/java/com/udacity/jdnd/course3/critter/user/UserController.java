package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);

        List<Pet> customerPets = customer.getPets();
        if (customerPets != null) {
            List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }

        return customerDTO;
    }

    private EmployeeDTO convertEmpToEmpDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;

    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = new Customer(customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes());
        try {
            customerService.save(customer);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        CustomerDTO savedCustomer = convertCustomerToCustomerDTO(customer);
        return savedCustomer;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers;
        try {
            customers = customerService.getAlLCustomers();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        return customers.stream().
                map(this::convertCustomerToCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Customer customer;
        try {
            customer = customerService.getCustomerByPet(petId);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        return convertCustomerToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO.getName(), employeeDTO.getSkills(), employeeDTO.getDaysAvailable());

        try {
            employeeService.save(employee);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        EmployeeDTO savedEmployee = convertEmpToEmpDTO(employee);
        return savedEmployee;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee;
        try {
            employee = employeeService.getById(employeeId);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        return convertEmpToEmpDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee;
        try {
            employee = employeeService.getById(employeeId);
            employee.setDaysAvailable(daysAvailable);
            employeeService.save(employee);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employeeList;
        try {
            employeeList = employeeService.findForService(employeeDTO.getDate(), employeeDTO.getSkills());
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        return employeeList.stream().
                map(this::convertEmpToEmpDTO).collect(Collectors.toList());
    }
}
