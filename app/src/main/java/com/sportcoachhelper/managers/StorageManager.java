package com.sportcoachhelper.managers;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;

import com.sportcoachhelper.database.DatabaseHelper;
import com.sportcoachhelper.model.Play;
import com.sportcoachhelper.model.Template;
import com.sportcoachhelper.model.TemplateItem;
import com.sportcoachhelper.paths.CirclePath;
import com.sportcoachhelper.paths.ShapePath;
import com.sportcoachhelper.paths.SquarePath;
import com.sportcoachhelper.paths.TrianglePath;
import com.sportcoachhelper.paths.interfaces.Dibujables;
import com.sportcoachhelper.util.Utility;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static com.sportcoachhelper.util.Utility.*;

/**
 * Created by claudioveas on 12/07/14.
 */
public class StorageManager {

    private static StorageManager sInstance = new StorageManager();
    private DatabaseHelper mDataBaseHelper = DatabaseHelper.getInstance();

    private StorageManager(){

    }

    public static StorageManager getInstance() {
        return sInstance;
    }

    public void insertPlayComponent(String componentType, String data, long playId, int index) {
        mDataBaseHelper.insertPlayComponent(componentType, data.toString(), playId, index);
    }

    public void updatePlay(long id, String name, String field, String fieldType, String s, long time, byte[] byteArray) {
        mDataBaseHelper.updatePlay(id, name, field, fieldType, s, time,byteArray);
        mDataBaseHelper.deletePlayComponents(id);
    }

    public Cursor getPlayComponents(long id) {
        return mDataBaseHelper.getPlayComponents(id);
    }

    public Cursor getPlays(String court) {
       return mDataBaseHelper.getPlays(court);
    }

    public long insertPlay(String name, String field, String fieldType, String s, long time, byte[] byteArray) {
        return mDataBaseHelper.insertPlay(name, field,fieldType , s, time,byteArray);
    }


    public void saveDocument(Play play, String name, int width, int height, Bitmap mLinesBitmap) {

        try {
            play.setName(name);
            if(play.getId()!=-1) {
                updatePlay(play.getId(), name, play.getField(), play.getFieldType(), "", System.currentTimeMillis(), getByteArray(mLinesBitmap));

            } else {
                long playId;
                playId = insertPlay(name, play.getField(), play.getFieldType(), "", System.currentTimeMillis(), getByteArray(mLinesBitmap));
                play.setId(playId);

            }

            createDBComponents(play,play.getId());
            createTemplate(play,width, height);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creation and template saving
     */
    private void createTemplate(Play play, int width, int height) {
        Template template = new Template();
        template.setField(play.getField());
        template.setName(play.getName());
        ArrayList<Dibujables> plays = play.getUndoablePaths();
        for (Dibujables dibujable : plays) {
            if(dibujable instanceof ShapePath) {
                TemplateItem item = new TemplateItem();
                int shape = -1;
                if(dibujable instanceof CirclePath) {
                    shape = TemplateItem.SHAPE_CIRCLE;
                } else if(dibujable instanceof TrianglePath) {
                    shape = TemplateItem.SHAPE_TRIANGLE;
                } else if(dibujable instanceof SquarePath) {
                    shape = TemplateItem.SHAPE_SQUARE;
                }
                item.setShape(shape);
                item.setxPercentage(getPercentageDimension(((ShapePath) dibujable).getX(), width));
                item.setyPercentage(getPercentageDimension(((ShapePath) dibujable).getY(), height));
                template.add(item);
            }
        }
        File toSaveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + play.getName() + "_template");

        FileOutputStream output;
        try {
            if(!toSaveFile.exists()) {
                toSaveFile.createNewFile();
            }
            output = new FileOutputStream(toSaveFile);

            ObjectOutputStream stream = new ObjectOutputStream(output);
            stream.writeObject(template);
            stream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDBComponents(Play play,long playId) {
        try{
            if(playId!=-1) {
                ArrayList<Dibujables> components = play.getUndoablePaths();
                int index = 0;
                for(Dibujables component : components){
                    JSONObject data = new JSONObject();

                    data.put("DATA",component.toJsonData());
                    insertPlayComponent(component.getComponentType(), data.toString(), playId, index);
                    index++;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }


}
