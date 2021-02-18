package com.miro.repository.impl;

import com.miro.exception.WidgetNotFoundException;
import com.miro.model.RectangleCoordinates;
import com.miro.model.Widget;
import com.miro.repository.WidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This is the repository implementation to use in-memory ConcurrentHashMap as datasource for operations.
 * Besides the ConcurrentHashMap used to keep the widgets, there is another map(TreeMap) to keep widgetIds by their zIndex.
 * This TreeMap is basically used as an index for zIndex property.
 *
 * @author ahmetcetin
 */
@Repository
@Profile("in-memory")
@RequiredArgsConstructor
public class InMemoryRepository implements WidgetRepository {
    private static volatile Long widgetIdCounter = 0L;

    private final Map<Long, Widget> widgetDB = new ConcurrentHashMap<>();
    private final TreeMap<Integer, Long> zIndexDB = new TreeMap<>();

    private synchronized static Long getNextWidgetId() {
        return widgetIdCounter++;
    }

    @Override
    public Widget save(Widget widget) {         // This is a create operation
        if (widget.getId() == null) {
            widget.setId(getNextWidgetId());
        } else {                                // This is an update operation
            if (widgetDB.get(widget.getId()) == null) {
                throw new WidgetNotFoundException("Couldn't find widget to update with id: " + widget.getId());
            }
            zIndexDB.remove(widgetDB.get(widget.getId()).getZIndex()); // Delete old z-index reference.
        }

        // This will be applicable to insert only.
        if (widget.getZIndex() == null) {
            widget.setZIndex(getMaxZIndex());
        }

        Long widgetIdAtSameZIndex = zIndexDB.get(widget.getZIndex());
        if (widgetIdAtSameZIndex != null && !widgetIdAtSameZIndex.equals(widget.getId())) {
            shift(widget);
        }
        saveWidget(widget);
        return widget;
    }

    @Override
    public void deleteById(Long id) {
        if (!widgetDB.containsKey(id)) {
            throw new WidgetNotFoundException("Couldn't find widget to delete with id: " + id);
        }

        zIndexDB.remove(widgetDB.get(id).getZIndex()); // Remove first from zIndex map
        widgetDB.remove(id); // Then, remove from widget map
    }

    @Override
    public Widget findById(Long id) {
        Widget widget = widgetDB.get(id);
        if (widget == null) {
            throw new WidgetNotFoundException("Couldn't find widget by id: " + id);
        }
        return widget;
    }

    @Override
    public List<Widget> findWithLimit(Integer limit) {
        return zIndexDB.values()
                .stream()
                .limit(limit)
                .map(widgetDB::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Widget> findWithCoordinates(RectangleCoordinates coordinates, Integer limit) {
        return zIndexDB.values()
                .stream()
                .map(widgetDB::get)
                .filter(widget -> isInRectangle(widget, coordinates))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private void shift(Widget widget) {
        Integer endIndex = widget.getZIndex();
        for (int i = endIndex; i < zIndexDB.lastKey(); i++) {
            if (zIndexDB.containsKey(endIndex + 1)) {
                endIndex += 1;
            } else {
                break;
            }
        }

        // Move the widgets back one by one
        Integer finalEndIndex = endIndex;
        for (int i = finalEndIndex; i >= widget.getZIndex(); i--) {
            Integer newIndex = i + 1;

            Widget widgetToUpdate = widgetDB.get(zIndexDB.get(i));
            widgetToUpdate.setZIndex(newIndex);
            widgetDB.put(widgetToUpdate.getId(), widgetToUpdate);
            zIndexDB.put(newIndex, zIndexDB.get(i));
        }
    }

    private void saveWidget(Widget widget) {
        widget.setUpdateTime(LocalDateTime.now());
        widgetDB.put(widget.getId(), widget);
        zIndexDB.put(widget.getZIndex(), widget.getId());
    }

    private Integer getMaxZIndex() {
        return zIndexDB.isEmpty() ? 0 : zIndexDB.lastKey() + 1;
    }

    private boolean isInRectangle(Widget widget, RectangleCoordinates coordinates) {
        double halfWidth = (double) widget.getWidth() / 2;
        double halfHeight = (double) widget.getHeight() / 2;

        return widget.getXIndex() - halfWidth >= coordinates.getX0()
                && widget.getXIndex() + halfWidth <= coordinates.getX1()
                && widget.getYIndex() - halfHeight >= coordinates.getY0()
                && widget.getYIndex() + halfHeight <= coordinates.getY1();
    }
}
