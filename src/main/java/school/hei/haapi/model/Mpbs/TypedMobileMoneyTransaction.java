package school.hei.haapi.model.Mpbs;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.hibernate.type.SqlTypes.NAMED_ENUM;

import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import school.hei.haapi.endpoint.rest.model.MobileMoneyType;

@MappedSuperclass
@Getter
@Setter
public class TypedMobileMoneyTransaction {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private String id;

  private String pspId;

  @JdbcTypeCode(NAMED_ENUM)
  @Enumerated(STRING)
  private MobileMoneyType mobileMoneyType;

  @CreationTimestamp private Instant creationDatetime;
}
