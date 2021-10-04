package com.spring.mvc.single.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.javafaker.Faker;
import com.spring.mvc.single.entity.User;
import com.spring.mvc.single.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	//新增範例資料
	@GetMapping("/test/create_sample_data")
	@ResponseBody
	public String testCreareSampleData() {
		Random r=new Random();
		Faker faker=new Faker();
		int count=150;
		for(int i=0;i<count;i++) {
			User user=new User();
			user.setName(faker.name().lastName());
			user.setPassword(String.format("%04d",r.nextInt(10000)));
			user.setBirth(faker.date().birthday());
			//儲存到資料庫
			userRepository.saveAndFlush(user);
		}
		
		return "Create sample data ok !";
	}
	
	//查詢範例資料 1
	@GetMapping("/test/findall")
	@ResponseBody
	public List<User> testFindall() {
		List<User>users=userRepository.findAll();
		
		return users;
	}
	
	
	//查詢範例資料 2
	@GetMapping("/test/findall_sort")
	@ResponseBody
	public List<User> testFindallSort() {
		//ASC自然排序 小到大,DESC 大到小
		Sort sort=new Sort(Sort.Direction.ASC,"name");
		List<User>users=userRepository.findAll();
			
			return users;
		}
	
	//查詢範例資料 3
		@GetMapping("/test/findall_ids")
		@ResponseBody
		public List<User> testFindallIds() {
			//查找指定id的資料
			Iterable<Long>ids=Arrays.asList(1L,3L,5L);
			List<User>users=userRepository.findAll(ids);
				
				return users;
			}
		
		//查詢範例資料 4
		@GetMapping("/test/findall_example")
		@ResponseBody
		public List<User> testFindallExample() {
			User user=new User();
			user.setId(2L);
			user.setPassword("1234");
			//根據Example 所提供的實例(例如:user)來查詢
			Example<User>example=Example.of(user);
			List<User>users=userRepository.findAll(example);
						
				return users;
			}
		
		//查詢範例資料 4
		@GetMapping("/test/findall_example2")
		@ResponseBody
		public List<User> testFindallExample2() {
			User user=new User();
			user.setName("a");
			//name的內容是否有包含"a"
			//建立ExampleMatcher比對器
			ExampleMatcher matcher=ExampleMatcher.matching()
					.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
			Example<User>example=Example.of(user,matcher);
			List<User>users=userRepository.findAll(example);
								
			return users;
			}
}
