package com.kubara.michal.inzynierka.webapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.kubara.michal.inzynierka.core.entity.Category;

public class ExpertDTO extends UserDTO {

	private List<Category> selectedCategoriesFromCheckboxes = new ArrayList<>();;
	
	public ExpertDTO() {
	
	}

	public List<Category> getSelectedCategoriesFromCheckboxes() {
		return selectedCategoriesFromCheckboxes;
	}

	public void setSelectedCategoriesFromCheckboxes(List<Category> selectedCategoriesFromCheckboxes) {
		this.selectedCategoriesFromCheckboxes = selectedCategoriesFromCheckboxes;
	}
	
}
