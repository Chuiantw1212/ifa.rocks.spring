package rocks.ifa.spring.infrastructure.config;

/*
  This configuration is temporarily disabled by commenting out the annotations.
  This prevents Spring from trying to bind properties to it during startup,
  which would fail if the corresponding environment variables are not set.
*/
// @Component
// @ConfigurationProperties(prefix = "line.liff")
public record LineLiffProperties(String channelId) {
    // The record is kept for structural reference, but it will not be an active Spring bean.
}
