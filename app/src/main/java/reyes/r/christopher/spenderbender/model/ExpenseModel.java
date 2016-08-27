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
    public static final long UNSAVED_EXPENSE = -1;
    private final double amount;
    private final String name;
    private long id;
    private final int yearIncurred;
    private final int monthIncurred;
    private final int dayIncurred;
    private final int yearCreated;
    private final int monthCreated;
    private final int dayCreated;

    /**
     * Creates an ExpenseModel for a new expense, setting the created timestamp to the current timestamp
     * @param name          A Description of the expense
     * @param amount        The amount of the expense
     * @param yearIncurred  The year in which the expense was incurred. Must not be before 1000 AD
     * @param monthIncurred The month in which the expense was incurred. See the Calendar class
     * @param dayIncurred   The day of the month in which the expense was incurred
     */
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

        this.id = UNSAVED_EXPENSE;
    }

    /**
     * Creates an ExpenseModel for an existing expense
     * @param name          A Description of the expense
     * @param amount        The amount of the expense
     * @param yearIncurred  The year in which the expense was incurred
     * @param monthIncurred The month in which the expense was incurred
     * @param dayIncurred   The day of the month in which the expense was incurred
     * @param yearCreated   The year in which the expense was first created in the system
     * @param monthCreated  The month in which the expense was first created in the system. See Calendar class
     * @param dayCreated    The day of the month in which the expense was first created in the system
     * @param id            The primary key which allows the expense to be uniquely identified
     */
    public ExpenseModel(String name, double amount, int yearIncurred, int monthIncurred, int dayIncurred,
                        int yearCreated, int monthCreated, int dayCreated, long id) {
        this.name = name;
        this.amount = amount;
        this.yearIncurred = yearIncurred;
        this.monthIncurred = monthIncurred;
        this.dayIncurred = dayIncurred;
        this.yearCreated = yearCreated;
        this.monthCreated = monthCreated;
        this.dayCreated = dayCreated;
        this.id = id;
    }

    public double getAmount() {
        return this.amount;
    }

    /**
     * @return the amount of this expense, expressed as a string
     */
    public String getAmountAsString() {
        return Double.toString(this.amount);
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

    public long getId() {
        return this.id;
    }

    /**
     * This should only be called in the process of saving an expense to the database
     * @param id    The new value of the primary key which uniquely identifies this expense
     */
    public void setId(long id) {
        // TODO: 8/25/16 Figure out a way to enforce that this is only called when it's saved to the database
        this.id = id;
    }
}
