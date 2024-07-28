package com.dan_michael.example.demo;

import com.dan_michael.example.demo.model.dto.global.RegisterDtos;
import com.dan_michael.example.demo.model.dto.ob.CategoryDtos;
import com.dan_michael.example.demo.model.dto.ob.CommentDto;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;

import com.dan_michael.example.demo.model.dto.ob.sub.SubQuantity;

import com.dan_michael.example.demo.service.AuthenticationService;
import com.dan_michael.example.demo.service.CategoryService;
import com.dan_michael.example.demo.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PjCdtnApplication {

	public static void main(String[] args) {
		SpringApplication.run(PjCdtnApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			ProductService productService,
			CategoryService categoryService
	) {
		return args -> {


			var admin = RegisterDtos.builder()
					.email("admin@mail.com")
					.name("Admin")
					.password("password")
					.build();
			System.out.println("Admin token: " + service.createAdmin(admin));

			var user = RegisterDtos.builder()
					.email("phuhuong646@gmail.com")
					.name("Dan")
					.password("password")
					.build();
			System.out.println("User token: " + service.register(user).getJwt());


			List<String> brands0 = new ArrayList<>();
			brands0.add("BrandA");
			brands0.add("BrandB");
			brands0.add("BrandC");
			brands0.add("BrandD");

			List<String> brands1 = new ArrayList<>();
			brands1.add("BrandC");
			brands1.add("BrandD");
			brands1.add("BrandE");
			brands1.add("BrandF");
			CategoryDtos category0 = CategoryDtos.builder()
					.sku("JDFH6725")
					.categoryName("Category 2")
					.brands(brands1)
					.status(1)
					.build();

			CategoryDtos category1 = CategoryDtos.builder()
					.sku("576842548")
					.categoryName("Sample Category")
					.brands(brands1)
					.status(1)
					.build();
			categoryService.createCategory(category0);
			categoryService.createCategory(category1);

			SubQuantity sub0 = SubQuantity.builder()
					.color("Red")
					.size("Small")
					.quantity(5).build();
			SubQuantity sub1 = SubQuantity.builder()
					.color("Red")
					.size("Medium")
					.quantity(12).build();
			SubQuantity sub2 = SubQuantity.builder()
					.color("Red")
					.size("Large")
					.quantity(6).build();
			SubQuantity sub3 = SubQuantity.builder()
					.color("Blue")
					.size("Small")
					.quantity(50).build();
			SubQuantity sub4 = SubQuantity.builder()
					.color("Blue")
					.size("Medium")
					.quantity(10).build();
			SubQuantity sub5 = SubQuantity.builder()
					.color("Blue")
					.size("Large")
					.quantity(80).build();
			List<SubQuantity> listsub1 = new ArrayList<>();
			List<SubQuantity> listsub2 = new ArrayList<>();
			listsub1.add(sub0);
			listsub1.add(sub1);
			listsub1.add(sub2);
			listsub1.add(sub3);
			listsub1.add(sub4);
			listsub1.add(sub5);

			listsub2.add(sub3);
			listsub2.add(sub4);
			listsub2.add(sub5);

			var pro0 = ProductDtos.builder()
					.name("Product DTO 1")
					.description("Description for product DTO 1")
					.quantityDetail(listsub1)
					.category("Sample Category")
					.brand("BrandA")
					.favourite(true)
					.originalPrice(100.0f)
					.saleDiscountPercent(10.0f)
					.saleStatus(true)
					.newStatus(true)
					.createdByUserid(1)
					.build();
			var pro1 = ProductDtos.builder()
					.name("Product DTO 2")
					.description("Description for product DTO 2")
					.quantityDetail(listsub2)
					.category("Category 2")
					.brand("BrandC")
					.favourite(false)
					.originalPrice(200.0f)
					.saleDiscountPercent(20.0f)
					.saleStatus(true)
					.newStatus(false)
					.createdByUserid(2)
					.build();

			var comment0 = CommentDto.builder()
					.content("This is a great product!")
					.rating(4.5f)
					.productQuality("High")
					.username("Admin")
					.build();
			var comment1 = CommentDto.builder()
					.content("Not satisfied with the quality.")
					.rating(2.0f)
					.productQuality("Low")
					.username("Dan")
					.build();
			var comment2 = CommentDto.builder()
					.content("Very good value for money.")
					.rating(5.0f)
					.productQuality("Excellent")
					.username("Dan")
					.build();
			var pro0Response =productService.createProduct(pro0);
			var pro1Response = productService.createProduct(pro1);

			productService.createComment(comment0,pro0Response.getId());
			productService.createComment(comment1,pro0Response.getId());
			productService.createComment(comment2,pro1Response.getId());
		};
	}

}