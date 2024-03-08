package co.kr.compig.common.utils;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageUtil {
  private MessageSource messageSource;

  public String getMessage(String code, Object... args) {
    List<Object> objectList = new ArrayList<>();
    if (ArrayUtils.isNotEmpty(args)) {
      objectList.addAll(Arrays.asList(args));
    }
    return messageSource.getMessage(code, objectList.toArray(), code, getLocale());
  }

}