package com.spring.mvc.single.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.mvc.single.entity.User;


/*
 * ��k�R�W�W�h
 * 1.�d�ߤ�k�Hfind | read | get �}�Y
 * 2.
 * */

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	//�ھ�name �Ө��oUser
	List<User>getByName(String name);
	
	//Where name LIKE? AND id>=?
	List<User>getByNameStartingWithAndIdGreaterThanEqual(String name,Long id);
	
	//Where id in (?,?,?)
	List<User>getByIdIn(List<Long>ids);
	
	//Where birth<?
	List<User>getByBirthLessThan(Date birth);
	
}
