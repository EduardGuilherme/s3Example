package com.aula.s3.s3exemplo.rds.repository;

import com.aula.s3.s3exemplo.rds.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<Users,Long> {
}
