package com.kubara.michal.quickfix.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kubara.michal.quickfix.core.entity.Estate;

public interface EstateRepository extends JpaRepository<Estate, Long> {

}
