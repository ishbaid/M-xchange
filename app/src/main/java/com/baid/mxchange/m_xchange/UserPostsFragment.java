package com.baid.mxchange.m_xchange;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class UserPostsFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView myPosts;
    List<ParseObject> allPosts;
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_posts, container, false);

        myPosts = (ListView) rootView.findViewById(R.id.post_list);
        myPosts.setOnItemClickListener(this);

        //start
        ProgressDialog loadingDialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true);

        //search
        searchMyPosts();

        //finished
        loadingDialog.dismiss();
        return rootView;
    }

    private void searchMyPosts(){

        ParseQuery<ParseObject> query;
        if(MainActivity.book)
            query = new ParseQuery("Textbook");
        else
            query = new ParseQuery("Ticket");

        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if(e != null){

                    Log.d("Baid", e.getMessage());
                    return;
                }

                allPosts = parseObjects;

                if(parseObjects.size() == 0){

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alert ");
                    alertDialog.setMessage("You have no posts at this time!");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                    return;
                }

                ArrayList<String> postLabels = new ArrayList<String>();

                for(int i = 0; i < parseObjects.size(); i ++){

                    ParseObject post = parseObjects.get(i);
                    if(MainActivity.book){

                        String title = post.getString("title");
                        boolean selling = post.getBoolean("selling");
                        if(selling)
                            title += " (Selling)";
                        else
                            title += " (Buying)";
                        postLabels.add(title);
                    }
                    else{

                        ParseObject game = post.getParseObject("game");

                        try {

                            if(game != null){

                                game.fetchIfNeeded();
                                String name = game.getString("opponent");
                                boolean selling = post.getBoolean("selling");
                                if(selling)
                                    name += " (Selling)";
                                else
                                    name += " (Buying)";
                                postLabels.add(name);
                            }
                        }catch (Exception e2){

                            Log.d("Baid", e2.getMessage());
                        }
                    }

                }

                adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, postLabels);
                myPosts.setAdapter(adapter);

            }
        });

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {



        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        ParseObject toRemove = allPosts.get(position);
                        toRemove.deleteInBackground();

                        adapter.remove(adapter.getItem(position));
                        adapter.notifyDataSetChanged();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this post?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


}
