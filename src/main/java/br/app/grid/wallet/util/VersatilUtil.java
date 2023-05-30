package br.app.grid.wallet.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * <b>VersatilUtil</b>
 * 
 * @author Brunno José Guimarães de Almeida.
 * @since 11 de maio de 2023.
 */
public class VersatilUtil {
  private static ObjectMapper mapper = new ObjectMapper();
  static {
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }



  public static LocalDateTime toLocalDateTime(String string, String pattern) {
    // Pattern Example: yyyy-MM-dd HH:mm
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return LocalDateTime.parse(string, formatter);
  }

  public static String toJson(Object objeto) {
    try {
      return mapper.writeValueAsString(objeto);
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }

  public static String xmlToJson(String xml, int indentacao) {
    try {
      JSONObject xmlJSONObj = XML.toJSONObject(xml);
      String jsonPrettyPrintString = xmlJSONObj.toString(indentacao);
      System.out.println(jsonPrettyPrintString);
      return jsonPrettyPrintString;
    } catch (JSONException je) {
      je.printStackTrace();
      return null;
    }
  }

  public static Date toDate(LocalDate localDate) {
    if (Objects.isNull(localDate))
      return null;
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public static LocalDate toLocalDate(LocalDateTime data) {
    if (Objects.isNull(data))
      return null;
    return data.toLocalDate();
  }

  public static Date toDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

}
