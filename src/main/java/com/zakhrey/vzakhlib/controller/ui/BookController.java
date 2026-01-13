package com.zakhrey.vzakhlib.controller.ui;

import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.request.BookCreateRequest;
import com.zakhrey.vzakhlib.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("bookCreateRequest")) {
            model.addAttribute("bookCreateRequest", new BookCreateRequest());
        }
        return "books/create";
    }


    @PostMapping("/create")
    public String createBook(
            @ModelAttribute BookCreateRequest bookCreateRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.bookCreateRequest", 
                bindingResult
            );
            redirectAttributes.addFlashAttribute("bookCreateRequest", bookCreateRequest);
            return "redirect:/books/create";
        }
        
        try {
            BookDto createdBook = bookService.createBook(bookCreateRequest);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Книга \"" + createdBook.getName() + "\" успешно создана!");
            return "redirect:/books";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Ошибка при создании книги: " + e.getMessage());
            redirectAttributes.addFlashAttribute("bookCreateRequest", bookCreateRequest);
            return "redirect:/books/create";
        }
    }
}