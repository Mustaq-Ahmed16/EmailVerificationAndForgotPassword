package in.stockmarketmain.services;

import in.stockmarketmain.entities.User;

public interface UserService {
	public boolean registerUser(User user);
	public User loginUser(String email,String password);
	public void sendVerificationEmail(User user);
	public boolean verifyUser(String token);
	public void forgotPassword(String email);
	public void sendResetEmail(User user);
	public boolean resetPassword(String token, String newPassword);
}
