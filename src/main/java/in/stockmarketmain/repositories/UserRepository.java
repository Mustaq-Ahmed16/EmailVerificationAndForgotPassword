package in.stockmarketmain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import in.stockmarketmain.entities.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	User findByEmail(String email);
	User findByVerificationToken(String verificationToken);
	User findByResetToken(String resetToken);
	

}
