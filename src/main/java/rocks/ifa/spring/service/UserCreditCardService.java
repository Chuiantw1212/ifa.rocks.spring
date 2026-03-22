package rocks.ifa.spring.service;

import rocks.ifa.spring.model.UserCreditCardDto;

import java.util.List;

public interface UserCreditCardService {
    List<UserCreditCardDto> getCards(String firebaseUid);
    UserCreditCardDto getCard(Long id, String firebaseUid);
    UserCreditCardDto createCard(String firebaseUid);
    UserCreditCardDto updateCard(Long id, UserCreditCardDto dto, String firebaseUid);
    void deleteCard(Long id, String firebaseUid);
}
