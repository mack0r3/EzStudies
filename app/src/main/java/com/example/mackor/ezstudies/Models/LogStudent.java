package com.example.mackor.ezstudies.Models;

import java.lang.reflect.Field;

/**
 * Created by Korzonkie on 2016-02-14.
 */
public class LogStudent {
    private String indexNo;
    private String password;

    public LogStudent(String indexNo, String password) {
        this.indexNo = indexNo;
        this.password = password;
    }

    public boolean emptyFieldFound()
    {
        LogStudent logStudent = new LogStudent(indexNo, password);
        Field fields[] = getClass().getDeclaredFields();
        for (Field field: fields) {
            String value = "";
            try {
                value = (String) field.get(logStudent);
                if(value.isEmpty())return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
