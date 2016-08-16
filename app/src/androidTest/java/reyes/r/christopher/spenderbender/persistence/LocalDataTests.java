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
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Christopher R Reyes on 8/13/16.
 */
@RunWith(AndroidJUnit4.class)
public class LocalDataTests {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("reyes.r.christopher.spenderbender", appContext.getPackageName());
    }

    @Test
    public void databaseCreationTests() throws Exception {

        // XXX Placeholder
        Assert.assertTrue(Boolean.TRUE);

        // As part of the setup, backup the existing database (if it exists), and then delete it

        // After creating the database, a new database file should exist with the right file name

        // The created database should have a transaction table

        // Test idempotency of onCreate()
    }

}