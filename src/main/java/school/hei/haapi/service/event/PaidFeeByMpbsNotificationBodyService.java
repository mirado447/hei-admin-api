package school.hei.haapi.service.event;

import static school.hei.haapi.model.exception.ApiException.ExceptionType.SERVER_EXCEPTION;
import static school.hei.haapi.service.utils.DataFormatterUtils.numberToReadable;
import static school.hei.haapi.service.utils.DataFormatterUtils.numberToWords;
import static school.hei.haapi.service.utils.TemplateUtils.htmlToString;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import school.hei.haapi.endpoint.event.model.PaidFeeByMpbsNotificationBody;
import school.hei.haapi.mail.Email;
import school.hei.haapi.mail.Mailer;
import school.hei.haapi.model.exception.ApiException;
import school.hei.haapi.service.utils.Base64Converter;
import school.hei.haapi.service.utils.ClassPathResourceResolver;

@Service
@AllArgsConstructor
@Slf4j
public class PaidFeeByMpbsNotificationBodyService
    implements Consumer<PaidFeeByMpbsNotificationBody> {
  private final Mailer mailer;
  private final Base64Converter base64Converter;
  private final ClassPathResourceResolver classPathResourceResolver;

  private InternetAddress getInternetAdressFromEmail(String email) {
    try {
      return new InternetAddress(email);
    } catch (AddressException e) {
      log.info("bad email address", e);
      throw new ApiException(SERVER_EXCEPTION, e);
    }
  }

  private Context loadContext(PaidFeeByMpbsNotificationBody mailBodyContent) {
    Context initial = new Context();
    Resource emailSignatureImage = classPathResourceResolver.apply("Signature-HEI-v2", ".png");

    initial.setVariable("pspAmount", numberToReadable(mailBodyContent.getAmount()));
    initial.setVariable("pspAmountWord", numberToWords(mailBodyContent.getAmount()));
    initial.setVariable("mpbsAuthor", mailBodyContent.getMpbsAuthor());
    initial.setVariable("emailSignature", base64Converter.apply(emailSignatureImage));
    return initial;
  }

  @Override
  public void accept(PaidFeeByMpbsNotificationBody paidFeeByMpbsNotificationBody) {
    var body = htmlToString("paidFeeByMpbs", loadContext(paidFeeByMpbsNotificationBody));
    String mailTitle = "Ecollage - [ Orange Money ]";
    InternetAddress to =
        getInternetAdressFromEmail(paidFeeByMpbsNotificationBody.getMpbsAuthorEmail());
    mailer.accept(new Email(to, List.of(), List.of(), mailTitle, body, List.of()));
    log.info("mail {} sent to {}", mailTitle, to);
  }
}
