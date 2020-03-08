package app.disruptor.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import app.khorshid.R;
import app.khorshid.misc.Utility;

public class DialogUI
{
    public static void Toast(String Message)
    {
        int Padding = Utility.ToDP(16);
        Context context = Utility.GetContext();

        TextView TextViewMessage = new TextView(context);
        TextViewMessage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        TextViewMessage.setText(Message);
        TextViewMessage.setPadding(Padding, Padding, Padding, Padding);
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewMessage.setTextColor(Utility.Color(R.color.Primary1));
        TextViewMessage.setGravity(Gravity.CENTER);
        TextViewMessage.setBackground(context.getDrawable(R.drawable.general_toast));

        Toast ToastMain = new Toast(context);
        ToastMain.setGravity(Gravity.BOTTOM, 0, Utility.ToDP(80));
        ToastMain.setDuration(Toast.LENGTH_LONG);
        ToastMain.setView(TextViewMessage);
        ToastMain.show();
    }
}
