package rpc.demo.taskdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.demo.annotation.Service;
import rpc.demo.api.Student;
import rpc.demo.test.ClientTest;

@Service(StudentService.class)
public class StudentServiceImpl implements StudentService {
    private static Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    public Student getInfo() {
        Student stu = new Student();
        stu.setId(1);
        stu.setName("zhangsan");
        stu.setAge(20);
        return stu;
    }

    @Override
    public boolean printInfo(Student student) {
        if (student == null) {
            return false;
        }
        logger.info(student.toString());
        return true;
    }
}
