package bytech.co.tz.matundasi.modules.drill_survey.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.drill_survey.payload.DrillSurveyRequests;
import bytech.co.tz.matundasi.modules.drill_survey.response.DrillSurveyResponse;
import bytech.co.tz.matundasi.modules.drill_survey.service.DrillSurveyService;
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
@RequestMapping("/api/v1/drill-surveys")
@RequiredArgsConstructor
@Tag(name = "Drill Surveys", description = "Drill survey management API")
public class DrillSurveyController {
    private final DrillSurveyService drillSurveyService;

    @PostMapping
    @Operation(summary = "Create a new drill survey",
            description = "Registers a new drill survey reading. Fails with 409 if the hole already has an " +
                    "active survey at the same downdepth.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Drill survey created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Survey already exists for this hole at this downdepth"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DrillSurveyResponse>> create(@Valid @RequestBody DrillSurveyRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Drill survey created successfully.", HttpStatus.CREATED, drillSurveyService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List drill surveys",
            description = "Returns all active (non-deleted) drill surveys sorted by hole, then downdepth.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Drill surveys returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<DrillSurveyResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Drill surveys retrieved successfully.", HttpStatus.OK, drillSurveyService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a drill survey by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Drill survey returned"),
            @ApiResponse(responseCode = "404", description = "Drill survey not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DrillSurveyResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Drill survey retrieved successfully.", HttpStatus.OK, drillSurveyService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a drill survey",
            description = "Replaces the hole, downdepth, dip, azimuth and utm_az of an existing drill survey. " +
                    "Fails with 409 if the hole already has another active survey at the new downdepth.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Drill survey updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Drill survey not found"),
            @ApiResponse(responseCode = "409", description = "Survey already exists for this hole at this downdepth"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DrillSurveyResponse>> update(@PathVariable UUID id,
                                                                    @Valid @RequestBody DrillSurveyRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Drill survey updated successfully.", HttpStatus.OK, drillSurveyService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a drill survey", description = "Soft-deletes the drill survey (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Drill survey deleted"),
            @ApiResponse(responseCode = "404", description = "Drill survey not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        drillSurveyService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Drill survey deleted successfully.", HttpStatus.OK));
    }
}
