package com.example.divyanshu.smyt.CustomViews;

import android.content.Context;

import com.example.divyanshu.smyt.R;

/**
 * Created by divyanshuPC on 3/15/2017.
 */

public class ReusedCodes {

    public static String getComment(Context context, int count) {
        return context.getResources().getQuantityString(R.plurals.numberOfComments, count, count);
    }

    public static String getViews(Context context, String count) {
        if (count == null || count.isEmpty() || count.equals("null"))
            count = "0";
        return context.getResources().getQuantityString(R.plurals.numberOfViews, Integer.parseInt(count), Integer.parseInt(count));

    }

    public static String getLikes(Context context, String count) {
        if (count == null || count.isEmpty() || count.equals("null"))
            count = "0";
        return context.getResources().getQuantityString(R.plurals.numberOfLikes, Integer.parseInt(count), Integer.parseInt(count));

    }

    public static String getUserOneVote(Context context, String count) {
        if (count == null || count.isEmpty() || count.equals("null"))
            count = "0";
        return context.getResources().getQuantityString(R.plurals.numberOfVotes, Integer.parseInt(count), Integer.parseInt(count));
    }

    public static String getUserTwoVote(Context context, String count) {
        if (count == null || count.isEmpty() || count.equals("null"))
            count = "0";
        return context.getResources().getQuantityString(R.plurals.numberOfVotes, Integer.parseInt(count), Integer.parseInt(count));
    }
}
