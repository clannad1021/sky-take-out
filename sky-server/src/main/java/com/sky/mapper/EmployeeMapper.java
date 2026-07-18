package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee (username, name, phone, sex, id_number, password, status, create_time, update_time, create_user, update_user)" +
            " values (#{username}, #{name}, #{phone}, #{sex}, #{idNumber}, #{password}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void addEmployee(Employee employee);

    Page<Employee> empSelect(String name);

    void upDate(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
