package n.kidding.course.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import n.kidding.thrift.user.dto.UserDTO;
import n.kidding.user.client.LoginFilter;

public class CourseFilter extends LoginFilter {

    @Override
    protected void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) {

        request.setAttribute("user", userDTO);
    }
}
