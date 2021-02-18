package com.miro.repository;

import com.miro.model.RectangleCoordinates;
import com.miro.model.Widget;

import java.util.List;

/**
 * This is the repository interface for {@link Widget}.
 *
 * @author ahmetcetin
 */
public interface WidgetRepository {
    Widget save(Widget widget);

    void deleteById(Long id);

    Widget findById(Long id);

    List<Widget> findWithLimit(Integer limit);

    List<Widget> findWithCoordinates(RectangleCoordinates coordinates, Integer limit);
}
