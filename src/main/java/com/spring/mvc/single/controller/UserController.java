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

	// User ��ƺ��@����
	@GetMapping(value = { "/", "/index" })
	public String index(Model model) {
		List<User> users = userRepository.findAll();
		model.addAttribute("user", new User());
		model.addAttribute("users", users);
		model.addAttribute("_method", "POST");
		return "user/index"; // ���ɨ� /WEB-INF/view/user/index.jsp
	}

	// User �s�W
	@PostMapping(value = "/")
	public String create(User user) {
		userRepository.save(user);
		System.out.println("User create: " + user);
		return "redirect: ./";
	}

	// User �ק�
	@PutMapping(value = "/")
	public String update(User user) {
		userRepository.saveAndFlush(user);
		System.out.println("User update: " + user);
		return "redirect: ./";
	}

	// User �R��
	@DeleteMapping(value = "/")
	public String delete(User user) {
		userRepository.delete(user.getId());
		return "redirect: ./";
	}

	// �ھ� id �d�ߵ� update/edit �ϥ�
	@GetMapping("/{id}")
	public String getUserById(Model model, @PathVariable Long id) {
		User user = userRepository.findOne(id);
		List<User> users = userRepository.findAll();
		model.addAttribute("user", user);
		model.addAttribute("users", users);
		model.addAttribute("_method", "PUT");
		return "user/index"; // ���ɨ� /WEB-INF/view/user/index.jsp
	}
	
	// �ھ� id �d�ߵ� delete �ϥ�
		@GetMapping("/delete/{id}")
		public String getUserById4Del(Model model, @PathVariable Long id) {
			User user = userRepository.findOne(id);
			List<User> users = userRepository.findAll();
			model.addAttribute("user", user);
			model.addAttribute("users", users);
			model.addAttribute("_method", "DELETE");
			return "user/index"; // ���ɨ� /WEB-INF/view/user/index.jsp
		}
		
		// �d�ߤ���
		// ���|�d��:/page,/page?no=1, /page?no=10 etc...
		@GetMapping("/page")
		public String userPage(Model model,@RequestParam(name = "no",required = false,defaultValue = "0") Integer no) {
			int pageNo = no;
			int pageSize = 10;
			// �Ƨ�
			Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
			
			Sort sort = new Sort(order);

			// �����ШD
			PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
			Page<User> page = userRepository.findAll(pageRequest);
			model.addAttribute("users", page.getContent());
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("totalPage", page.getTotalPages());
			return "user/page"; //���ɨ� /WEB-INF/view/user/page.jsp
		}

			

	// -----------------------------
	// �H�U�O����user���{��
	// �s�W�d�Ҹ��
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
			// �x�s���Ʈw
			userRepository.saveAndFlush(user);
		}

		return "Create sample data ok !";
	}

	// �d�߽d�Ҹ�� 1
	@GetMapping("/test/findall")
	@ResponseBody
	public List<User> testFindall() {
		List<User> users = userRepository.findAll();

		return users;
	}

	// �d�߽d�Ҹ�� 2
	@GetMapping("/test/findall_sort")
	@ResponseBody
	public List<User> testFindallSort() {
		// ASC�۵M�Ƨ� �p��j,DESC �j��p
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		List<User> users = userRepository.findAll();

		return users;
	}

	// �d�߽d�Ҹ�� 3
	@GetMapping("/test/findall_ids")
	@ResponseBody
	public List<User> testFindallIds() {
		// �d����wid�����
		Iterable<Long> ids = Arrays.asList(1L, 3L, 5L);
		List<User> users = userRepository.findAll(ids);

		return users;
	}

	// �d�߽d�Ҹ�� 4
	@GetMapping("/test/findall_example")
	@ResponseBody
	public List<User> testFindallExample() {
		User user = new User();
		user.setId(2L);
		user.setPassword("1234");
		// �ھ�Example �Ҵ��Ѫ����(�Ҧp:user)�Ӭd��
		Example<User> example = Example.of(user);
		List<User> users = userRepository.findAll(example);

		return users;
	}

	// �d�߽d�Ҹ�� 4
	@GetMapping("/test/findall_example2")
	@ResponseBody
	public List<User> testFindallExample2() {
		User user = new User();
		user.setName("a");
		// name�����e�O�_���]�t"a"
		// �إ�ExampleMatcher��ﾹ
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name",
				ExampleMatcher.GenericPropertyMatchers.contains());
		Example<User> example = Example.of(user, matcher);
		List<User> users = userRepository.findAll(example);

		return users;
	}

	// �d�߽d�Ҹ�� 4
	@GetMapping("/test/get_one")
	@ResponseBody
	public User getOne() {

		return userRepository.findOne(5L);
	}

	// �d�ߤ���
	@GetMapping("/test/page/{no}")
	@ResponseBody
	public List<User> testPage(@PathVariable("no") Integer no) {
		int pageNo = no;
		int pageSize = 10;
		// �Ƨ�
		Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "name");
		Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "name");
		Sort sort = new Sort(order1, order2);

		// �����ШD
		PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
		Page<User> page = userRepository.findAll(pageRequest);

		return page.getContent();
	}

	@GetMapping("test/name")
	@ResponseBody
	public List<User> getByName(@RequestParam("name") String name) {
		return userRepository.getByName(name);
	}

	// ����url:/mvc/user/test/name/id/S/50
	@GetMapping("test/name/id/{name}/{id}")
	@ResponseBody
	public List<User> getByNameAndId(@PathVariable("name") String name, @PathVariable("id") Long id) {
		return userRepository.getByNameStartingWithAndIdGreaterThanEqual(name, id);
	}

	// ����url:/mvc/user/test/ids?ids=5,10,15
	@GetMapping("test/ids")
	@ResponseBody
	public List<User> getByIds(@RequestParam("ids") List<Long> ids) {
		return userRepository.getByIdIn(ids);
	}

	// ����url:/mvc/user/test/birth?birth=2000-9-9
	@GetMapping("test/birth")
	@ResponseBody
	public List<User> getByBirthLessThan(@RequestParam("birth") @DateTimeFormat(iso = ISO.DATE) Date birth) {
		return userRepository.getByBirthLessThan(birth);
	}

	// ����url:/mvc/user/test/birth_between?begin=1965-1-1&end=1970-12-31
	@GetMapping("test/birth_between")
	@ResponseBody
	public List<User> getByBirthBetween(@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) Date begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) Date end) {
		return userRepository.getByBirthBetween(begin, end);
	}
}
