package com.leek.demo1_springsec.Controller;

import com.leek.demo1_springsec.model.Customer;
import com.leek.demo1_springsec.model.SecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableMethodSecurity
public class CustomerController {
    private final List<Customer> customers = List.of(
            Customer.builder().id("1").name("John Doe").email("john@mail.com").phone("1234567890").build(),
            Customer.builder().id("2").name("Jane Doe").email("Jane@mail.com").phone("0987654321").build()
    );

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/customer/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // Đảm bảo sử dụng authority đúng
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(this.customers);
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')") // Đảm bảo sử dụng authority đúng
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id) {
        List<Customer> filteredCustomers = this.customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .toList();

        if (filteredCustomers.isEmpty()) {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy khách hàng
        }

        return ResponseEntity.ok(filteredCustomers.get(0));
    }
}

