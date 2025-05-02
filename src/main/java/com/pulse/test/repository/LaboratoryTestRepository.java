package com.pulse.test.repository;

import com.pulse.test.model.LaboratoryTest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LaboratoryTestRepository extends JpaRepository<LaboratoryTest,Long> {
    List<LaboratoryTest> findByLaboratory_LaboratoryId(Long labId);
    List<LaboratoryTest> findByTest_TestId(Long testId);
}