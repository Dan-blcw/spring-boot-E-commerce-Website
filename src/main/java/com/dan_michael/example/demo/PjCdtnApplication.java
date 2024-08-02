package com.dan_michael.example.demo;

import com.dan_michael.example.demo.chat_socket.entities.Status;
import com.dan_michael.example.demo.chat_socket.entities.UserAccountInfo;
import com.dan_michael.example.demo.chat_socket.service.UserAccountInfoService;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.dtos.QuestionOfGuestInfoDtos;
import com.dan_michael.example.demo.chatbot.service.ChatbotService;
import com.dan_michael.example.demo.model.dto.global.RegisterDtos;
import com.dan_michael.example.demo.model.dto.ob.*;

import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;

import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import com.dan_michael.example.demo.service.AuthenticationService;
import com.dan_michael.example.demo.service.CategoryService;
import com.dan_michael.example.demo.service.Payment.PaymentMethodsService;
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
			CategoryService categoryService,
			UserAccountInfoService userAccountInfoService,
			ChatbotService chatbotService,
			PaymentMethodsService paymentMethodsService
	) {
		return args -> {

			var admin = RegisterDtos.builder()
					.email("admin@mail.com")
					.name("Admin")
					.password("0")
					.build();
			System.out.println("Admin token: " + service.createAdmin(admin));

			var user = RegisterDtos.builder()
					.email("phuhuong646@gmail.com")
					.name("Dan")
					.password("0")
					.build();
			System.out.println("User token: " + service.register(user).getJwt());
//----------------------Tạo Tài Khoản ChatBot ----------------------------------------------
			var chatbot = RegisterDtos.builder()
					.email("chatbot@mail.com")
					.name("Aza Chōbei Assistant")
					.password("0")
					.build();
			service.createAdmin(chatbot);
			userAccountInfoService.saveUser(UserAccountInfo.builder()
					.name("Aza Chōbei Assistant")
					.fullName("Aza Chōbei Assistant")
					.status(Status.ONLINE)
					.build());

			var responseAI1 = QuestionAnswer.builder()
					.question("What is e-commerce?")
					.answer("E-commerce is the buying and selling of goods or services using the internet.")
					.build();

			var responseAI2 = QuestionAnswer.builder()
					.question("What are the types of e-commerce?")
					.answer("The types of e-commerce are B2B, B2C, C2C, and C2B.")
					.build();

			var responseAI3 = QuestionAnswer.builder()
					.question("What is a payment gateway?")
					.answer("A payment gateway is a technology that reads and transfers payment information from a customer to a merchant's bank account.")
					.build();

			var responseAI4 = QuestionAnswer.builder()
					.question("What is shopping cart software?")
					.answer("Shopping cart software is a software that allows customers to select and store items for purchase.")
					.build();

			var responseAI5 = QuestionAnswer.builder()
					.question("What is dropshipping?")
					.answer("Dropshipping is a retail fulfillment method where a store doesn't keep the products it sells in stock.")
					.build();

			var responseAI6 = QuestionAnswer.builder()
					.question("What is inventory management?")
					.answer("Inventory management is the process of ordering, storing, and using a company's inventory.")
					.build();

			var responseAI7 = QuestionAnswer.builder()
					.question("What is an e-commerce platform?")
					.answer("An e-commerce platform is a software application that allows online businesses to manage their website, marketing, sales, and operations.")
					.build();

			var responseAI8 = QuestionAnswer.builder()
					.question("What is SSL in e-commerce?")
					.answer("SSL (Secure Sockets Layer) is a standard security technology for establishing an encrypted link between a server and a client.")
					.build();

			var responseAI9 = QuestionAnswer.builder()
					.question("What is PCI DSS?")
					.answer("PCI DSS (Payment Card Industry Data Security Standard) is a set of security standards designed to ensure that all companies that accept, process, store, or transmit credit card information maintain a secure environment.")
					.build();

			var responseAI10 = QuestionAnswer.builder()
					.question("What is customer relationship management (CRM)?")
					.answer("CRM is a technology for managing all your company's relationships and interactions with current and potential customers.")
					.build();

			var responseAI11 = QuestionAnswer.builder()
					.question("What is SEO in e-commerce?")
					.answer("SEO (Search Engine Optimization) is the practice of increasing the quantity and quality of traffic to your website through organic search engine results.")
					.build();

			var responseAI12 = QuestionAnswer.builder()
					.question("What is the difference between multichannel and omnichannel?")
					.answer("Multichannel refers to using multiple channels to reach customers, while omnichannel means providing a seamless experience across all channels.")
					.build();

			var responseAI13 = QuestionAnswer.builder()
					.question("What is mobile commerce (m-commerce)?")
					.answer("M-commerce is the buying and selling of goods and services through wireless handheld devices such as smartphones and tablets.")
					.build();

			var responseAI14 = QuestionAnswer.builder()
					.question("What is a fulfillment center?")
					.answer("A fulfillment center is a third-party logistics (3PL) warehouse where incoming orders are received, processed, and fulfilled.")
					.build();

			var responseAI15 = QuestionAnswer.builder()
					.question("What is an abandoned cart?")
					.answer("An abandoned cart refers to when a customer adds items to their online shopping cart but leaves the site without completing the purchase.")
					.build();

			var responseAI16 = QuestionAnswer.builder()
					.question("What is cross-selling?")
					.answer("Cross-selling is the practice of selling an additional product or service to an existing customer.")
					.build();

			var responseAI17 = QuestionAnswer.builder()
					.question("What is up-selling?")
					.answer("Up-selling is the practice of encouraging customers to purchase a more expensive item or add-ons to increase the value of the sale.")
					.build();

			var responseAI18 = QuestionAnswer.builder()
					.question("What is a return policy?")
					.answer("A return policy is a set of rules established by a retailer to manage how customers can return or exchange unwanted products.")
					.build();

			var responseAI19 = QuestionAnswer.builder()
					.question("What is a customer loyalty program?")
					.answer("A customer loyalty program is a rewards program offered by a company to customers who frequently make purchases.")
					.build();

			var responseAI20 = QuestionAnswer.builder()
					.question("What is a product feed?")
					.answer("A product feed is a file containing a list of products and their attributes used to provide information to e-commerce platforms and marketplaces.")
					.build();
			chatbotService.createQuestionAnswer(responseAI1);
			chatbotService.createQuestionAnswer(responseAI2);
			chatbotService.createQuestionAnswer(responseAI3);
			chatbotService.createQuestionAnswer(responseAI4);
			chatbotService.createQuestionAnswer(responseAI5);
			chatbotService.createQuestionAnswer(responseAI6);
			chatbotService.createQuestionAnswer(responseAI7);
			chatbotService.createQuestionAnswer(responseAI8);
			chatbotService.createQuestionAnswer(responseAI9);
			chatbotService.createQuestionAnswer(responseAI10);
			chatbotService.createQuestionAnswer(responseAI11);
			chatbotService.createQuestionAnswer(responseAI12);
			chatbotService.createQuestionAnswer(responseAI13);
			chatbotService.createQuestionAnswer(responseAI14);
			chatbotService.createQuestionAnswer(responseAI15);
			chatbotService.createQuestionAnswer(responseAI16);
			chatbotService.createQuestionAnswer(responseAI17);
			chatbotService.createQuestionAnswer(responseAI18);
			chatbotService.createQuestionAnswer(responseAI19);
			chatbotService.createQuestionAnswer(responseAI20);
			/*
				What is e-commerce?
				What are the types of e-commerce?
				What is a payment gateway?
				What is shopping cart software?
				What is dropshipping?
				What is inventory management?
				What is an e-commerce platform?
				What is SSL in e-commerce?
				What is PCI DSS?
				What is customer relationship management (CRM)?
				What is SEO in e-commerce?
				What is the difference between multichannel and omnichannel?
				What is mobile commerce (m-commerce)?
				What is a fulfillment center?
				What is an abandoned cart?
				What is cross-selling?
				What is up-selling?
				What is a return policy?
				What is a customer loyalty program?
				What is a product feed?
			*/
			var Question0 = QuestionOfGuestInfoDtos.builder()
					.question("Câu Hỏi Cho Khách Hàng 0?")
					.name("Dan")
					.email("phuhuong646@gmail.com")
					.phone("54645")
					.build();
			var Question1 = QuestionOfGuestInfoDtos.builder()
					.question("Câu Hỏi Cho Khách Hàng 1?")
					.name("Dan")
					.email("phuhuong646@gmail.com")
					.phone("54645")
					.build();
			chatbotService.createQuestionForGuest(Question0);
			chatbotService.createQuestionForGuest(Question1);
//----------------------Payment Method ..... ----------------------------------------------
			var paymentMethod0 = PaymentMethodsDtos.builder()
					.description("Thanh toán khi nhận hàng là hình thức mà người mua hàng sẽ thanh toán trực tiếp cho nhân viên giao hàng hoặc người bán tại thời điểm nhận hàng. Điều này có nghĩa là người mua không cần phải trả tiền trước khi hàng hóa được giao đến")
					.status(1)
					.paymentMethodsName("Thanh Toán Khi Nhận Hàng (COD)")
					.build();
			var paymentMethod1 = PaymentMethodsDtos.builder()
					.description("Quét mã QR là phương thức thanh toán trong đó người mua sử dụng ứng dụng ngân hàng hoặc ví điện tử để quét mã QR của người bán và thực hiện thanh toán trực tiếp.")
					.status(1)
					.paymentMethodsName("Quét Mã QR")
					.build();
			var paymentMethod2 = PaymentMethodsDtos.builder()
					.description("VNPay là một trong những cổng thanh toán trực tuyến hàng đầu tại Việt Nam, cung cấp dịch vụ thanh toán điện tử và chuyển tiền trực tuyến cho cả khách hàng cá nhân và doanh nghiệp. VNPay cung cấp nhiều giải pháp thanh toán đa dạng")
					.status(1)
					.paymentMethodsName("VNPay")
					.build();
			var paymentMethod3 = PaymentMethodsDtos.builder()
					.description("PayPal là một dịch vụ thanh toán trực tuyến quốc tế, cho phép các cá nhân và doanh nghiệp gửi và nhận tiền qua mạng Internet. Được thành lập vào năm 1998 và sau đó được mua lại bởi eBay vào năm 2002, PayPal đã trở thành một trong những phương thức thanh toán trực tuyến phổ biến và đáng tin cậy nhất trên thế giới.")
					.status(1)
					.paymentMethodsName("PayPal")
					.build();
			paymentMethodsService.createPaymentMethods(paymentMethod0);
			paymentMethodsService.createPaymentMethods(paymentMethod1);
			paymentMethodsService.createPaymentMethods(paymentMethod2);
			paymentMethodsService.createPaymentMethods(paymentMethod3);
//----------------------Category - Brands----------------------------------------------
			List<String> brands0 = new ArrayList<>();
			brands0.add("Áo Sơ Mi");
			brands0.add("Áo Sơ Thun");
			brands0.add("Áo Khoác");
			brands0.add("Áo Quần què");

			List<String> brands1 = new ArrayList<>();
			brands1.add("Quần short");
			brands1.add("Quần short_1");
			brands1.add("Quần short_2");
			brands1.add("Quần short_3");

			CategoryDtos category0 = CategoryDtos.builder()
					.sku("576842548")
					.categoryName("Áo")
					.brands(brands0)
					.status(1)
					.build();

			CategoryDtos category1 = CategoryDtos.builder()
					.sku("JDFH6725")
					.categoryName("Quần")
					.brands(brands1)
					.status(1)
					.build();

			categoryService.createCategory(category0);
			categoryService.createCategory(category1);

//			size & Quantity
			List<SubSizeQuantity> subSizeQuantityList0 = new ArrayList<>();
			List<SubSizeQuantity> subSizeQuantityList1 = new ArrayList<>();
			List<SubSizeQuantity> subSizeQuantityList2 = new ArrayList<>();
			List<SubSizeQuantity> subSizeQuantityList3 = new ArrayList<>();
			List<SubSizeQuantity> subSizeQuantityList4 = new ArrayList<>();
			List<SubSizeQuantity> subSizeQuantityList5 = new ArrayList<>();
			List<SubSizeQuantity> subSizeQuantityList6 = new ArrayList<>();
			List<SubSizeQuantity> subSizeQuantityList7 = new ArrayList<>();
			List<SubSizeQuantity> subSizeQuantityList8 = new ArrayList<>();
//			Red
			SubSizeQuantity subSizeQuantity0 = SubSizeQuantity.builder()
					.size("small")
					.quantity(56)
					.build();
			SubSizeQuantity subSizeQuantity1 = SubSizeQuantity.builder()
					.size("medium")
					.quantity(12).build();
//			blue
			SubSizeQuantity subSizeQuantity2 = SubSizeQuantity.builder()
					.size("large")
					.quantity(60).build();
			SubSizeQuantity subSizeQuantity3 = SubSizeQuantity.builder()
					.size("superSmall")
					.quantity(50).build();
//			black
			SubSizeQuantity subSizeQuantity4 = SubSizeQuantity.builder()
					.size("superMedium")
					.quantity(10).build();
			SubSizeQuantity subSizeQuantity5 = SubSizeQuantity.builder()
					.size("superLarge")
					.quantity(80).build();

			SubSizeQuantity subSizeQuantity6 = SubSizeQuantity.builder()
					.size("small")
					.quantity(4753)
					.build();
			SubSizeQuantity subSizeQuantity7 = SubSizeQuantity.builder()
					.size("medium")
					.quantity(43).build();
//			blue
			SubSizeQuantity subSizeQuantity8 = SubSizeQuantity.builder()
					.size("large")
					.quantity(87).build();
			SubSizeQuantity subSizeQuantity9 = SubSizeQuantity.builder()
					.size("superSmall")
					.quantity(43).build();
//			black
			SubSizeQuantity subSizeQuantity10 = SubSizeQuantity.builder()
					.size("superMedium")
					.quantity(42).build();
			SubSizeQuantity subSizeQuantity11 = SubSizeQuantity.builder()
					.size("superLarge")
					.quantity(42).build();


			subSizeQuantityList0.add(subSizeQuantity0);
			subSizeQuantityList0.add(subSizeQuantity1);

			subSizeQuantityList1.add(subSizeQuantity4);
			subSizeQuantityList1.add(subSizeQuantity5);

			subSizeQuantityList2.add(subSizeQuantity2);
			subSizeQuantityList2.add(subSizeQuantity3);
//
//			subSizeQuantityList3.add(subSizeQuantity6);
//			subSizeQuantityList4.add(subSizeQuantity7);
//
//			subSizeQuantityList5.add(subSizeQuantity8);
//			subSizeQuantityList5.add(subSizeQuantity9);
//
//			subSizeQuantityList6.add(subSizeQuantity10);
//			subSizeQuantityList6.add(subSizeQuantity11);

//			Color & SubSizeQuantity
			SubColor sub0 = SubColor.builder()
					.color("Đỏ")
					.sizes(subSizeQuantityList0)
					.build();
//			red medium - 12
			SubColor sub1 = SubColor.builder()
					.color("Xanh Dương")
					.sizes(subSizeQuantityList1)
					.build();

			SubColor sub2 = SubColor.builder()
					.color("Đen")
					.sizes(subSizeQuantityList2)
					.build();
//			Color & SubSizeQuantity
			SubColor sub3 = SubColor.builder()
					.color("Trắng")
					.sizes(subSizeQuantityList3)
					.build();
//			red medium - 12
			SubColor sub4 = SubColor.builder()
					.color("blue")
					.sizes(subSizeQuantityList4)
					.build();

			List<SubColor> listsub1 = new ArrayList<>();
			List<SubColor> listsub2 = new ArrayList<>();



			listsub1.add(sub0);
			listsub1.add(sub1);

			listsub2.add(sub2);
//----------------------Sản phẩm ..... ----------------------------------------------
			var pro0 = ProductDtos.builder()
					.name("Áo Sơ Mi Cuban Netstripes")
					.description("Áo sơ mi Cuban Netstripes là một kiểu áo sơ mi phổ biến có nguồn gốc từ Cuba, được biết đến với phong cách đặc trưng và thoải mái.")
					.quantityDetails(listsub1)
					.category("Áo")
					.subCategory("Áo Sơ Mi")
					.tradeMask("Netstripes")
					.originalPrice(1000000.0f)
					.saleDiscountPercent(10.0f)
					.createdByUserid(1)
					.build();
			var pro1 = ProductDtos.builder()
					.name("Quần short Slim Denim Back Devipock")
					.description("Quần short Slim Denim Back Devipock là một sản phẩm thời trang hiện đại, kết hợp giữa sự thoải mái và phong cách.")
					.quantityDetails(listsub2)
					.category("Quần")
					.subCategory("Quần Short")
					.tradeMask("Devipock")
					.originalPrice(800000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(2)
					.build();
			var pro2 = ProductDtos.builder()
					.name("Áo sơ mi Denim Cloud Gray BTW")
					.description("Áo sơ mi Denim Cloud Gray BTW là một sản phẩm thời trang phổ biến, thường được làm từ chất liệu vải denim chất lượng cao")
					.subCategory("Áo sơ mi")
					.quantityDetails(listsub2)
					.category("Áo")
					.tradeMask("BTW")
					.originalPrice(8000000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro3 = ProductDtos.builder()
					.name("Áo Sơ Mi Artemisia Basic")
					.description("Áo sơ mi Artemisia Basic là một sản phẩm thời trang phổ biến với thiết kế đơn giản nhưng tinh tế.")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro4 = ProductDtos.builder()
					.name("Product 4")
					.description("description 4")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Thun")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro5 = ProductDtos.builder()
					.name("Product 5")
					.description("description 5")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro6 = ProductDtos.builder()
					.name("Product 6")
					.description("description 6")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro7 = ProductDtos.builder()
					.name("Product 7")
					.description("description 7")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro8 = ProductDtos.builder()
					.name("Product 8")
					.description("description 8")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro9 = ProductDtos.builder()
					.name("Product 9")
					.description("description 9")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro10 = ProductDtos.builder()
					.name("Product 10")
					.description("description 10")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro11 = ProductDtos.builder()
					.name("Product 11")
					.description("description 11")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro12 = ProductDtos.builder()
					.name("Product 12")
					.description("description 12")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro13 = ProductDtos.builder()
					.name("Product 13")
					.description("description 13")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro14 = ProductDtos.builder()
					.name("Product 14")
					.description("description 14")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro15 = ProductDtos.builder()
					.name("Product 15")
					.description("description 15")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro16 = ProductDtos.builder()
					.name("Product 16")
					.description("description 16")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro17 = ProductDtos.builder()
					.name("Product 17")
					.description("description 17")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro18 = ProductDtos.builder()
					.name("Product 18")
					.description("description 18")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro19 = ProductDtos.builder()
					.name("Product 19")
					.description("description 19")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro20 = ProductDtos.builder()
					.name("Product 20")
					.description("description 20")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro21 = ProductDtos.builder()
					.name("Product 21")
					.description("description 21")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(23500000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
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
			var pro2Response = productService.createProduct(pro2);
			var pro3Response = productService.createProduct(pro3);
			var pro4Response = productService.createProduct(pro4);
			var pro5Response = productService.createProduct(pro5);
			var pro6Response = productService.createProduct(pro6);
			var pro7Response = productService.createProduct(pro7);
			var pro8Response = productService.createProduct(pro8);
			var pro9Response = productService.createProduct(pro9);
			var pro10Response = productService.createProduct(pro10);
			var pro11Response = productService.createProduct(pro11);
			var pro12Response = productService.createProduct(pro12);
			var pro13Response = productService.createProduct(pro13);
			var pro14Response = productService.createProduct(pro14);
			var pro15Response = productService.createProduct(pro15);
			var pro16Response = productService.createProduct(pro16);
			var pro17Response = productService.createProduct(pro17);
			var pro18Response = productService.createProduct(pro18);
			var pro19Response = productService.createProduct(pro19);
			var pro20Response = productService.createProduct(pro20);
			var pro21Response = productService.createProduct(pro21);

			productService.createComment(comment0,pro0Response.getId());
			productService.createComment(comment1,pro0Response.getId());
			productService.createComment(comment2,pro1Response.getId());
			productService.createComment(comment1,pro3Response.getId());
			productService.createComment(comment2,pro3Response.getId());
			productService.createComment(comment0,pro4Response.getId());
			productService.createComment(comment2,pro4Response.getId());
			productService.createComment(comment1,pro5Response.getId());
			productService.createComment(comment0,pro6Response.getId());
			productService.createComment(comment2,pro7Response.getId());
			productService.createComment(comment1,pro8Response.getId());
			productService.createComment(comment1,pro8Response.getId());
			productService.createComment(comment0,pro9Response.getId());
			productService.createComment(comment2,pro10Response.getId());
			productService.createComment(comment1,pro11Response.getId());
			productService.createComment(comment2,pro11Response.getId());
			productService.createComment(comment2,pro12Response.getId());
			productService.createComment(comment1,pro13Response.getId());
			productService.createComment(comment1,pro14Response.getId());
			productService.createComment(comment0,pro15Response.getId());
			productService.createComment(comment1,pro16Response.getId());
			productService.createComment(comment2,pro16Response.getId());
			productService.createComment(comment2,pro17Response.getId());
			productService.createComment(comment1,pro18Response.getId());
			productService.createComment(comment1,pro19Response.getId());
			productService.createComment(comment0,pro20Response.getId());
			productService.createComment(comment1,pro21Response.getId());
			productService.createComment(comment2,pro21Response.getId());
//---------------------------------------------------------------------------------------
			var trademask_0 = TradeMaskDtos.builder()
					.tradeMarkSku("NS-001")
					.tradeMarkName("Burberry")
					.description("Burberry là thương hiệu thời trang cao cấp của Anh, nổi tiếng với các sản phẩm từ chất liệu cao cấp, bao gồm áo khoác trench, quần áo, túi xách và phụ kiện.")
					.build();
			var trademask_1 = TradeMaskDtos.builder()
					.tradeMarkSku("DP-001")
					.tradeMarkName("Devipock")
					.description("Devipock là thương hiệu thời trang tiên phong, tập trung vào các thiết kế độc đáo và sáng tạo. Các sản phẩm của Devipock bao gồm quần áo, giày dép và phụ kiện với phong cách hiện đại và phá cách. Thương hiệu này cam kết sử dụng chất liệu cao cấp và thân thiện với môi trường, nhằm mang lại sự thoải mái và phong cách cho khách hàng.")
					.build();
			var trademask_2 = TradeMaskDtos.builder()
					.tradeMarkSku("BU-001")
					.tradeMarkName("Devipock")
					.description("Devipock là thương hiệu thời trang tiên phong, tập trung vào các thiết kế độc đáo và sáng tạo. Các sản phẩm của Devipock bao gồm quần áo, giày dép và phụ kiện với phong cách hiện đại và phá cách. Thương hiệu này cam kết sử dụng chất liệu cao cấp và thân thiện với môi trường, nhằm mang lại sự thoải mái và phong cách cho khách hàng.")
					.build();
			productService.saveTrask(trademask_0);
			productService.saveTrask(trademask_1);
			productService.saveTrask(trademask_2);
		};

	}

}