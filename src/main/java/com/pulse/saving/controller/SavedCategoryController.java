package com.pulse.saving.controller;

import com.pulse.saving.model.SavedCategory;
import com.pulse.saving.service.SavedCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/static")
public class SavedCategoryController {

    @Autowired
    private SavedCategoryService savedCategoryService;

    @GetMapping("/categories")
    public List<SavedCategory> getCategories() {
        return savedCategoryService.getAllCategories();
    }
}
