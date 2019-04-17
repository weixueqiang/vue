package com.example.demo.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.CallResult;

/**
 * 
 * 前台页面控制器
 * 
 * @author weixueqiang
 * @version 1.0.0
 * @date 2018年10月22日 上午10:11:21
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

	@RequestMapping("votest")
	public CallResult votest(@Valid TestVo testVo) {
		System.out.println(testVo);
		return CallResult.ok();
	}

	@RequestMapping("test")
	public CallResult test(@RequestBody @Valid TestVo testVo) {
		System.out.println(testVo);
		return CallResult.ok();
	}

	@RequestMapping("voList")
	public CallResult voList(@RequestBody @Valid List<Child> testVo) {
		System.out.println(testVo);
		return CallResult.ok();
	}

	@RequestMapping("voList2")
	public CallResult voList2(@RequestBody @Valid List<Child> testVo, HttpSession session) {
		System.out.println(testVo);
		return CallResult.ok();
	}

}

class Simple implements Serializable {
	private Integer salary;

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

}

class TestVo implements Serializable {
	@NotBlank(message = "名称不能为空")
	private String name;
	@NotNull(message = "年龄不能为空")
	private Integer age;

	// @Pattern(regexp = "\\d*", message = "必须为整数")
	private Integer high;
	@Pattern(regexp = "\\d*", message = "必须为整数")
	private String high2;

	private Integer salary;

	@NotNull(message = "时间不能为空")
	private Date time;

	@NotEmpty(message = "子集不能为空")
	@Valid
	private List<Child> children;

	@Override
	public String toString() {
		return "TestVo [name=" + name + ", age=" + age + ", high=" + high + ", high2=" + high2 + ", salary=" + salary
				+ ", time=" + time + ", children=" + children + "]";
	}

	public Integer getHigh() {
		return high;
	}

	public void setHigh(Integer high) {
		this.high = high;
	}

	public String getHigh2() {
		return high2;
	}

	public void setHigh2(String high2) {
		this.high2 = high2;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public List<Child> getChildren() {
		return children;
	}

	public void setChildren(List<Child> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}

class Child implements Serializable {

	@NotBlank(message = "标题不能为空")
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Child [title=" + title + "]";
	}

}
