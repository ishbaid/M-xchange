package com.baid.mxchange.m_xchange;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Ish on 9/6/14.
 */
public class Fragment1 extends Fragment implements View.OnClickListener{

    Button buy, sell;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_choice, container, false);

        buy = (Button) rootView.findViewById(R.id.textbook);
        sell = (Button) rootView.findViewById(R.id.sell);

        buy.setOnClickListener(this);
        sell.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {


            //buy
            if(view.getId() == buy.getId()){

                Log.d("Baid", "Buyer");
                MainActivity.buy = new Boolean(true);

            }
            //sell
            else if (view.getId() == sell.getId()){

                Log.d("Baid", "Seller");
                MainActivity.buy = new Boolean(false);

            }

            //start fragment2 regardless of what button was pressed
            Fragment2 newFragment = new Fragment2();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, newFragment);
            transaction.addToBackStack(null);
// Commit the transaction
            transaction.commit();



    }
}
