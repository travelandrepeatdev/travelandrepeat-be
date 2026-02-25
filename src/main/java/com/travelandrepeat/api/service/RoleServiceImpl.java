package com.travelandrepeat.api.service;

import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepo roleRepo;

}
