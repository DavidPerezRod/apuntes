package dabiz.me.cqrs.command.api.controllers;

import dabiz.me.cqrs.command.api.commands.OpenAccountCommand;
import dabiz.me.cqrs.command.api.dto.OpenAccountResponse;
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
import java.util.UUID;

@RestController
@RequestMapping(path= "/api/v1/openBankAccount")
@Slf4j
@RequiredArgsConstructor
public class OpenAccountController {
    private final CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command){
        var id= UUID.randomUUID().toString();
        command.setId(id);
        try{
            commandDispatcher.send(command);
            return new ResponseEntity<>(new OpenAccountResponse("Bank account creation request completed " +
                    "successfully", id), HttpStatus.CREATED);
        }catch (IllegalStateException e){
            log.warn(MessageFormat.format("Client made a bad request - {0}.", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            var message = MessageFormat.format("Error while process8ing request to open a new bank account for id - " +
                    "{0}.", id);
            log.error(message, e);
            return new ResponseEntity<>(new OpenAccountResponse(message, id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
