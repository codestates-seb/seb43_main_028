package backend.section6mainproject.coordinate.controller;

import backend.section6mainproject.coordinate.dto.AnonymousCoordinateControllerDTO;
import backend.section6mainproject.coordinate.dto.AnonymousCoordinateServiceDTO;
import backend.section6mainproject.coordinate.mapper.AnonymousCoordinateMapper;
import backend.section6mainproject.coordinate.service.AnonymousCoordinateService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AnonymousCoordinateController {
    private final AnonymousCoordinateService coordinateService;
    private final AnonymousCoordinateMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/anonymous/walk-logs")
    public void publishCoordinateForAnonymous(@Valid @Payload AnonymousCoordinateControllerDTO.Pub pub) {
        String userId = (String) SimpAttributesContextHolder.getAttributes().getAttribute("userId");
        pub.setUserId(userId);
        AnonymousCoordinateServiceDTO.Input input = mapper.controllerPubDTOTOServiceInputDTO(pub);
        if(userId != null) input.setUserIdSaved(true);
        AnonymousCoordinateServiceDTO.Output output = coordinateService.createCoordinate(input);
        messagingTemplate.convertAndSend("/sub/anonymous/" + output.getUserId(), mapper.serviceOutputDTOToControllerSubDTO(output));
    }
}
