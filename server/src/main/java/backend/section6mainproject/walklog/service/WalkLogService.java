package backend.section6mainproject.walklog.service;

import backend.section6mainproject.dto.PageInfo;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WalkLogService {
    WalkLogServiceDTO.CreateOutput createWalkLog(WalkLogServiceDTO.CreateInput createInput);

    WalkLogServiceDTO.Output updateWalkLog(WalkLogServiceDTO.UpdateInput updateInput);

    WalkLogServiceDTO.Output exitWalkLog(WalkLogServiceDTO.ExitInput exitInput);
    void deleteWalkLog(Long walkLogId);
    WalkLogServiceDTO.Output findWalkLog(Long walkLogId);
    List<WalkLogServiceDTO.FindsOutput> findTotalWalkLogs ();
    Page<WalkLogServiceDTO.FindsOutput> findMyWalkLogs(WalkLogServiceDTO.FindsInput findsInput);
    List<WalkLogServiceDTO.CalenderFindsOutput> findMyMonthWalkLogs(WalkLogServiceDTO.CalenderFindsInput totalFindsInput);
    WalkLog findVerifiedWalkLog(Long walkLogId);
    PageInfo createPageInfo(Page<WalkLogServiceDTO.FindsOutput> findsOutputs);


}
