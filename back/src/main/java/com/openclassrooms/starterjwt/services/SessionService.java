package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    public SessionService(
            SessionRepository sessionRepository,
            TeacherRepository teacherRepository,
            UserRepository userRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    public Session create(Session session, Long teacherId, List<Long> userIds) {
        session.setTeacher(findTeacher(teacherId));
        session.setUsers(findUsers(userIds));
        return this.sessionRepository.save(session);
    }

    public void delete(Long id) {
        Session session = getById(id);
        this.sessionRepository.delete(session);
    }

    public List<Session> findAll() {
        return this.sessionRepository.findAll();
    }

    public Session getById(Long id) {
        return this.sessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found"));
    }

    @Transactional
    public Session update(Long id, Session changes, Long teacherId) {
        Session session = getById(id);
        session.setName(changes.getName());
        session.setDate(changes.getDate());
        session.setDescription(changes.getDescription());
        session.setTeacher(findTeacher(teacherId));

        return this.sessionRepository.save(session);
    }

    @Transactional
    public void participate(Long id, Long userId) {
        Session session = getById(id);
        User user = findUser(userId);

        boolean alreadyParticipate = session.getUsers().stream().anyMatch(o -> o.getId().equals(userId));
        if (alreadyParticipate) {
            throw new BadRequestException("User already participates in this session");
        }

        session.getUsers().add(user);

        this.sessionRepository.save(session);
    }

    @Transactional
    public void noLongerParticipate(Long id, Long userId) {
        Session session = getById(id);

        boolean alreadyParticipate = session.getUsers().stream().anyMatch(o -> o.getId().equals(userId));
        if (!alreadyParticipate) {
            throw new BadRequestException("User does not participate in this session");
        }

        session.getUsers().removeIf(user -> user.getId().equals(userId));

        this.sessionRepository.save(session);
    }

    private com.openclassrooms.starterjwt.models.Teacher findTeacher(Long teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private List<User> findUsers(List<Long> userIds) {
        if (userIds == null) {
            return new ArrayList<>();
        }

        return userIds.stream()
                .map(this::findUser)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
