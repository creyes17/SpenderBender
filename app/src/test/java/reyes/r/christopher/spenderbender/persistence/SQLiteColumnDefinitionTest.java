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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by Christopher R Reyes on 8/15/16.
 */
public class SQLiteColumnDefinitionTest {
    private HashMap<String, SQLiteColumnDefinition.DataType> structuredTypeBySqlType = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        // Create a mapping from SQLite Data type to our structured representation
        // See https://www.sqlite.org/datatype3.html

        // NOTE: This would be much better by using an actual parsing library for SQLite since this list is not complete

        // SQLite Integer Types
        structuredTypeBySqlType.put("INT", SQLiteColumnDefinition.DataType.INT);
        structuredTypeBySqlType.put("INTEGER", SQLiteColumnDefinition.DataType.INT);
        structuredTypeBySqlType.put("TINYINT", SQLiteColumnDefinition.DataType.INT);
        structuredTypeBySqlType.put("SMALLINT", SQLiteColumnDefinition.DataType.INT);
        structuredTypeBySqlType.put("MEDIUMINT", SQLiteColumnDefinition.DataType.INT);
        structuredTypeBySqlType.put("BIGINT", SQLiteColumnDefinition.DataType.INT);
        structuredTypeBySqlType.put("UNSIGNED BIG INT", SQLiteColumnDefinition.DataType.INT);
        structuredTypeBySqlType.put("INT2", SQLiteColumnDefinition.DataType.INT);
        structuredTypeBySqlType.put("INT8", SQLiteColumnDefinition.DataType.INT);

        // SQLite Text types
        structuredTypeBySqlType.put("CHARACTER", SQLiteColumnDefinition.DataType.TEXT);
        structuredTypeBySqlType.put("VARCHAR", SQLiteColumnDefinition.DataType.TEXT);
        structuredTypeBySqlType.put("VARYING CHARACTER", SQLiteColumnDefinition.DataType.TEXT);
        structuredTypeBySqlType.put("NCHAR", SQLiteColumnDefinition.DataType.TEXT);
        structuredTypeBySqlType.put("NATIVE CHARACTER", SQLiteColumnDefinition.DataType.TEXT);
        structuredTypeBySqlType.put("NVARCHAR", SQLiteColumnDefinition.DataType.TEXT);
        structuredTypeBySqlType.put("TEXT", SQLiteColumnDefinition.DataType.TEXT);
        structuredTypeBySqlType.put("CLOB", SQLiteColumnDefinition.DataType.TEXT);

        // SQLite Blob types
        //structuredTypeBySqlType.put("BLOB", SQLiteColumnDefinition.DataType.BLOB);

        // SQLite Real types
        structuredTypeBySqlType.put("REAL", SQLiteColumnDefinition.DataType.REAL);
        structuredTypeBySqlType.put("DOUBLE", SQLiteColumnDefinition.DataType.REAL);
        structuredTypeBySqlType.put("DOUBLE PRECISION", SQLiteColumnDefinition.DataType.REAL);
        structuredTypeBySqlType.put("FLOAT", SQLiteColumnDefinition.DataType.REAL);

        // SQLite Numeric types
        //structuredTypeBySqlType.put("NUMERIC", SQLiteColumnDefinition.DataType.NUMERIC);
        //structuredTypeBySqlType.put("DECIMAL", SQLiteColumnDefinition.DataType.NUMERIC);
        //structuredTypeBySqlType.put("BOOLEAN", SQLiteColumnDefinition.DataType.NUMERIC);
        //structuredTypeBySqlType.put("DATE", SQLiteColumnDefinition.DataType.NUMERIC);
        //structuredTypeBySqlType.put("DATETIME", SQLiteColumnDefinition.DataType.NUMERIC);
    }

    @Test
    public void test_toString() throws Exception {

        // toString should yield a column definition for use in a SQLite Create Table statement

        // Test basic column: just name and type
        String name1 = "foo";
        SQLiteColumnDefinition.DataType realType = SQLiteColumnDefinition.DataType.REAL;

        SQLiteColumnDefinition realName1 = new SQLiteColumnDefinition(name1, realType);

        // Statement should be "[name1] [real type]"
        String statement = realName1.toString();

        /* TODO: This is just for reference while I finish writing these tests
         * @param name - The name of the column
         * @param type - The DataType of the column
         * @param isPrimaryKey - Is this column the primary key for the table?
         * @param doesAutoIncrement - Should this column auto-increment? (Only applies to primary keys)
         * @param isUnique - Should there be a default unique constraint on this column?
         * @param isNotNull - Is this a required column?
         */
    }

    @Test
    public void standardPrimaryKey() throws Exception {
        // The standard Android primary key should be an "_id" column and an Integer
        SQLiteColumnDefinition primaryKey = SQLiteColumnDefinition.StandardPrimaryKey();

        Assert.assertNotNull("Primary Key should not be null", primaryKey);
        Assert.assertEquals("Primary key should be '_id'", "_id", primaryKey.getName());
        Assert.assertTrue("Primary Key should be indicated as a Primary key", primaryKey.getIsPrimaryKey());
        Assert.assertEquals("Primary Key should be an Integer type", SQLiteColumnDefinition.DataType.INT, primaryKey.getType());
    }

}