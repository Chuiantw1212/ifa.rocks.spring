package rocks.ifa.spring.application.status;

import java.util.Map;

public interface StatusService {
    Map<String, Object> getServerStatus();
}
