package rocks.ifa.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

/**
 * A development-only service to manually initialize the database schema.
 * This bean is only created when the 'dev' profile is active.
 */
@Slf4j
@Service
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseAdminService {

    private final JdbcTemplate jdbcTemplate;
    private final ResourcePatternResolver resourceResolver;

    /**
     * Finds all .sql files in the manual_schema directory, sorts them by name,
     * and executes them sequentially.
     */
    @Transactional
    public void initializeDatabase() {
        log.warn("==================================================================");
        log.warn("🔥 EXECUTING MANUAL SCHEMA INITIALIZATION! THIS IS FOR DEV ONLY! 🔥");
        log.warn("==================================================================");

        try {
            // Drop all tables in the public schema to ensure a clean state
            log.info("Dropping all existing tables in 'public' schema...");
            jdbcTemplate.execute("DROP SCHEMA public CASCADE; CREATE SCHEMA public;");
            log.info("✅ Schema 'public' has been reset.");

            // Find, sort, and execute all schema files
            Resource[] resources = resourceResolver.getResources("classpath:db/manual_schema/*.sql");
            Arrays.sort(resources, Comparator.comparing(Resource::getFilename));

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                log.info("Executing script: {}", filename);
                String sqlScript = resource.getContentAsString(StandardCharsets.UTF_8);
                jdbcTemplate.execute(sqlScript);
                log.info("✅ Successfully executed {}", filename);
            }

            log.info("🎉 Database schema initialization completed successfully.");

        } catch (IOException e) {
            log.error("❌ Failed to read schema files.", e);
            throw new RuntimeException("Failed to read schema files.", e);
        } catch (Exception e) {
            log.error("❌ An error occurred during database initialization.", e);
            throw new RuntimeException("Database initialization failed.", e);
        }
    }
}
