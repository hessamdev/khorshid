package app.khorshid.misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;

@SuppressWarnings("StaticFieldLeak")
public class Utility
{
    private static final String TAG = "Khorshid";

    private static volatile Context context;

    public static void SetContext(Context c)
    {
        context = c;
    }

    public static Context GetContext()
    {
        return context;
    }

    public static void SetEditTextColor(EditText editText, int Color)
    {
        try
        {
            // noinspection all
            Field field = TextView.class.getDeclaredField("mEditor");

            if (!field.isAccessible())
                field.setAccessible(true);

            Object editor = field.get(editText);

            String[] HandleName = { "mSelectHandleLeft", "mSelectHandleRight", "mSelectHandleCenter" };
            String[] ResourceName = { "mTextSelectHandleLeftRes", "mTextSelectHandleRightRes", "mTextSelectHandleRes" };

            for (int I = 0; I < HandleName.length; I++)
            {
                Field FieldHandle = editor.getClass().getDeclaredField(HandleName[I]);

                if (!FieldHandle.isAccessible())
                    FieldHandle.setAccessible(true);

                Drawable DrawableHandle = (Drawable) FieldHandle.get(editor);

                if (DrawableHandle == null)
                {
                    Field FieldResource = TextView.class.getDeclaredField(ResourceName[I]);

                    if (!FieldResource.isAccessible())
                        FieldResource.setAccessible(true);

                    DrawableHandle = editText.getResources().getDrawable(FieldResource.getInt(editText), null);
                }

                if (DrawableHandle != null)
                {
                    Drawable drawable = DrawableHandle.mutate();
                    drawable.setColorFilter(Color(Color), PorterDuff.Mode.SRC_IN);

                    FieldHandle.set(editor, drawable);
                }
            }
        }
        catch (Exception e)
        {
            //
        }
    }

    public static String GetString(String Key)
    {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(Key, "");
    }

    public static void SetString(String Key, String Value)
    {
        SharedPreferences.Editor Editor = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        Editor.putString(Key, Value);
        Editor.apply();
    }

    public static int GetInt(String Key, int Default)
    {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getInt(Key, Default);
    }

    public static void SetInt(String Key, int Value)
    {
        SharedPreferences.Editor Editor = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        Editor.putInt(Key, Value);
        Editor.apply();
    }

    public static int NavigationBarHeight()
    {
        int ResourceID = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");

        if (ResourceID > 0)
            return context.getResources().getDimensionPixelSize(ResourceID);

        return 0;
    }

    public static int StatusBarHeight()
    {
        int ResourceID = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (ResourceID > 0)
            return context.getResources().getDimensionPixelSize(ResourceID);

        return 0;
    }

    static boolean IsJSON(String Value)
    {
        try
        {
            new JSONObject(Value);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public static int Color(int ID)
    {
        return context.getResources().getColor(ID);
    }

    public static void UIThread(Runnable runnable)
    {
        new Handler(context.getMainLooper()).post(runnable);
    }

    public static int ToDP(float Value)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Value, context.getResources().getDisplayMetrics());
    }

    public static void Debug(String Message)
    {
        new Thread(() ->
        {
            try
            {
                Log.e("DISRUPTOR", Message);

                File FileLog = new File(context.getApplicationInfo().dataDir + File.separator + "Debug.log");

                if (!FileLog.exists()) // noinspection all
                    FileLog.createNewFile();

                FileOutputStream FileOutputStreamLog = new FileOutputStream(FileLog, true);

                PrintWriter PrintWriterLog = new PrintWriter(FileOutputStreamLog);
                PrintWriterLog.println(Message);
                PrintWriterLog.flush();
                PrintWriterLog.close();

                FileOutputStreamLog.close();
            }
            catch (Exception e)
            {
                System.exit(1);
            }
        }).start();
    }
}
