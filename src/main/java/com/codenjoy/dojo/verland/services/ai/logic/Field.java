package com.codenjoy.dojo.verland.services.ai.logic;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.games.verland.Element;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;

import static com.codenjoy.dojo.games.verland.Element.CLEAR;
import static java.util.stream.Collectors.toCollection;

public class Field {

    private int size;
    private PointField cells;
    private List<Group> groups;
    private Cell[][] matrix;

    public Field(int size) {
        this.size = size;
        cells = new PointField().size(size);
        createCells();
    }

    public void clear() {
        groups = new ArrayList<>();

        List<QDirection> directions = QDirection.getValues();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Cell cell = matrix[x][y];
                cell.clear();

                for (QDirection direction : directions) {
                    Point to = direction.change(cell);
                    if (to.isOutOf(size)) continue;
                    cell.add(matrix[to.getX()][to.getY()]);
                }
            }
        }
    }

    private void createCells() {
        matrix = new Cell[size][size];
        for (int x = 0; x < size; x++) {
            matrix[x] = new Cell[size];
            for (int y = 0; y < size; y++) {
                Cell cell = new Cell(x, y);
                matrix[x][y] = cell;
                cells.add(cell);
            }
        }
    }

    public void scan(Function<Point, Element> get) {
        for (Cell cell : cells()) {
            cell.set(get.apply(cell));
        }
        setGroups();
    }

    private void setGroups() {
        for (Cell cell : cells()) {
            List<Cell> unknown = cell.unknownCells();
            if (cell.isValued() && !unknown.isEmpty()) {
                groups.add(new Group(unknown, cell.element()));
            }
        }
    }

    private Accessor<Cell> cells() {
        return cells.of(Cell.class);
    }

    private boolean isReachable(Cell cell) {
        return cell.neighbours().stream()
                .anyMatch(it -> it.isValued() && (it.element() == CLEAR));
    }

    public Collection<Cell> actions() {
        return groups.stream()
                // все группы клеток разбиваем в плоскую коллекцию
                .flatMap(group -> group.list().stream())
                // пропускаем клеточки в отношении которых ничего не поделать
                .filter(cell -> cell.action() != Action.NOTHING)
                // активные действия CURE совершаются в направлении '*' а значит туда мы не пойдем
                // а вот GO надо бы проверить на доступность клеточки
                .filter(cell -> cell.action() == Action.CURE || isReachable(cell))
                // сперва нас интересуют активные действия в устранении заразы
                .sorted((cell1, cell2) -> Boolean.compare(cell1.action() != Action.CURE, cell2.action() != Action.CURE))
                // мы исключаем все дубликаты
                .collect(toCollection(LinkedHashSet::new));
    }

    public Cell cell(Point pt) {
        return matrix[pt.getX()][pt.getY()];
    }
}
