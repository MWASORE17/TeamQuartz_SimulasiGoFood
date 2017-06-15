package quartz.gofoodsimulation.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBarFragment extends Fragment {

    public TextView etSearch;
    public static final String SEARCH_KEY = "searchValue";
    public static final String SEARCH_CATEGORY = "category";
    public static String searchCategory;

    public SearchBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchbar_layout, container, false);
        etSearch = (EditText) view.findViewById(R.id.main_et_searchbar);

        event();

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    public void event() {
        if (etSearch.getText().toString().matches("")) {
            etSearch.setHint("What would you like to eat?");
        }
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etSearch.isFocused()) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    SellerListFragment sellerListFragment = new SellerListFragment();
                    Bundle b = new Bundle();
                    b.putString(SEARCH_KEY, s.toString());
                    b.putString(SEARCH_CATEGORY, searchCategory);
                    sellerListFragment.setArguments(b);
                    ((MainActivity) getActivity()).changeFragment(R.id.home, sellerListFragment);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etSearch.isFocused()) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    SellerListFragment sellerListFragment = new SellerListFragment();
                    Bundle b = new Bundle();
                    b.putString(SEARCH_KEY, s.toString());
                    b.putString(SEARCH_CATEGORY, searchCategory);
                    sellerListFragment.setArguments(b);
                    ((MainActivity) getActivity()).changeFragment(R.id.home, sellerListFragment);
                }
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etSearch.isFocused() && HomeFragment.LISTTYPE > 4 && etSearch.getText().toString().matches("")) {
                    SellerListFragment sellerListFragment = new SellerListFragment();
                    Bundle b = new Bundle();
                    b.putString(SEARCH_KEY, "");
                    sellerListFragment.setArguments(b);
                    ((MainActivity) getActivity()).changeFragment(R.id.home, sellerListFragment);
                }
            }
        });
    }

}
