package app.khorshid;

import android.app.Application;

import android.util.Log;

import app.khorshid.misc.Utility;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Utility.SetContext(getApplicationContext());

        Thread.UncaughtExceptionHandler DefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((T, E) ->
        {
            Utility.Debug("Crash: " + Log.getStackTraceString(E));

            DefaultUncaughtExceptionHandler.uncaughtException(T, E);
        });
    }
}
