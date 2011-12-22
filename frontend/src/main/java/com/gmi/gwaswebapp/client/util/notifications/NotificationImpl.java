package com.gmi.gwaswebapp.client.util.notifications;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;

public class NotificationImpl {
        public static final int PERMISSION_ALLOWED = 0;
        public static final int PERMISSION_NOT_ALLOWED = 1;
        public static final int PERMISSION_DENIED = 2;
        private JavaScriptObject jsObject;

        protected NotificationImpl() {
                
        }
        
        public native int checkPermission() /*-{
                return $wnd.webkitNotifications.checkPermission();
        }-*/;
        
        public void requestPermission(Callback<Void, Void> callback) {
                this.requestPermission(this, callback);
        }

        private native void requestPermission(NotificationImpl x, Callback<Void, Void> callback) /*-{
                $wnd.webkitNotifications.requestPermission($entry(function() {
                        x.@com.gmi.gwaswebapp.client.util.notifications.NotificationImpl::callbackRequestPermission(Lcom/google/gwt/core/client/Callback;)(callback);
                }));
        }-*/;

        private void callbackRequestPermission(Callback<Void,Void> callback) {
                if (callback != null) {
                        callback.onSuccess(null);
                }
        }

        public void createNotification(String iconUrl, String title, String body) {
                this.jsObject = null;
                this.jsObject = this.createJSNotification(iconUrl, title, body);
        }

        public void createNotification(String contentUrl) {
                this.jsObject = null;
                this.jsObject = this.createHtmlNotification(contentUrl);
        }
        
        private native JavaScriptObject createJSNotification(String iconUrl, String title, String body) /*-{
                return $wnd.webkitNotifications.createNotification(iconUrl, title, body);
        }-*/;
        
        private native JavaScriptObject createHtmlNotification(String contentUrl) /*-{
                return $wnd.webkitNotifications.createHTMLNotification(contentUrl);
        }-*/;
        
        public native void show() /*-{
                var obj = this.@com.gmi.gwaswebapp.client.util.notifications.NotificationImpl::jsObject;
                obj.show();
        }-*/;
        
}

