package school.hei.haapi.service.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import school.hei.haapi.endpoint.rest.model.AttendanceStatus;
import school.hei.haapi.model.StudentAttendance;
import school.hei.haapi.repository.AttendanceRepository;

@Component
@AllArgsConstructor
public class AttendanceServiceUtils {
  private final AttendanceRepository attendanceRepository;

  @Transactional
  public List<StudentAttendance> saveStudentAttendance(
      StudentAttendance toSave, List<StudentAttendance> toCreateMapped
  ) {
    Instant begin = LocalDateTime.ofInstant(toSave.getCreatedAt() , ZoneId.of("UTC+3"))
        .minusMinutes(30)
        .atZone(ZoneId.of("UTC+3"))
        .toInstant();
    Instant end = LocalDateTime.ofInstant(toSave.getCreatedAt(), ZoneId.of("UTC+3"))
        .plusHours(2)
        .plusMinutes(30)
        .atZone(ZoneId.of("UTC+3"))
        .toInstant();
    Optional<StudentAttendance> predicate = attendanceRepository
        .findStudentAttendanceByFromCourseAndEndCourseAndStudent(begin, end, toSave.getStudent());
    if (predicate.isPresent()) {
      StudentAttendance attendance = predicate.get();
      attendance.setAttendanceMovementType(toSave.getAttendanceMovementType());
      attendance.setPlace(toSave.getPlace());
      attendance.setCreatedAt(toSave.getCreatedAt());
      toCreateMapped.add(attendanceRepository.save(attendance));
    }
    else {
      toCreateMapped.add(attendanceRepository.save(toSave));
    }
    return toCreateMapped;
  }
  public static List<StudentAttendance> filterAttendanceFromTwoSet(
      List<StudentAttendance> givenData, Set<StudentAttendance> toCompare
  ) {
    return givenData.stream().filter(toCompare::contains).collect(Collectors.toList());
  }

  public static int getFilterCase(List<AttendanceStatus> attendanceStatuses) {
    int filterCase = 0;
    if (attendanceStatuses != null && !attendanceStatuses.isEmpty()) {
      filterCase = 1;
    }
    if ((attendanceStatuses == null)) {
      filterCase = 2;
    }
    return filterCase;
  }
}
