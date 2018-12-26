package cn.zmd.frame.service.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CommonJobService {
    private static final Logger logger = LoggerFactory.getLogger(CommonJobService.class);

    @Scheduled(cron = "0 * * * * ?")
    public void testScheduledTask(){
        logger.info("scheduled task running...");
    }
}
