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

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Christopher R Reyes on 8/13/16.
 *
 * Defines a single immutable column in SQLite.
 * All of these methods are package-local. They don't need to be exposed beyond this package
 */

class SQLiteColumnDefinition {

    enum DataType {
        TEXT, INT, REAL
    }

    private final DataType type;
    private final String name;
    private final Boolean isPrimaryKey;
    private final Boolean doesAutoIncrement;
    private final Boolean isUnique;
    private final Boolean isNotNull;

    private final Map<DataType, String> dataTypeStringMap;

    private final String toSQLText;

    /**
     * Generates a new immutable SQLiteColumnDefinition
     *
     * @param name - The name of the column
     * @param type - The DataType of the column
     * @param isPrimaryKey - Is this column the primary key for the table?
     * @param doesAutoIncrement - Should this column auto-increment? (Only applies to primary keys)
     * @param isUnique - Should there be a default unique constraint on this column?
     * @param isNotNull - Is this a required column?
     */
    SQLiteColumnDefinition(String name, DataType type, @Nullable Boolean isPrimaryKey, @Nullable Boolean doesAutoIncrement, @Nullable Boolean isUnique, @Nullable Boolean isNotNull) {
        this.name = name;
        this.type = type;

        if (isPrimaryKey == null ) {
            this.isPrimaryKey = Boolean.FALSE;
        }
        else {
            this.isPrimaryKey = isPrimaryKey;
        }

        if ( doesAutoIncrement != null && doesAutoIncrement && this.isPrimaryKey ) {
            this.doesAutoIncrement = Boolean.TRUE;
        }
        else {
            this.doesAutoIncrement = Boolean.FALSE;
        }

        if ( isUnique != null ) {
            this.isUnique = isUnique;
        }
        else {
            this.isUnique = Boolean.FALSE;
        }

        if ( isNotNull != null ) {
            this.isNotNull = isNotNull;
        }
        else {
            this.isNotNull = Boolean.FALSE;
        }

        this.dataTypeStringMap = generateDataTypeStringMap();
        this.toSQLText = generateColumnSQL();
    }

    /**
     * Generates a new immutable SQLiteColumnDefinition
     *
     * @param name - The name of the column
     * @param type - The DataType of the column
     */
    SQLiteColumnDefinition(String name, DataType type) {
        this(name, type, null, null, null, null);
    }

    DataType getType() {
        return this.type;
    }

    String getName() {
        return this.name;
    }

    Boolean getIsPrimaryKey() {
        return this.isPrimaryKey;
    }

    Boolean getDoesAutoIncrement() {
        return this.doesAutoIncrement;
    }

    Boolean getIsUnique() {
        return this.isUnique;
    }

    Boolean getIsNotNull() {
        return this.isNotNull;
    }

    /**
     * Creates a mapping from DataType to the actual SQLite type to use when constructing a statement
     * @return The mapping
     */
    private static Map<DataType, String> generateDataTypeStringMap() {
        HashMap<DataType, String> conversion = new HashMap<>();
        conversion.put(DataType.INT, "INTEGER");
        conversion.put(DataType.REAL, "REAL");
        conversion.put(DataType.TEXT, "TEXT");

        return conversion;
    }

    /**
     * Creates the column definition portion of a SQL Statement to include this column in a table
     * @return the String SQL command
     */
    private String generateColumnSQL() {
        String sqlText = String.format(Locale.getDefault(), "%s %s", this.name, this.dataTypeStringMap.get(this.type));
        if( this.isPrimaryKey) {
            sqlText = sqlText + " PRIMARY KEY";

            if (this.doesAutoIncrement) {
                sqlText = sqlText + " AUTOINCREMENT";
            }
        }

        if(this.isUnique) {
            sqlText = sqlText + " UNIQUE";
        }

        if (this.isNotNull) {
            sqlText = sqlText + " NOT NULL";
        }

        return sqlText;
    }

    @Override
    public String toString() {
        return toSQLText;
    }

    /**
     * @return The standard column definition for a table's primary key
     */
    static SQLiteColumnDefinition StandardPrimaryKey() {
        return new SQLiteColumnDefinition(
                "_id",          // Name
                DataType.INT,   // Type
                Boolean.TRUE,   // isPrimaryKey
                Boolean.TRUE,   // doesAutoIncrement
                Boolean.TRUE,   // isUnique
                Boolean.TRUE    // isNotNull
        );
    }
}
