package sistema;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataUtils {

    // Converte uma String "dd/MM/yyyy" em java.sql.Date
    public static Date converterParaSQLDate(String dataTexto) {
        if (dataTexto != null && dataTexto.matches("\\d{2}/\\d{2}/\\d{4}")) {
            try {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                formato.setLenient(false); // valida a data (ex: não aceita 31/02/2024)
                java.util.Date dataUtil = formato.parse(dataTexto);
                return new Date(dataUtil.getTime());
            } catch (ParseException e) {
                throw new IllegalArgumentException("Data inválida. Use o formato dd/MM/yyyy.");
            }
        } else {
            throw new IllegalArgumentException("Formato de data inválido. Use dd/MM/yyyy.");
        }
    }

    // Converte java.sql.Date para String "dd/MM/yyyy"
    public static String formatarParaTexto(Date data) {
        if (data == null) return "";
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(data);
    }
}
