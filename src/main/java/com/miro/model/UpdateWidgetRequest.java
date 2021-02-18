package com.miro.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * This is the request model of Widget update,
 * differently from {@link CreateWidgetRequest}, id needs to be provided.
 * All other fields besides zIndex need to be provided, otherwise the widget wouldn't be updated.
 *
 * @author ahmetcetin
 */
@Data
@Builder
public class UpdateWidgetRequest {
    @NotNull(message = "id should be provided to update an existing widget.")
    private Long id;

    @NotNull(message = "xIndex should be provided.")
    private Integer xIndex;

    @NotNull(message = "xIndex should be provided.")
    private Integer yIndex;

    private Integer zIndex;

    @NotNull(message = "xIndex should be provided.")
    @Positive(message = "width should be positive.")
    private Integer width;

    @NotNull(message = "xIndex should be provided.")
    @Positive(message = "height should be positive.")
    private Integer height;

    public Widget toWidget() {
        return Widget.builder()
                .id(id)
                .xIndex(xIndex)
                .yIndex(yIndex)
                .zIndex(zIndex)
                .width(width)
                .height(height)
                .build();
    }
}
