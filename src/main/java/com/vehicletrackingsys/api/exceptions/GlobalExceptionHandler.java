package com.vehicletrackingsys.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex,
                                         HttpServletRequest request,
                                         Model model,
                                         HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        model.addAttribute("error", "File size exceeds the 5MB limit.");

        String requestURI = request.getRequestURI();

        if (requestURI.contains("/v/create")) {
            redirectAttributes.addFlashAttribute("pageLabel", "Create vehicle");
            return "redirect:/user/Vehicle/create-vehicle";
        } else if (requestURI.contains("/v/edit")) {
            redirectAttributes.addFlashAttribute("pageLabel", "Edit vehicle");
            return "redirect:/user/Vehicle/edit-vehicle";
        } else if (requestURI.contains("/d/create")) {
            redirectAttributes.addFlashAttribute("pageLabel", "Create driver");
            return "redirect:/user/Driver/create-driver";
        } else if (requestURI.contains("/d/edit")) {
            redirectAttributes.addFlashAttribute("pageLabel", "Edit driver");
            return "redirect:/user/Driver/edit-driver";
        } else {
            return "redirect:/error";
        }
    }

}

