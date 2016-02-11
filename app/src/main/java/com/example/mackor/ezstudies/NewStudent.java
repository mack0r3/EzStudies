package com.example.mackor.ezstudies;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bogus on 2016-02-08.
 */
public class NewStudent {

    private static String insertUserURL = "http://46.101.168.84/EzStudiesCRUD/create_user.php";

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

}



