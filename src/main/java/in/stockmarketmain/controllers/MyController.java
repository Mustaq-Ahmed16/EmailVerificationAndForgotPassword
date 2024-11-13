package in.stockmarketmain.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.stockmarketmain.entities.User;
import in.stockmarketmain.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MyController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/regPage")
	public String openRegPage(Model model)
	{
		model.addAttribute("user",new User());
		return "register";
	}
	@PostMapping("/regForm")
	public String submitRegForm(@ModelAttribute User user,Model model)
	{
		userService.registerUser(user);
        model.addAttribute("message", "A verification email has been sent to " + user.getEmail());
        return "login";
	}
	
	@GetMapping("/verify")
    public String verifyUser(@RequestParam String token, Model model) {
        boolean verified = userService.verifyUser(token);
        if (verified) {
            model.addAttribute("message", "Your account has been successfully verified!");
        } else {
            model.addAttribute("message", "Invalid or expired verification token.");
        }
        return "verification_status"; // Page showing verification result
    }
	
	
	@GetMapping("/loginPage")
	public String openLoginPage(Model model)
	{
		model.addAttribute("user",new User());
		return "login";
	}
	@PostMapping("/loginForm")
	public String submitLoginForm(@ModelAttribute User user,Model model)
	{
		User validUser = userService.loginUser(user.getEmail(), user.getPassword());
		if(validUser != null)
		{
			model.addAttribute("modelName",validUser.getName());
			return "profile";
		}
		else {
			model.addAttribute("errorMsg","EmailId and password doesnot match.");
			return "login";
		}
		
	}
	@GetMapping("/logout")
	public String logout(HttpServletRequest req)
	{
		HttpSession session = req.getSession(false);
		if(session != null)
		{
			session.invalidate();
		}
		return "redirect:/loginPage";
	}
	@PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model model) {
        userService.forgotPassword(email);
        model.addAttribute("message", "A password reset link has been sent to your email.");
        return "login";
    }
	@GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password"; // This is the reset password HTML form
    }
	
	@PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, 
                                @RequestParam("password") String newPassword, 
                                Model model) {
        boolean resetSuccessful = userService.resetPassword(token, newPassword);
        if (resetSuccessful) {
            model.addAttribute("message", "Password reset successfully.");
            return "login";
        } else {
            model.addAttribute("message", "Invalid reset token.");
            return "reset_password";
        }
    }
}
