// //package com.ps20652.DATN.paypal.api;
// //
// //import org.springframework.beans.factory.annotation.Autowired;
// //import org.springframework.stereotype.Controller;
// //import org.springframework.web.bind.annotation.GetMapping;
// //import org.springframework.web.bind.annotation.ModelAttribute;
// //import org.springframework.web.bind.annotation.PostMapping;
// //import org.springframework.web.bind.annotation.RequestParam;
// //import com.paypal.api.payments.Links;
// //import com.paypal.api.payments.Payment;
// //import com.paypal.base.rest.PayPalRESTException;
// //import com.ps20652.DATN.entity.Order;  // Đảm bảo bạn đã import đúng đối tượng Order
// //
// //@Controller
// //public class PaypalController {
// //
// //    @Autowired
// //    PaypalService service;
// //
// //    public static final String SUCCESS_URL = "pay/success";
// //    public static final String CANCEL_URL = "pay/cancel";
// //
// //    @GetMapping("/paypal")
// //    public String home() {
// //        return "home";
// //    }
// //
// //    @PostMapping("/pay")
// //    public String payment(@ModelAttribute("order") Order order) {
// //        try {
// //            order.setIntent("sale");
// //            order.setDescription("SALE");
// //            order.setMethod("paypal");
// //
// //            // Sử dụng order trực tiếp để tạo thanh toán PayPal
// //            Payment payment = service.createPayment(order, order.getMethod(),
// //                order.getIntent(), order.getDescription(), "http://localhost:8080/" + CANCEL_URL,
// //                "http://localhost:8080/" + SUCCESS_URL);
// //
// //            for (Links link : payment.getLinks()) {
// //                if (link.getRel().equals("approval_url")) {
// //                    return "redirect:" + link.getHref();
// //                }
// //            }
// //        } catch (PayPalRESTException e) {
// //            e.printStackTrace();
// //        }
// //        return "redirect:/";
// //    }
// //
// //    @PostMapping(value = SUCCESS_URL)
// //    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
// //        try {
// //            Payment payment = service.executePayment(paymentId, payerId);
// //            System.out.println(payment.toJSON());
// //            if (payment.getState().equals("approved")) {
// //                return "success";
// //            }
// //        } catch (PayPalRESTException e) {
// //            System.out.println(e.getMessage());
// //        }
// //        return "redirect:/";
// //    }
// //}

// package com.ps20652.DATN.paypal;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import com.paypal.api.payments.Links;
// import com.paypal.api.payments.Payment;
// import com.paypal.base.rest.PayPalRESTException;
// import com.ps20652.DATN.entity.Order; // Đảm bảo bạn đã import đúng đối tượng Order

// @Controller
// public class PaypalController {

// 	@Autowired
// 	PaypalService service;

// 	public static final String SUCCESS_URL = "pay/success";
// 	public static final String CANCEL_URL = "/pay/cancel";

// 	@GetMapping("/paypal")
// 	public String home() {
// 		return "home";
// 	}

// 	@PostMapping("/pay")
// 	public String payment(@ModelAttribute("order") Order order) {
// 		try {
// 			String method = "paypal";
// 			String intent = "sale";
// 			String description = "SALE";
// 			String cancelUrl = "http://localhost:8080/" + CANCEL_URL;
// 			String successUrl = "http://localhost:8080/" + SUCCESS_URL;

// 			order.setIntent(intent);
// 			order.setDescription(description);
// 			order.setMethod(method);

// 			Payment payment = service.createPayment(order, method, intent, description, cancelUrl, successUrl);

// 			for (Links link : payment.getLinks()) {
// 				if (link.getRel().equals("approval_url")) {
// 					System.out.println(link.getHref());
// 					return "redirect:" + link.getHref();
					
// 				}
// 			}
// 		} catch (PayPalRESTException e) {
// 			e.printStackTrace();
// 		}
// 		return "redirect:/";
// 	}

// 	 @GetMapping(value = CANCEL_URL)
// 	    public String cancelPay() {
// 	        return "cancel";
// 	    }

// 	    @GetMapping(value = SUCCESS_URL)
// 	    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
// 	        try {
// 	            Payment payment = service.executePayment(paymentId, payerId);
// 	            System.out.println(payment.toJSON());
// 	            if (payment.getState().equals("approved")) {
// 	                return "success";
// 	            }
// 	        } catch (PayPalRESTException e) {
// 	         System.out.println(e.getMessage());
// 	        }
// 	        return "redirect:/";
// 	    }
// }