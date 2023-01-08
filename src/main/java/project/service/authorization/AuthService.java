package project.service.authorization;

import project.persistence.entity.UserEntity;

public interface AuthService {

    boolean isSupervisor(String role);

    boolean isJournalist(String role);

    boolean isVisitor(String role);

    UserEntity login(String userName, String password);
}
