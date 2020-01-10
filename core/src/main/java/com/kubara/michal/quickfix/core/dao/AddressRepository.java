package com.kubara.michal.quickfix.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.quickfix.core.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
