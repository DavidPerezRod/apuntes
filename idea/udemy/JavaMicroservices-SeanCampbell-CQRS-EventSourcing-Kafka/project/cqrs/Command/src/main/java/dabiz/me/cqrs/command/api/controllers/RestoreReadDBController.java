package dabiz.me.cqrs.command.api.controllers;

import dabiz.me.cqrs.command.api.commands.OpenAccountCommand;
import dabiz.me.cqrs.command.api.commands.RestoreReadDbCommand;
import dabiz.me.cqrs.common.dto.BaseResponse;
import dabiz.me.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@RequestMapping(path= "/api/v1/restoreReadDb")
@Slf4j
@RequiredArgsConstructor
public class RestoreReadDBController {
    private final CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> readDb(){
        try{
            commandDispatcher.send(new RestoreReadDbCommand());
            return new ResponseEntity<>(new BaseResponse("Read database restore request completed successfully!"),HttpStatus.CREATED);
        }catch (IllegalStateException e){
            log.warn(MessageFormat.format("Client made a bad request - {0}.", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            String message="Error while processing request to restore database";
            log.error("Error while processing request to restore database", e);
            return new ResponseEntity<>(new BaseResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
