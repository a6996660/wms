package com.project.wms.common;

public class TenDigitIdGenerator {
    // 起始时间戳（2023-01-01）
    private final static long START_TIMESTAMP = 1672531200000L; // 精确到毫秒

    // 每毫秒内可以生成的ID数量
    private final static int MAX_IDS_PER_MILLIS = 1000;

    // 序列号
    private int sequence = 0;
    // 上一次时间戳
    private long lastTimestamp = -1L;

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) % MAX_IDS_PER_MILLIS;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        // 计算10位ID
        long id = (timestamp - START_TIMESTAMP) * MAX_IDS_PER_MILLIS + sequence;

        // 将ID转换为字符串
        String idStr = Long.toString(id);

        // 如果ID不足10位，直接返回
        if (idStr.length() <= 10) {
            return id;
        }

        // 截取前10位
        String truncatedIdStr = idStr.substring(0, 10);

        // 将截取后的字符串转换回long类型
        return Long.parseLong(truncatedIdStr);
        
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }
    
}
