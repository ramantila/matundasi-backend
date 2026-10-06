package bytech.co.tz.matundasi.modules.elements.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.elements.payload.ElementRequests;
import bytech.co.tz.matundasi.modules.elements.response.ElementResponse;
import bytech.co.tz.matundasi.modules.elements.service.ElementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/elements")
@RequiredArgsConstructor
@Tag(name = "Elements", description = "Element management API")
public class ElementController {
    private final ElementService elementService;

    @PostMapping
    @Operation(summary = "Create a new element",
            description = "Registers a new element. Fails with 409 if the name or symbol is already used by " +
                    "another active element.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Element created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Element name or symbol already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ElementResponse>> create(@Valid @RequestBody ElementRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Element created successfully.", HttpStatus.CREATED, elementService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List elements", description = "Returns all active (non-deleted) elements sorted by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Elements returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<ElementResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Elements retrieved successfully.", HttpStatus.OK, elementService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an element by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Element returned"),
            @ApiResponse(responseCode = "404", description = "Element not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ElementResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Element retrieved successfully.", HttpStatus.OK, elementService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an element",
            description = "Replaces the name and symbol of an existing element. Fails with 409 if the new name " +
                    "or symbol is already used by another active element.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Element updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Element not found"),
            @ApiResponse(responseCode = "409", description = "Element name or symbol already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ElementResponse>> update(@PathVariable UUID id,
                                                                @Valid @RequestBody ElementRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Element updated successfully.", HttpStatus.OK, elementService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an element", description = "Soft-deletes the element (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Element deleted"),
            @ApiResponse(responseCode = "404", description = "Element not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        elementService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Element deleted successfully.", HttpStatus.OK));
    }
}
