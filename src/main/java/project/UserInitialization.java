package project;

import java.util.Optional;
import org.springframework.stereotype.Component;
import project.persistence.entity.UserEntity;
import project.persistence.repository.UserRepository;
import project.service.enums.Roles;

@Component
public class UserInitialization {

    private UserRepository userRepository;


    public UserInitialization(UserRepository userRepository) {
        this.userRepository = userRepository;
        initUsers();

    }

    private void initUsers() {
        Optional<UserEntity> journalistOptional = userRepository.findUserEntityByUsername("journalist");
        Optional<UserEntity> supervisorOptional = userRepository.findUserEntityByUsername("supervisor");


        if(!journalistOptional.isPresent()){
            UserEntity journalist = new UserEntity();
            journalist.setPassword("journalist");
            journalist.setUsername("journalist");
            journalist.setFirstName("journalist first name");
            journalist.setLastName("journalist last name");
            journalist.setRole(Roles.JOURNALIST.name());

            userRepository.save(journalist);
        }

        if(!supervisorOptional.isPresent()){
            UserEntity supervisor = new UserEntity();
            supervisor.setPassword("supervisor");
            supervisor.setUsername("supervisor");
            supervisor.setFirstName("supervisor first name");
            supervisor.setLastName("supervisor last name");
            supervisor.setRole(Roles.SUPERVISOR.name());

            userRepository.save(supervisor);
        }

    }
}

