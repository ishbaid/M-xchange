package com.baid.mxchange.m_xchange;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    Switch postType;
    EditText title, price, edition, description;
    Spinner condition;
    Button submit;

    TextView descLabel, conditonLabel;

    double priceVal = -1;
    String nameVal = null;
    String editionVal = null;

    final static String TAG = "NewPostFragment";

    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_post, container, false);

        //by default user is trying to sell a textbook
        MainActivity.buy = false;


        descLabel = (TextView) rootView.findViewById(R.id.desc_label);
        conditonLabel = (TextView) rootView.findViewById(R.id.condition_label);

        postType = (Switch) rootView.findViewById(R.id.post_type_switch);
        postType.setOnCheckedChangeListener(this);

        title = (EditText) rootView.findViewById(R.id.textbook_name);
        price = (EditText) rootView.findViewById(R.id.textbook_price);
        edition = (EditText) rootView.findViewById(R.id.textbook_edition);
        description = (EditText) rootView.findViewById(R.id.textbook_description);

        submit = (Button) rootView.findViewById(R.id.submit_button);
        submit.setOnClickListener(this);

        String[]values = {"New", "Fair", "Used"};

        condition = (Spinner) rootView.findViewById(R.id.condition_spinner);
        condition.setOnItemSelectedListener(this);

        //spinner will allow user to select the condition of textbook
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, values);
        condition.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == submit.getId()){

            Log.d("NewPostFragment", "Creating a post");
            //hide keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            //must enter price
            if(!price.getText().toString().equals("")){

                priceVal = Double.parseDouble(price.getText().toString());
            }
            else{

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alert ");
                alertDialog.setMessage("Must enter price.");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }

            //edition is optional
            if(!edition.getText().toString().equals("")){

                editionVal = edition.getText().toString();
            }


            //textbook title is required
            if(!title.getText().toString().equals("")){

                nameVal = title.getText().toString();
            }
            else{

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alert ");
                alertDialog.setMessage("Must enter textbook title.");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }

            if(priceVal >= 0 && nameVal != null){

                Log.d("Baid", "Ready to submit textbook");



                String conditionText;
                //selling
                if(!postType.isChecked())
                    conditionText = condition.getSelectedItem().toString();
                else
                    conditionText = "Unknown";


                String descriptionText = description.getText().toString();
                String editionText = edition.getText().toString();

                MainActivity.searchResults = new SearchTextbooks(MainActivity.searchInterface);

                //TODO: showNumber argument is undefined currently
                MainActivity.searchResults.createTextbookEntry(conditionText, descriptionText, editionText, priceVal, nameVal, false);

            }

        }
    }

    //reset values
    @Override
    public void onResume() {
        super.onResume();
        priceVal = -1;
        editionVal = null;
        nameVal = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int id = buttonView.getId();
        if(id == postType.getId()){

            Log.d(TAG, "Switching post type");

            //switched to sell
            if(!isChecked){

                MainActivity.buy = false;
                condition.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                conditonLabel.setVisibility(View.VISIBLE);
                descLabel.setVisibility(View.VISIBLE);

            }
            //switched to buy
            else{

                MainActivity.buy = true;
                condition.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
                conditonLabel.setVisibility(View.INVISIBLE);
                descLabel.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
