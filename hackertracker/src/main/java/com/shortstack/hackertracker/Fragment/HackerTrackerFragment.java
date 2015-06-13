package com.shortstack.hackertracker.Fragment;

/**
 * Created by Whitney Champion on 4/18/14.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.shortstack.hackertracker.Activity.HomeActivity;
import com.shortstack.hackertracker.Adapter.DatabaseAdapter;
import com.shortstack.hackertracker.Adapter.DatabaseAdapterOfficial;
import com.shortstack.hackertracker.Application.HackerTrackerApplication;
import com.shortstack.hackertracker.Common.Constants;
import com.shortstack.hackertracker.Model.Contest;
import com.shortstack.hackertracker.Model.Default;
import com.shortstack.hackertracker.Model.Event;
import com.shortstack.hackertracker.Model.Party;
import com.shortstack.hackertracker.Model.Speaker;
import com.shortstack.hackertracker.Model.Vendor;

import java.util.ArrayList;
import java.util.List;

public class HackerTrackerFragment extends Fragment {

    private FragmentManager fragmentManager;
    private Context context;
    private DatabaseAdapterOfficial myOfficialDbHelper;
    private DatabaseAdapter myDbHelper;

    public HackerTrackerFragment() {

        // get fragment manager
        fragmentManager = HomeActivity.fragmentManager;

        // get context
        context = HackerTrackerApplication.getAppContext();

        // database
        myOfficialDbHelper = HackerTrackerApplication.myOfficialDbHelper;
        myDbHelper = HackerTrackerApplication.myDbHelper;

    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage, int shape) {
        int targetWidth = shape;
        int targetHeight = shape;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public static String getTitle(Context ctxt, int position) {
        switch (position) {
            case -1:
                return Constants.DAY_0;
            case 0:
                return Constants.DAY_1;
            case 1:
                return Constants.DAY_2;
            case 2:
                return Constants.DAY_3;
            case 3:
                return Constants.DAY_4;
            default:
                return "";
        }
    }

    public static String getDate(int date) {
        String day = "";
        switch (date) {
            case 0:
                day = Constants.LONG_DAY_0;
                break;
            case 1:
                day = Constants.LONG_DAY_1;
                break;
            case 2:
                day = Constants.LONG_DAY_2;
                break;
            case 3:
                day = Constants.LONG_DAY_3;
                break;
            case 4:
                day = Constants.LONG_DAY_4;
                break;
        }
        return day;
    }

    /*
     * get list of {type} for {day}
     *
     * speaker = 1
     * contest = 2
     * event = 3
     * party = 4
     * vendors = 5
     * skytalks = 6
     * message = 7
     * unofficial = 8
     * dcib = 9
     * stupid = 10
     * kids = 11
     * joke = 12
     *
     */

    public List<Default> getItemByDate(String day, String type) {
        ArrayList<Default> result = new ArrayList<Default>();

        SQLiteDatabase db;

        // if it's a speaker or from official database, use officialDbHelper
        if (type.equals(Constants.TYPE_SPEAKER)
                || type.equals(Constants.TYPE_CONTEST)
                || type.equals(Constants.TYPE_PARTY)
                || type.equals(Constants.TYPE_SKYTALKS)
                || type.equals(Constants.TYPE_EVENT)
                ) {
            db = HackerTrackerApplication.myOfficialDbHelper.getWritableDatabase();
        } else {
            db = HackerTrackerApplication.myDbHelper.getWritableDatabase();
        }

        Cursor myCursor = db.rawQuery("SELECT * FROM data WHERE date=? AND type=? ORDER BY begin", new String[] {day, String.valueOf(type)});

        try{
            if (myCursor.moveToFirst()){
                do{

                    Default item;

                    switch (type) {
                        case Constants.TYPE_SPEAKER:
                            item = new Speaker();
                            break;
                        case Constants.TYPE_CONTEST:
                            item = new Contest();
                            break;
                        case Constants.TYPE_EVENT:
                            item = new Event();
                            break;
                        case Constants.TYPE_PARTY:
                            item = new Party();
                            break;
                        case Constants.TYPE_VENDOR:
                            item = new Vendor();
                            break;
                        case Constants.TYPE_SKYTALKS:
                            item = new Default();
                            break;
                        case Constants.TYPE_MESSAGE:
                            item = new Default();
                            break;
                        case Constants.TYPE_UNOFFICIAL:
                            item = new Default();
                            break;
                        case Constants.TYPE_DCIB:
                            item = new Default();
                            break;
                        case Constants.TYPE_STUPID:
                            item = new Default();
                            break;
                        case Constants.TYPE_KIDS:
                            item = new Default();
                            break;
                        case Constants.TYPE_JOKE:
                            item = new Default();
                            break;
                        default:
                            item = new Speaker();
                            break;
                    }

                    item.setId(myCursor.getInt(myCursor.getColumnIndex("id")));
                    item.setType(myCursor.getString(myCursor.getColumnIndex("type")));
                    item.setDate(myCursor.getString(myCursor.getColumnIndex("date")));
                    item.setTitle(myCursor.getString(myCursor.getColumnIndex("title")));
                    item.setDescription(myCursor.getString(myCursor.getColumnIndex("description")));
                    item.setName(myCursor.getString(myCursor.getColumnIndex("who")));
                    item.setEnd(myCursor.getString(myCursor.getColumnIndex("end")));
                    item.setBegin(myCursor.getString(myCursor.getColumnIndex("begin")));
                    item.setLocation(myCursor.getString(myCursor.getColumnIndex("location")));
                    item.setStarred(myCursor.getInt(myCursor.getColumnIndex("starred")));
                    item.setImage(myCursor.getString(myCursor.getColumnIndex("image")));
                    item.setLink(myCursor.getString(myCursor.getColumnIndex("link")));
                    item.setIsNew(myCursor.getInt(myCursor.getColumnIndex("is_new")));
                    item.setDemo(myCursor.getInt(myCursor.getColumnIndex("demo")));
                    item.setTool(myCursor.getInt(myCursor.getColumnIndex("tool")));
                    item.setExploit(myCursor.getInt(myCursor.getColumnIndex("exploit")));

                    result.add(item);
                }while(myCursor.moveToNext());
            }
        }finally{
            myCursor.close();
        }
        db.close();

        return result;
    }

    public List<Default> getStars(String day) {
        ArrayList<Default> result = new ArrayList<Default>();

        SQLiteDatabase dbOfficial = myOfficialDbHelper.getWritableDatabase();
        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        Cursor myOfficialCursor = dbOfficial.rawQuery("SELECT * FROM data WHERE date=? AND starred=1 ORDER BY begin", new String[]{day});
        Cursor myCursor = db.rawQuery("SELECT * FROM data WHERE date=? AND starred=1 ORDER BY begin", new String[] {day});

        // get speakers from official database

        try{
            if (myOfficialCursor.moveToFirst()){
                do{

                    Default item = new Default();

                    item.setId(myOfficialCursor.getInt(myOfficialCursor.getColumnIndex("id")));
                    item.setType(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("type")));
                    item.setTitle(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("title")));
                    item.setDescription(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("description")));
                    item.setName(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("who")));
                    item.setDate(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("date")));
                    item.setEnd(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("end")));
                    item.setBegin(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("begin")));
                    item.setLocation(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("location")));
                    item.setStarred(myOfficialCursor.getInt(myOfficialCursor.getColumnIndex("starred")));
                    item.setImage(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("image")));
                    item.setLink(myOfficialCursor.getString(myOfficialCursor.getColumnIndex("link")));
                    item.setIsNew(myOfficialCursor.getInt(myOfficialCursor.getColumnIndex("is_new")));
                    item.setDemo(myOfficialCursor.getInt(myOfficialCursor.getColumnIndex("demo")));
                    item.setTool(myOfficialCursor.getInt(myOfficialCursor.getColumnIndex("tool")));
                    item.setExploit(myOfficialCursor.getInt(myOfficialCursor.getColumnIndex("exploit")));

                    result.add(item);

                }while(myOfficialCursor.moveToNext());
            }
        }finally{
            myOfficialCursor.close();
        }

        dbOfficial.close();

        // get all other events from unofficial database

        try{
            if (myCursor.moveToFirst()){
                do{

                    Default item = new Default();

                    item.setId(myCursor.getInt(myCursor.getColumnIndex("id")));
                    item.setType(myCursor.getString(myCursor.getColumnIndex("type")));
                    item.setTitle(myCursor.getString(myCursor.getColumnIndex("title")));
                    item.setDescription(myCursor.getString(myCursor.getColumnIndex("description")));
                    item.setName(myCursor.getString(myCursor.getColumnIndex("who")));
                    item.setDate(myCursor.getString(myCursor.getColumnIndex("date")));
                    item.setEnd(myCursor.getString(myCursor.getColumnIndex("end")));
                    item.setBegin(myCursor.getString(myCursor.getColumnIndex("begin")));
                    item.setLocation(myCursor.getString(myCursor.getColumnIndex("location")));
                    item.setStarred(myCursor.getInt(myCursor.getColumnIndex("starred")));
                    item.setImage(myCursor.getString(myCursor.getColumnIndex("image")));
                    item.setLink(myCursor.getString(myCursor.getColumnIndex("link")));
                    item.setIsNew(myCursor.getInt(myCursor.getColumnIndex("is_new")));
                    item.setDemo(myCursor.getInt(myCursor.getColumnIndex("demo")));
                    item.setTool(myCursor.getInt(myCursor.getColumnIndex("tool")));
                    item.setExploit(myCursor.getInt(myCursor.getColumnIndex("exploit")));

                    result.add(item);

                }while(myCursor.moveToNext());
            }
        }finally{
            myCursor.close();
        }

        db.close();

        // return all items

        return result;
    }
}