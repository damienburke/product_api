package com.ssd.web

import com.ssd.persistance.entities.ProductEntity
import com.ssd.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import java.math.BigDecimal

@Controller
class ProductController(val productService: ProductService) {

    @GetMapping("/products")
    fun showProducts(model: Model): String {
        model.addAttribute("products", productService.getProducts());
        return "products"
    }

    @GetMapping("/add-product")
    fun showAddProductForm(model: Model): String {
        model.addAttribute("product", ProductEntity())
        return "add-product"
    }

    @PostMapping("/add-product")
    fun addProduct(@ModelAttribute product: ProductEntity): String {
        productService.addProduct(product)
        return "redirect:/products"
    }

    @GetMapping("/product/{id}")
    fun getProduct(@PathVariable id: Int, model: Model): String {
        val product = productService.findProductById(id)

        if (product.isEmpty)
            return "redirect:/products"
        else {
            model.addAttribute("product", product.get())
            return "product-detail"
        }
    }
}