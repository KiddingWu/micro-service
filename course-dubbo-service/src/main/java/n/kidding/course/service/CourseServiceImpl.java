package n.kidding.course.service;


import com.alibaba.dubbo.config.annotation.Service;

import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import n.kidding.course.dto.CourseDTO;
import n.kidding.course.mapper.CourseMapper;
import n.kidding.thrift.user.UserInfo;
import n.kidding.thrift.user.dto.TeacherDTO;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ServiceProvider serviceProvider;

    @Override
    public List<CourseDTO> courseList() {

        List<CourseDTO> courseDTOS = courseMapper.listCourse();
        if (courseDTOS != null) {
            for (CourseDTO courseDTO : courseDTOS) {
                // 根据课程查询用户id
                Integer teacherId = courseMapper.getCourseTeacher(courseDTO.getId());
                if (teacherId != null) {
                    try {
                        UserInfo userInfo = serviceProvider.getUserService().getTeacherById(teacherId);
                        courseDTO.setTeacher(trans2Teacher(userInfo));
                    } catch (TException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return courseDTOS;
    }

    private TeacherDTO trans2Teacher(UserInfo userInfo) {
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(userInfo, teacherDTO);
        return teacherDTO;
    }
}
