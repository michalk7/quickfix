package com.kubara.michal.inzynierka.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
