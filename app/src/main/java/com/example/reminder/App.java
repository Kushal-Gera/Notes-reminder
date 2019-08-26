package com.example.reminder;

import androidx.fragment.app.Fragment;

public class App extends Fragment {

    public App() {
    }

    //only one method is added !!
    public void finish(Fragment fragment) {
        try {
            fragment.getActivity().finish();
        }catch (NullPointerException e){
            e.getStackTrace();
        }

    }

}
