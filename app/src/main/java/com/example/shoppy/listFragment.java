package com.example.shoppy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class listFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public RecyclerView.Adapter mAdapter;
    public ArrayList<String> listData;

    public listFragment() {
        // Required empty public constructor
    }


    public static listFragment newInstance() {
        listFragment fragment = new listFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void AddNewListItem(String newItemName) {
        // TODO: Start AsyncTask to insert new item, then update the view.
        AsyncAddNewShoppingListItem asyncAddNewShoppingListItem = new AsyncAddNewShoppingListItem(this, newItemName);
        asyncAddNewShoppingListItem.execute();
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnAddToList) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.title_add_new_shopping_item);

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddNewListItem(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }

            if (v.getId() == R.id.btnSearchFromList)
            {
                Intent intent = new Intent(getContext(), MultipleSearchResultsActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = rootView.findViewById(R.id.shoppingList);

        Button btnSearchFromList = rootView.findViewById(R.id.btnSearchFromList);
        btnSearchFromList.setOnClickListener(mOnClickListener);

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.btnAddToList);
        floatingActionButton.setOnClickListener(mOnClickListener);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // load "listData" from file? or cloud?
        listData = new ArrayList<>();

        // start async task
        AsyncGetShoppingListForListFragment asyncGetShoppingList = new AsyncGetShoppingListForListFragment(this);
        asyncGetShoppingList.execute();

        SetupAdapter();

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return rootView;
    }

    public void SetupAdapter()
    {
        mAdapter = new ShoppyListAdapter(listData.toArray(new String[0]), getContext(), this);
        recyclerView.setAdapter(mAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
