package com.example.mackor.ezstudies;

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

    public boolean validateName()
    {
        String namePattern = "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]{1,20}$";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher fnameMatcher = pattern.matcher(fname),
                lnameMatcher = pattern.matcher(lname);

        return (fnameMatcher.matches() && lnameMatcher.matches());
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
}
