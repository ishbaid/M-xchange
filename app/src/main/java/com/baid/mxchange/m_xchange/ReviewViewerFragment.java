package com.baid.mxchange.m_xchange;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewViewerFragment extends Fragment {


    private ViewPager pager = null;
    private MainPagerAdapter pagerAdapter = null;

    public ReviewViewerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review_viewer, container, false);

        pagerAdapter = new MainPagerAdapter();
        pager = (ViewPager) rootView.findViewById(R.id.view_pager);
        pager.setAdapter (pagerAdapter);

        LayoutInflater ifl = getActivity().getLayoutInflater();
        LinearLayout v0 = (LinearLayout) ifl.inflate (R.layout.fragment_tb, null);
        pagerAdapter.addView (v0, 0);
        RelativeLayout v1 = (RelativeLayout) ifl.inflate (R.layout.activity_sell_, null);
        pagerAdapter.addView(v1);
        pagerAdapter.notifyDataSetChanged();
        return rootView;
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to add a view to the ViewPager.
    public void addView (View newPage)
    {
        int pageIndex = pagerAdapter.addView (newPage);
        // You might want to make "newPage" the currently displayed page:
        pager.setCurrentItem (pageIndex, true);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView (View defunctPage)
    {
        int pageIndex = pagerAdapter.removeView (pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem (pageIndex);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage ()
    {
        return pagerAdapter.getView (pager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage (View pageToShow)
    {
        pager.setCurrentItem (pagerAdapter.getItemPosition (pageToShow), true);
    }


}
