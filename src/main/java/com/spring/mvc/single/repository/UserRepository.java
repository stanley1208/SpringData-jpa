package com.spring.mvc.single.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.mvc.single.entity.User;


/*
 * 方法命名規則
 * 1.查詢方法以find | read | get 開頭
 * 2.
 * */

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	//根據name 來取得User
	List<User>getByName(String name);
	
	//Where name LIKE? AND id>=?
	List<User>getByNameStartingWithAndIdGreaterThanEqual(String name,Long id);
	
	//Where id in (?,?,?)
	List<User>getByIdIn(List<Long>ids);
	
	//Where birth<?
	List<User>getByBirthLessThan(Date birth);
	
}
