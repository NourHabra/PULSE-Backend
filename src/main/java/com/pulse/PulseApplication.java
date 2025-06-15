package com.pulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import com.pulse.user.model.Admin;
import com.pulse.user.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackages ={

        "com.pulse"
})
public class PulseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PulseApplication.class, args);
    }


//  A java bean for hardcoding the first admin into DB
//    Uncomment this when first running the server and feel free to comment it back

//    @Bean
//    public CommandLineRunner loadData(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
//        return (args) -> {
//            // Check if the first admin already exists
//            if (adminRepository.findByEmail("firstadmin@pulse.com") == null) {
//                Admin firstAdmin = new Admin();
//                firstAdmin.setFirstName("first");
//                firstAdmin.setLastName("admin");
//                firstAdmin.setEmail("firstadmin@pulse.com");
//                firstAdmin.setEnabled(true);
//                firstAdmin.setPassword(passwordEncoder.encode("first123"));
//                firstAdmin.setRole("ADMIN");
//
//                adminRepository.save(firstAdmin);
//
//                System.out.println("First admin created!");
//            } else {
//                System.out.println("First admin already exists.");
//            }
//        };
//    }

}
