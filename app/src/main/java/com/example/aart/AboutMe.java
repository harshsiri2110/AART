package com.example.aart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutMe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutMe extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String description, foster_name, foster_number;

    TextView descriptionText, fosterNameText, fosterNumberText;

    public AboutMe() {
        // Required empty public constructor
    }

    public AboutMe(String description,String foster_name,String foster_number)
    {
        this.description = description;
        this.foster_name = foster_name;
        this.foster_number = foster_number;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutMe.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutMe newInstance(String param1, String param2) {
        AboutMe fragment = new AboutMe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_about_me, container, false);

        descriptionText = root.findViewById(R.id.about_me_text);
        fosterNameText = root.findViewById(R.id.fosterName);
        fosterNumberText = root.findViewById(R.id.fosterNumber);

        descriptionText.setText(description);
        fosterNameText.setText(foster_name);
        fosterNumberText.setText(foster_number);

        return root;
    }
}