package com.example.wellnesscoach.checkup.service;

import com.example.wellnesscoach.checkup.Checkup;
import com.example.wellnesscoach.checkup.MenuItem;
import com.example.wellnesscoach.checkup.exception.NotFoundCheckupException;
import com.example.wellnesscoach.checkup.repository.CheckupRepository;
import com.example.wellnesscoach.checkup.service.request.SaveCheckupCommand;
import com.example.wellnesscoach.entity.User;
import com.example.wellnesscoach.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckupService {

    private CheckupRepository checkupRepository;
    private UserRepository userRepository;

    public CheckupService(CheckupRepository checkupRepository, UserRepository userRepository) {
        this.checkupRepository = checkupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Checkup saveCheckup(SaveCheckupCommand saveCheckupCommand) {
        Checkup checkup;

        User user = userRepository.findById(saveCheckupCommand.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (saveCheckupCommand.checkupId() != null) {
            checkup = checkupRepository.findById(saveCheckupCommand.checkupId())
                    .orElseThrow(() -> new NotFoundCheckupException("해당 진단지를 찾을 수 없습니다."));
        } else {
            checkup = new Checkup();
        }

        List<MenuItem> menuItems = saveCheckupCommand.menuItemCommands().stream()
                .map(menuItemRequest -> new MenuItem(checkup, menuItemRequest.type(), menuItemRequest.name()))
                .collect(Collectors.toList());

        checkup.update(
                user,
                saveCheckupCommand.date(),
                menuItems,
                saveCheckupCommand.memo()
        );
        return checkupRepository.save(checkup);
    }

    /*public Checkup submit(Checkup checkup) {

    }*/
}
