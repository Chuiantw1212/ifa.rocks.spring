package rocks.ifa.spring.infra.status;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/status")
@Tag(name = "Infra: Status", description = "伺服器狀態與健康檢查")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping
    @Operation(summary = "獲取當前伺服器運行狀態")
    public Map<String, Object> getServerStatus() {
        return statusService.getServerStatus();
    }
}
