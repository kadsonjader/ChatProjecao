package com.example.kadson.chatprojecao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kadson.chatprojecao.fragment.ContatosFragment;
import com.example.kadson.chatprojecao.fragment.ConversasFragment;

public class TabAdapter extends FragmentStatePagerAdapter {
    private String[] tituloAbas = {"CONVERSAS", "COORDENADORES"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ConversasFragment();
                break;
            case 1 :
                fragment = new ContatosFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
