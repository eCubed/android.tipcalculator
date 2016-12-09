package net.idfernando.android.tipcalculator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Iz on 12/9/2016.
 */

public class AddTipEntryDialogFragment extends DialogFragment {

    private AddTipEntryDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (AddTipEntryDialogListener)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_addtipentry, container, false);

        final EditText newPercentage_tb = (EditText)rootView.findViewById(R.id.add_new_percentage_tb);

        Button ok_bt = (Button)rootView.findViewById(R.id.add_tipentry_bt);
        ok_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    mListener.onPercentageAdded(Double.parseDouble(newPercentage_tb.getText().toString()));
                }
                catch(Exception e){
                    // Do nothing right now.
                    int dummy = 3;
                }

                dismiss();
            }
        });

        Button cancel_bt = (Button)rootView.findViewById(R.id.cancel_tipentry_bt);
        cancel_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

        return rootView;
    }

    public interface AddTipEntryDialogListener{
        void onPercentageAdded(double newPercentage);
    }
}
