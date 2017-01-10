package com.bushic.taskslist;

public class Constants {
    public static class URL{
        private static final String HOST = "http://192.168.1.4:8080/";
        //public static final String GET_USERBYID = HOST + "users/";
        public static final String GET_USERBYLOGIN = HOST + "users/";
        public static final String GET_LISTBYID= HOST + "lists/";
        public static final String GET_TASKSBYLISTID = HOST + "tasks/";
        public static final String GET_PERMISSONSBYUSERID = HOST + "permissions/";
    }
}
