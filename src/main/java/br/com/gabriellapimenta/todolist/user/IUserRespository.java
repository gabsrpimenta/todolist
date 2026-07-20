package br.com.gabriellapimenta.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IUserRespository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username);


}
