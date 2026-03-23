package rocks.ifa.spring.domain.client;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.clientCareer.ClientCareerService;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceService;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionService;
import rocks.ifa.spring.domain.clientProfile.ClientProfile;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.domain.clientProfile.ClientProfileService;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementService;
import rocks.ifa.spring.domain.clientTax.ClientTaxService;
import rocks.ifa.spring.domain.agent.UserFullDataRes;
import rocks.ifa.spring.domain.agent.UserRegistrationReq;

public interface UserService {
    UserFullDataRes getFullUserData(String uid);
    void deleteUser(String uid);
    void registerUser(UserRegistrationReq req);

    @Slf4j
    @Service
    @RequiredArgsConstructor
    class UserServiceImpl implements UserService {

        private final ClientProfileRepository clientProfileRepository;
        private final ClientProfileService clientProfileService;
        private final ClientCareerService clientCareerService;
        private final ClientLaborPensionService clientLaborPensionService;
        private final ClientLaborInsuranceService clientLaborInsuranceService;
        private final ClientRetirementService clientRetirementService;
        private final ClientTaxService clientTaxService;

        @Override
        public UserFullDataRes getFullUserData(String uid) {
            log.info("🔍 [UserService] Assembling core user data for UID: {}", uid);
            UserFullDataRes response = new UserFullDataRes();

            response.setProfile(clientProfileService.getProfile(uid));
            response.setCareer(clientCareerService.getCareer(uid));
            response.setLaborPension(clientLaborPensionService.getLaborPension(uid));
            response.setLaborInsurance(clientLaborInsuranceService.getLaborInsurance(uid));
            response.setRetirement(clientRetirementService.getRetirement(uid));
            response.setTax(clientTaxService.getTax(uid));

            if (response.getProfile() != null) {
                response.setId(response.getProfile().getId());
            }

            log.info("✅ [UserService] Core user data assembled successfully for UID: {}", uid);
            return response;
        }

        @Override
        @Transactional
        public void deleteUser(String uid) {
            log.warn("🗑️ [DELETE] Starting deletion of all data for user: UID={}", uid);
            clientProfileRepository.deleteByFirebaseUid(uid);
            log.info("  - All user data has been deleted for UID: {}", uid);

            try {
                FirebaseAuth.getInstance().deleteUser(uid);
                log.info("🔥 [DELETE] Firebase Auth account has been successfully deleted for UID: {}", uid);
            } catch (FirebaseAuthException e) {
                log.error("❌ [DELETE] Failed to delete Firebase Auth account for UID: {}, Error: {}", uid, e.getMessage());
                throw new RuntimeException("Failed to delete Firebase account. Database operations have been rolled back.", e);
            }
        }

        @Override
        public void registerUser(UserRegistrationReq req) {
            // This is a placeholder. You should implement your own logic here.
        }

        @Transactional
        public void syncUser(String uid) {
            if (!clientProfileRepository.existsByFirebaseUid(uid)) {
                log.info("✨ [Sync] New user detected. Creating initial profile for UID: {}", uid);
                ClientProfile newProfile = new ClientProfile();
                newProfile.setFirebaseUid(uid);
                clientProfileRepository.save(newProfile);
            }
        }
    }
}
