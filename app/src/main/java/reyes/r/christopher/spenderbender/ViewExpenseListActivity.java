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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import java.util.List;

import reyes.r.christopher.spenderbender.databinding.ActivityViewExpenseListBinding;
import reyes.r.christopher.spenderbender.databinding.SingleExpenseItemBinding;
import reyes.r.christopher.spenderbender.model.ExpenseModel;
import reyes.r.christopher.spenderbender.persistence.LocalDatabaseHandler;
import reyes.r.christopher.spenderbender.viewmodel.TransactionViewModel;

public class ViewExpenseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense_list);

        LocalDatabaseHandler dbh = new LocalDatabaseHandler(this);
        TransactionViewModel viewModel = new TransactionViewModel(dbh);

        viewModel.loadAllExpenses();
        this.updateExpenseList(viewModel.getExpenseModelList());
    }

    private void updateExpenseList(List<ExpenseModel> expenses) {
        TableLayout table = (TableLayout) findViewById(R.id.viewExpenseListTable);
        assert table != null;

        table.removeAllViews();

        for (ExpenseModel expense :
                expenses) {
            SingleExpenseItemBinding binding = SingleExpenseItemBinding.inflate(getLayoutInflater());
            binding.setExpense(expense);

            table.addView(binding.getRoot());
        }
    }
}
