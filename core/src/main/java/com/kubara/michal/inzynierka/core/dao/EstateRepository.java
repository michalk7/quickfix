package com.kubara.michal.inzynierka.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.inzynierka.core.entity.Estate;

public interface EstateRepository extends JpaRepository<Estate, Long> {

}
