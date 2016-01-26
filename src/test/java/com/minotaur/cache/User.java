package com.minotaur.cache;

import java.io.Serializable;
import java.util.Date;

/**
 * user
 * Created by minotaur on 15/11/17.
 */
public class User implements Serializable{

    private static final long serialVersionUID = -6484188423796584529L;

    int    age;
    String name;
    Date   birthDay;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}
