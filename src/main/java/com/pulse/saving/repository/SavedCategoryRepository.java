package com.pulse.saving.repository;

import com.pulse.saving.model.SavedCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedCategoryRepository extends JpaRepository<SavedCategory, Long> {
}
