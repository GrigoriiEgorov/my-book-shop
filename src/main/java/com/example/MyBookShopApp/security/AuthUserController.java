package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.SearchWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AuthUserController {

    private final BookstoreUserRegister userRegister;

    @Autowired
    public AuthUserController(BookstoreUserRegister userRegister) {
        this.userRegister = userRegister;
    }

    @ModelAttribute("nameGetter")
    public String nameGetter(){
        return "";
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }


    @GetMapping("/signin")
    public String handleSignIn(){
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model){
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload){
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload){
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model){
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse){
//        return userRegister.login(payload);
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(){
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model){
        model.addAttribute("curUsr", userRegister.getCurrentUser());
        return "profile";
    }


//    @GetMapping("/logout")
//    public String handleLogout(HttpServletRequest request){
//        HttpSession session = request.getSession();
//        SecurityContextHolder.clearContext();
//        if(session != null){
//            session.invalidate();
//        }
//
//        for(Cookie cookie : request.getCookies()){
//            cookie.setMaxAge(0);
//        }
//
//        return "redirect:/";
//
//    }

}
