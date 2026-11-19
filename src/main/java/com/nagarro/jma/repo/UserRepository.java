package com.nagarro.jma.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nagarro.jma.model.User;

public interface UserRepository  extends JpaRepository<User, Long> {

	@Query(value="SELECT u FROM User u order by u.id LIMIT:limit OFFSET:offset",nativeQuery=false)
	ArrayList<User> findAllAfterOffset(@Param("offset") int offset, @Param("limit") int limit);
	}
