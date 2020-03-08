package app.khorshid.misc;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class API
{
    private static OkHttpClient Client;

    private static final String BASE_URL = "https://pubg.ash-wow.ir/";

    private static OkHttpClient Client()
    {
        if (Client == null)
            Client = new OkHttpClient();

        return Client;
    }

    public static void Login(String Username, String Password, CallBack callBack)
    {
        RequestBody RequestBodyLogin = new FormBody.Builder().add("Username", Username).add("Password", Password).build();

        Request RequestLogin = new Request.Builder().url(BASE_URL + "Login").post(RequestBodyLogin).build();

        Client().newCall(RequestLogin).enqueue(callBack);
    }

    public static void Update(CallBack callBack)
    {
        RequestBody RequestBodyUpdate = new FormBody.Builder().add("Auth", Utility.GetString("Auth")).build();

        Request RequestUpdate = new Request.Builder().url(BASE_URL + "Update").post(RequestBodyUpdate).build();

        Client().newCall(RequestUpdate).enqueue(callBack);
    }

    public static void Download(String URL, File file, CallBack callBack)
    {
        new Thread(() ->
        {
            BufferedSink BufferedSinkMain = null;
            BufferedSource BufferedSourceMain = null;

            try
            {
                Response ResponseMain = Client().newCall(new Request.Builder().url(BASE_URL + URL).get().build()).execute();

                if (!ResponseMain.isSuccessful())
                    throw new Exception();

                ResponseBody ResponseBodyMain = ResponseMain.body();

                if (ResponseBodyMain == null)
                    throw new Exception();

                long BytesRead;
                long ByteDownload = 0;
                long ByteTotal = ResponseBodyMain.contentLength();

                callBack.OnProcess(0, ByteTotal);

                BufferedSourceMain = ResponseBodyMain.source();
                BufferedSinkMain = Okio.buffer(Okio.sink(file));
                Buffer BufferSink = BufferedSinkMain.getBuffer();

                while ((BytesRead = BufferedSourceMain.read(BufferSink, 65536)) != -1)
                {
                    BufferedSinkMain.emit();

                    ByteDownload += BytesRead;

                    if (ByteDownload != ByteTotal)
                        callBack.OnProcess(ByteDownload, ByteTotal);
                }

                BufferedSinkMain.flush();

                callBack.OnProcess(ByteDownload, ByteTotal);
            }
            catch (SocketTimeoutException e)
            {
                //
            }
            catch (Exception e)
            {
                Utility.Debug("API-Download: " + e.getMessage());
            }

            if (BufferedSinkMain != null)
                Util.closeQuietly(BufferedSinkMain);

            if (BufferedSourceMain != null)
                Util.closeQuietly(BufferedSourceMain);
        }).start();
    }

    public static abstract class CallBack implements okhttp3.Callback
    {
        public void OnCallBack(JSONObject Response) throws Exception
        {

        }

        public void OnProcess(long ByteDownload, long ByteTotal)
        {

        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e)
        {
            String URL = call.request().url().toString();

            Utility.Debug("API-onFailure-" + URL + ": " + e.getMessage());

            Utility.UIThread(() ->
            {
                try
                {
                    OnCallBack(null);
                }
                catch (Exception e2)
                {
                    Utility.Debug("API-onFailure-" + URL + ": " + e2.getMessage());
                }
            });
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response)
        {
            try
            {
                ResponseBody ResponseBodyMain = response.body();

                if (ResponseBodyMain == null)
                {
                    Utility.UIThread(() ->
                    {
                        try
                        {
                            OnCallBack(null);
                        }
                        catch (Exception e)
                        {
                            String URL = call.request().url().toString();

                            Utility.Debug("API-onResponse1-" + URL + ": " + e.getMessage());
                        }
                    });
                    return;
                }

                String Message = ResponseBodyMain.string();

                if (Utility.IsJSON(Message))
                {
                    Utility.UIThread(() ->
                    {
                        try
                        {
                            OnCallBack(new JSONObject(Message));
                        }
                        catch (Exception e)
                        {
                            String URL = call.request().url().toString();

                            Utility.Debug("API-onResponse2-" + URL + ": " + e.getMessage());
                        }
                    });
                    return;
                }

                Utility.UIThread(() ->
                {
                    try
                    {
                        OnCallBack(null);
                    }
                    catch (Exception e)
                    {
                        String URL = call.request().url().toString();

                        Utility.Debug("API-onResponse3-" + URL + ": " + e.getMessage());
                    }
                });
            }
            catch (Exception e)
            {
                String URL = call.request().url().toString();

                Utility.Debug("API-onResponse4-" + URL + ": " + e.getMessage());
            }
        }
    }
}
