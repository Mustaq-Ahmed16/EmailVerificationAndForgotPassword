package in.stockmarketmain.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import in.stockmarketmain.entities.User;
import in.stockmarketmain.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private JavaMailSender mailSender;

	@Override
	public boolean registerUser(User user) {
		
		try {
			String verificationToken = UUID.randomUUID().toString();
	        user.setVerificationToken(verificationToken);
	        user.setEnabled(false); // Initially set to false
	        userRepository.save(user);

	        // Send verification email
	        sendVerificationEmail(user);

			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	
	}
	@Override
	public User loginUser(String email,String password)
	{
		User validUser = userRepository.findByEmail(email);
		if(validUser != null && validUser.getPassword().equals(password))
		{
			return validUser;
		}
		
		return null;
		
	}
	@Override
	public void sendVerificationEmail(User user) {
		// TODO Auto-generated method stub
		String verificationUrl = "http://localhost:8181/verify?token=" + user.getVerificationToken();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Email Verification");
        mailMessage.setText("Hello " + user.getName() + ",\n\n" +
                            "Please click the following link to verify your email address:\n" +
                            verificationUrl + "\n\n" +
                            "Thank you,\n" +
                            "Your Company");

        mailSender.send(mailMessage);
		
	}
	@Override
	public boolean verifyUser(String token) {
		// TODO Auto-generated method stub
		User user = userRepository.findByVerificationToken(token);
		if (user != null) {
		    // User with the given token was found
		    user.setEnabled(true); // Enable the user
		    user.setVerificationToken(null); // Clear the token after verification
		    userRepository.save(user);
		    return true;
		} else {
		    // No user found with the given token
		    return false;
		}
	}
	@Override
	public void forgotPassword(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
		    String resetToken = UUID.randomUUID().toString();
		    user.setResetToken(resetToken);
		    userRepository.save(user);

		    sendResetEmail(user);
		}

		
	}
	@Override
	public void sendResetEmail(User user) {
		// TODO Auto-generated method stub
		String resetLink = "http://localhost:8080/reset-password?token=" + user.getResetToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("Click the link to reset your password: " + resetLink);
        mailSender.send(mailMessage);
		
	}
	@Override
	public boolean resetPassword(String token, String newPassword) {
		// TODO Auto-generated method stub
		User user = userRepository.findByResetToken(token);
		if (user != null) {
		    user.setPassword(newPassword); // You may want to encrypt this password
		    user.setResetToken(null); // Clear the reset token after password is changed
		    userRepository.save(user);
		    return true;
		}

		return false;
	}
	

}
