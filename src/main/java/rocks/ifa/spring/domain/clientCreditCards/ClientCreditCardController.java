package rocks.ifa.spring.domain.clientCreditCards;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.common.config.SecurityUtils;
import rocks.ifa.spring.domain.clientCreditCards.dtos.CreateCreditCardReq;
import rocks.ifa.spring.domain.clientCreditCards.dtos.CreditCardRes;
import rocks.ifa.spring.domain.clientCreditCards.dtos.UpdateCreditCardReq;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/credit-cards")
@Tag(name = "Client Credit Cards", description = "管理客戶的信用卡資料")
@RequiredArgsConstructor
public class ClientCreditCardController {

    private final ClientCreditCardService creditCardService;

    @Operation(summary = "為指定客戶新增一張信用卡")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardRes createCreditCard(
            @PathVariable UUID clientId,
            @RequestBody @Valid CreateCreditCardReq req) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        return creditCardService.createCreditCard(clientId, req, requesterUid);
    }

    @Operation(summary = "獲取指定客戶的所有信用卡列表")
    @GetMapping
    public List<CreditCardRes> getCreditCards(@PathVariable UUID clientId) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        return creditCardService.getCreditCardsByClientId(clientId, requesterUid);
    }

    @Operation(summary = "更新指定的信用卡資料")
    @PutMapping("/{cardId}")
    public CreditCardRes updateCreditCard(
            @PathVariable UUID clientId, // clientId is available from the path
            @PathVariable UUID cardId,
            @RequestBody @Valid UpdateCreditCardReq req) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        return creditCardService.updateCreditCard(clientId, cardId, req, requesterUid);
    }

    @Operation(summary = "刪除指定的信用卡")
    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCreditCard(
            @PathVariable UUID clientId, // clientId is available from the path
            @PathVariable UUID cardId) {
        String requesterUid = SecurityUtils.getCurrentUserUid();
        creditCardService.deleteCreditCard(clientId, cardId, requesterUid);
    }
}
