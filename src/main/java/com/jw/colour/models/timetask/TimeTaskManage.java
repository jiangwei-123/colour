package com.jw.colour.models.timetask;

import com.jw.colour.common.ftp.FtpUtil;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;


/**
 * Manage scheduled tasks
 *
 * @author jw on 2021/1/26
 */
@Component
public class TimeTaskManage {

    @Scheduled(cron = "0/1 * * * * ?")
    public void ftp() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int i = random.nextInt(10000);
        FtpUtil.getFtpUtil().uploadFile(i + ".txt", "D:\\test\\ddd.txt");
    }
}
