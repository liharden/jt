package com.jt.aop;

import com.jt.result.ApiResult;
import com.jt.util.RedisLock;
import com.jt.util.RequestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Aspect
@Component
public class RepeatSubmitAspect {

    private final static Logger logger = LoggerFactory.getLogger(RepeatSubmitAspect.class);

    @Autowired
    private RedisLock redisLock;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit){}

    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        int lockSeconds = noRepeatSubmit.lockTime();

        HttpServletRequest request = RequestUtils.getRequset();
        Assert.notNull(request, "requset can not null");

        // 此处可以用token或者JSessionId
        String token = request.getHeader("Authorization");
        String path = request.getServletPath();
        String key = getKey(token, path);
        String clientId = getclientId();

        boolean isSucccess = redisLock.tryLock(key, clientId, lockSeconds);

        if (isSucccess){
            logger.info("tryLock Success, Key = [{}], clientId = [{}]", key, clientId);
            // 获取锁成功，执行进程
            Object result;
            try {
                result = pjp.proceed();
            } finally {
                // 解锁
                redisLock.releaseLock(key, clientId);
                logger.info("releaseLock Success, Key = [{}], clientId = [{}]", key, clientId);
            }
            return result;
        } else {
            // 获取锁失败，认为是重复提交的请求
            logger.info("tryLock Fail, key = [{}]", key);
            return new ApiResult(200, "重复请求，请稍后再试", null);
        }
    }

    private String getKey(String token, String path){
        return token + path;
    }

    private String getclientId(){
        return UUID.randomUUID().toString();
    }
}
