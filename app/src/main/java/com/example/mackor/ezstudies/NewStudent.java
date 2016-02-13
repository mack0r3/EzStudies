package com.example.mackor.ezstudies;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bogus on 2016-02-08.
 */
public class NewStudent {

    private String fname;
    private String lname;
    private String indexNo;
    private String group;
    private String password;

    public NewStudent(String fname, String lname, String indexNo, String group, String password) {
        this.fname = fname.trim();
        this.lname = lname.trim();
        this.indexNo = indexNo;
        this.group = group;
        this.password = password;
    }

    public boolean emptyFieldFound()
    {
        NewStudent newStudent = new NewStudent(fname, lname, indexNo, group, password);
        Field fields[] = getClass().getDeclaredFields();
        for (Field field: fields) {
            String value = "";
            try {
                value = (String) field.get(newStudent);
                if(value.isEmpty())return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean validateName()
    {
        String regex = "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]{1,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher fnameMatcher = pattern.matcher(fname),
                lnameMatcher = pattern.matcher(lname);
        return (fnameMatcher.matches() && lnameMatcher.matches());
    }
    public boolean validateIndexNo()
    {
        String regex = "^[0-9]{6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher indexMachter = pattern.matcher(indexNo);
        return indexMachter.matches();
    }
    public boolean validatePassword()
    {
        return (password.length() >= 5 && password.length() <= 20);
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getIndexNo() {
        return indexNo;
    }

    public String getGroup() {
        return group;
    }

    public String getPassword() {
        return password;
    }

    private void _(String message)
    {
        Log.v("ERRORS", message);
    }

}



