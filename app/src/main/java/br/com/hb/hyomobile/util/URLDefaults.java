package br.com.hb.hyomobile.util;

/**
 * Created by vanderson on 20/03/2017.
 */

public class URLDefaults {
    public static String URL_REST="http://hyoteste.servehttp.com:3090/hyoapi/v1/";
    //public static String URL_REST="http://192.168.1.13:3090/hyoapi/v1/";



    public static String getUrlLogin(){
        return URL_REST+"login/";
    }

    public static String getUrlLogout(){
        return URL_REST+"logout/";
    }


    public static String getUrlGetPerson(){
        return URL_REST+"person_info/";
    }

}
