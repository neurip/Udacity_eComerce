package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

    public static void inject(Object target, String fieldName, Object source){

        boolean isPrivate = false;
        try {

            Field field = target.getClass().getDeclaredField(fieldName);
            if(!field.isAccessible()){
                field.setAccessible(true);
                isPrivate = false;
            }

            field.set(target, source);
            if(isPrivate){
                field.setAccessible(false);
            }
        }
        catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
