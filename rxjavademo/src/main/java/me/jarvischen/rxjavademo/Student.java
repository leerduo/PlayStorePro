package me.jarvischen.rxjavademo;

import java.util.List;

/**
 * Created by chenfuduo on 2016/5/18.
 */
public class Student {
    private String name;
    private List<Course> courses;

    public Student(String name,List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
