package n.kidding.course.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import n.kidding.course.dto.CourseDTO;

@Mapper
public interface CourseMapper {

    @Select("select * from pe_course")
    List<CourseDTO> listCourse();

    @Select("select user_id from pr_user_course where course_id=#{courseId}")
    Integer getCourseTeacher(@Param("courseId") int courseId);

}
