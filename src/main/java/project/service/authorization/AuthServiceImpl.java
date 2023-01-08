package project.service.authorization;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.errorhandling.exception.AuthException;
import project.persistence.entity.UserEntity;
import project.persistence.repository.UserRepository;
import project.service.enums.Roles;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isSupervisor(String role) {
       return Roles.SUPERVISOR.name().equals(role);
    }

    @Override
    public boolean isJournalist(String role) {
        return Roles.JOURNALIST.name().equals(role);
    }

    @Override
    public boolean isVisitor(String role) {
        return Roles.VISITOR.name().equals(role);
    }

    @Override
    public UserEntity login(String userName, String password) {

        if(userName == null || password == null){
            UserEntity visitor = new UserEntity();
            visitor.setRole(Roles.VISITOR.name());
            return visitor;
        }
        Optional<UserEntity> userOptional = userRepository.findUserEntityByUsernameAndPassword(userName, password);

        if(userOptional.isPresent()){
            return userOptional.get();
        }else{
            throw new AuthException();
        }
    }
}
