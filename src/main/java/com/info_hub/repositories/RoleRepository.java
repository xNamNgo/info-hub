package com.info_hub.repositories;

import com.info_hub.models.Role;

public interface RoleRepository extends BaseRepository<Role> {
    Role findByCode(String code);
}
