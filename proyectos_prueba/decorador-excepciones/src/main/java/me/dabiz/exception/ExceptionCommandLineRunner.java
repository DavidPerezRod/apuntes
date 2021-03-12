package me.dabiz.exception;

import me.dabiz.exception.info.Layer;
import me.dabiz.exception.info.Micro;
import me.dabiz.exception.info.body.subcategory.http.HttpSubcategoryInfo;
import me.dabiz.exception.info.business.rest.RestBusinessExceptionInfo;
import me.dabiz.exception.info.body.subcategory.http.ServiceTypeError;
import me.dabiz.exception.type.ZertiUncheckedException;
import me.dabiz.exception.info.body.base.BusinessExceptionCategoryInfoBody;
import me.dabiz.exception.info.body.subcategory.http.rest.RestSubcategoryInfo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static me.dabiz.exception.info.body.subcategory.http.HttpSubcategoryInfo.HttpSubcategorytype.SERVICE;


@Component
public class ExceptionCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        main(args);
    }

    public static void main(String[] args) {
        try {
            showMeTheMoney();
        }catch (ZertiUncheckedException ex){
            System.out.println(ex.toString());
        }
    }

    private static void showMeTheMoney(){
        BusinessExceptionCategoryInfoBody body=new BusinessExceptionCategoryInfoBody("Sin novedad en el frente", Layer.PERSITENCE);
        RestBusinessExceptionInfo programmingExceptionInfo= new RestBusinessExceptionInfo(Micro.AIS, body, new RestSubcategoryInfo(SERVICE.name(), ServiceTypeError.INTERNAL_SERVER_ERROR));
        throw new ZertiUncheckedException(programmingExceptionInfo);
    }
}
