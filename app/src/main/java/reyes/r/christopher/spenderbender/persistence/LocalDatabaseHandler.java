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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Christopher R Reyes on 8/11/16.
 *
 * Manages the local database(s) on the device
 */

public class LocalDatabaseHandler extends SQLiteOpenHelper {

    private static final String DatabaseName = "SpenderBenderSQLiteDB";
    private static final int DatabaseVersion = 1;

    public LocalDatabaseHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DatabaseName, factory, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatement =  TransactionContract.schema.toString();

        db.execSQL(createStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database Version 1. Nothing to do on update because there is no prior version yet.
    }
}
