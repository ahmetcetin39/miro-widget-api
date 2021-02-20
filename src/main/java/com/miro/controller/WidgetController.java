package com.miro.controller;

import com.miro.model.CreateWidgetRequest;
import com.miro.model.RectangleCoordinates;
import com.miro.model.UpdateWidgetRequest;
import com.miro.model.Widget;
import com.miro.service.WidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This is the controller for Widget related operations.
 *
 * @author ahmetcetin
 */
@RestController
@RequestMapping("/v1/widgets")
@RequiredArgsConstructor
public class WidgetController {
    private static final String DEFAULT_LIMIT = "10";

    private final WidgetService widgetService;

    @PostMapping
    public ResponseEntity<Widget> create(@Valid @RequestBody CreateWidgetRequest createWidgetRequest) {
        return new ResponseEntity<>(widgetService.createWidget(createWidgetRequest.toWidget()), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Widget> update(@Valid @RequestBody UpdateWidgetRequest updateWidgetRequest) {
        return new ResponseEntity<>(widgetService.updateWidget(updateWidgetRequest.toWidget()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        widgetService.deleteWidget(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Widget> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(widgetService.findWidgetById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Widget>> findAll(@RequestParam(required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
                                                @RequestParam(required = false) Integer x0,
                                                @RequestParam(required = false) Integer y0,
                                                @RequestParam(required = false) Integer x1,
                                                @RequestParam(required = false) Integer y1) {
        validateLimit(limit);

        // All four points need to be provided
        if (x0 != null && y0 != null && x1 != null && y1 != null) {
            RectangleCoordinates coordinates = RectangleCoordinates.builder()
                    .x0(x0).y0(y0)
                    .x1(x1).y1(y1)
                    .build();
            if (!coordinates.isValid()) {
                throw new IllegalArgumentException("x1 should be bigger than x0 & y1 should be bigger than y0.");
            }
            return new ResponseEntity<>(widgetService.findWithCoordinates(coordinates, limit), HttpStatus.OK);
        }

        return new ResponseEntity<>(widgetService.findWithLimit(limit), HttpStatus.OK);
    }

    private void validateLimit(Integer limit) {
        if(limit <= 0 || limit > 500) {
            throw new IllegalArgumentException("Limit should be between 1-500. Default is 10.");
        }
    }
}
