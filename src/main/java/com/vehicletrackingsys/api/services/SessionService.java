package com.vehicletrackingsys.api.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }
}
/*

return sessionService.isUserLoggedIn(session) ? "user/home" : "redirect:/login";*/
