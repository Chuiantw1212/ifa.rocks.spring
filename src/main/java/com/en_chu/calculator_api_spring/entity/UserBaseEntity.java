package com.en_chu.calculator_api_spring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserBaseEntity {

    /**
     * The unique identifier for the record (e.g., Portfolio ID, Career ID).
     * This is used by the frontend for updating (PUT) or deleting (DELETE) specific records.
     * It is read-only from the client's perspective.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    /**
     * The Firebase UID that owns this data record.
     * @JsonIgnore ensures this field is never serialized and sent to the client for security reasons.
     */
    @JsonIgnore
    private String firebaseUid;

    /**
     * The timestamp when the record was created.
     * Using OffsetDateTime to correctly map to PostgreSQL's TIMESTAMPTZ type.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime createdAt;

    /**
     * The timestamp when the record was last updated.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime updatedAt;
}
