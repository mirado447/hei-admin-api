package school.hei.haapi.endpoint.rest.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.haapi.endpoint.rest.model.CreateAttendanceMovement;
import school.hei.haapi.endpoint.rest.model.StudentAttendanceMovement;
import school.hei.haapi.model.StudentAttendance;
import school.hei.haapi.model.exception.NotFoundException;
import school.hei.haapi.repository.UserRepository;

@AllArgsConstructor
@Component
public class AttendanceMapper {
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  public StudentAttendanceMovement toRestMovement (school.hei.haapi.model.StudentAttendance domain) {
    return new StudentAttendanceMovement()
        .id(domain.getId())
        .attendanceMovementType(domain.getAttendanceMovementType())
        .createdAt(domain.getCreatedAt())
        .place(domain.getPlace())
        .student(userMapper.toRestStudent(domain.getStudent()));
  }

  public StudentAttendance toDomain (CreateAttendanceMovement toCreate) {
    return StudentAttendance.builder()
        .attendanceMovementType(toCreate.getAttendanceMovementType())
        .place(toCreate.getPlace())
        .createdAt(toCreate.getCreatedAt())
        .student(userRepository.findById(toCreate.getStudentId()).orElseThrow(() -> {
          throw new NotFoundException("student with #"+toCreate.getStudentId()+" not found");
        }))
        .build();
  }
}
