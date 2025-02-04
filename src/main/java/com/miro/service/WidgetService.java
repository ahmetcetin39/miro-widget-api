package com.miro.service;

import com.miro.model.RectangleCoordinates;
import com.miro.model.Widget;

import java.util.List;

/**
 * This is the service interface for widget related operations.
 *
 * @author ahmetcetin
 */
public interface WidgetService {
    /**
     * @param widget to create
     * @return created widget
     */
    Widget createWidget(Widget widget);

    /**
     * @param widget to update
     * @return updated widget
     */
    Widget updateWidget(Widget widget);

    /**
     * @param id of widget to delete
     */
    void deleteWidget(Long id);

    /**
     * @param id of widget to find
     * @return found widget
     */
    Widget findWidgetById(Long id);

    /**
     * @param limit for queried Widget entities
     * @return the list of widgets sorted by zIndex with limit
     */
    List<Widget> findWithLimit(Integer limit);

    /**
     * @param coordinates to find out which widgets are in
     * @param limit       for queried Widget entities
     * @return the widgets found in the specific coordinates sorted by zIndex with limit
     */
    List<Widget> findWithCoordinates(RectangleCoordinates coordinates, Integer limit);
}
