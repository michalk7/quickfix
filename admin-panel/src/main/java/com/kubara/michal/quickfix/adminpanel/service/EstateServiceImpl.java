package com.kubara.michal.quickfix.adminpanel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubara.michal.quickfix.adminpanel.dto.EstateDTO;
import com.kubara.michal.quickfix.adminpanel.dto.StreetDTO;
import com.kubara.michal.quickfix.core.dao.EstateRepository;
import com.kubara.michal.quickfix.core.dao.RoleRepository;
import com.kubara.michal.quickfix.core.dao.StreetRepository;
import com.kubara.michal.quickfix.core.dao.UserRepository;
import com.kubara.michal.quickfix.core.entity.Address;
import com.kubara.michal.quickfix.core.entity.Estate;
import com.kubara.michal.quickfix.core.entity.Street;
import com.kubara.michal.quickfix.core.entity.User;

@Service
public class EstateServiceImpl implements EstateService {

	@Autowired
	private EstateRepository estateRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StreetRepository streetRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	@Transactional
	public List<Estate> findAll() {
		return estateRepository.findAll();
	}

	@Override
	@Transactional
	public Optional<Estate> findById(long estateId) {
		return estateRepository.findById(estateId);
	}

	@Override
	@Transactional
	public Page<Estate> findAll(Pageable pageable) {
		return estateRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Estate create(EstateDTO estateDto) {
		Estate estate = new Estate();
		estate.setName(estateDto.getName());
		return estateRepository.save(estate);
	}

	@Override
	@Transactional
	public Estate update(Estate estateToEdit, EstateDTO estateDTO) {
		estateToEdit.setName(estateDTO.getName());
		return estateRepository.save(estateToEdit);
	}

	@Override
	@Transactional
	public Street addStreet(StreetDTO streetDto) {
		Optional<Estate> estateOpt = estateRepository.findById(streetDto.getEstateId());
		if(!estateOpt.isPresent()) {
			return null;
		}
		Estate estate = estateOpt.get();
		Street street = new Street();
		street.setStreetName(streetDto.getStreetName());
		street.setStreetNumber(streetDto.getStreetNumber());
		street.setCity(streetDto.getCity());
		street.setDistrict(streetDto.getDistrict());
		street.setPostCode(streetDto.getPostCode());
		street.setEstate(estate);
		estate.getStreets().add(street);
		
		Street result = streetRepository.save(street);
		
		if(result != null) {
			List<User> usersWithoutEstate = userRepository.findAllByRolesAndEnabledAndUserEstate(roleRepository.findByName("ROLE_USER"), true, null);
			
			String streetDistrict = "";
			if(street.getDistrict() != null) {
				streetDistrict = street.getDistrict();
			}
			
			for(User user : usersWithoutEstate) {
				Address address = user.getAddress();
				
				String addressDistrict = "";
				if(address.getDistrict() != null) {
					addressDistrict = address.getDistrict();
				}
				if(address.getStreet().equals(street.getStreetName())
						&& address.getHouseNumber().equals(street.getStreetNumber())
						&& address.getCity().equals(street.getCity())
						&& addressDistrict.equals(streetDistrict)
						&& address.getPostCode().equals(street.getPostCode())
					) {
					estate.getUsers().add(user);
					user.setUserEstate(estate);
					estateRepository.save(estate);
				}
			}
		}
		
		
		return result;
	}

	@Override
	@Transactional
	public Optional<Street> findStreetById(long streetId) {
		return streetRepository.findById(streetId);
	}

	@Override
	@Transactional
	public void deleteStreet(Street street, Estate estate) {
		String streetDistrict = "";
		if(street.getDistrict() != null) {
			streetDistrict = street.getDistrict();
		}
		for(User user : estate.getUsers()) {
			Address address = user.getAddress();
			
			String addressDistrict = "";
			if(address.getDistrict() != null) {
				addressDistrict = address.getDistrict();
			}
			if(address.getStreet().equals(street.getStreetName())
					&& address.getHouseNumber().equals(street.getStreetNumber())
					&& address.getCity().equals(street.getCity())
					&& addressDistrict.equals(streetDistrict)
					&& address.getPostCode().equals(street.getPostCode())
				) {
				estate.getUsers().remove(user);
				user.setUserEstate(null);
				estateRepository.save(estate);
			}
		}
		streetRepository.delete(street);
	}

	@Override
	@Transactional
	public void deleteExpert(Estate estate, User expert) {
		expert.getExpertEstates().remove(estate);
		estate.getExperts().remove(expert);
		estateRepository.save(estate);
	}

	@Override
	@Transactional
	public void delete(Estate estate) {
		estate.getExperts().forEach(e -> e.getExpertEstates().remove(estate));
		estate.getExperts().clear();
		for( User user : estate.getUsers() ) {
			user.setUserEstate(null);
		}
		estate.getUsers().clear();
		estateRepository.save(estate);
		estateRepository.delete(estate);
	}

}
