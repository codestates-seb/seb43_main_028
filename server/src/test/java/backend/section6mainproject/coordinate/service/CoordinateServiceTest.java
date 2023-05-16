package backend.section6mainproject.coordinate.service;

import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.coordinate.entity.Coordinate;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.coordinate.repository.CoordinateRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CoordinateServiceTest {
    @InjectMocks
    private CoordinateServiceImpl coordinateService;
    @Mock
    private CoordinateRepository coordinateRepository;
    @Mock
    private CoordinateMapper mapper;
    private StubData stubData = new StubData();

    @Test
    void createCoordinate() {
        //given
        CoordinateServiceDTO.Input input = stubData.getInput();
        CoordinateServiceDTO.Output output = stubData.getOutput();

        given(mapper.serviceInputDTOToEntity(Mockito.any(CoordinateServiceDTO.Input.class))).willReturn(new Coordinate());
        given(coordinateRepository.save(Mockito.any(Coordinate.class))).willReturn(new Coordinate());
        given(mapper.entityToServiceOutputDTO(Mockito.any(Coordinate.class))).willReturn(output);

        //when
        CoordinateServiceDTO.Output result = coordinateService.createCoordinate(input);

        //then
        MatcherAssert.assertThat(result, Matchers.is(Matchers.equalTo(output)));
    }

    private class StubData {
        private long walkLogId = 1L;
        private double lat = 1.1;
        private double lng = 2.2;

        private CoordinateServiceDTO.Input getInput() {
            CoordinateServiceDTO.Input input = new CoordinateServiceDTO.Input();
            input.setWalkLogId(walkLogId);
            input.setLat(lat);
            input.setLng(lng);
            return input;
        }

        private CoordinateServiceDTO.Output getOutput() {
            return new CoordinateServiceDTO.Output(1L, walkLogId, lat, lng, LocalDateTime.now());
        }
    }
}