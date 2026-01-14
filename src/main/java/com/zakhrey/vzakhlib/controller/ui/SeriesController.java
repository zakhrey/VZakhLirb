package com.zakhrey.vzakhlib.controller.ui;

import com.zakhrey.vzakhlib.exception.ResourceAlreadyExistsException;
import com.zakhrey.vzakhlib.model.SeriesDto;
import com.zakhrey.vzakhlib.model.request.SeriesCreateRequest;
import com.zakhrey.vzakhlib.service.SeriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    /**
     * Показать форму создания серии
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("seriesCreateRequest")) {
            model.addAttribute("seriesCreateRequest", new SeriesCreateRequest());
        }
        return "series/create";
    }

    /**
     * Обработка создания серии
     */
    @PostMapping("/create")
    public String createSeries(
            @Valid @ModelAttribute("seriesCreateRequest") SeriesCreateRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        log.info("Получен запрос на создание серии: {}", request.getName());
        
        // Проверка на ошибки валидации
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.seriesCreateRequest", 
                bindingResult
            );
            redirectAttributes.addFlashAttribute("seriesCreateRequest", request);
            return "redirect:/series/create";
        }
        
        try {
            // Создание серии через сервис
            SeriesDto createdSeries = seriesService.createSeries(request);
            
            // Добавляем сообщение об успехе
            redirectAttributes.addFlashAttribute("successMessage", 
                "Серия '" + createdSeries.getName() + "' успешно создана!");
            
            return "redirect:/series";
            
        } catch (ResourceAlreadyExistsException e) {
            // Обработка ошибки дублирования названия
            bindingResult.rejectValue("name", "error.seriesCreateRequest", e.getMessage());
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.seriesCreateRequest", 
                bindingResult
            );
            redirectAttributes.addFlashAttribute("seriesCreateRequest", request);
            return "redirect:/series/create";
        }
    }
}