package dabiz.me.cqrs.command.api.controllers;

import dabiz.me.cqrs.command.api.commands.CloseAccountCommand;
import dabiz.me.cqrs.command.api.commands.OpenAccountCommand;
import dabiz.me.cqrs.command.api.dto.OpenAccountResponse;
import dabiz.me.cqrs.common.dto.BaseResponse;
import dabiz.me.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/api/v1/closeAccount")
public class CloseAccountController {
    private final CommandDispatcher commandDispatcher;

    @DeleteMapping(path= "{id}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable(value= "id") String id){
        try{
            commandDispatcher.send(new CloseAccountCommand(id));
            return new ResponseEntity<>(new OpenAccountResponse("Bank account close request completed " +
                    "successfully", id), HttpStatus.OK);
        }catch (IllegalStateException e){
            log.warn(MessageFormat.format("Client made a bad request - {0}.", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            var message = MessageFormat.format("Error while processing request to close a new bank account for id - " +
                    "{0}.", id);
            log.error(message, e);
            return new ResponseEntity<>(new OpenAccountResponse(message, id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
