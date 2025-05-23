package com.pulse.saving.service;

import com.pulse.saving.model.SavedCategory;
import com.pulse.saving.repository.SavedCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class SavedCategoryService {

    @Autowired
    private SavedCategoryRepository savedCategoryRepository;

    // This will run when the application starts to insert hardcoded data
//    @PostConstruct
//    public void init() {
//        // Check if the table is empty before adding data
//        if (savedCategoryRepository.count() == 0) {
//            savedCategoryRepository.save(new SavedCategory("Doctors", "https://test.walidalgower.com/wp-content/uploads/2025/04/doctor.png"));
//            savedCategoryRepository.save(new SavedCategory("Pharmacies", "https://test.walidalgower.com/wp-content/uploads/2025/04/pharmacy.png"));
//            savedCategoryRepository.save(new SavedCategory("Laboratories", "https://test.walidalgower.com/wp-content/uploads/2025/04/laboratory.png"));
//        }
//    }

    public List<SavedCategory> getAllCategories() {
        return savedCategoryRepository.findAll();
    }
}
