package com.pulse.user.repository;

import com.pulse.user.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByEmail(String email);
    @Query(value = """
        SELECT  u.*,            
                d.*             
        FROM    doctor d
        JOIN    users  u ON u.UserID = d.DoctorID   
        ORDER BY MD5(CONCAT(CURDATE(), d.DoctorID))
        LIMIT 4
        """,
            nativeQuery = true)
    List<Doctor> findTodayFeaturedDoctors();
}
