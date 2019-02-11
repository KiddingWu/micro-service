package n.kidding.course.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import n.kidding.course.dto.CourseDTO;
import n.kidding.course.service.ICourseService;
import n.kidding.thrift.user.dto.UserDTO;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Reference
    private ICourseService courseService;

    @RequestMapping(value = "/courseList", method = RequestMethod.GET)
    @ResponseBody
    private List<CourseDTO> courseList(HttpServletRequest request) {

        UserDTO userDTO = (UserDTO) request.getAttribute("user");
        System.out.println(userDTO.toString());

        return courseService.courseList();
    }
}
