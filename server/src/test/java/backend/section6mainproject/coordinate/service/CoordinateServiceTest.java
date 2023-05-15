package backend.section6mainproject.coordinate.service;

import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.coordinate.entity.Coordinate;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.coordinate.repository.CoordinateRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class CoordinateServiceTest {
    @Autowired
    private CoordinateService coordinateService;
    @MockBean
    private CoordinateRepository coordinateRepository;
    @MockBean
    private CoordinateMapper mapper;
    private StubData stubData = new StubData();

    @Test
    void createCoordinate() {
        //given
        CoordinateServiceDTO.CreateParam createParam = stubData.getCreateParam();
        CoordinateServiceDTO.CreateReturn createReturn = stubData.getCreateReturn();

        given(mapper.serviceCreateParamDTOToEntity(Mockito.any(CoordinateServiceDTO.CreateParam.class))).willReturn(new Coordinate());
        given(coordinateRepository.save(Mockito.any(Coordinate.class))).willReturn(new Coordinate());
        given(mapper.entityToServiceCreateReturnDTO(Mockito.any(Coordinate.class))).willReturn(createReturn);

        //when
        CoordinateServiceDTO.CreateReturn result = coordinateService.createCoordinate(createParam);

        //then
        MatcherAssert.assertThat(result, Matchers.is(Matchers.equalTo(createReturn)));
    }

    private class StubData {
        private long walkLogId = 1L;
        private double lat = 1.1;
        private double lng = 2.2;

        private CoordinateServiceDTO.CreateParam getCreateParam() {
            CoordinateServiceDTO.CreateParam createParam = new CoordinateServiceDTO.CreateParam();
            createParam.setWalkLogId(walkLogId);
            createParam.setLat(lat);
            createParam.setLng(lng);
            return createParam;
        }

        private CoordinateServiceDTO.CreateReturn getCreateReturn() {
            return new CoordinateServiceDTO.CreateReturn(1L, walkLogId, lat, lng, LocalDateTime.now());
        }
    }
}