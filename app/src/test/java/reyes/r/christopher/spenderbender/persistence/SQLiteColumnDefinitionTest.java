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
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Christopher R Reyes on 8/15/16.
 k
 * Tests for our SQLite column definitions
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
        SQLiteColumnDefinition.DataType type1 = SQLiteColumnDefinition.DataType.REAL;

        SQLiteColumnDefinition columnDefinition1 = new SQLiteColumnDefinition(name1, type1);

        // Stringified form should be "[name1] [type1]"
        String[] tokens = columnDefinition1.toString().split("\\s", 2);

        Assert.assertEquals("Column name should match input", name1.toUpperCase(Locale.getDefault()), tokens[0].toUpperCase(Locale.getDefault()));
        Assert.assertEquals("Data type should match input data type", type1, structuredTypeBySqlType.get(tokens[1].toUpperCase(Locale.getDefault())));

        // Test basic column: name and different type
        String name2 = "bar";
        SQLiteColumnDefinition.DataType type2 = SQLiteColumnDefinition.DataType.INT;

        SQLiteColumnDefinition columnDefinition2 = new SQLiteColumnDefinition(name2, type2);

        // Stringified form should be "[name2] [type2]"
        tokens = columnDefinition2.toString().split("\\s", 2);

        Assert.assertEquals("Column name should match new input", name2.toUpperCase(Locale.getDefault()), tokens[0].toUpperCase(Locale.getDefault()));
        Assert.assertEquals("Data type should match new input data type", type2, structuredTypeBySqlType.get(tokens[1].toUpperCase(Locale.getDefault())));

        // Test basic column: name and Text type
        SQLiteColumnDefinition.DataType type3 = SQLiteColumnDefinition.DataType.TEXT;

        SQLiteColumnDefinition columnDefinition3 = new SQLiteColumnDefinition(name1, type3);

        // Stringified form should be "[name1] [type3]"
        tokens = columnDefinition3.toString().split("\\s", 2);

        Assert.assertEquals("Column name should match new input", name1.toUpperCase(Locale.getDefault()), tokens[0].toUpperCase(Locale.getDefault()));
        Assert.assertEquals("Data type should match input data type", type3, structuredTypeBySqlType.get(tokens[1].toUpperCase(Locale.getDefault())));

        // Test primary key modifier
        SQLiteColumnDefinition columnDefinition4 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                null,           // Auto Increment
                null,           // Unique
                null            // Not Null
        );

        Pattern endsWithPrimaryKeyPattern = Pattern.compile("^(?!.*AUTOINCREMENT)(?!.*UNIQUE)(?!.*NOT\\s+NULL).*PRIMARY\\s+KEY\\s*$", Pattern.CASE_INSENSITIVE);

        Assert.assertTrue("Column definition should end with PRIMARY KEY", endsWithPrimaryKeyPattern.matcher(columnDefinition4.toString()).matches());

        // Test primary key with auto increment
        SQLiteColumnDefinition columnDefinition5 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                null,           // Unique
                null            // Not Null
        );

        Pattern endsWithAutoIncrementPrimaryKey = Pattern.compile("^(?!.*UNIQUE)(?!.*NOT\\s+NULL).*PRIMARY\\s+KEY\\s+AUTOINCREMENT\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column definition should end with PRIMARY KEY AUTOINCREMENT", endsWithAutoIncrementPrimaryKey.matcher(columnDefinition5.toString()).matches());

        // Test auto increment without primary key. (Auto increment should be ignored)
        SQLiteColumnDefinition columnDefinition6 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                Boolean.TRUE,   // Auto Increment
                null,           // Unique
                null            // Not Null
        );

        Pattern noModifiers = Pattern.compile("^(?!.*UNIQUE)(?!.*NOT\\s+NULL)(?!.*AUTOINCREMENT)(?!.*PRIMARY\\s+KEY).*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("AUTOINCREMENT should not be valid without PRIMARY KEY", noModifiers.matcher(columnDefinition6.toString()).matches());

        // Test Unique modifier
        SQLiteColumnDefinition columnDefinition7 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                null,           // Auto Increment
                Boolean.TRUE,   // Unique
                null            // Not Null
        );

        Pattern endsWithUnique = Pattern.compile("^(?!.*NOT\\s+NULL)(?!.*PRIMARY\\s+KEY)(?!.*AUTOINCREMENT).*UNIQUE\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column Definition should end with UNIQUE", endsWithUnique.matcher(columnDefinition7.toString()).matches());

        // Test Not Null modifier
        SQLiteColumnDefinition columnDefinition8 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                null,           // Auto Increment
                null,           // Unique
                Boolean.TRUE    // Not Null
        );

        Pattern endsWithNotNull = Pattern.compile("^(?!.*UNIQUE)(?!.*PRIMARY\\sKEY)(?!.*AUTOINCREMENT).*NOT\\s+NULL\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column Definition should end with NOT NULL", endsWithNotNull.matcher(columnDefinition8.toString()).matches());

        // Test combination of UNIQUE and NOT NULL modifiers
        SQLiteColumnDefinition columnDefinition9 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                null,           // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Pattern uniqueAndNotNull = Pattern.compile("^(?!.*PRIMARY\\s+KEY)(?!.*AUTOINCREMENT)(?=.*UNIQUE)(?=.*NOT\\s+NULL).*(UNIQUE|NOT\\s+NULL)\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column definition should include both UNIQUE and NOT NULL (in any order) at the end of the definition", uniqueAndNotNull.matcher(columnDefinition9.toString()).matches());

        // Test combination of UNIQUE, NOT NULL, and AUTOINCREMENT modifiers
        SQLiteColumnDefinition columnDefinition10 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertTrue("Column definition should include both UNIQUE and NOT NULL (in any order), but not AUTOINCREMENT at the end of the definition", uniqueAndNotNull.matcher(columnDefinition10.toString()).matches());

        // Test combination of UNIQUE, NOT NULL, PRIMARY KEY, and AUTOINCREMENT modifiers
        SQLiteColumnDefinition columnDefinition11 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Pattern primaryAutoIncrementUniqueNotNull = Pattern.compile("^(?=.*UNIQUE)(?=.*NOT\\s+NULL)(?=.*PRIMARY\\s+KEY\\s+AUTOINCREMENT).*(UNIQUE|NOT\\s+NULL|PRIMARY\\s+KEY\\s+AUTOINCREMENT)\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column definition should include UNIQUE, NOT NULL, and PRIMARY KEY AUTOINCREMENT (in any order) at the end of the definition", primaryAutoIncrementUniqueNotNull.matcher(columnDefinition11.toString()).matches());

        // Test combination of UNIQUE, NOT NULL, and PRIMARY KEY modifiers
        SQLiteColumnDefinition columnDefinition12 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                null,           // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Pattern primaryUniqueNotNull = Pattern.compile("^(?!.*AUTOINCREMENT)(?=.*UNIQUE)(?=.*NOT\\s+NULL)(?=.*PRIMARY\\s+KEY).*(UNIQUE|NOT\\s+NULL|PRIMARY\\s+KEY)\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column definition should include UNIQUE, NOT NULL, and PRIMARY KEY (in any order) at the end of the definition", primaryUniqueNotNull.matcher(columnDefinition12.toString()).matches());

        // Test combination of UNIQUE and PRIMARY KEY AUTOINCREMENT modifiers
        SQLiteColumnDefinition columnDefinition13 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                null            // Not Null
        );

        Pattern primaryAutoIncrementUnique = Pattern.compile("^(?!.*NOT\\sNULL)(?=.*UNIQUE)(?=.*PRIMARY\\s+KEY\\s+AUTOINCREMENT).*(UNIQUE|PRIMARY\\s+KEY\\s+AUTOINCREMENT)\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column definition should include UNIQUE and PRIMARY KEY AUTOINCREMENT (in any order) at the end of the definition", primaryAutoIncrementUnique.matcher(columnDefinition13.toString()).matches());

        // Test combination of UNIQUE and PRIMARY KEY modifiers
        SQLiteColumnDefinition columnDefinition14 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                null,           // Auto Increment
                Boolean.TRUE,   // Unique
                null            // Not Null
        );

        Pattern primaryUnique = Pattern.compile("^(?!.*AUTOINCREMENT)(?!.*NOT\\s+NULL)(?=.*UNIQUE)(?=.*PRIMARY\\s+KEY).*(UNIQUE|PRIMARY\\s+KEY)\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column definition should include UNIQUE and PRIMARY KEY (in any order) at the end of the definition", primaryUnique.matcher(columnDefinition14.toString()).matches());

        // Test combination of NOT NULL and AUTOINCREMENT modifiers
        SQLiteColumnDefinition columnDefinition15 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                Boolean.TRUE,   // Auto Increment
                null,           // Unique
                Boolean.TRUE    // Not Null
        );

        Pattern notNullAutoIncrement = Pattern.compile("^(?!.*AUTOINCREMENT)(?=.*NOT\\s+NULL)(?!.*UNIQUE)(?!.*PRIMARY\\s+KEY).*NOT\\s+NULL\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column definition should include NOT NULL at the end of the definition but not AUTOINCREMENT", notNullAutoIncrement.matcher(columnDefinition15.toString()).matches());

        // Test combination of NOT NULL and AUTOINCREMENT modifiers
        SQLiteColumnDefinition columnDefinition16 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                null            // Not Null
        );

        Assert.assertTrue("Column definition should include UNIQUE at the end of the definition but not AUTOINCREMENT", endsWithUnique.matcher(columnDefinition16.toString()).matches());

        // Test combination of NOT NULL, PRIMARY KEY, and AUTOINCREMENT modifiers
        SQLiteColumnDefinition columnDefinition17 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                null,           // Unique
                Boolean.TRUE    // Not Null
        );

        Pattern notNullPrimaryAutoIncrement = Pattern.compile("^(?=.*NOT\\s+NULL)(?!.*UNIQUE)(?=.*PRIMARY\\s+KEY\\s+AUTOINCREMENT).*(NOT\\s+NULL|PRIMARY\\s+KEY\\s+AUTOINCREMENT)\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column definition should include NOT NULL and PRIMARY KEY AUTOINCREMENT at the end of the definition", notNullPrimaryAutoIncrement.matcher(columnDefinition17.toString()).matches());

        // Test combination of NOT NULL, PRIMARY KEY, and AUTOINCREMENT modifiers
        SQLiteColumnDefinition columnDefinition18 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                null,           // Auto Increment
                null,           // Unique
                Boolean.TRUE    // Not Null
        );

        Pattern notNullPrimary = Pattern.compile("^(?=.*NOT\\s+NULL)(?!.*UNIQUE)(?=.*PRIMARY\\s+KEY)(?!.*AUTOINCREMENT).*(NOT\\s+NULL|PRIMARY\\s+KEY)\\s*$", Pattern.CASE_INSENSITIVE);
        Assert.assertTrue("Column definition should include NOT NULL and PRIMARY KEY at the end of the definition but not AUTOINCREMENT", notNullPrimary.matcher(columnDefinition18.toString()).matches());
    }

    @Test
    public void constructorTest() throws Exception {
        String name1 = "column1";
        String name2 = "column2";

        SQLiteColumnDefinition.DataType type1 = SQLiteColumnDefinition.DataType.INT;
        SQLiteColumnDefinition.DataType type2 = SQLiteColumnDefinition.DataType.TEXT;

        // Test that short constructor works as expected
        SQLiteColumnDefinition columnDefinition1 = new SQLiteColumnDefinition(
                name1,
                type1
        );

        Assert.assertEquals("Column Definition short constructor should set Name appropriately", name1,                         columnDefinition1.getName());
        Assert.assertEquals("Column Definition short constructor should set type appropriately", type1,                         columnDefinition1.getType());
        Assert.assertFalse("Column Definition short constructor should not default isPrimaryKey to True",                       columnDefinition1.getIsPrimaryKey());
        Assert.assertFalse("Column Definition short constructor should not default doesAutoIncrement to True",                  columnDefinition1.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition short constructor should not default isUnique to True",                           columnDefinition1.getIsUnique());
        Assert.assertFalse("Column Definition short constructor should not default isNotNull to True",                          columnDefinition1.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition1 = null;

        SQLiteColumnDefinition columnDefinition2 = new SQLiteColumnDefinition(
                name2,
                type2
        );

        Assert.assertEquals("Column Definition short constructor should set Name appropriately", name2,                         columnDefinition2.getName());
        Assert.assertEquals("Column Definition short constructor should set type appropriately", type2,                         columnDefinition2.getType());
        Assert.assertFalse("Column Definition short constructor should not default isPrimaryKey to True",                       columnDefinition2.getIsPrimaryKey());
        Assert.assertFalse("Column Definition short constructor should not default doesAutoIncrement to True",                  columnDefinition2.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition short constructor should not default isUnique to True",                           columnDefinition2.getIsUnique());
        Assert.assertFalse("Column Definition short constructor should not default isNotNull to True",                          columnDefinition2.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition2 = null;

        // Test that full constructor works as expected
        SQLiteColumnDefinition columnDefinition3 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,   // Is primary key
                null,   // Auto Increment
                null,   // Unique
                null    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition3.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition3.getType());
        Assert.assertFalse("Column Definition full constructor should not default isPrimaryKey to True",                        columnDefinition3.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not default doesAutoIncrement to True",                   columnDefinition3.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should not default isUnique to True",                            columnDefinition3.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should not default isNotNull to True",                           columnDefinition3.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition3 = null;

        SQLiteColumnDefinition columnDefinition4 = new SQLiteColumnDefinition(
                name2,
                type2,
                null,   // Is primary key
                null,   // Auto Increment
                null,   // Unique
                null    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name2,                          columnDefinition4.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type2,                          columnDefinition4.getType());
        Assert.assertFalse("Column Definition full constructor should not default isPrimaryKey to True",                        columnDefinition4.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not default doesAutoIncrement to True",                   columnDefinition4.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should not default isUnique to True",                            columnDefinition4.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should not default isNotNull to True",                           columnDefinition4.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition4 = null;

        SQLiteColumnDefinition columnDefinition5 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                null,           // Auto Increment
                null,           // Unique
                null            // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition5.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition5.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition5.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not default doesAutoIncrement to True",                   columnDefinition5.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should not default isUnique to True",                            columnDefinition5.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should not default isNotNull to True",                           columnDefinition5.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition5 = null;

        SQLiteColumnDefinition columnDefinition6 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                Boolean.TRUE,   // Auto Increment
                null,           // Unique
                null            // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition6.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition6.getType());
        Assert.assertFalse("Column Definition full constructor should not default isPrimaryKey to True",                        columnDefinition6.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition6.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should not default isUnique to True",                            columnDefinition6.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should not default isNotNull to True",                           columnDefinition6.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition6 = null;

        SQLiteColumnDefinition columnDefinition7 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                null,           // Unique
                null            // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition7.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition7.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition7.getIsPrimaryKey());
        Assert.assertTrue("Column Definition full constructor should set doesAutoIncrement appropriately",                      columnDefinition7.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should not default isUnique to True",                            columnDefinition7.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should not default isNotNull to True",                           columnDefinition7.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition7 = null;

        SQLiteColumnDefinition columnDefinition8 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                null,           // Auto Increment
                Boolean.TRUE,   // Unique
                null            // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition8.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition8.getType());
        Assert.assertFalse("Column Definition full constructor should not default isPrimaryKey to True",                        columnDefinition8.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition8.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition8.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should not default isNotNull to True",                           columnDefinition8.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition8 = null;

        SQLiteColumnDefinition columnDefinition9 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                null,           // Auto Increment
                Boolean.TRUE,   // Unique
                null            // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition9.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition9.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition9.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition9.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition9.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should not default isNotNull to True",                           columnDefinition9.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition9 = null;

        SQLiteColumnDefinition columnDefinition10 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                null            // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition10.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition10.getType());
        Assert.assertFalse("Column Definition full constructor should not default isPrimaryKey to True",                        columnDefinition10.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition10.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition10.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should not default isNotNull to True",                           columnDefinition10.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition10 = null;

        SQLiteColumnDefinition columnDefinition11 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                null            // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition11.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition11.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition11.getIsPrimaryKey());
        Assert.assertTrue("Column Definition full constructor should set doesAutoIncrement appropriately",                      columnDefinition11.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition11.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should not default isNotNull to True",                           columnDefinition11.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition11 = null;

        SQLiteColumnDefinition columnDefinition12 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                null,           // Auto Increment
                null,           // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition12.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition12.getType());
        Assert.assertFalse("Column Definition full constructor should not default isPrimaryKey to True",                        columnDefinition12.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not default doesAutoIncrement to True",                   columnDefinition12.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should not default isUnique to True",                            columnDefinition12.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition12.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition12 = null;

        SQLiteColumnDefinition columnDefinition13 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                null,           // Auto Increment
                null,           // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition13.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition13.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition13.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not default doesAutoIncrement to True",                   columnDefinition13.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should not default isUnique to True",                            columnDefinition13.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition13.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition13 = null;

        SQLiteColumnDefinition columnDefinition14 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                Boolean.TRUE,   // Auto Increment
                null,           // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition14.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition14.getType());
        Assert.assertFalse("Column Definition full constructor should not default isPrimaryKey to True",                        columnDefinition14.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition14.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should not default isUnique to True",                            columnDefinition14.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition14.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition14 = null;

        SQLiteColumnDefinition columnDefinition15 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                null,           // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition15.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition15.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition15.getIsPrimaryKey());
        Assert.assertTrue("Column Definition full constructor should set doesAutoIncrement appropriately",                      columnDefinition15.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should not default isUnique to True",                            columnDefinition15.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition15.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition15 = null;

        SQLiteColumnDefinition columnDefinition16 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                null,           // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition16.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition16.getType());
        Assert.assertFalse("Column Definition full constructor should not default isPrimaryKey to True",                        columnDefinition16.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not default doesAutoIncrement to True",                   columnDefinition16.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition16.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition16.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition16 = null;

        SQLiteColumnDefinition columnDefinition17 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                null,           // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition17.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition17.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition17.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not default doesAutoIncrement to True",                   columnDefinition17.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition17.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition17.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition17 = null;

        SQLiteColumnDefinition columnDefinition18 = new SQLiteColumnDefinition(
                name1,
                type1,
                null,           // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition18.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition18.getType());
        Assert.assertFalse("Column Definition full constructor should not default isPrimaryKey to True",                        columnDefinition18.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition18.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition18.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition18.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition18 = null;

        SQLiteColumnDefinition columnDefinition19 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition19.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition19.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition19.getIsPrimaryKey());
        Assert.assertTrue("Column Definition full constructor should set doesAutoIncrement appropriately",                      columnDefinition19.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition19.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition19.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition19 = null;

        // Test that full constructor works as expected with False instead of nulls
        SQLiteColumnDefinition columnDefinition20 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.FALSE,   // Is primary key
                Boolean.FALSE,   // Auto Increment
                Boolean.FALSE,   // Unique
                Boolean.FALSE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition20.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition20.getType());
        Assert.assertFalse("Column Definition full constructor should set isPrimaryKey appropriately",                          columnDefinition20.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should set doesAutoIncrement appropriately",                     columnDefinition20.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should set isUnique appropriately",                              columnDefinition20.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should set isNotNull appropriately",                             columnDefinition20.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition20 = null;

        SQLiteColumnDefinition columnDefinition21 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.FALSE,  // Auto Increment
                Boolean.FALSE,  // Unique
                Boolean.FALSE   // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition21.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition21.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition21.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should set doesAutoIncrement appropriately",                     columnDefinition21.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should set isUnique appropriately",                              columnDefinition21.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should set isNotNull appropriately",                             columnDefinition21.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition21 = null;

        SQLiteColumnDefinition columnDefinition22 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.FALSE,  // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.FALSE,  // Unique
                Boolean.FALSE   // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition22.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition22.getType());
        Assert.assertFalse("Column Definition full constructor should set isPrimaryKey appropriately",                          columnDefinition22.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition22.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should set isUnique appropriately",                              columnDefinition22.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should set isNotNull appropriately",                             columnDefinition22.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition22 = null;

        SQLiteColumnDefinition columnDefinition23 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.FALSE,  // Unique
                Boolean.FALSE   // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition23.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition23.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition23.getIsPrimaryKey());
        Assert.assertTrue("Column Definition full constructor should set doesAutoIncrement appropriately",                      columnDefinition23.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should set isUnique appropriately",                              columnDefinition23.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should set isNotNull appropriately",                             columnDefinition23.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition23 = null;

        SQLiteColumnDefinition columnDefinition24 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.FALSE,  // Is primary key
                Boolean.FALSE,  // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.FALSE   // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition24.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition24.getType());
        Assert.assertFalse("Column Definition full constructor should set isPrimaryKey appropriately",                          columnDefinition24.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition24.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition24.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should set isNotNull appropriately",                             columnDefinition24.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition24 = null;

        SQLiteColumnDefinition columnDefinition25 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.FALSE,  // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.FALSE   // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition25.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition25.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition25.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition25.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition25.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should set isNotNull appropriately",                             columnDefinition25.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition25 = null;

        SQLiteColumnDefinition columnDefinition26 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.FALSE,  // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.FALSE   // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition26.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition26.getType());
        Assert.assertFalse("Column Definition full constructor should set isPrimaryKey appropriately",                          columnDefinition26.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition26.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition26.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should set isNotNull appropriately",                             columnDefinition26.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition26 = null;

        SQLiteColumnDefinition columnDefinition27 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.FALSE   // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition27.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition27.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition27.getIsPrimaryKey());
        Assert.assertTrue("Column Definition full constructor should set doesAutoIncrement appropriately",                      columnDefinition27.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition27.getIsUnique());
        Assert.assertFalse("Column Definition full constructor should set isNotNull appropriately",                             columnDefinition27.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition27 = null;

        SQLiteColumnDefinition columnDefinition28 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.FALSE,  // Is primary key
                Boolean.FALSE,  // Auto Increment
                Boolean.FALSE,  // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition28.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition28.getType());
        Assert.assertFalse("Column Definition full constructor should set isPrimaryKey appropriately",                          columnDefinition28.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should set doesAutoIncrement appropriately",                     columnDefinition28.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should set isUnique appropriately",                              columnDefinition28.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition28.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition28 = null;

        SQLiteColumnDefinition columnDefinition29 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.FALSE,  // Auto Increment
                Boolean.FALSE,  // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition29.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition29.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition29.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should set doesAutoIncrement appropriately",                     columnDefinition29.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should set isUnique appropriately",                              columnDefinition29.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition29.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition29 = null;

        SQLiteColumnDefinition columnDefinition30 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.FALSE,  // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.FALSE,  // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition30.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition30.getType());
        Assert.assertFalse("Column Definition full constructor should set isPrimaryKey appropriately",                          columnDefinition30.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition30.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should set isUnique appropriately",                              columnDefinition30.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition30.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition30 = null;

        SQLiteColumnDefinition columnDefinition31 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.FALSE,  // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition31.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition31.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition31.getIsPrimaryKey());
        Assert.assertTrue("Column Definition full constructor should set doesAutoIncrement appropriately",                      columnDefinition31.getDoesAutoIncrement());
        Assert.assertFalse("Column Definition full constructor should set isUnique appropriately",                              columnDefinition31.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition31.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition31 = null;

        SQLiteColumnDefinition columnDefinition32 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.FALSE,  // Is primary key
                Boolean.FALSE,  // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition32.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition32.getType());
        Assert.assertFalse("Column Definition full constructor should set isPrimaryKey appropriately",                          columnDefinition32.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should set doesAutoIncrement appropriately",                     columnDefinition32.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition32.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition32.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition32 = null;

        SQLiteColumnDefinition columnDefinition33 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.TRUE,   // Is primary key
                Boolean.FALSE,  // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition33.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition33.getType());
        Assert.assertTrue("Column Definition full constructor should set isPrimaryKey appropriately",                           columnDefinition33.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should set doesAutoIncrement appropriately",                     columnDefinition33.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition33.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition33.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition33 = null;

        SQLiteColumnDefinition columnDefinition34 = new SQLiteColumnDefinition(
                name1,
                type1,
                Boolean.FALSE,  // Is primary key
                Boolean.TRUE,   // Auto Increment
                Boolean.TRUE,   // Unique
                Boolean.TRUE    // Not Null
        );

        Assert.assertEquals("Column Definition full constructor should set Name appropriately", name1,                          columnDefinition34.getName());
        Assert.assertEquals("Column Definition full constructor should set type appropriately", type1,                          columnDefinition34.getType());
        Assert.assertFalse("Column Definition full constructor should set isPrimaryKey appropriately",                          columnDefinition34.getIsPrimaryKey());
        Assert.assertFalse("Column Definition full constructor should not set doesAutoIncrement when isPrimaryKey is false",    columnDefinition34.getDoesAutoIncrement());
        Assert.assertTrue("Column Definition full constructor should set isUnique appropriately",                               columnDefinition34.getIsUnique());
        Assert.assertTrue("Column Definition full constructor should set isNotNull appropriately",                              columnDefinition34.getIsNotNull());

        // Unset this column definition so we don't accidentally use it in future tests
        columnDefinition34 = null;
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