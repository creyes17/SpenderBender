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

import java.util.ArrayList;

/**
 * Created by Christopher R Reyes on 8/13/16.
 *
 * Holds the schema for the Transaction Table.
 */

class TransactionContract {
    static final String TableName = "financialtransaction";

    static final SQLiteColumnDefinition PrimaryKey = SQLiteColumnDefinition.StandardPrimaryKey();
    static final SQLiteColumnDefinition TransactionName = new SQLiteColumnDefinition(
            "name",                                 // Column Name
            SQLiteColumnDefinition.DataType.TEXT,   // Type
            Boolean.FALSE,                          // isPrimaryKey
            Boolean.FALSE,                          // doesAutoIncrement
            Boolean.FALSE,                          // isUnique
            Boolean.TRUE                            // isNotNull
    );
    static final SQLiteColumnDefinition Amount = new SQLiteColumnDefinition(
            "amount",                               // Column Name
            SQLiteColumnDefinition.DataType.REAL,   // Type
            Boolean.FALSE,                          // isPrimaryKey
            Boolean.FALSE,                          // doesAutoIncrement
            Boolean.FALSE,                          // isUnique
            Boolean.TRUE                            // isNotNull
    );
    static final SQLiteColumnDefinition DateIncurred = new SQLiteColumnDefinition(
            "incurreddate",                               // Column Name
            SQLiteColumnDefinition.DataType.TEXT,   // Type
            Boolean.FALSE,                          // isPrimaryKey
            Boolean.FALSE,                          // doesAutoIncrement
            Boolean.FALSE,                          // isUnique
            Boolean.TRUE                            // isNotNull
    );

    static ArrayList<SQLiteColumnDefinition> AllColumns;
    static {
        AllColumns = new ArrayList<>(4);
        AllColumns.add(PrimaryKey);
        AllColumns.add(TransactionName);
        AllColumns.add(Amount);
        AllColumns.add(DateIncurred);
    }

    static final SQLiteTableSchema schema = new SQLiteTableSchema( TableName, AllColumns );

}
