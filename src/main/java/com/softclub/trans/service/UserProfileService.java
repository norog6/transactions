package com.softclub.trans.service;

import com.softclub.trans.DTO.USerProfileDTO;
import com.softclub.trans.Mapper.UserProfileMapper;
import com.softclub.trans.entity.UserProfile;
import com.softclub.trans.repository.UserProfileREpositry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {
    @Autowired
    UserProfileREpositry userProfileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Transactional()
    public void Update(USerProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        UserProfile userProfile1 = userProfileRepository.findByName(userProfile.getName())
                .orElseThrow(() -> new RuntimeException("User profile not found"));
        userProfile1.setName(userProfile.getEmail());
        userProfile1.setGender(userProfile.getGender());
        UpdateUserProfile(userProfile1);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void UpdateUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Transactional
    public void updateUserProfile(Long userId, USerProfileDTO uSerProfileDTO) {
        UserProfile userProfile = userProfileMapper.toEntity(uSerProfileDTO);
        boolean updated = false;
        while (!updated) {
            try {
                UserProfile newUserProfile = entityManager.find(UserProfile.class, userId);
                if (userProfile != null) {
                    newUserProfile.setName(userProfile.getName());
                    newUserProfile.setEmail(userProfile.getEmail());
                    newUserProfile.setGender(userProfile.getGender());
                    entityManager.merge(newUserProfile);
                    updated = true;
                } else {
                    throw new RuntimeException("UserProfile not found");
                }
            } catch (OptimisticLockException e) {
                System.out.println("Optimistic lock exception occurred, retrying...");
            }
        }
    }
}
