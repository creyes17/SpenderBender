/*
 * SpenderBender: A free offline budget tracking tool.
 * Copyright (c) 2016 Christopher R Reyes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package reyes.r.christopher.spenderbender.persistence;

import android.text.TextUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by Christopher R Reyes on 8/13/16.
 *
 * Defines a new immutable SQLite Database Table.
 * All of these methods are package-local. They don't need to be exposed beyond this package
 */

class SQLiteTableSchema {
    private final List<SQLiteColumnDefinition> columns;
    private final String name;

    private final String SQLCreateStatement;

    /**
     * Creates a new Table Schema
     * @param name - The name of the table
     * @param columns - A list of column definitions. Can be empty
     */
    SQLiteTableSchema(String name, List<SQLiteColumnDefinition> columns) {
        this.name = name;
        this.columns = columns;

        this.SQLCreateStatement = generateCreateSQL();
    }

    List<SQLiteColumnDefinition> getColumns() {
        return columns;
    }

    String getName() {
        return name;
    }

    /**
     * @return The Table Schema represented as a SQLite statement to create the table.
     */
    @Override
    public String toString() {
        return this.SQLCreateStatement;
    }

    /**
     * @return A valid SQLite Create statement for creating the table
     */
    private String generateCreateSQL() {
        String createStatement = String.format(Locale.getDefault(), "CREATE TABLE IF NOT EXISTS %s", this.name);

        if ( !this.columns.isEmpty() ) {
            String columnDefinition = TextUtils.join(", ", this.columns);

            createStatement = String.format(Locale.getDefault(), "%s ( %s );", createStatement, columnDefinition);
        }

        return createStatement;
    }
}
