package me.dabiz.exception;


import me.dabiz.exception.info.category.http.error.HttpErrorType;
import me.dabiz.exception.info.type.UncheckedException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static me.dabiz.exception.info.category.Layer.*;
import static me.dabiz.exception.info.scope.Micro.TOKEN;
import static me.dabiz.exception.info.builder.ScopeInfoBuilder.newProgrammingExceptionInfo;
import static me.dabiz.exception.info.category.http.error.ClientTypeError.BAD_REQUEST;
import static me.dabiz.exception.info.category.http.error.rest.RestResourceCategoryInfoBuilder.newRestResourceExceptionInfo;

@Component
public class ExceptionCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        main(args);
    }

    public static void main(String[] args) {
        try {
            showMeTheMoney();
        }catch (UncheckedException ex){
            System.out.println(ex.toString());
        }
    }

    private static void showMeTheMoney(){
//        throw crateProgrammingException();
        throw createRestResourceException();
    }

    private static UncheckedException crateProgrammingException(){
        return new UncheckedException(newProgrammingExceptionInfo().baseInfo("Error de inicializacion de variables", SERVICE).micro(TOKEN).build());
    }

    private static UncheckedException createRestResourceException(){
        return new UncheckedException(newRestResourceExceptionInfo().categoryInfo(HttpErrorType.HTTP_CLIENT_ERROR, BAD_REQUEST).baseInfo("peticcion con errores", CONTROL).micro(TOKEN).build());
    }

}
