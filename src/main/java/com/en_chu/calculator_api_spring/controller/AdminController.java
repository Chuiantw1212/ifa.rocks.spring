package com.en_chu.calculator_api_spring.controller;

import com.en_chu.calculator_api_spring.service.DatabaseAdminService;
import com.en_chu.calculator_api_spring.service.FirebaseSeedingService;
import com.en_chu.calculator_api_spring.service.StartupDataCleanupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A development-only controller for administrative tasks.
 * This controller and its endpoints are only available when the 'dev' profile is active.
 */
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin API (DEV ONLY)", description = "Administrative tasks for development")
@Profile("dev")
@RequiredArgsConstructor
public class AdminController {

    private final FirebaseSeedingService firebaseSeedingService;
    private final StartupDataCleanupService startupDataCleanupService;
    private final DatabaseAdminService databaseAdminService;

    @Operation(summary = "[DB] Initialize Database",
               description = "Drops the 'public' schema and re-creates all tables from the .sql files. THIS IS A DESTRUCTIVE OPERATION.")
    @PostMapping("/db/initialize")
    public ResponseEntity<String> initializeDatabase() {
        databaseAdminService.initializeDatabase();
        return ResponseEntity.ok("Database initialization successful.");
    }

    @Operation(summary = "[Firestore] Sync Metadata Configs",
               description = "Syncs configuration files from the local /init-data folder to Firestore.")
    @PostMapping("/firestore/sync-metadata")
    public ResponseEntity<String> syncMetadata() {
        firebaseSeedingService.syncMetadataConfigs();
        return ResponseEntity.ok("Metadata configs synced successfully.");
    }

    @Operation(summary = "[Firestore] Sync Life Table",
               description = "Syncs the life table from local /init-data to Firestore. This is a time-consuming operation.")
    @PostMapping("/firestore/sync-life-table")
    public ResponseEntity<String> syncLifeTable() {
        firebaseSeedingService.syncLifeTable();
        return ResponseEntity.ok("Life Table data sync started/completed.");
    }

    @Operation(summary = "[DB] Cleanup Orphaned Data",
               description = "Manually triggers the cleanup of all orphaned records in child tables that no longer have a corresponding user in user_profiles.")
    @PostMapping("/db/cleanup-orphans")
    public ResponseEntity<String> cleanupOrphanedData() {
        startupDataCleanupService.cleanupOrphanedData();
        return ResponseEntity.ok("Orphaned data cleanup process finished.");
    }
}
