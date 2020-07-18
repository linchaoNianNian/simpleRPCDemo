package rpc.demo.taskdemo;

import rpc.demo.api.Student;

public interface StudentService {
    Student getInfo();
    boolean printInfo(Student student);
}
