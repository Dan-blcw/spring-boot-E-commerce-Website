package com.dan_michael.example.demo;

import com.dan_michael.example.demo.chat_socket.entities.Status;
import com.dan_michael.example.demo.chat_socket.entities.UserAccountInfo;
import com.dan_michael.example.demo.chat_socket.service.UserAccountInfoService;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.dtos.QuestionOfGuestInfoDtos;
import com.dan_michael.example.demo.chatbot.service.ChatbotService;
import com.dan_michael.example.demo.controller.GuestController;
import com.dan_michael.example.demo.controller.OrderController;
import com.dan_michael.example.demo.model.dto.global.RegisterDtos;
import com.dan_michael.example.demo.model.dto.ob.*;

import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;

import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import com.dan_michael.example.demo.model.entities.Role;
import com.dan_michael.example.demo.service.AuthenticationService;
import com.dan_michael.example.demo.service.CategoryService;
import com.dan_michael.example.demo.service.Payment.PaymentMethodsService;
import com.dan_michael.example.demo.service.ProductService;
import com.dan_michael.example.demo.util.Constants;
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
			PaymentMethodsService paymentMethodsService,
			GuestController guestController,
			OrderController orderController
	) {
		return args -> {
			List<String> images = new ArrayList<>();
			images.add("https://i.pinimg.com/564x/81/06/a7/8106a71477e9230cd5f7b0440c1db220.jpg");
			images.add("https://i.pinimg.com/564x/13/1a/45/131a455d54060e2b0180a1ee37e173c5.jpg");
			images.add("https://i.pinimg.com/736x/ba/34/85/ba34856fb7579d2d1118c2aa77737835.jpg");
			images.add("https://i.pinimg.com/564x/69/e5/bc/69e5bcccb9c3e6385827f18232b5c52c.jpg");
			images.add("https://i.pinimg.com/564x/13/0c/75/130c753071e9886c345715d8c3ab868e.jpg");
			images.add("https://i.pinimg.com/564x/74/04/0c/74040ca016ef22847ca483036a4643da.jpg");
			images.add("https://i.pinimg.com/564x/87/a1/73/87a1731e885854d779303a38e2ec3198.jpg");
			images.add("https://i.pinimg.com/564x/95/8f/a2/958fa290846ed1ce122643070a10074f.jpg");
			images.add("https://i.pinimg.com/564x/fd/69/32/fd6932452e50a5615723a5a151667d2f.jpg");
			images.add("https://i.pinimg.com/564x/ab/d7/3f/abd73f1825068a84dff0176cc2c02b1e.jpg");
			images.add("https://i.pinimg.com/564x/71/05/95/710595873b21bcf5e0912821ddf4cf61.jpg");

			images.add("https://i.pinimg.com/564x/83/c4/9f/83c49f3d411968498a9e9b2391429899.jpg");
			images.add("https://i.pinimg.com/564x/7f/12/a0/7f12a0220aadecd8ef79a9569acfc39f.jpg");
			images.add("https://i.pinimg.com/564x/c0/9b/b9/c09bb92b43e1804fee8637c37b0702c6.jpg");
			images.add("https://i.pinimg.com/564x/83/75/10/8375105393d5bbdadd2076ab72f1c497.jpg");
			images.add("https://i.pinimg.com/564x/d4/b6/82/d4b682db09a0e578fe60ac6293832366.jpg");
			images.add("https://i.pinimg.com/564x/fb/4a/88/fb4a884b9df7be75dab420159f3ffd24.jpg");
			images.add("https://i.pinimg.com/564x/09/69/8d/09698da63c7199c20fc9bac3e76decaa.jpg");
			images.add("https://i.pinimg.com/736x/44/d1/69/44d1690aa7ccdf62f2ff82a1ae2d4ce5.jpg");
			images.add("https://i.pinimg.com/564x/74/60/97/74609721fb58ef0f351b35eda2281a21.jpg");
			images.add("https://i.pinimg.com/564x/9d/8d/72/9d8d720e71cac03d6467a3c8b4c34085.jpg");
			images.add("https://i.pinimg.com/564x/4c/1a/b1/4c1ab177ae96e00f841be19924176b36.jpg");
			images.add("https://i.pinimg.com/564x/94/37/31/94373186e36a74925acf30bd9aea0a9f.jpg");
			images.add("https://i.pinimg.com/564x/2b/38/67/2b38670de40bf6710713f3c41c2d379e.jpg");
			images.add("https://i.pinimg.com/564x/a5/74/b2/a574b24e98ad5a17e4a2651a282de198.jpg");
			images.add("https://i.pinimg.com/564x/03/b6/50/03b650466842b5a5bb2b878f5c2c5fa1.jpg");
			images.add("https://i.pinimg.com/736x/ff/73/f7/ff73f715e1d9a9187b9c7387e12ba919.jpg");
			images.add("https://i.pinimg.com/736x/81/63/78/81637861f1566bb718979b454ce94eed.jpg");
			images.add("https://i.pinimg.com/736x/13/d2/e6/13d2e68e67bbd1e58c3aa5ef95c63331.jpg");
			images.add("https://i.pinimg.com/564x/09/b1/37/09b137b84a6cdfb0a1e9235a3394fe7e.jpg");
			images.add("https://i.pinimg.com/736x/3d/af/0b/3daf0b623cdfae5540b4ece7229a562d.jpg");
			images.add("https://i.pinimg.com/564x/04/6e/78/046e78d1f6119daa967f5335b778b5b5.jpg");
			images.add("https://i.pinimg.com/564x/5d/20/84/5d2084bea4d8bb7dfca769a5331a5b46.jpg");
			images.add("https://i.pinimg.com/564x/b9/fb/7c/b9fb7cd335d16eb5ca4e638f0cf3c818.jpg");
			images.add("https://i.pinimg.com/564x/e4/ae/96/e4ae9665b3bcd1809699751735fec17c.jpg");
			images.add("https://i.pinimg.com/564x/1a/d2/62/1ad262a326f12475826604e43e647c2f.jpg");
			images.add("https://i.pinimg.com/564x/d2/6e/f6/d26ef6e8479c816e07fb2c9fc62e3294.jpg");
			images.add("https://i.pinimg.com/736x/46/88/66/468866981478e7b63dbc5d8214a04f6e.jpg");
			images.add("https://i.pinimg.com/564x/95/5e/9a/955e9a42e6ecbdbac1826e62e5216a82.jpg");
			images.add("https://i.pinimg.com/564x/84/7c/26/847c26b9bc67059891a0880f393f4ee0.jpg");
			images.add("https://i.pinimg.com/564x/48/d4/06/48d406db66ad71736a079aa52c875897.jpg");
			images.add("https://i.pinimg.com/564x/cc/10/21/cc1021d86f6a53adb783d0cbdfe3032a.jpg");
			images.add("https://i.pinimg.com/564x/b3/1c/c8/b31cc811d1d4d57ea9924479064e6975.jpg");
			images.add("https://i.pinimg.com/736x/85/97/43/8597430660c7fec44b74cb3e695e7554.jpg");
			images.add("https://i.pinimg.com/564x/a4/6f/66/a46f66baefcd0a34a237551bea08acc5.jpg");
			images.add("https://i.pinimg.com/564x/c5/0a/5d/c50a5d5eb438d6c002a37d2747976ad2.jpg");
			images.add("https://i.pinimg.com/564x/f7/f0/b1/f7f0b19e0f4e642ae8f60e1612b16a40.jpg");
			images.add("https://i.pinimg.com/736x/66/59/c7/6659c7a56081a9578b3c6464719833c0.jpg");
			images.add("https://i.pinimg.com/564x/42/e7/a7/42e7a71c4e86b8b04826bd457a707d26.jpg");
			images.add("https://i.pinimg.com/564x/36/6a/c0/366ac03e6913d08d48a22bef2ddeced1.jpg");
			images.add("https://i.pinimg.com/564x/30/b6/fa/30b6faee98b6b44410fe8b7fe9bf55a6.jpg");
			images.add("https://i.pinimg.com/736x/dc/f2/51/dcf2513deef8948c5e7f67b81c217597.jpg");
			images.add("https://i.pinimg.com/564x/7f/a3/fc/7fa3fcd26a398190d94e6fa8d68bd8fa.jpg");
			images.add("https://i.pinimg.com/564x/0e/66/8d/0e668d2e37e7a2e3ac58ca8f61ed6ca2.jpg");
			images.add("https://i.pinimg.com/564x/80/2d/df/802ddf721747291f041cfddd942a7e16.jpg");
			images.add("https://i.pinimg.com/736x/83/d5/cc/83d5cc97bdc6330b89ddfc2c552d7c19.jpg");
			images.add("https://i.pinimg.com/736x/46/97/d2/4697d262b66200348baf700a3d230ae2.jpg");
			images.add("https://i.pinimg.com/736x/f4/0b/30/f40b3033e8667994bf1ce133eac0e5c5.jpg");
			images.add("https://i.pinimg.com/564x/13/1e/df/131edf984bcb9b939323a786b5c00bbb.jpg");
			images.add("https://i.pinimg.com/736x/f0/65/ef/f065ef0c1c543279bfb9819f2dc9adbc.jpg");
			images.add("https://i.pinimg.com/564x/14/23/3e/14233e28247f515cb7cf6ea2e75ff964.jpg");
			images.add("https://i.pinimg.com/564x/cc/ba/49/ccba49d1ab1d778b5aaacea40739e017.jpg");
			images.add("https://i.pinimg.com/564x/ce/b0/a9/ceb0a99754d241550a03276cc468289b.jpg");
			images.add("https://i.pinimg.com/736x/3a/f8/2a/3af82aedbc6634f2054ea0b2fc8190a5.jpg");
			images.add("https://i.pinimg.com/736x/dd/2f/b4/dd2fb488fed73916ba4e4f63d89c9739.jpg");
			guestController.post_All_ticker_Chat_Bot(images);
			var responseAI0 = QuestionAnswer.builder()
					.question(Constants.Start_Answer_Chat_Bot_)
					.answer("Chào bạn! \uD83D\uDC4B Tôi là "+ Constants.Chat_Bot_Name +"\nTôi ở đây để giúp bạn tìm sản phẩm hoặc giải đáp bất kỳ thắc mắc nào. Bạn cần tìm kiếm sản phẩm nào hay có câu hỏi gì về dịch vụ của chúng tôi? Đừng ngần ngại cho tôi biết, tôi sẵn sàng hỗ trợ bạn!")
					.build();

//			var responseAI1 = QuestionAnswer.builder()
//					.question("What is e-commerce?")
//					.answer("E-commerce is the buying and selling of goods or services using the internet.")
//					.build();
//
//			var responseAI2 = QuestionAnswer.builder()
//					.question("What are the types of e-commerce?")
//					.answer("The types of e-commerce are B2B, B2C, C2C, and C2B.")
//					.build();
//
//			var responseAI3 = QuestionAnswer.builder()
//					.question("What is a payment gateway?")
//					.answer("A payment gateway is a technology that reads and transfers payment information from a customer to a merchant's bank account.")
//					.build();
//
//			var responseAI4 = QuestionAnswer.builder()
//					.question("What is shopping cart software?")
//					.answer("Shopping cart software is a software that allows customers to select and store items for purchase.")
//					.build();
//
//			var responseAI5 = QuestionAnswer.builder()
//					.question("What is dropshipping?")
//					.answer("Dropshipping is a retail fulfillment method where a store doesn't keep the products it sells in stock.")
//					.build();
//
//			var responseAI6 = QuestionAnswer.builder()
//					.question("What is inventory management?")
//					.answer("Inventory management is the process of ordering, storing, and using a company's inventory.")
//					.build();
//
//			var responseAI7 = QuestionAnswer.builder()
//					.question("What is an e-commerce platform?")
//					.answer("An e-commerce platform is a software application that allows online businesses to manage their website, marketing, sales, and operations.")
//					.build();
//
//			var responseAI8 = QuestionAnswer.builder()
//					.question("What is SSL in e-commerce?")
//					.answer("SSL (Secure Sockets Layer) is a standard security technology for establishing an encrypted link between a server and a client.")
//					.build();
//
//			var responseAI9 = QuestionAnswer.builder()
//					.question("What is PCI DSS?")
//					.answer("PCI DSS (Payment Card Industry Data Security Standard) is a set of security standards designed to ensure that all companies that accept, process, store, or transmit credit card information maintain a secure environment.")
//					.build();
//
//			var responseAI10 = QuestionAnswer.builder()
//					.question("What is customer relationship management (CRM)?")
//					.answer("CRM is a technology for managing all your company's relationships and interactions with current and potential customers.")
//					.build();
//
//			var responseAI11 = QuestionAnswer.builder()
//					.question("What is SEO in e-commerce?")
//					.answer("SEO (Search Engine Optimization) is the practice of increasing the quantity and quality of traffic to your website through organic search engine results.")
//					.build();
//
//			var responseAI12 = QuestionAnswer.builder()
//					.question("What is the difference between multichannel and omnichannel?")
//					.answer("Multichannel refers to using multiple channels to reach customers, while omnichannel means providing a seamless experience across all channels.")
//					.build();
//
//			var responseAI13 = QuestionAnswer.builder()
//					.question("What is mobile commerce (m-commerce)?")
//					.answer("M-commerce is the buying and selling of goods and services through wireless handheld devices such as smartphones and tablets.")
//					.build();
//
//			var responseAI14 = QuestionAnswer.builder()
//					.question("What is a fulfillment center?")
//					.answer("A fulfillment center is a third-party logistics (3PL) warehouse where incoming orders are received, processed, and fulfilled.")
//					.build();
//
//			var responseAI15 = QuestionAnswer.builder()
//					.question("What is an abandoned cart?")
//					.answer("An abandoned cart refers to when a customer adds items to their online shopping cart but leaves the site without completing the purchase.")
//					.build();
//
//			var responseAI16 = QuestionAnswer.builder()
//					.question("What is cross-selling?")
//					.answer("Cross-selling is the practice of selling an additional product or service to an existing customer.")
//					.build();
//
//			var responseAI17 = QuestionAnswer.builder()
//					.question("What is up-selling?")
//					.answer("Up-selling is the practice of encouraging customers to purchase a more expensive item or add-ons to increase the value of the sale.")
//					.build();
//
//			var responseAI18 = QuestionAnswer.builder()
//					.question("What is a return policy?")
//					.answer("A return policy is a set of rules established by a retailer to manage how customers can return or exchange unwanted products.")
//					.build();
//
//			var responseAI19 = QuestionAnswer.builder()
//					.question("What is a customer loyalty program?")
//					.answer("A customer loyalty program is a rewards program offered by a company to customers who frequently make purchases.")
//					.build();
//
//			var responseAI20 = QuestionAnswer.builder()
//					.question("What is a product feed?")
//					.answer("A product feed is a file containing a list of products and their attributes used to provide information to e-commerce platforms and marketplaces.")
//					.build();
			chatbotService.createQuestionAnswer(responseAI0);
//			chatbotService.createQuestionAnswer(responseAI1);
//			chatbotService.createQuestionAnswer(responseAI2);
//			chatbotService.createQuestionAnswer(responseAI3);
//			chatbotService.createQuestionAnswer(responseAI4);
//			chatbotService.createQuestionAnswer(responseAI5);
//			chatbotService.createQuestionAnswer(responseAI6);
//			chatbotService.createQuestionAnswer(responseAI7);
//			chatbotService.createQuestionAnswer(responseAI8);
//			chatbotService.createQuestionAnswer(responseAI9);
//			chatbotService.createQuestionAnswer(responseAI10);
//			chatbotService.createQuestionAnswer(responseAI11);
//			chatbotService.createQuestionAnswer(responseAI12);
//			chatbotService.createQuestionAnswer(responseAI13);
//			chatbotService.createQuestionAnswer(responseAI14);
//			chatbotService.createQuestionAnswer(responseAI15);
//			chatbotService.createQuestionAnswer(responseAI16);
//			chatbotService.createQuestionAnswer(responseAI17);
//			chatbotService.createQuestionAnswer(responseAI18);
//			chatbotService.createQuestionAnswer(responseAI19);
//			chatbotService.createQuestionAnswer(responseAI20);
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
			var admin = RegisterDtos.builder()
					.email("admin@mail.com")
					.name("Admin")
					.img_url("https://static.fireant.vn/individuals/photo/629?width=200&height=200")
					.password("123456")
					.build();
			System.out.println("Admin token: " + service.createAdmin(admin));

			var user = RegisterDtos.builder()
					.email("phuhuong646@gmail.com")
					.name("Dan")
					.img_url("https://avatars.githubusercontent.com/u/127305381?v=4")
					.password("123456")
					.build();
			System.out.println("User token: " + service.register(user).getJwt());
//----------------------Tạo Tài Khoản ChatBot ----------------------------------------------
			var chatbot = RegisterDtos.builder()
					.email("chatbot@mail.com")
					.img_url("https://i.pinimg.com/564x/4f/b5/e6/4fb5e6fc1eb937458849bcbe6a2b3807.jpg")
					.name("Aza Assistant")
					.password("123456")
					.build();
			service.createAdmin(chatbot);

			userAccountInfoService.saveUser(UserAccountInfo.builder()
					.name("Aza Assistant")
					.img_url(chatbot.getImg_url())
					.fullName("Aza Assistant")
					.role(Role.ADMIN)
					.build());
			var AdminInfo = UserAccountInfo.builder()
					.name("Admin")
					.role(Role.ADMIN)
					.img_url(admin.getImg_url())
					.fullName("Admin")
					.build();

			userAccountInfoService.saveUser(AdminInfo);
			userAccountInfoService.disconnect(AdminInfo);

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
			brands0.add("Áo Phông");

			List<String> brands1 = new ArrayList<>();
			brands1.add("Quần short");
			brands1.add("Quần short_1");
			brands1.add("Quần short_2");
			brands1.add("Quần short_3");

			CategoryDtos category0 = CategoryDtos.builder()
					.sku("576842548")
					.categoryName("Áo")
					.image_url("https://product.hstatic.net/1000026602/product/img_6707_8aa638c6f11e4dd89d3b9a05c9b09576_master.jpg")
					.brands(brands0)
					.status(1)
					.build();

			CategoryDtos category1 = CategoryDtos.builder()
					.sku("JDFH6725")
					.categoryName("Quần")
					.image_url("https://i.pinimg.com/564x/cd/17/3e/cd173e563b0ad39cfddc4ded2dab67ad.jpg")
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
//			Red
			SubSizeQuantity subSizeQuantity0 = SubSizeQuantity.builder()
					.size("S")
					.quantity(56)
					.build();
			SubSizeQuantity subSizeQuantity1 = SubSizeQuantity.builder()
					.size("M")
					.quantity(12).build();
//			blue
			SubSizeQuantity subSizeQuantity2 = SubSizeQuantity.builder()
					.size("L")
					.quantity(60).build();
			SubSizeQuantity subSizeQuantity3 = SubSizeQuantity.builder()
					.size("XL")
					.quantity(50).build();
////			black
//			SubSizeQuantity subSizeQuantity4 = SubSizeQuantity.builder()
//					.size("XXL")
//					.quantity(10).build();
//			SubSizeQuantity subSizeQuantity5 = SubSizeQuantity.builder()
//					.size("XS")
//					.quantity(80).build();



			subSizeQuantityList0.add(subSizeQuantity0);
			subSizeQuantityList0.add(subSizeQuantity1);

			subSizeQuantityList0.add(subSizeQuantity2);
//			subSizeQuantityList1.add(subSizeQuantity2);

			subSizeQuantityList2.add(subSizeQuantity2);
			subSizeQuantityList2.add(subSizeQuantity3);

//			Color & SubSizeQuantity
			SubColor sub0 = SubColor.builder()
					.color("Đỏ")
					.sizes(subSizeQuantityList0)
					.build();
//			red medium - 12
			SubColor sub1 = SubColor.builder()
					.color("Xanh Dương")
					.sizes(subSizeQuantityList0)
					.build();

			SubColor sub2 = SubColor.builder()
					.color("Đen")
					.sizes(subSizeQuantityList2)
					.build();
//			Color & SubSizeQuantity
//			SubColor sub3 = SubColor.builder()
//					.color("Trắng")
//					.sizes(subSizeQuantityList3)
//					.build();
//			red medium - 12

			List<SubColor> listsub1 = new ArrayList<>();
			List<SubColor> listsub2 = new ArrayList<>();



			listsub1.add(sub0);
			listsub1.add(sub1);

			listsub2.add(sub2);
//----------------------Sản phẩm ..... ----------------------------------------------
			var pro0 = ProductDtos.builder()
					.name("Áo Sơ Mi Cuban Netstripes")
					.description("Áo sơ mi Cuban Netstripes là một kiểu áo sơ mi phổ biến có nguồn gốc từ Cuba, được biết đến với phong cách đặc trưng và thoải mái.")
					.quantitySold(18)
					.style("Sport Wear")
					.material("Cotton")
					.quantityDetails(listsub1)
					.category("Áo")
					.subCategory("Áo Sơ Mi")
					.imageMain("https://product.hstatic.net/1000026602/product/img_4447_0e9cd93e708c4936a1acc6d26276f00f_master.jpg")
					.tradeMask("Netstripes")
					.originalPrice(100000.0f)
					.saleDiscountPercent(24.0f)
					.createdByUserid(1)
					.build();
			var pro1 = ProductDtos.builder()
					.name("Quần short Slim Denim Back Devipock")
					.description("Quần short Slim Denim Back Devipock là một sản phẩm thời trang hiện đại, kết hợp giữa sự thoải mái và phong cách.")
					.quantityDetails(listsub2)
					.category("Quần")
					.quantitySold(20)
					.style("Casual")
					.material("Cotton")
					.imageMain("https://product.hstatic.net/1000026602/product/img_2373_1f20f528d40b4e57877df775f304f78c_master.jpg")
					.subCategory("Quần Short")
					.tradeMask("Devipock")
					.originalPrice(80000.0f)
					.saleDiscountPercent(26.0f)
					.createdByUserid(2)
					.build();
			var pro2 = ProductDtos.builder()
					.name("Áo Sơ Mi Denim Cloud Gray BTW")
					.description("Áo sơ mi Denim Cloud Gray BTW là một sản phẩm thời trang phổ biến, thường được làm từ chất liệu vải denim chất lượng cao")
					.subCategory("Áo Sơ Mi")
					.quantityDetails(listsub2)
					.category("Áo")
					.quantitySold(21)
					.style("Casual")
					.material("Cotton")
					.imageMain("https://product.hstatic.net/1000026602/product/img_4448_ec4132cc5f3d4a89afa21ecec8643381_master.jpg")
					.tradeMask("BTW")
					.originalPrice(800000.0f)
					.saleDiscountPercent(19.0f)
					.createdByUserid(1)
					.build();
			var pro3 = ProductDtos.builder()
					.name("Áo Sơ Mi Artemisia Basic")
					.description("Áo sơ mi Artemisia Basic là một sản phẩm thời trang phổ biến với thiết kế đơn giản nhưng tinh tế.")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(18)
					.style("Sport Wear")
					.material("Cotton")
					.tradeMask("Artemisia")
					.imageMain("https://product.hstatic.net/1000026602/product/img_1851_359eb91c8f384c0e96ff4e88e51d94f1_master.jpg")
					.subCategory("Áo Sơ Mi")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(0.0f)
					.createdByUserid(1)
					.build();
			var pro4 = ProductDtos.builder()
					.name("Product 4")
					.description("description 4")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.quantitySold(17)
					.style("Sport Wear")
					.material("Cotton")
					.imageMain("https://product.hstatic.net/1000026602/product/img_4321_bba557f524b94ecc9fbfc77d90a6915d_master.jpg")
					.subCategory("Áo Thun")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(16.0f)
					.createdByUserid(1)
					.build();
			var pro5 = ProductDtos.builder()
					.name("Product 5")
					.description("description 5")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(16)
					.style("Casual")
					.material("Vải Dạ")
					.tradeMask("Artemisia")
					.subCategory("Áo Phông")
					.imageMain("https://product.hstatic.net/1000026602/product/img_6614_82afb831e0464b4e9cdd88c834db6b8b_master.jpg")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(19.0f)
					.createdByUserid(1)
					.build();
			var pro6 = ProductDtos.builder()
					.name("Product 6")
					.description("description 6")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(15)
					.style("Casual")
					.material("Cotton")
					.tradeMask("Artemisia")
					.subCategory("Áo Phông")
					.imageMain("https://product.hstatic.net/1000026602/product/img_4324_517bbc4cc40140e99a70bb46c7a2083f_master.jpg")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(0.0f)
					.createdByUserid(1)
					.build();
			var pro7 = ProductDtos.builder()
					.name("Product 7")
					.description("description 7")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.quantitySold(14)
					.style("Casual")
					.material("Vải Dạ")
					.imageMain("https://product.hstatic.net/1000026602/product/img_4768_e0e9892a08fc4e18b4a9d35fe6001fbe_master.jpg")
					.subCategory("Áo Sơ Mi")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(14.0f)
					.createdByUserid(1)
					.build();
			var pro8 = ProductDtos.builder()
					.name("Product 8")
					.description("description 8")
					.quantityDetails(listsub1)
					.category("Áo")
					.tradeMask("Artemisia")
					.quantitySold(13)
					.style("Casual")
					.material("Cotton")
					.subCategory("Áo Sơ Mi")
					.imageMain("https://product.hstatic.net/1000026602/product/img_0443_019f9288f0234ec48aada420e96cd316_master.jpg")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(14.0f)
					.createdByUserid(1)
					.build();
			var pro9 = ProductDtos.builder()
					.name("Product 9")
					.description("description 9")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(12)
					.style("Casual")
					.material("Vải Dạ")
					.imageMain("https://product.hstatic.net/1000026602/product/img_5703_be15e67520964287b86ce4f6ec62f281_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Phông")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(19.0f)
					.createdByUserid(1)
					.build();
			var pro10 = ProductDtos.builder()
					.name("Product 10")
					.description("description 10")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(11)
					.style("Casual")
					.material("Cotton")
					.imageMain("https://product.hstatic.net/1000026602/product/img_7752_fb896354845e4b9d8293c36ffe212eac_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Phông")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(20.0f)
					.createdByUserid(1)
					.build();
			var pro11 = ProductDtos.builder()
					.name("Product 11")
					.description("description 11")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(10)
					.style("Sport Wear")
					.material("Cotton")
					.tradeMask("Artemisia")
					.imageMain("https://product.hstatic.net/1000026602/product/img_9129_9f40c23f32d74c2e818c13aa433ad4b8_master.jpg")
					.subCategory("Áo Sơ Mi")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(25.0f)
					.createdByUserid(1)
					.build();
			var pro12 = ProductDtos.builder()
					.name("Product 12")
					.description("description 12")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(11)
					.style("Sport Wear")
					.material("Cotton")
					.imageMain("https://product.hstatic.net/1000026602/product/00000095_c7e78956d6db4b65971859f31ca66a85_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(9.0f)
					.createdByUserid(1)
					.build();
			var pro13 = ProductDtos.builder()
					.name("Product 13")
					.description("description 13")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(11)
					.style("Sport Wear")
					.material("Cotton")
					.tradeMask("Artemisia")
					.imageMain("https://product.hstatic.net/1000026602/product/img_4260_8383ba5e2fdb4a9381747421d92bec4f_master.jpg")
					.subCategory("Áo Khoác")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(0.0f)
					.createdByUserid(1)
					.build();
			var pro14 = ProductDtos.builder()
					.name("Product 14")
					.description("description 14")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(11)
					.style("Casual")
					.material("Vải Dạ")
					.imageMain("https://product.hstatic.net/1000026602/product/img_6707_8aa638c6f11e4dd89d3b9a05c9b09576_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Khoác")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(2.0f)
					.createdByUserid(1)
					.build();
			var pro15 = ProductDtos.builder()
					.name("Product 15")
					.description("description 15")
					.quantityDetails(listsub1)
					.category("Áo")
					.quantitySold(11)
					.style("Casual")
					.material("Cotton")
					.imageMain("https://product.hstatic.net/1000026602/product/img_6137_559a685d9a884fc2b92d212b72e711be_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Khoác")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(17.0f)
					.createdByUserid(1)
					.build();
			var pro16 = ProductDtos.builder()
					.name("Product 16")
					.description("description 16")
					.quantityDetails(listsub1)
					.quantitySold(11)
					.style("Street Style")
					.material("Vải Dạ")
					.category("Áo")
					.imageMain("https://product.hstatic.net/1000026602/product/img_6925_e6f52748e358465896b80061597d8d71_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Phông")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(14.0f)
					.createdByUserid(1)
					.build();
			var pro17 = ProductDtos.builder()
					.name("Product 17")
					.description("description 17")
					.quantityDetails(listsub1)
					.quantitySold(11)
					.style("Sport Wear")
					.material("Cotton")
					.category("Áo")
					.tradeMask("Artemisia")
					.imageMain("https://product.hstatic.net/1000026602/product/img_5217_f773bb9fa92c4cba8eae86ca7163994c_master.jpg")
					.subCategory("Áo Sơ Mi")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(18.0f)
					.createdByUserid(1)
					.build();
			var pro18 = ProductDtos.builder()
					.name("Product 18")
					.description("description 18")
					.quantityDetails(listsub1)
					.category("Áo")
					.imageMain("https://product.hstatic.net/1000026602/product/abc01955_608aca368feb437aabedccc9df9b50cc_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Khoác")
					.quantitySold(11)
					.style("Street Style")
					.material("Vải Dạ")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(25.0f)
					.createdByUserid(1)
					.build();
			var pro19 = ProductDtos.builder()
					.name("Product 19")
					.description("description 19")
					.quantityDetails(listsub1)
					.category("Áo")
					.imageMain("https://product.hstatic.net/1000026602/product/img_2731_2ec7f583b710452783087b53ac09ef49_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Khoác")
					.quantitySold(11)
					.style("Sport Wear")
					.material("Cotton")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(5.0f)
					.createdByUserid(1)
					.build();
			var pro20 = ProductDtos.builder()
					.name("Product 20")
					.description("description 20")
					.quantityDetails(listsub1)
					.category("Áo")
					.imageMain("https://product.hstatic.net/1000026602/product/img_4448_ec4132cc5f3d4a89afa21ecec8643381_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Sơ Mi")
					.quantitySold(11)
					.style("Casual")
					.material("Denim")
					.originalPrice(2350000.0f)
					.saleDiscountPercent(19.0f)
					.createdByUserid(1)
					.build();
			var pro21 = ProductDtos.builder()
					.name("Product 21")
					.description("description 21")
					.quantityDetails(listsub1)
					.category("Áo")
					.imageMain("https://product.hstatic.net/1000026602/product/img_1105_e11b0ecefdf548629cecd16bc1e13d59_master.jpg")
					.tradeMask("Artemisia")
					.subCategory("Áo Phông")
					.originalPrice(2350000.0f)
					.quantitySold(11)
					.style("Street Style")
					.material("Denim")
					.saleDiscountPercent(15.0f)
					.createdByUserid(1)
					.build();

			var comment0 = CommentDto.builder()
					.content("\"An excellent deal for the cost. The performance and durability of the product make it stand out in its price range, ensuring customers get more than what they pay for.")
					.rating(4.5f)
					.color("Đỏ")
					.size("M")
					.status(1)
//					.productQuality("High")
					.username("Admin")
					.build();
			var comment1 = CommentDto.builder()
					.content("A fantastic balance of affordability and quality. It's rare to find something this well-made at such a competitive price, offering users both reliability and great value for their money.")
					.rating(2.0f)
					.color("Xanh")
					.size("L")
					.status(1)
//					.productQuality("Low")
					.username("Dan")
					.build();
			var comment2 = CommentDto.builder()
					.content("\"This product offers exceptional value for the price. The quality far exceeds expectations given the cost, making it a smart and budget-friendly choice for anyone looking to get the most out of their investment.")
					.rating(5.0f)
					.color("Đen")
					.size("XL")
					.status(1)
//					.productQuality("Excellent")
					.username("Dan")
					.build();

			var comment3 = CommentDto.builder()
//					.content("Very good value for money.")
					.rating(0.0f)
					.status(1)
					.color("Đen")
					.size("XL")
//					.productQuality("Excellent")
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

			productService.createComment(comment3,pro2Response.getId());
			productService.createComment(comment3,pro17Response.getId());
			productService.createComment(comment3,pro10Response.getId());
			productService.createComment(comment3,pro8Response.getId());

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
//			OrderDtos orderDtos = OrderDtos.builder()
//					.
//					.build();
//			orderController.createOrder()
//			var trademask_0 = TradeMaskDtos.builder()
//					.sku("NS-001")
//					.tradeMarkName("Burberry")
//					.status(0)
//					.image_url("https://i.pinimg.com/564x/1e/85/b8/1e85b83dfc74daa9408da37cb011775d.jpg")
//					.description("Burberry là thương hiệu thời trang cao cấp của Anh, nổi tiếng với các sản phẩm từ chất liệu cao cấp, bao gồm áo khoác trench, quần áo, túi xách và phụ kiện.")
//					.build();
//			var trademask_1 = TradeMaskDtos.builder()
//					.sku("DP-001")
//					.tradeMarkName("Devipock")
//					.status(1)
//					.image_url("https://i.pinimg.com/originals/2e/bc/1e/2ebc1ea4ab4eff518990b30cc10a1f6f.gif")
//					.description("Devipock là thương hiệu thời trang tiên phong, tập trung vào các thiết kế độc đáo và sáng tạo. Các sản phẩm của Devipock bao gồm quần áo, giày dép và phụ kiện với phong cách hiện đại và phá cách. Thương hiệu này cam kết sử dụng chất liệu cao cấp và thân thiện với môi trường, nhằm mang lại sự thoải mái và phong cách cho khách hàng.")
//					.build();
//			var trademask_2 = TradeMaskDtos.builder()
//					.sku("BU-001")
//					.tradeMarkName("Artemisia")
//					.image_url("https://i.pinimg.com/736x/03/90/fe/0390fef222b2b427a5bf5dbda6064762.jpg")
//					.status(1)
//					.description("Artemisia là thương hiệu thời trang tiên phong, tập trung vào các thiết kế độc đáo và sáng tạo. Các sản phẩm của Artemisia bao gồm quần áo, giày dép và phụ kiện với phong cách hiện đại và phá cách. Thương hiệu này cam kết sử dụng chất liệu cao cấp và thân thiện với môi trường, nhằm mang lại sự thoải mái và phong cách cho khách hàng.")
//					.build();
//			productService.saveTrask(trademask_0);
//			productService.saveTrask(trademask_1);
//			productService.saveTrask(trademask_2);
//
//			var style_0 = StyleDtos.builder()
//					.styleName("Casual")
//					.status(1)
//					.image_url("https://hoang-phuc.com/media/magefan_blog/2022/02/casual-2.jpg")
//					.description("Phong cách Casual là một phong cách thời trang thoải mái, không quá cầu kỳ, và thích hợp cho các hoạt động hàng ngày. Đây là kiểu trang phục mà người mặc cảm thấy tự do, dễ chịu, và không bị ràng buộc bởi các quy tắc thời trang khắt khe.")
//					.build();
//			var style_1 = StyleDtos.builder()
//					.styleName("Sport Wear")
//					.status(0)
//					.image_url("https://images.jdmagicbox.com/quickquotes/images_main/sports-shorts-for-men-women-2010414365-m67kn3ee.jpg")
//					.description("Sport Wear (hay còn gọi là trang phục thể thao) là phong cách thời trang dành riêng cho các hoạt động thể dục thể thao, mang đến sự thoải mái, thoáng mát và hỗ trợ tốt nhất cho người mặc khi tham gia các hoạt động thể chất. Sport Wear không chỉ phục vụ cho mục đích thể thao mà còn trở thành một phong cách thời trang phổ biến trong đời sống hàng ngày.")
//					.build();
//			var style_2 = StyleDtos.builder()
//					.styleName("Street Style")
//					.image_url("https://img.vuahanghieu.com/unsafe/0x0/left/top/smart/filters:quality(90)/https://admin.vuahanghieu.com/upload/news/content/2023/05/street-style-la-gi-8-jpg-1684894499-24052023091459.jpg")
//					.status(1)
//					.description("Street Style (phong cách đường phố) là một phong cách thời trang bắt nguồn từ văn hóa đường phố, đặc biệt là từ các thành phố lớn như New York, Tokyo, Paris, và London. Phong cách này thường phản ánh sự tự do, sáng tạo, và cá tính riêng của người mặc, không bị giới hạn bởi những quy tắc thời trang truyền thống.")
//					.build();
//			productService.createStyle(style_0);
//			productService.createStyle(style_1);
//			productService.createStyle(style_2);
//
//
//			var material_0 = MaterialDtos.builder()
//					.materialName("Denim")
//					.status(1)
//					.image_url("https://brydenapparel.com/wp-content/uploads/2023/07/Denim-Fabric-1.jpg")
//					.description("Denim là một loại vải dệt bền và chắc, thường được làm từ sợi cotton, được biết đến nhiều nhất trong việc sử dụng để may quần jeans. Denim có nguồn gốc từ thành phố Nîmes ở Pháp, và tên \"denim\" xuất phát từ cụm từ \"serge de Nîmes,\" có nghĩa là \"vải bông chéo của Nîmes.\"")
//					.build();
//			var material_1 = MaterialDtos.builder()
//					.materialName("Vải Dạ")
//					.status(1)
//					.image_url("https://bizweb.dktcdn.net/100/348/534/products/vai-da-7-d165c408-3cf2-410f-8e05-4b0e9f4046cf.jpg?v=1634041228343")
//					.description("Vải dạ (hay còn gọi là dạ len) là một loại vải dày, ấm áp và mềm mịn, thường được làm từ sợi len hoặc sợi tổng hợp. Vải dạ có bề mặt mịn màng và khả năng giữ nhiệt tốt, vì vậy nó thường được sử dụng trong các trang phục mùa đông như áo khoác, mũ, khăn quàng cổ và váy.")
//					.build();
//			var material_2 = MaterialDtos.builder()
//					.materialName("Cotton")
//					.image_url("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9iZP60MCAlNvyakAqWDrHzBb9fl_exs6Oug&s")
//					.status(1)
//					.description("Cotton là một loại vải tự nhiên được làm từ sợi bông, nổi tiếng với sự mềm mại, thoáng mát và thân thiện với làn da. Đây là một trong những loại vải phổ biến nhất trên thế giới, được sử dụng rộng rãi trong nhiều lĩnh vực, đặc biệt là trong ngành may mặc.")
//					.build();
//			productService.createMaterial(material_0);
//			productService.createMaterial(material_1);
//			productService.createMaterial(material_2);
		};

	}

}