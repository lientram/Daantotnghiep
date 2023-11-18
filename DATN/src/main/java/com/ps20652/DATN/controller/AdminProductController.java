package com.ps20652.DATN.controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ps20652.DATN.DTO.ProductDTO;
import com.ps20652.DATN.entity.Category;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.entity.StockHistory;
import com.ps20652.DATN.service.CategoryService;
import com.ps20652.DATN.service.ProductService;
import com.ps20652.DATN.service.UploadService;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

	@Autowired
	private UploadService uploadService;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public String listProducts(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Principal principal) {

		int pageSize = 6; // Số lượng đơn hàng trên mỗi trang
		Page<Product> productPage = productService.getAllOrdersPaginated(PageRequest.of(page, pageSize));
		// List<Product> products = productService.findAll();
		List<Category> categories = categoryService.findAll();
		model.addAttribute("products", productPage);
		model.addAttribute("categories", categories);
		return "AdminCpanel/ui-buttons";

	}

	@PostMapping("/add")
	public String addProduct(@ModelAttribute("productDTO") ProductDTO productDTO,
			@RequestParam("image") MultipartFile image) {
		Product product = new Product();
		try {
			// Sử dụng service để tải lên hình ảnh và nhận lại tên hình ảnh đã lưu
			String imageString = uploadService.uploadImage(image);

			// Thiết lập dữ liệu cho sản phẩm
			product.setName(productDTO.getName());
			product.setDescription(productDTO.getDescription());
			product.setPrice(productDTO.getPrice());
			product.setQuantityInStock(productDTO.getQuantityInStock());
			product.setCategory(productDTO.getCategory());
			product.setImage(imageString);
			product.setPurchasePrice(productDTO.getPurchasePrice());

			// Lưu sản phẩm vào cơ sở dữ liệu
			productService.create(product);

			// Chuyển hướng đến trang danh sách sản phẩm sau khi thêm
			return "redirect:/admin/products";
		} catch (IOException e) {
			e.printStackTrace();
			// Xử lý lỗi khi tải lên hình ảnh
			// Có thể xem xét việc báo lỗi cho người dùng
			return "Error uploading image";
		}
	}

	@GetMapping("edit/{productId}")
	public String showEditProductForm(@PathVariable("productId") Integer productId, Model model) {
		// Lấy sản phẩm từ cơ sở dữ liệu theo productId
		Product product = productService.findbyId(productId);
		List<Category> categories = categoryService.findAll();

		// Kiểm tra xem sản phẩm có tồn tại hay không
		if (product == null) {
			return "redirect:/admin/products";
		}

		model.addAttribute("product", product);
		model.addAttribute("categories", categories);
		return "admin/edit-product";
	}

	@PostMapping("/edit/{productId}")
	public String handleEditProductForm(@PathVariable("productId") Integer productId,
			@ModelAttribute("productDTO") ProductDTO editedProduct, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile image) {
		try {
			Product existingProduct = productService.findbyId(productId);
			if (existingProduct == null) {
				// Nếu sản phẩm không tồn tại, bạn có thể thực hiện xử lý phù hợp ở đây, ví dụ:
				// hiển thị thông báo lỗi
				return "redirect:/admin/products";
			}

			// Cập nhật thông tin sản phẩm từ editedProduct

			// Xử lý tải lên hình ảnh và cập nhật đường dẫn ảnh

			Product product = productService.findbyId(productId);
			String img = product.getImage();

			String imageString = img; // Giá trị của img cũ
			if (!image.isEmpty()) {
				try {

					// Lấy đường dẫn thực của dự án
					Resource resource = resourceLoader.getResource("classpath:/");
					String projectPath = resource.getFile().getAbsolutePath();

					// Đường dẫn lưu trữ hình ảnh
					String uploadPath = projectPath + "/static/assets/images/";
					Path path = Paths.get(uploadPath);

					// Lưu trữ hình ảnh vào thư mục uploads
					Files.copy(image.getInputStream(), path.resolve(image.getOriginalFilename()),
							StandardCopyOption.REPLACE_EXISTING);

					// Lấy tên hình ảnh đã lưu
					imageString = image.getOriginalFilename();
				} catch (IOException e) {
					e.printStackTrace();
					// Xử lý lỗi khi tải lên hình ảnh

				}
			}

			existingProduct.setName(editedProduct.getName());
			existingProduct.setCategory(editedProduct.getCategory());
			existingProduct.setDescription(editedProduct.getDescription());
			existingProduct.setPrice(editedProduct.getPrice());
			existingProduct.setPurchasePrice(editedProduct.getPurchasePrice());
			existingProduct.setQuantityInStock(editedProduct.getQuantityInStock());
			existingProduct.setImage(imageString);

			// Lưu sản phẩm đã cập nhật vào cơ sở dữ liệu
			productService.update(existingProduct);

			redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được cập nhật thành công.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật sản phẩm.");
		}

		return "redirect:/admin/products";
	}

	@GetMapping("/delete/{productId}")
	public String deleteProduct(@PathVariable("productId") Integer productId, RedirectAttributes redirectAttributes) {
		try {
			// Kiểm tra xem sản phẩm có tồn tại không
			Product product = productService.findbyId(productId);
			if (product != null) {
				// Nếu sản phẩm tồn tại, thực hiện xóa sản phẩm
				productService.delete(product);
				redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được xóa thành công.");
			} else {
				// Nếu sản phẩm không tồn tại, hiển thị thông báo lỗi
				redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm để xóa.");
			}
		} catch (Exception e) {
			// Xử lý lỗi nếu có
			redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa sản phẩm.");
		}

		return "redirect:/admin/products";
	}

	// @GetMapping("/searchName")
	// public String searchProductsNAME(@RequestParam("name") String productName,
	// Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
	// // Sử dụng phương thức tìm kiếm theo tên từ ProductDAO
	//
	// int pageSize = 6;
	// // Đây là ví dụ tìm kiếm theo tên sản phẩm
	// List<Product> searchResults = productService.findByName(productName);
	// Page<Product> productPage =
	// productService.getAllOrdersPaginated(PageRequest.of(page, pageSize));
	// model.addAttribute("products", productPage);
	// model.addAttribute("products", searchResults);
	//
	// return "aaa/ui-buttons"; // Trả về view để hiển thị kết quả tìm kiếm
	// }

	@GetMapping("/searchId")
	public String searchProductsID(@RequestParam("productId") Integer productId, Model model,
			@RequestParam(name = "page", defaultValue = "0") int page,
			Principal principal) {
		// Sử dụng phương thức tìm kiếm theo Id từ ProductDAO
		// Đây là ví dụ tìm kiếm theo Id sản phẩm
		Product searchResults = productService.findbyId(productId);

		model.addAttribute("products", searchResults);

		return "AdminCpanel/ui-buttons"; // Trả về view để hiển thị kết quả tìm kiếm
	}

	@GetMapping("/searchPrice")
	public String price(HttpServletRequest request, Model model, @RequestParam("min") double min,
			@RequestParam("max") double max) {
		// List<Product> items = dao.findByPriceBetween(min, max);

		List<Product> searchResults = productService.findByPrice(min, max);
		model.addAttribute("products", searchResults);

		return "AdminCpanel/ui-buttons"; // Trả về view để hiển thị kết quả lọc
	}

	@PostMapping("/addStock")
	public String addStock(@RequestParam("productId") Integer productId,
			@ModelAttribute("productDTO") ProductDTO productDTO) {
		try {
			productService.updateQuantityInStock(productId, productDTO.getQuantityAdded());
			// Redirect to the product list or another appropriate page
			return "redirect:/admin/products";
		} catch (Exception e) {
			e.printStackTrace();
			// Handle the exception, e.g., display an error message
			return "Error updating stock";
		}
	}

}
