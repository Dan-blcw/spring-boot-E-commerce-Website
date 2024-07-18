package com.dan_michael.example.demo;

import com.dan_michael.example.demo.model.dto.global.RegisterDtos;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.service.AuthenticationService;
import com.dan_michael.example.demo.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Date;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PjCdtnApplication {

	public static void main(String[] args) {
		SpringApplication.run(PjCdtnApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {


			var admin = RegisterDtos.builder()
					.email("admin@mail.com")
					.name("Admin")
					.password("password")
					.build();
			System.out.println("Admin token: " + service.createAdmin(admin));

			var user = RegisterDtos.builder()
					.email("dan@mail.com")
					.name("dan")
					.password("password")
					.build();
			System.out.println("User token: " + service.register(user).getJwt());


//			var product_flag = Product.builder()
//					.images(request.getImages())
//					.name(request.getName())
//					.description(request.getDescription())
//					.price(request.getPrice())
//					.quantity(request.getQuantity())
//					.category(request.getCategory())
//					.colour(request.getColour())
//					.size(request.getSize())
//					.nRating(0)
//					.favourite(false)
//					.saleStatus(request.getSaleStatus())
//					.salePrice(request.getSalePrice())
//					.newStatus(request.getNewStatus())
//					.createDate(new Date())
//					.createdByUserid(request.getCreatedByUserid())
//					.build();
//
//			repository.save(product_flag);
		};
	}
}