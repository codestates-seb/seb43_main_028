package backend.section6mainproject.coordinate.service;

import backend.section6mainproject.coordinate.dto.AnonymousCoordinateServiceDTO;
import backend.section6mainproject.coordinate.entity.AnonymousCoordinate;
import backend.section6mainproject.coordinate.mapper.AnonymousCoordinateMapper;
import backend.section6mainproject.coordinate.repository.AnonymousCoordinateRepository;
import backend.section6mainproject.walklog.entity.AnonymousWalkLog;
import backend.section6mainproject.walklog.repository.AnonymousWalkLogRepository;
import backend.section6mainproject.walklog.service.AnonymousWalkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnonymousCoordinateServiceImpl implements AnonymousCoordinateService {
    private final AnonymousCoordinateRepository coordinateRepository;
    private final AnonymousWalkLogService walkLogService;
    private final AnonymousCoordinateMapper mapper;

    @Override
    public AnonymousCoordinateServiceDTO.Output createCoordinate(AnonymousCoordinateServiceDTO.Input input) {
        if (!input.isUserIdSaved()) {
            AnonymousWalkLog walkLog = walkLogService.findVerifiedWalkLogByUserId(input.getUserId());
            SimpAttributesContextHolder.getAttributes().setAttribute("walkLogId", walkLog.getWalkLogId());
            input.setWalkLogId(walkLog.getWalkLogId());
        }
        AnonymousCoordinate coordinate = mapper.serviceInputDTOToEntity(input);
        return mapper.entityToServiceOutputDTO(coordinateRepository.save(coordinate));
    }
}
