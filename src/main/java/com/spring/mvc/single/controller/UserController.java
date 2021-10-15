package com.spring.mvc.single.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.enterprise.inject.New;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.javafaker.Faker;
import com.spring.mvc.single.entity.User;
import com.spring.mvc.single.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	// User 資料維護首頁
	@GetMapping(value = { "/", "/index" })
	public String index(Model model) {
		List<User> users = userRepository.findAll();
		model.addAttribute("user", new User());
		model.addAttribute("users", users);
		model.addAttribute("_method", "POST");
		return "user/index"; // 重導到 /WEB-INF/view/user/index.jsp
	}

	// User 新增
	@PostMapping(value = "/")
	public String create(User user) {
		userRepository.save(user);
		System.out.println("User create: " + user);
		return "redirect: ./";
	}

	// User 修改
	@PutMapping(value = "/")
	public String update(User user) {
		userRepository.saveAndFlush(user);
		System.out.println("User update: " + user);
		return "redirect: ./";
	}

	// User 刪除
	@DeleteMapping(value = "/")
	public String delete(User user) {
		userRepository.delete(user.getId());
		return "redirect: ./";
	}

	// 根據 id 查詢給 update/edit 使用
	@GetMapping("/{id}")
	public String getUserById(Model model, @PathVariable Long id) {
		User user = userRepository.findOne(id);
		List<User> users = userRepository.findAll();
		model.addAttribute("user", user);
		model.addAttribute("users", users);
		model.addAttribute("_method", "PUT");
		return "user/index"; // 重導到 /WEB-INF/view/user/index.jsp
	}
	
	// 根據 id 查詢給 delete 使用
		@GetMapping("/delete/{id}")
		public String getUserById4Del(Model model, @PathVariable Long id) {
			User user = userRepository.findOne(id);
			List<User> users = userRepository.findAll();
			model.addAttribute("user", user);
			model.addAttribute("users", users);
			model.addAttribute("_method", "DELETE");
			return "user/index"; // 重導到 /WEB-INF/view/user/index.jsp
		}
		
		// 查詢分頁
		// 路徑範例:/page,/page?no=1, /page?no=10 etc...
		@GetMapping("/page")
		public String userPage(Model model,@RequestParam(name = "no",required = false,defaultValue = "0") Integer no) {
			int pageNo = no;
			int pageSize = 10;
			// 排序
			Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
			
			Sort sort = new Sort(order);

			// 分頁請求
			PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
			Page<User> page = userRepository.findAll(pageRequest);
			model.addAttribute("users", page.getContent());
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("totalPage", page.getTotalPages());
			return "user/page"; //重導到 /WEB-INF/view/user/page.jsp
		}

			

	// -----------------------------
	// 以下是測試user的程式
	// 新增範例資料
	@GetMapping("/test/create_sample_data")
	@ResponseBody
	public String testCreareSampleData() {
		Random r = new Random();
		Faker faker = new Faker();
		int count = 150;
		for (int i = 0; i < count; i++) {
			User user = new User();
			user.setName(faker.name().lastName());
			user.setPassword(String.format("% 04d", r.nextInt(10000)));
			user.setBirth(faker.date().birthday());
			// 儲存到資料庫
			userRepository.saveAndFlush(user);
		}

		return "Create sample data ok !";
	}

	// 查詢範例資料 1
	@GetMapping("/test/findall")
	@ResponseBody
	public List<User> testFindall() {
		List<User> users = userRepository.findAll();

		return users;
	}

	// 查詢範例資料 2
	@GetMapping("/test/findall_sort")
	@ResponseBody
	public List<User> testFindallSort() {
		// ASC自然排序 小到大,DESC 大到小
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		List<User> users = userRepository.findAll();

		return users;
	}

	// 查詢範例資料 3
	@GetMapping("/test/findall_ids")
	@ResponseBody
	public List<User> testFindallIds() {
		// 查找指定id的資料
		Iterable<Long> ids = Arrays.asList(1L, 3L, 5L);
		List<User> users = userRepository.findAll(ids);

		return users;
	}

	// 查詢範例資料 4
	@GetMapping("/test/findall_example")
	@ResponseBody
	public List<User> testFindallExample() {
		User user = new User();
		user.setId(2L);
		user.setPassword("1234");
		// 根據Example 所提供的實例(例如:user)來查詢
		Example<User> example = Example.of(user);
		List<User> users = userRepository.findAll(example);

		return users;
	}

	// 查詢範例資料 4
	@GetMapping("/test/findall_example2")
	@ResponseBody
	public List<User> testFindallExample2() {
		User user = new User();
		user.setName("a");
		// name的內容是否有包含"a"
		// 建立ExampleMatcher比對器
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name",
				ExampleMatcher.GenericPropertyMatchers.contains());
		Example<User> example = Example.of(user, matcher);
		List<User> users = userRepository.findAll(example);

		return users;
	}

	// 查詢範例資料 4
	@GetMapping("/test/get_one")
	@ResponseBody
	public User getOne() {

		return userRepository.findOne(5L);
	}

	// 查詢分頁
	@GetMapping("/test/page/{no}")
	@ResponseBody
	public List<User> testPage(@PathVariable("no") Integer no) {
		int pageNo = no;
		int pageSize = 10;
		// 排序
		Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "name");
		Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "name");
		Sort sort = new Sort(order1, order2);

		// 分頁請求
		PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
		Page<User> page = userRepository.findAll(pageRequest);

		return page.getContent();
	}

	@GetMapping("test/name")
	@ResponseBody
	public List<User> getByName(@RequestParam("name") String name) {
		return userRepository.getByName(name);
	}

	// 測試url:/mvc/user/test/name/id/S/50
	@GetMapping("test/name/id/{name}/{id}")
	@ResponseBody
	public List<User> getByNameAndId(@PathVariable("name") String name, @PathVariable("id") Long id) {
		return userRepository.getByNameStartingWithAndIdGreaterThanEqual(name, id);
	}

	// 測試url:/mvc/user/test/ids?ids=5,10,15
	@GetMapping("test/ids")
	@ResponseBody
	public List<User> getByIds(@RequestParam("ids") List<Long> ids) {
		return userRepository.getByIdIn(ids);
	}

	// 測試url:/mvc/user/test/birth?birth=2000-9-9
	@GetMapping("test/birth")
	@ResponseBody
	public List<User> getByBirthLessThan(@RequestParam("birth") @DateTimeFormat(iso = ISO.DATE) Date birth) {
		return userRepository.getByBirthLessThan(birth);
	}

	// 測試url:/mvc/user/test/birth_between?begin=1965-1-1&end=1970-12-31
	@GetMapping("test/birth_between")
	@ResponseBody
	public List<User> getByBirthBetween(@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) Date begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) Date end) {
		return userRepository.getByBirthBetween(begin, end);
	}
}
