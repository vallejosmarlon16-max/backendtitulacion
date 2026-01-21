package ec.yavirac.yavigestion.configs;

import ec.yavirac.yavigestion.modules.core.dtos.response.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 Error de base de datos (unique, fk, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseException(DataIntegrityViolationException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "Ya existe un registro con ese dato",
                extraerDetailCompleto(ex.getMessage()),
                null
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    // 🔹 Error general (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "Error interno del servidor",
                ex.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    private String extraerDetailCompleto(String errorMessage) {
        if (errorMessage == null) return null;

        Pattern detailPattern = Pattern.compile("Detail: (.+?)(?=\\]|$)");
        Matcher matcher = detailPattern.matcher(errorMessage);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }
}
