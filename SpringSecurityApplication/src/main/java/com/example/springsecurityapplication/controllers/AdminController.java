package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.enumm.Role;
import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.*;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.services.OrderService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final PersonService personService;

    @Value("${upload.path}")
    private String uploadPath;

    private final CategoryRepository categoryRepository;

    public AdminController(ProductService productService, OrderService orderService, OrderRepository orderRepository, PersonService personService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.personService = personService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five, @RequestParam("category") int category, Model model) throws IOException {
        Category category_db = (Category) categoryRepository.findById(category).orElseThrow();
        System.out.println(category_db.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }

        if(file_one != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);

        }

        if(file_two != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_three != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_four != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_five != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five .getOriginalFilename();
            file_five .transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        productService.saveProduct(product, category_db);
        return "redirect:/admin";
    }


    @GetMapping("")
    public String admin(Model model)
    {
        model.addAttribute("products", productService.getAllProduct());
        return "admin/index";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }

    @GetMapping("/order/edit/{id}")
    public String getCart(Model model, @PathVariable("id") int id){
        model.addAttribute("order", orderService.getOrderId(id));
        model.addAttribute("all_statuses", Status.values());
        return "admin/edit_order";
    }

    @PostMapping("/order/edit/{id}")
    public String editCart(@ModelAttribute("order") Order order, @PathVariable("id") int id, Model model){
        orderService.updateOrder(id, order);
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders")
    public String ordersList(Model model){
        model.addAttribute("orders", orderService.getAllOrder());
        return "admin/orders";
    }

    @PostMapping("/order/search")
    public String orderSearch(@RequestParam("title") String order_number, Model model){
        model.addAttribute("orders", orderService.getAllOrder());

        System.out.println("NUMBER: " + order_number);
        System.out.println(orderRepository.findNumberLastChars(order_number));

        model.addAttribute("value_title", order_number);
        model.addAttribute("search_order", orderRepository.findNumberLastChars(order_number));
        return "admin/orders";
    }

    @GetMapping("/users")
    public String getUsers(Model model){
        model.addAttribute("users", personService.getAllPersons());
        model.addAttribute("roles", Role.values());
        return "admin/users";
    }

    @GetMapping("/user/edit/{id}")
    public String getUser(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personService.findById(id));
        model.addAttribute("roles", Role.values());
        return "admin/edit_user";
    }

    @PostMapping("/user/edit/{id}")
    public String editUser(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("users", personService.getAllPersons());
            return "admin/users";
        }
        personService.updateUser(id, person);
        return "redirect:/admin/users";
    }
}
