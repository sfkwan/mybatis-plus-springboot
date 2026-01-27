package com.example.restservice.genericresponse;

/**
 * Response wrapper for delete operations.
 * Contains the result status, a descriptive message, and the ID of the deleted
 * entity.
 * 
 * @param success Indicates if the delete operation was successful.
 * @param message Descriptive message about the delete operation.
 * @param id      The ID of the entity that was deleted.
 * 
 * @author Application Development Team
 * @since 1.0
 */
public record DeleteResult(boolean success, String message, String id) {
}
