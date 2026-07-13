package rocks.ifa.spring.common.status;

import java.util.Map;

public interface StatusService {
    Map<String, Object> getServerStatus();
}
