package com.spring.mvc.single.controller;

import java.util.List;

import javax.management.loading.PrivateClassLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.mvc.single.entity.User;
import com.spring.mvc.single.repository.UserRepository;

@RestController //�b�C�Ӥ�k�|�۰ʥ[�W@ResponseBody
@RequestMapping("/rest/user")
public class UserRestController {
	@Autowired
	private UserRepository userRepository;
	
	//�d��
	@GetMapping("/")
	public List<User> queryAll(){
		Sort sort=new Sort(Sort.Direction.DESC,"id");
		List<User>users=userRepository.findAll(sort);
		return users;
	}
	
	//�d�߳浧
	@GetMapping("/{id}")
	public User getOne(@PathVariable("id") long id) {
		return userRepository.findOne(id);
	}
	
	//�s�W
	@PostMapping("/")
	public User create(@RequestBody User user) {
		userRepository.save(user);
		return user; 
	}
	
	//�ק�
	@PutMapping("/")
	public User update(@RequestBody User user) {
		userRepository.saveAndFlush(user);
		return user; 
	}
	
	//�R��
	@PutMapping("/{id}")
	public String delete(@PathVariable("id") long id) {
		userRepository.delete(id);
		return id+""; 
	}
	
}
