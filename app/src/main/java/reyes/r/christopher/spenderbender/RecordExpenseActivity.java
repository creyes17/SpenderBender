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

package reyes.r.christopher.spenderbender;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import reyes.r.christopher.spenderbender.databinding.RecordExpenseActivityBinding;
import reyes.r.christopher.spenderbender.persistence.LocalDatabaseHandler;
import reyes.r.christopher.spenderbender.viewmodel.TransactionViewModel;

public class RecordExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalDatabaseHandler dbh = new LocalDatabaseHandler(this);
        TransactionViewModel viewModel = new TransactionViewModel(dbh);

        final RecordExpenseActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.record_expense_activity);
        binding.setViewModel(viewModel);

        Button submit = (Button) findViewById(R.id.submit);
        assert submit != null;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 8/21/16 Implement onClick as SaveExpense, then make toast
                binding.getViewModel().setAmount(binding.getViewModel().getAmount() + 0.01);
                binding.getViewModel().setName(binding.getViewModel().getName() + "+");
                binding.getViewModel().setYearIncurred(binding.getViewModel().getYearIncurred() + 1);
                binding.getViewModel().setMonthIncurred((binding.getViewModel().getMonthIncurred() + 1) % 12);
                binding.getViewModel().setDayIncurred((binding.getViewModel().getDayIncurred() + 1) % 29);
            }
        });
    }
}
