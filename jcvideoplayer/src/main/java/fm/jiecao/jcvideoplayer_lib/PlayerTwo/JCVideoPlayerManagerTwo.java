package fm.jiecao.jcvideoplayer_lib.PlayerTwo;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import fm.jiecao.jcvideoplayer_lib.JCMediaPlayerListener;

/**
 * Created by divyanshu on 9/25/2016.
 */

public class JCVideoPlayerManagerTwo {
    public static WeakReference<JCMediaPlayerListener> CURRENT_SCROLL_LISTENER;
    public static LinkedList<WeakReference<JCMediaPlayerListener>> LISTENERLIST = new LinkedList<>();

    public static void setCurrentScrollPlayerListener(JCMediaPlayerListener listener) {
        CURRENT_SCROLL_LISTENER = new WeakReference<>(listener);
    }

    public static JCMediaPlayerListener getCurrentScrollPlayerListener() {
        if (CURRENT_SCROLL_LISTENER != null && CURRENT_SCROLL_LISTENER.get() != null) {
            return CURRENT_SCROLL_LISTENER.get();
        } else {
            return null;
        }
    }

    public static void putListener(JCMediaPlayerListener listener) {
        LISTENERLIST.push(new WeakReference<>(listener));
    }

    public static JCMediaPlayerListener popListener() {
        if (LISTENERLIST.size() == 0) {
            return null;
        }
        return LISTENERLIST.pop().get();
    }

    public static JCMediaPlayerListener getFirst() {
        if (LISTENERLIST.size() == 0) {
            return null;
        }
        return LISTENERLIST.getFirst().get();
    }

    public static void completeAll() {
        JCMediaPlayerListener ll = popListener();
        while (ll != null) {
            ll.onCompletion();
            ll = popListener();
        }
    }
}


