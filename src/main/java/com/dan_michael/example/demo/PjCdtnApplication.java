package com.dan_michael.example.demo;

import com.dan_michael.example.demo.model.dto.global.RegisterDtos;
import com.dan_michael.example.demo.model.dto.ob.CommentDto;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.model.entities.SubEn.Brands;
import com.dan_michael.example.demo.model.entities.SubEn.Colors;
import com.dan_michael.example.demo.model.entities.SubEn.FavouriteProduct;
import com.dan_michael.example.demo.model.entities.SubEn.Sizes;
import com.dan_michael.example.demo.service.AuthenticationService;
import com.dan_michael.example.demo.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
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
			ProductService productService
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

			var pro0 = ProductDtos.builder()
					.name("Product DTO 1")
					.description("Description for product DTO 1")
					.quantity(100)
					.category("Category 1")
					.colours(List.of("Red", "Blue"))
					.sizes(List.of("S", "M", "L"))
					.brands(List.of("Brand A", "Brand B"))
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
					.quantity(200)
					.category("Category 2")
					.colours(List.of("Green", "Yellow"))
					.sizes(List.of("M", "L", "XL"))
					.brands(List.of("Brand C", "Brand D"))
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