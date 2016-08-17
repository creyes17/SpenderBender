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

package reyes.r.christopher.spenderbender.model;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Christopher R Reyes on 8/17/16.
 *
 * Manages data associated with an expense transaction
 */

public class ExpenseModel {
    private final double amount;
    private final String name;
    private final int yearIncurred;
    private final int monthIncurred;
    private final int dayIncurred;
    private final int yearCreated;
    private final int monthCreated;
    private final int dayCreated;

    public ExpenseModel(String name, double amount, int yearIncurred, int monthIncurred, int dayIncurred) {
        this.name = name;
        this.amount = amount;
        this.yearIncurred = yearIncurred;
        this.monthIncurred = monthIncurred;
        this.dayIncurred = dayIncurred;

        Calendar today = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        this.yearCreated = today.get(Calendar.YEAR);
        this.monthCreated = today.get(Calendar.MONTH);
        this.dayCreated = today.get(Calendar.DAY_OF_MONTH);
    }

    public double getAmount() {
        return this.amount;
    }

    public String getName() {
        return this.name;
    }

    public int getYearIncurred() {
        return this.yearIncurred;
    }

    public int getMonthIncurred() {
        return this.monthIncurred;
    }

    public int getDayIncurred() {
        return this.dayIncurred;
    }

    public int getYearCreated() {
        return this.yearCreated;
    }

    public int getMonthCreated() {
        return this.monthCreated;
    }

    public int getDayCreated() {
        return this.dayCreated;
    }
}
