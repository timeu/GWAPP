package com.gmi.gwaswebapp.client.util.notifications;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.PartialSupport;


@PartialSupport
public class Notification {
        /**
         * Detector for browser support of Desktop Notification.
         */
        private static class NotificationSupportDetector {
                private final boolean isNotificationSupported = detectNotificationSupport();
                
                public boolean isNotificationSupported() {
                        return this.isNotificationSupported;
                }
                
                private native boolean detectNotificationSupport() /*-{
                        return typeof $wnd.webkitNotifications != "undefined";
                }-*/;
        }
        
        /**
         * Detector for browser support of Desktop Notification.
         */
        @SuppressWarnings("unused")
        private static class NotificationSupportDetectorNo extends NotificationSupportDetector {
                @Override
                public boolean isNotificationSupported() {
                        return false;
                }
        }
        
        private static final NotificationImpl impl = GWT.create(NotificationImpl.class);
        
        private static int permission = -1;

        private static NotificationSupportDetector supportDetectorImpl;

        private static Notification notification;
        
        //TODO: Some variable
        
        //TODO: Some API function for event
        
        /**
         * Check current status of notification is allowed or not
         * 
         * @return true if user allow to use notification
         */
        public static boolean isNotificationAllowed() {
                checkPermission();
                return permission == NotificationImpl.PERMISSION_ALLOWED;
        }
        
        /**
         * Check current status of notification is set or not
         * 
         * @return true if user doesn't set permission (never choose 'Allow' or 'Deny')
         */
        public static boolean isNotificationNotAllowed() {
                checkPermission();
                return permission == NotificationImpl.PERMISSION_NOT_ALLOWED;
        }
        
        /**
         * Check current status of notification is denied or not
         * 
         * @return true if user deny to use notification
         */
        public static boolean isNotificationDenied() {
                checkPermission();
                return permission == NotificationImpl.PERMISSION_DENIED;
        }
        
        /**
         * Get current status of notification permission
         * @return
         */
        public static int checkPermission() {
                //if (permission == -1) {
                        permission = impl.checkPermission();
                //}
                return permission;
        }
        
        public static void requestPermission() {
                impl.requestPermission(null);
        }
        
        public static void requestPermission(Callback<Void,Void> callback) {
                impl.requestPermission(callback);
        }
        
        public static boolean isSupported() {
                return getNotificationSupportDetector().isNotificationSupported();
        }
        
        private static NotificationSupportDetector getNotificationSupportDetector() {
                if (supportDetectorImpl == null) {
                        supportDetectorImpl = GWT.create(NotificationSupportDetector.class);
                }
                return supportDetectorImpl;
        }
        
        public static Notification createIfSupported(String contentUrl) {
                if (isSupported()) {
                        if (notification == null) {
                                notification =  new Notification(contentUrl);
                        }
                        else {
                        	notification.contentUrl = contentUrl;
                        }
                        return notification;
                }
                return null;
        }
        
        public static Notification createIfSupported(String iconUrl,String title,String body) {
        	if (isSupported()) {
                if (notification == null) {
                        notification =  new Notification(iconUrl,title,body);
                }else {
                	notification.iconUrl = iconUrl;
                	notification.title = title;
                	notification.body = body;
                }
                return notification;
        	}
        	return null;
        }

        private String contentUrl;
        private String iconUrl;
        private String title;
        private String body;
        
        private Notification(String contentUrl) {
                this.contentUrl = contentUrl;
        }
        
        private Notification(String iconUrl, String title, String body) {
                this.contentUrl = null;
                this.iconUrl = iconUrl;
                this.title = title;
                this.body = body;
        }
        
        public void show() {
        	if (!isNotificationAllowed())
        		return;
        	if (contentUrl != null)	{
        		impl.createNotification(contentUrl);
        	}
        	else {
        		impl.createNotification(iconUrl, title, body);
        	}
        	impl.show();
        }

}
