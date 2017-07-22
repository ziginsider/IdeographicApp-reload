package data;

import android.content.Context;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;

/**
 * Created by zigin on 12.12.2016.
 */

public class AllExpDataCreator {

    private DatabaseHandlerExternal dba;
    static AllExpDataCreator _allExpDataCreator;
    ArrayList<ParentObject> mParentList;

    public  AllExpDataCreator(Context context) {

        dba = new DatabaseHandlerExternal(context);

        this.mParentList = new ArrayList<>();

        mParentList = dba.getAllExpParent();

        dba.close();
    }

    public static AllExpDataCreator get(Context context) {

        if (_allExpDataCreator == null) {

            _allExpDataCreator = new AllExpDataCreator(context);
        }
        return _allExpDataCreator;
    }

    public ArrayList<ParentObject> getAll() {
        return mParentList;
    }
}
