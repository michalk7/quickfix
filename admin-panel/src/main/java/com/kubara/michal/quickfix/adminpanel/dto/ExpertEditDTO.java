package com.kubara.michal.quickfix.adminpanel.dto;

import java.util.ArrayList;
import java.util.List;

import com.kubara.michal.quickfix.core.entity.Category;

public class ExpertEditDTO extends UserEditDTO {
	
	private List<Category> selectedCategoriesFromCheckboxes = new ArrayList<>();;
	
	public ExpertEditDTO() {
	
	}
	
	public List<Category> getSelectedCategoriesFromCheckboxes() {
		return selectedCategoriesFromCheckboxes;
	}

	public void setSelectedCategoriesFromCheckboxes(List<Category> selectedCategoriesFromCheckboxes) {
		this.selectedCategoriesFromCheckboxes = selectedCategoriesFromCheckboxes;
	}
	
}
