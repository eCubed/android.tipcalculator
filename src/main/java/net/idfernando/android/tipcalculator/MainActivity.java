package net.idfernando.android.tipcalculator;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.idfernando.android.uitools.CustomArrayAdapterBase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddTipEntryDialogFragment.AddTipEntryDialogListener {

    private List<TipEntry> tipEntries;
    private ListView tipEntries_lv;

    private EditText initialCharge_tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialCharge_tb = (EditText)findViewById(R.id.initial_charge_tb);

        double[] percentages;
        tipEntries = new ArrayList<>();

        if (savedInstanceState == null){
            percentages = new double[]{ 15, 20};
        }
        else{
            String percentagesAsString = savedInstanceState.getString("PERCENTAGES");
            String[] percentagesStringArray = percentagesAsString.split(",");

            percentages = new double[percentagesStringArray.length];

            for(int i = 0; i < percentages.length; i++){
                percentages[i] = Double.parseDouble(percentagesStringArray[i]);
            }
        }

        double initialCharge = Double.parseDouble(initialCharge_tb.getText().toString());

        for(int j = 0; j < percentages.length; j++){
            addTipEntry(percentages[j] / 100, initialCharge);
        }

        initialCharge_tb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(MainActivity.this, editable.toString(), Toast.LENGTH_SHORT).show();
                try{
                    updateInitialCharge(Double.parseDouble(editable.toString()));
                }catch(Exception e){
                    updateInitialCharge(0);
                }
            }
        });

        tipEntries_lv = (ListView)findViewById(R.id.tipentries_lv);
        TipEntryArrayAdapter adapter = new TipEntryArrayAdapter(this, R.layout.itemrenderer_tipentry, tipEntries);
        tipEntries_lv.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void addTipEntry(double percentage, double initialChargeAmount){
        tipEntries.add(new TipEntry(percentage, initialChargeAmount));
    }

    private void updateInitialCharge(double currentInitialCharge){
        for(TipEntry tipEntry: tipEntries){
            tipEntry.setInitialChargeAmount(currentInitialCharge);
        }

        tipEntries_lv.invalidateViews();
    }

    @Override
    public void onPercentageAdded(double newPercentage) {
        // That percentage will be 0-100, so we will have to divide by 100.
        double initialCharge = 0;

        try{
            initialCharge = Double.parseDouble(initialCharge_tb.getText().toString());
        }catch(Exception e){
        }

        tipEntries.add(new TipEntry(newPercentage / 100,  initialCharge));

        tipEntries_lv.invalidateViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addpercentage) {

            AddTipEntryDialogFragment dialog = new AddTipEntryDialogFragment();
            dialog.show(getSupportFragmentManager(), "DIALOG_FRAGMENT");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        String concattedPercentages = "";
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < tipEntries.size(); i++){
            sb.append(Double.toString(tipEntries.get(i).getPercentage()));

            if (i < tipEntries.size() - 1)
                sb.append(",");
        }

        String toSave = sb.toString();

        outState.putString("PERCENTAGES", toSave);

        super.onSaveInstanceState(outState);
    }

    private void removePercentage(double percentage){
        TipEntry tipEntry = null;
        for(int i = 0; i < tipEntries.size(); i++){
            TipEntry currentTipEntry = tipEntries.get(i);
            if (currentTipEntry.getPercentage() == percentage){
                tipEntries.remove(currentTipEntry);
                break;
            }
        }

        tipEntries_lv.invalidateViews();
    }

    public class TipEntryArrayAdapter extends CustomArrayAdapterBase<TipEntry>{

        public TipEntryArrayAdapter(Context context, int resource, List<TipEntry> objects) {
            super(context, resource, objects);
        }

        @Override
        protected void loadItemRenderer(final TipEntry item, View itemRenderer) {
            TextView percent = (TextView)itemRenderer.findViewById(R.id.percentage_tv);
            percent.setText(String.format("%.2f",item.getPercentage()*100) + "%");

            TextView tipAmount = (TextView)itemRenderer.findViewById(R.id.tip_amount_tv);
            tipAmount.setText("$" + String.format("%.2f",item.calculateTipAmount()));

            TextView totalCharge = (TextView)itemRenderer.findViewById(R.id.total_charge_tv);
            totalCharge.setText("$" + String.format("%.2f",item.calculateTotalAmount()));

            Button delete_bt= (Button)itemRenderer.findViewById(R.id.delete_percentage_bt);
            delete_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(MainActivity.this, "About to delete " + item.getPercentage(), Toast.LENGTH_SHORT).show();
                    MainActivity.this.removePercentage(item.getPercentage());
                }
            });

        }
    }
}
