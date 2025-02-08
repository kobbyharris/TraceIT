package com.vehicletrackingsys.api.controllers;


import com.vehicletrackingsys.api.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/t")
public class SettingController {

    @Autowired
    private UserService userService;

    @GetMapping("/settings")
    public String showSettingsPage(HttpSession session, Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("pageLabel", "Settings");
        return "user/settings";
    }

    @GetMapping("/settings/profile")
    public String showProfilePage(HttpSession session, Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("pageLabel", "Settings");
        return "user/profile";
    }

    @GetMapping("/settings/security")
    public String showSecurityPage(HttpSession session, Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("pageLabel", "Settings");
        return "user/security";
    }

    @PostMapping("/update_security")
    public String updateSecurity(HttpSession session,
                                 @RequestParam("current-password") String currentPassword,
                                 @RequestParam("new-password") String newPassword,
                                 @RequestParam("confirm-password") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {


        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New password and confirm password do not match.");
            return "redirect:/t/settings/security";
        }

        UUID userId = (UUID) session.getAttribute("userId");

        try {
            userService.updatePassword(userId, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/t/settings/security";
    }


    @PostMapping("/update_profile")
    public String updateProfile(HttpSession session,
                                @RequestParam("profile-username") String username,
                                @RequestParam("profile-email") String email,
                                RedirectAttributes redirectAttributes) {

        UUID userId = (UUID) session.getAttribute("userId");

        try {
            userService.updateProfile(userId, username, email);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/t/settings/profile";
    }





}
