package com.zakhrey.vzakhlib.controller.ui;

import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.model.request.BookCreateRequest;
import com.zakhrey.vzakhlib.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

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

    @GetMapping
    public String getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<BookDto> booksPage = bookService.getAllBooks(pageable);

        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("totalItems", booksPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "books/list";
    }

    @GetMapping("/view")
    public String getBookDetails(
            @RequestParam UUID id,
            Model model) {

        // Если есть метод getBookById в сервисе, можно использовать его
        BookDto book = bookService.getBookById(id);
        model.addAttribute("book", book);

        // Пока возвращаем на список, если нет отдельного метода
        return "redirect:/books";
    }
}