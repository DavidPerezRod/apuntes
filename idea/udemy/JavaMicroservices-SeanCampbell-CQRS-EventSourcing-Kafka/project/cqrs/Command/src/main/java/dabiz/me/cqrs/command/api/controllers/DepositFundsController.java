package dabiz.me.cqrs.command.api.controllers;

import dabiz.me.cqrs.command.api.commands.DepositFundsCommand;
import dabiz.me.cqrs.command.api.dto.OpenAccountResponse;
import dabiz.me.cqrs.common.dto.BaseResponse;
import dabiz.me.cqrs.core.excepions.AggregationNotFoundException;
import dabiz.me.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequestMapping(path = "/api/v1/depositFunds")
@Slf4j
@RequiredArgsConstructor
public class DepositFundsController {
    private final CommandDispatcher commandDispatcher;

    @PutMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> depositFunds(
            @PathVariable(value = "id") String id,
            @RequestBody DepositFundsCommand command) {
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(new BaseResponse("Deposit funds request completed successfully" +
                    "successfully"), HttpStatus.OK);
        } catch (IllegalStateException | AggregationNotFoundException exception) {
            log.warn(
                    MessageFormat.format("Client made a bad request - {0}.", exception.toString()));
            return new ResponseEntity<>(new BaseResponse(exception.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            var message = MessageFormat.format("Error while processing request to deposit funds to bank account" +
                    " with id - " +
                    "{0}.", id);
            log.error(
                    message, e);
            return new ResponseEntity<>(new BaseResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
